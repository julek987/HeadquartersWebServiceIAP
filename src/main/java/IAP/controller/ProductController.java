package IAP.controller;

import IAP.model.Product;
import IAP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) { this.productService = productService; }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        product.setCreatedAt(now);
        product.setModifiedAt(now);

        productService.addProduct(product);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable long id) {
        Product existingProduct = productService.getProduct(id);
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Preserve the createdAt field from the existing entity
        product.setCreatedAt(existingProduct.getCreatedAt());
        product.setModifiedAt(new Timestamp(System.currentTimeMillis()  / 1000));
        product.setId(id);

        productService.updateProduct(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> listProducts() {
        List<Product> products = productService.listProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getProduct(@PathVariable long id) {
        Product product = productService.getProduct(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
