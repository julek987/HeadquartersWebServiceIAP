package IAP.service;

import IAP.model.Product;
import IAP.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) { this.productRepository = productRepository;}

    @Override
    public void addProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public List<Product> listProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(long id) {
        return this.productRepository.findById(id);
    }



}
