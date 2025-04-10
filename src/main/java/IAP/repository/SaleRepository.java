package IAP.repository;

import IAP.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findById(long id);
}