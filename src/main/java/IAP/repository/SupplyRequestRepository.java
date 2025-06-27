package IAP.repository;

import IAP.model.SupplyRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SupplyRequestRepository extends JpaRepository<SupplyRequest, Long> {
    Optional<SupplyRequest> findById(long id);
    List<SupplyRequest> findAllByStatus(String status);
    boolean existsByBranchIdAndBranchRequestID(long branchId, long branchRequestId);
}
