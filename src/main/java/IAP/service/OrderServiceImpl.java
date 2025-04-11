package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Order;
import IAP.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order addOrder(Order order) throws InvalidDataException, ResourceNotFoundException {
        validateOrder(order);
        order.setCreatedAt(LocalDateTime.now());
        order.setModifiedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(long id, Order order) throws InvalidDataException, ResourceNotFoundException {
        validateOrder(order);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        existingOrder.setBranch(order.getBranch());
        existingOrder.setProduct(order.getProduct());
        existingOrder.setSale(order.getSale());
        existingOrder.setQuantitySold(order.getQuantitySold());
        existingOrder.setSalePrice(order.getSalePrice());
        existingOrder.setModifiedAt(LocalDateTime.now());

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(long id) throws ResourceNotFoundException {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrder(long id) throws ResourceNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private void validateOrder(Order order) throws InvalidDataException {
        if (order.getBranch() == null) {
            throw new InvalidDataException("Branch is required");
        }
        if (order.getProduct() == null) {
            throw new InvalidDataException("Product is required");
        }
        if (order.getSale() == null) {
            throw new InvalidDataException("Sale is required");
        }
        if (order.getQuantitySold() == null || order.getQuantitySold() < 1) {
            throw new InvalidDataException("Quantity must be at least 1");
        }
        if (order.getSalePrice() == null || order.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Price must be greater than 0");
        }
    }
}