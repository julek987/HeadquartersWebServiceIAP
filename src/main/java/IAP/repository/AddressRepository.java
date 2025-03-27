package IAP.repository;

import IAP.model.Address;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findById(long id);
}
