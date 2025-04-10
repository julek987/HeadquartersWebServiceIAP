package IAP.controller;

import IAP.model.Order;
import IAP.model.Branch;
import IAP.model.Product;
import IAP.model.Sale;
import IAP.model.DTO.OrderDTO;
import IAP.service.OrderService;
import IAP.service.BranchService;
import IAP.service.ProductService;
import IAP.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order")
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
    public ResponseEntity<OrderDTO> addOrder(@RequestBody OrderDTO orderDTO) {
        Branch existingBranch = branchService.getBranch(orderDTO.branchId);
        Product existingProduct = productService.getProduct(orderDTO.productId);
        Sale existingSale = saleService.getSale(orderDTO.SaleId);

        if (existingBranch == null || existingProduct == null || existingSale == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order newOrder = new Order();
        newOrder.setBranch(existingBranch);
        newOrder.setProduct(existingProduct);
        newOrder.setSale(existingSale);
        newOrder.setQuantitySold(orderDTO.quantitySold);
        newOrder.setSalePrice(orderDTO.salePrice);
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setModifiedAt(LocalDateTime.now());

        orderService.addOrder(newOrder);
        return new ResponseEntity<>(new OrderDTO(newOrder), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable long id, @RequestBody OrderDTO orderDTO) {
        Order existingOrder = orderService.getOrder(id);
        if (existingOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Branch existingBranch = branchService.getBranch(orderDTO.branchId);
        Product existingProduct = productService.getProduct(orderDTO.productId);
        Sale existingSale = saleService.getSale(orderDTO.SaleId);

        if (existingBranch == null || existingProduct == null || existingSale == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingOrder.setBranch(existingBranch);
        existingOrder.setProduct(existingProduct);
        existingOrder.setSale(existingSale);
        existingOrder.setQuantitySold(orderDTO.quantitySold);
        existingOrder.setSalePrice(orderDTO.salePrice);
        existingOrder.setModifiedAt(LocalDateTime.now());

        orderService.updateOrder(existingOrder);
        return new ResponseEntity<>(new OrderDTO(existingOrder), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> listOrders() {
        List<Order> orders = orderService.listOrders();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(OrderDTO::new)
                .toList();

        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrder(@PathVariable long id) {
        Order order = orderService.getOrder(id);
        if (order != null) {
            return new ResponseEntity<>(new OrderDTO(order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
