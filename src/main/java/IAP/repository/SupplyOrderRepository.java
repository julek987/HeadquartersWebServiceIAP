package IAP.repository;

import IAP.model.Sale;
import IAP.model.SupplyOrder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface SupplyOrderRepository extends JpaRepository<SupplyOrder, Long> {
    Optional<SupplyOrder> findById(long id);
}
