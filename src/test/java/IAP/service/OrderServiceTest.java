package IAP.service;

import IAP.configuration.TestConfig;
import IAP.model.Branch;
import IAP.model.Order;
import IAP.model.Product;
import IAP.model.Sale;
import IAP.repository.OrderRepository;
import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test; // ✅ JUnit 4

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @Before
    public void setup() {
        order = new Order();
        order.setId(1L);
        order.setBranch(new Branch());
        order.setSale(new Sale());
        order.setProduct(new Product());
        order.setQuantitySold(3);
        order.setSalePrice(new BigDecimal("99.99"));
        order.setCreatedAt(LocalDateTime.now());
        order.setModifiedAt(LocalDateTime.now());

        when(orderRepository.save(order)).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    }

    @Test
    public void testAddOrder() throws InvalidDataException, ResourceNotFoundException {
        Order result = orderService.addOrder(order);
        assertEquals(order, result);
        verify(orderRepository).save(order);
    }

    @Test
    public void testUpdateOrder() throws InvalidDataException, ResourceNotFoundException {
        when(orderRepository.existsById(order.getId())).thenReturn(true);
        when(orderRepository.save(order)).thenReturn(order);
        Order result = orderService.updateOrder(order.getId(), order);
        assertEquals(order, result);
    }

    @Test
    public void testUpdateOrder_NotFound() {
        when(orderRepository.existsById(order.getId())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () ->
                orderService.updateOrder(order.getId(), order));
    }

    @Test
    public void testDeleteOrder() throws ResourceNotFoundException {
        when(orderRepository.existsById(1L)).thenReturn(true);
        orderService.deleteOrder(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    public void testDeleteOrder_NotFound() {
        when(orderRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () ->
                orderService.deleteOrder(1L));
    }

    @Test
    public void testListOrders() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderService.listOrders();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetOrder() throws ResourceNotFoundException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Order result = orderService.getOrder(1L);
        assertEquals(order, result);
    }

    @Test
    public void testGetOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                orderService.getOrder(1L));
    }
}
