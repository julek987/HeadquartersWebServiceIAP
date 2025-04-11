package IAP.repository;

import IAP.model.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findById(long id);
}
