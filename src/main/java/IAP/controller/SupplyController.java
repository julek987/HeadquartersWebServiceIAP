/*
    Note:
        SupplyOrder is an element of SupplyRequest
        for example:
            SupplyRequest nr. 323:
                - Order nr. 1 | Wooden doors    | x5
                - Order nr. 2 | Wooden table    | x1
                - Order nr. 3 | Metal mattress  | x3
            SupplyRequest nr. 324:
                - Order nr. 1 | Ivory plates    | x20
                - Order nr. 2 | Diamond pickaxe | x1

    Summing up:
        supply request is a wrapper for supply orders
 */
package IAP.controller;

import IAP.model.DTO.SupplyOrderDTO;
import IAP.model.DTO.SupplyRequestDTO;
import IAP.model.DTO.SupplyRequestDecisionDTO;
import IAP.model.SupplyOrder;
import IAP.model.SupplyRequest;
import IAP.service.SupplyOrderService;
import IAP.service.SupplyRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/supply-requests")
public class SupplyController {

    private final SupplyRequestService  supplyRequestService;
    private final SupplyOrderService    supplyOrderService;
    private final RestTemplate          restTemplate;


    public SupplyController(
            SupplyRequestService    supplyRequestService,
            SupplyOrderService      supplyOrderService) {
        this.supplyRequestService   = supplyRequestService;
        this.supplyOrderService     = supplyOrderService;
        this.restTemplate           = new  RestTemplate();
    }

    // statuses:
    // - pending
    // - reviewed (awaiting decision)
    // - accepted
    // - rejected

    // list all supply requests - active and inactive
    @GetMapping()
    public ResponseEntity<List<SupplyRequestDTO>> listSupplyRequests(){

        List<SupplyRequest> listSupplyRequest = supplyRequestService.listSupplyRequest();
        List<SupplyRequestDTO> listSupplyRequestDTO = new ArrayList<>();

        listSupplyRequest.forEach(supplyRequest -> {
            listSupplyRequestDTO.add(new SupplyRequestDTO(supplyRequest));
        });

        return ResponseEntity.ok(listSupplyRequestDTO);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<SupplyRequestDTO>> listPendingSupplyRequests(){
        // Keep fetching REVIEWED requests since that's how they come from branches
        List<SupplyRequest> listSupplyRequest = supplyRequestService.listSupplyRequestByStatus(SupplyRequest.Status.REVIEWED);
        List<SupplyRequestDTO> listSupplyRequestDTO = new ArrayList<>();

        listSupplyRequest.forEach(supplyRequest -> {
            listSupplyRequestDTO.add(new SupplyRequestDTO(supplyRequest));
        });

        return ResponseEntity.ok(listSupplyRequestDTO);
    }

    // returns all orders for supply request with given id
    @GetMapping("/{id}")
    public ResponseEntity<List<SupplyOrderDTO>> listSupplyOrders(@PathVariable long id){
        SupplyRequest supplyRequest = supplyRequestService.getSupplyRequest(id);

        if (supplyRequest == null) {
            return ResponseEntity.notFound().build();
        }

        List<SupplyOrder> listSupplyOrder = supplyOrderService.listSupplyOrderBySupplyRequestId(supplyRequest.getId());
        List<SupplyOrderDTO> listSupplyOrderDTO = new ArrayList<>();

        listSupplyOrder.forEach(supplyOrder -> {
            listSupplyOrderDTO.add(new SupplyOrderDTO(supplyOrder));
        });

        return ResponseEntity.ok(listSupplyOrderDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplyRequest(@PathVariable long id, @RequestBody SupplyRequestDecisionDTO supplyRequestDecisionDTO) {
        SupplyRequest supplyRequest = supplyRequestService.getSupplyRequest(id);

        if (supplyRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Supply Request with id " + id + " does not exist");
        }

        // Modified validation: Only allow updates for REVIEWED requests (which are awaiting decision)
        // Don't allow updates for already ACCEPTED or REJECTED requests
        if (supplyRequest.getStatus() == SupplyRequest.Status.ACCEPTED ||
                supplyRequest.getStatus() == SupplyRequest.Status.REJECTED) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This supply request has already been processed and cannot be updated anymore.");
        }

        // Only allow REVIEWED requests to be updated (not PENDING)
        if (supplyRequest.getStatus() != SupplyRequest.Status.REVIEWED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Only reviewed requests can be accepted or rejected.");
        }

        if (!SupplyRequest.Status.valueOf(supplyRequestDecisionDTO.status).equals(SupplyRequest.Status.ACCEPTED) &&
                !SupplyRequest.Status.valueOf(supplyRequestDecisionDTO.status).equals(SupplyRequest.Status.REJECTED)) {
            return ResponseEntity.badRequest().body("Invalid decision status. Must be ACCEPTED or REJECTED.");
        }

        // Update the request with the decision
        supplyRequest.setStatus(SupplyRequest.Status.valueOf(supplyRequestDecisionDTO.status));
        supplyRequest.setAnnotation(supplyRequestDecisionDTO.annotation);
        // TODO: get the actual user making the decision
        supplyRequest.setReviewedBy("[System]");

        // Update the reviewedAt timestamp to reflect when the decision was actually made
        supplyRequest.setReviewedAt(LocalDateTime.now());
        supplyRequest.setModifiedAt(LocalDateTime.now());

        supplyRequestService.updateRequest(supplyRequest.getId(), supplyRequest);

        // Notify the branch about the decision
        try {
            boolean isAccepted = SupplyRequest.Status.valueOf(supplyRequestDecisionDTO.status).equals(SupplyRequest.Status.ACCEPTED);
            String decision    = isAccepted ? "accept" : "reject";
            String updateUrl   = "http://localhost:8080/api/supply-requests/" + supplyRequest.getBranchRequestID() + "/" + decision;
            restTemplate.postForEntity(updateUrl, null, Void.class);
        } catch (Exception e) {
            // Log the error but don't fail the request update
            System.err.println("Failed to notify branch about decision: " + e.getMessage());
        }

        return new ResponseEntity<>(new SupplyRequestDTO(supplyRequest), HttpStatus.OK);
    }

}