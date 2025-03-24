package IAP.repository;

import IAP.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(long id);
}
