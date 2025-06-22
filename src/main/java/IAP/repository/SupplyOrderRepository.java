package IAP.repository;

import IAP.model.Sale;
import IAP.model.SupplyOrder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface SupplyOrderRepository {
    Optional<SupplyOrder> findById(long id);
}
