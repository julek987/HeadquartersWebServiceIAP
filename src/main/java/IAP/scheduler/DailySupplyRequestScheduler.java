package IAP.scheduler;

import IAP.model.DTO.SupplyOrderDTO;
import IAP.model.DTO.SupplyRequestDTO;
import IAP.model.SupplyRequest;
import IAP.service.SupplyOrderService;
import IAP.service.SupplyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DailySupplyRequestScheduler {

    private final RestTemplate          restTemplate;
    private final SupplyRequestService  supplyRequestService;
    private final SupplyOrderService    supplyOrderService;

    public DailySupplyRequestScheduler(
            SupplyRequestService    supplyRequestService,
            SupplyOrderService      supplyOrderService
    ) {
        this.restTemplate           = new RestTemplate();
        this.supplyRequestService   = supplyRequestService;
        this.supplyOrderService     = supplyOrderService;
    }

    @Scheduled(cron = "0/30 * * * * ?") // every day at 08:00 AM
    public void getDailySupplyRequests() {
        String branchRequestUrl = "http://localhost:8081/api/supply-requests";
        String branchOrdersUrl = "http://localhost:8081/api/supply-orders";
        System.out.println(LocalDateTime.now() + "| Fetching Supply Requests");

        try {
            // Step 1: Fetch supply requests from the branch
            ResponseEntity<List<SupplyRequestDTO>> response =
                    restTemplate.exchange(branchRequestUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<SupplyRequestDTO>>() {});
            List<SupplyRequestDTO> branchRequests = response.getBody();

            branchRequests.forEach(System.out::println);

            if (branchRequests == null || branchRequests.isEmpty()) return;

            for (SupplyRequestDTO dto : branchRequests) {
                // Only handle PENDING requests
                if (dto.status != SupplyRequest.Status.PENDING) continue;

                boolean exists = supplyRequestService.existsByBranchIdAndBranchRequestId(dto.branchId, dto.id);
                if (exists) continue; // already imported

                // Step 2: Create local HQ version of the request
                SupplyRequestDTO localDTO = new SupplyRequestDTO();
                localDTO.branchId = dto.branchId;
                localDTO.branchRequestID = dto.id; // original ID from branch
                localDTO.status = SupplyRequest.Status.REVIEWED;
                localDTO.reviewedAt = LocalDateTime.now();
                localDTO.reviewedBy = "HQ_SYSTEM";
                localDTO.annotation = "[Auto-sync]";
                localDTO.createdAt = dto.createdAt;
                localDTO.modifiedAt = LocalDateTime.now();

                SupplyRequest created = supplyRequestService.createFromDTO(localDTO);

                // Step 3: Fetch all orders from branch (simplified for now)
                ResponseEntity<List<SupplyOrderDTO>> orderResponse =
                        restTemplate.exchange(branchOrdersUrl, HttpMethod.GET, null,
                                new ParameterizedTypeReference<List<SupplyOrderDTO>>() {});
                List<SupplyOrderDTO> branchOrders = orderResponse.getBody();

                if (branchOrders != null) {
                    for (SupplyOrderDTO orderDTO : branchOrders) {
                        // Match only orders for this supply request (using branchRequestID)
                        if (!orderDTO.supplyRequestId.equals(dto.id)) continue;

                        // Update order to match HQ model
                        orderDTO.supplyRequestId = created.getId(); // link to HQ version
                        orderDTO.branchId = dto.branchId;

                        // Save order
                        supplyOrderService.saveFromDTO(orderDTO);
                    }
                }

                // Step 4: Notify branch that request was reviewed
                String updateUrl = "http://localhost:8081/api/supply-requests/" + dto.id + "/review";
                restTemplate.postForEntity(updateUrl, null, Void.class);
            }

            System.out.println(LocalDateTime.now() + "| Fetching Supply Requests finished SUCCESSFULLY");

        } catch (Exception ex) {
            System.err.println("❌ getDailySupplyRequests failed: " + ex.getMessage());
        }
    }

}
