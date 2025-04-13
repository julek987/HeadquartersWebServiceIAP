package IAP.service;

import IAP.configuration.TestConfig;
import IAP.model.AppUser;
import IAP.model.Product;
import IAP.repository.ProductRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test; // ✅ JUnit 4

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @Before
    public void setup() {
        product = new Product();
        product.setId(1L);
        product.setProductName("Ultra Comfy Chair");
        product.setPrice(129.99f);
        product.setWidth(50);
        product.setHeight(75);
        product.setDepth(60);
        product.setAddedBy(new AppUser());
        product.setCreatedAt(LocalDateTime.now());
        product.setModifiedAt(LocalDateTime.now());

        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    }

    @Test
    public void testAddProduct() {
        productService.addProduct(product);
        verify(productRepository).save(product);
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.existsById(product.getId())).thenReturn(true);
        productService.updateProduct(product);
        verify(productRepository).save(product);
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.existsById(product.getId())).thenReturn(false);
        assertThrows(RuntimeException.class, () -> productService.updateProduct(product));
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    public void testListProduct() {
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.listProduct();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.getProduct(1L);
        assertEquals(product, result);
    }

    @Test
    public void testGetProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Product result = productService.getProduct(1L);
        assertNull(result);
    }
}
