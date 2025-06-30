package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.BranchOrderDTO;
import IAP.model.Order;
import IAP.model.Sale;
import IAP.repository.OrderRepository;
import IAP.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository   orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(
            OrderRepository     orderRepository,
            ProductRepository   productRepository) {
        this.orderRepository    = orderRepository;
        this.productRepository  = productRepository;
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

    @Override
    public List<Order> listOrderBySale(Sale sale) {
        return orderRepository.findAllBySale(sale);
    }

    private void validateOrder(Order order) throws InvalidDataException {
        if (order.getProduct() == null) {
            throw new InvalidDataException("Product is required");
        }
        if (order.getSale() == null) {
            throw new InvalidDataException("Sale is required");
        }
        if (order.getQuantitySold() == null || order.getQuantitySold() < 1) {
            throw new InvalidDataException("Quantity must be at least 1");
        }
        if (order.getSalePrice() == null || order.getSalePrice().compareTo(0d) <= 0) {
            throw new InvalidDataException("Price must be greater than 0");
        }
    }

    @Override
    public List<Order> getOrdersBySale(Sale sale) {
        return orderRepository.findBySale(sale);
    }

    @Override
    public boolean ordersDiffer(List<Order> local, List<BranchOrderDTO> remote) {
        if (local.size() != remote.size()) return true;

        for (Order order : local) {
            boolean matched = remote.stream().anyMatch(remoteOrder ->
                    remoteOrder.getProductId().equals(order.getProduct().getId()) &&
                            remoteOrder.getQuantitySold().equals(order.getQuantitySold()) &&
                            remoteOrder.getSalePrice().equals(order.getSalePrice())
            );
            if (!matched) return true;
        }

        return false;
    }

    @Override
    public Order saveFromDTO(BranchOrderDTO dto, Sale sale) {
        Order order = new Order();
        order.setSale(sale);
        order.setProduct(productRepository.findById(dto.getProductId()).orElseThrow());
        order.setQuantitySold(dto.getQuantitySold());
        order.setSalePrice(dto.getSalePrice());
        order.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        order.setModifiedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public void updateOrdersForSale(List<BranchOrderDTO> remoteOrders, Sale sale) {
        List<Order> existing = orderRepository.findBySale(sale);
        for (Order o : existing) {
            orderRepository.delete(o);
        }
        for (BranchOrderDTO dto : remoteOrders) {
            saveFromDTO(dto, sale);
        }
    }

}