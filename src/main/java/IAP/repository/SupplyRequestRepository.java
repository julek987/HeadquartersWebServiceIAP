package IAP.repository;

import IAP.model.SupplyRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface SupplyRequestRepository {
    Optional<SupplyRequest> findById(long id);
}
