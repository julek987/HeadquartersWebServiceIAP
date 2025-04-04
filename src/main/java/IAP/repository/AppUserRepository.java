package IAP.repository;


import IAP.model.AppUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findById(long id);
}
