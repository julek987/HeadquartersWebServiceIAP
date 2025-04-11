package IAP.repository;

import IAP.model.ProductChangeLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface ProductChangeLogRepository extends JpaRepository<ProductChangeLog, Long> {
    Optional<ProductChangeLog> findById(long id);
}
