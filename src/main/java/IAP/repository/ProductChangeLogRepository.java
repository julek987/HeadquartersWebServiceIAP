package IAP.repository;

import IAP.model.ProductChangeLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ProductChangeLogRepository extends JpaRepository<ProductChangeLog, Long> {
    ProductChangeLog findById(long id);
}
