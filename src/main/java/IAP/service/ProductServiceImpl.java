package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Product;
import IAP.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) throws InvalidDataException {
        validateProduct(product);
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) throws ResourceNotFoundException, InvalidDataException {
        if (!productRepository.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
        }
        validateProduct(product);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) throws ResourceNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> listProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(long id) throws ResourceNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private void validateProduct(Product product) throws InvalidDataException {
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new InvalidDataException("Product name is required");
        }
        if (product.getPrice() <= 0) {
            throw new InvalidDataException("Price must be greater than 0");
        }
        if (product.getWidth() <= 0 || product.getDepth() <= 0 || product.getHeight() <= 0) {
            throw new InvalidDataException("Width, Depth, and Height must be greater than 0");
        }
    }
}
