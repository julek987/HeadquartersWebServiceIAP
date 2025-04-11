package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Product;
import IAP.model.ProductChangeLog;
import IAP.repository.ProductChangeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductChangeLogServiceImpl implements ProductChangeLogService {

    private final ProductChangeLogRepository productChangeLogRepository;

    @Autowired
    public ProductChangeLogServiceImpl(ProductChangeLogRepository productChangeLogRepository) {
        this.productChangeLogRepository = productChangeLogRepository;
    }

    @Override
    public void addProductChangeLog(ProductChangeLog productChangeLog) throws InvalidDataException {
        validateProductChangeLog(productChangeLog);
        productChangeLog.setCreatedAt(LocalDateTime.now());
        productChangeLogRepository.save(productChangeLog);
    }

    @Override
    public void updateProductChangeLog(ProductChangeLog productChangeLog) throws ResourceNotFoundException, InvalidDataException {
        if (!productChangeLogRepository.existsById(productChangeLog.getId())) {
            throw new ResourceNotFoundException("ProductChangeLog not found with id: " + productChangeLog.getId());
        }
        validateProductChangeLog(productChangeLog);
        productChangeLogRepository.save(productChangeLog);
    }

    @Override
    public void deleteProductChangeLog(long id) throws ResourceNotFoundException {
        if (!productChangeLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("ProductChangeLog not found with id: " + id);
        }
        productChangeLogRepository.deleteById(id);
    }

    @Override
    public List<ProductChangeLog> listProductChangeLog() {
        return productChangeLogRepository.findAll();
    }

    @Override
    public ProductChangeLog getProductChangeLog(long id) throws ResourceNotFoundException {
        return productChangeLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductChangeLog not found with id: " + id));
    }

    private void validateProductChangeLog(ProductChangeLog productChangeLog) throws InvalidDataException {
        if (productChangeLog.getProduct() == null) {
            throw new InvalidDataException("Product is required");
        }
        if (productChangeLog.getChangedBy() == null) {
            throw new InvalidDataException("ChangedBy user is required");
        }
        if (productChangeLog.getChanges() == null) {
            throw new InvalidDataException("Changes are required");
        }
    }
}
