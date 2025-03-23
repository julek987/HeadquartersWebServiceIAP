package IAP.service;

import IAP.model.Product;
import IAP.model.ProductChangeLog;
import IAP.repository.ProductChangeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductChangeLogServiceImpl implements ProductChangeLogService {

    private ProductChangeLogRepository productChangeLogRepository;

    @Autowired
    public ProductChangeLogServiceImpl(ProductChangeLogRepository productChangeLogRepository) {
        this.productChangeLogRepository = productChangeLogRepository;
    }

    @Override
    public void addProductChangeLog(ProductChangeLog productChangeLog) {
        productChangeLogRepository.save(productChangeLog);
    }

    @Override
    public void updateProductChangeLog(Product product) {
        productChangeLogRepository.save(productChangeLogRepository.findById(product.getId()));
    }

    @Override
    public void deleteProductChangeLog(long id) {
        productChangeLogRepository.deleteById(id);
    }

    @Override
    public List<ProductChangeLog> listProductChangeLog() {
        return productChangeLogRepository.findAll();
    }

    public ProductChangeLog getProductChangeLog(long id) {
        return productChangeLogRepository.findById(id);
    }

}
