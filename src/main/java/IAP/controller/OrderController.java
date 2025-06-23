package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.*;
import IAP.model.DTO.OrderDTO;
import IAP.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final BranchService branchService;
    private final ProductService productService;
    private final SaleService saleService;

    @Autowired
    public OrderController(OrderService orderService,
                           BranchService branchService,
                           ProductService productService,
                           SaleService saleService) {
        this.orderService = orderService;
        this.branchService = branchService;
        this.productService = productService;
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            Branch branch = branchService.getBranch(orderDTO.branchId);
            if (branch == null) {
                throw new ResourceNotFoundException("Branch with ID " + orderDTO.branchId + " not found");
            }

            Product product = productService.getProduct(orderDTO.productId);
            if (product == null) {
                throw new ResourceNotFoundException("Product with ID " + orderDTO.productId + " not found");
            }

            Sale sale = saleService.getSale(orderDTO.saleId);
            if (sale == null) {
                throw new ResourceNotFoundException("Sale with ID " + orderDTO.saleId + " not found");
            }

            Order newOrder = new Order();
            newOrder.setProduct(product);
            newOrder.setSale(sale);
            newOrder.setQuantitySold(orderDTO.quantitySold);
            newOrder.setSalePrice(orderDTO.salePrice);
            newOrder.setCreatedAt(LocalDateTime.now());
            newOrder.setModifiedAt(LocalDateTime.now());

            orderService.addOrder(newOrder);
            OrderDTO savedOrderDTO = new OrderDTO(newOrder);
            return new ResponseEntity<>(savedOrderDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable long id, @Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order existingOrder = orderService.getOrder(id);
            if (existingOrder == null) {
                throw new ResourceNotFoundException("Order with ID " + id + " not found");
            }

            Branch branch = branchService.getBranch(orderDTO.branchId);
            if (branch == null) {
                throw new ResourceNotFoundException("Branch with ID " + orderDTO.branchId + " not found");
            }

            Product product = productService.getProduct(orderDTO.productId);
            if (product == null) {
                throw new ResourceNotFoundException("Product with ID " + orderDTO.productId + " not found");
            }

            Sale sale = saleService.getSale(orderDTO.saleId);
            if (sale == null) {
                throw new ResourceNotFoundException("Sale with ID " + orderDTO.saleId + " not found");
            }

            existingOrder.setProduct(product);
            existingOrder.setSale(sale);
            existingOrder.setQuantitySold(orderDTO.quantitySold);
            existingOrder.setSalePrice(orderDTO.salePrice);
            existingOrder.setModifiedAt(LocalDateTime.now());

            orderService.addOrder(existingOrder);
            OrderDTO updatedOrderDTO = new OrderDTO(existingOrder);
            return new ResponseEntity<>(updatedOrderDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> listOrders() {
        List<Order> orders = orderService.listOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable long id) {
        try {
            Order order = orderService.getOrder(id);
            if (order == null) {
                throw new ResourceNotFoundException("Order with ID " + id + " not found");
            }
            return ResponseEntity.ok(new OrderDTO(order));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
