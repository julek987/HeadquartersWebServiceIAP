package IAP.repository;

import IAP.model.Order;
import IAP.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllBySale(Sale sale);
    List<Order> findBySale(Sale sale);
}
