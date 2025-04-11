package IAP.controller;

import IAP.model.AppUser;
import IAP.model.DTO.ProductDTO;
import IAP.model.Product;
import IAP.service.AppUserService;
import IAP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final AppUserService appUserService;

    @Autowired
    public ProductController(
            ProductService productService,
            AppUserService appUserService
    ) {
        this.productService = productService;
        this.appUserService = appUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listProducts() {
        try {
            List<Product> listProduct = productService.listProduct();
            List<ProductDTO> listProductDTO = new ArrayList<>(0);
            for (Product product : listProduct) {
                listProductDTO.add(new ProductDTO(product));
            }

            return new ResponseEntity<>(listProductDTO, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("title", "Internal Server Error");
            errorResponse.put("detail", "An unexpected error occurred: " + e.getMessage());
            errorResponse.put("instance", UUID.randomUUID().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProduct(@PathVariable long id) {
        try {
            Product existingProduct = productService.getProduct(id);
            if (existingProduct == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                errorResponse.put("title", "Resource not found");
                errorResponse.put("detail", "Product not found with id: " + id);
                errorResponse.put("instance", UUID.randomUUID().toString());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            ProductDTO productDTO = new ProductDTO(existingProduct);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("title", "Internal Server Error");
            errorResponse.put("detail", "An unexpected error occurred: " + e.getMessage());
            errorResponse.put("instance", UUID.randomUUID().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        try {
            AppUser addedBy = appUserService.getAppUser(productDTO.addedById);
            if (addedBy == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                errorResponse.put("title", "Resource not found");
                errorResponse.put("detail", "AppUser not found with id: " + productDTO.addedById);
                errorResponse.put("instance", UUID.randomUUID().toString());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Product product = new Product();
            product.setProductName(productDTO.productName);
            product.setPrice(productDTO.price);
            product.setWidth(productDTO.width);
            product.setDepth(productDTO.depth);
            product.setHeight(productDTO.height);
            product.setAddedBy(addedBy);
            product.setCreatedAt(LocalDateTime.now());
            product.setModifiedAt(LocalDateTime.now());

            productService.addProduct(product);
            ProductDTO addedProductDTO = new ProductDTO(product);
            return new ResponseEntity<>(addedProductDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("title", "Internal Server Error");
            errorResponse.put("detail", "An unexpected error occurred: " + e.getMessage());
            errorResponse.put("instance", UUID.randomUUID().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable long id) {
        try {
            Product existingProduct = productService.getProduct(id);
            if (existingProduct == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                errorResponse.put("title", "Resource not found");
                errorResponse.put("detail", "Product not found with id: " + id);
                errorResponse.put("instance", UUID.randomUUID().toString());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            existingProduct.setProductName(productDTO.productName);
            existingProduct.setPrice(productDTO.price);
            existingProduct.setWidth(productDTO.width);
            existingProduct.setDepth(productDTO.depth);
            existingProduct.setHeight(productDTO.height);
            existingProduct.setModifiedAt(LocalDateTime.now());

            productService.updateProduct(existingProduct);

            ProductDTO updatedProductDTO = new ProductDTO(existingProduct);
            return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("title", "Internal Server Error");
            errorResponse.put("detail", "An unexpected error occurred: " + e.getMessage());
            errorResponse.put("instance", UUID.randomUUID().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id) {
        try {
            Product existingProduct = productService.getProduct(id);
            if (existingProduct == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("status", HttpStatus.NOT_FOUND.value());
                errorResponse.put("title", "Resource not found");
                errorResponse.put("detail", "Product not found with id: " + id);
                errorResponse.put("instance", UUID.randomUUID().toString());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("title", "Internal Server Error");
            errorResponse.put("detail", "An unexpected error occurred: " + e.getMessage());
            errorResponse.put("instance", UUID.randomUUID().toString());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
