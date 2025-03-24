package IAP.service;

import IAP.model.Product;

import java.util.List;

public interface ProductService {

    public void addProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProduct(long id);
    public List<Product> listProduct();
    public Product getProduct(long id);
}
