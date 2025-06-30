package IAP.scheduler;

import IAP.model.DTO.BranchOrderDTO;
import IAP.model.DTO.BranchSaleDTO;
import IAP.model.DTO.SupplyRequestDTO;
import IAP.model.Order;
import IAP.model.Sale;
import IAP.service.OrderService;
import IAP.service.SaleService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DailySalesCollectorScheduler {

    private final RestTemplate restTemplate;
    private final SaleService  saleService;
    private final OrderService orderService;

    public DailySalesCollectorScheduler(
            SaleService saleService,
            OrderService orderService) {
        this.saleService    = saleService;
        this.orderService   = orderService;

        this.restTemplate = new RestTemplate();
    }

    // Cron: second minute hour day month weekday
    // @Scheduled(cron = "0 0 8 * * *") // Every day at 8:00 AM
    @Scheduled(cron = "0/15 * * * * *") //
    public void getDailySalesFromBranches() {
        String branchSalesUrl  = "http://localhost:8080/api/sales";
        String branchOrdersUrl = "http://localhost:8080/api/branch-orders";

        // Todo: Right now branch ID and URLs are fixed, there is a need to change it in the future*
        // future* - probably never :D
        long fixedBranchId = 1;

        try {
            // Step 1: Fetch all sales from branch
            ResponseEntity<List<BranchSaleDTO>> saleResponse = restTemplate.exchange(
                    branchSalesUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BranchSaleDTO>>() {}
            );
            List<BranchSaleDTO> salesFromBranch = saleResponse.getBody();
            if (salesFromBranch == null) return;

            // Step 2: Fetch all orders from branch
            ResponseEntity<List<BranchOrderDTO>> orderResponse = restTemplate.exchange(
                    branchOrdersUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BranchOrderDTO>>() {}
            );
            List<BranchOrderDTO> allOrders = orderResponse.getBody();
            if (allOrders == null) return;

            for (BranchSaleDTO saleDTO : salesFromBranch) {
                boolean exists = saleService.existsByBranchIdAndBranchSaleId(fixedBranchId, saleDTO.getId());

                if (!exists) {
                    // New sale: create sale and related orders
                    Sale createdSale = saleService.saveFromDTO(saleDTO, fixedBranchId);
                    for (BranchOrderDTO orderDTO : allOrders) {
                        if (orderDTO.saleId.equals(saleDTO.getId())) {
                            orderService.saveFromDTO(orderDTO, createdSale);
                        }
                    }
                } else {
                    // Existing sale: check if order set has changed
                    Sale localSale = saleService.getByBranchIdAndBranchSaleId(fixedBranchId, saleDTO.getId());
                    List<Order> localOrders = orderService.getOrdersBySale(localSale);

                    List<BranchOrderDTO> remoteOrders = allOrders.stream()
                            .filter(o -> o.saleId.equals(saleDTO.getId()))
                            .toList();

                    if (orderService.ordersDiffer(localOrders, remoteOrders)) {
                        orderService.updateOrdersForSale(remoteOrders, localSale);
                    }
                }
            }

            System.out.println("✅ Sales are synchronized SUCCESSFULLY");

        } catch (Exception e) {
            System.err.println("❌ getDailySupplyRequests failed: " + e.getMessage());
        }
    }
}