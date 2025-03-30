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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    private final AppUserService appUserService;

    @Autowired
    public ProductController(
            ProductService productService,
            AppUserService appUserService
            )
    {
        this.productService = productService;
        this.appUserService = appUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> listProducts() {
        List<Product> listProduct       = productService.listProduct();
        List<ProductDTO> listProductDTO = new ArrayList<>(0);
        for (Product product : listProduct)
            listProductDTO.add(new ProductDTO(product));

        return new ResponseEntity<>(listProductDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getProduct(@PathVariable long id) {
        Product existingProduct = productService.getProduct(id);
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductDTO productDTO = new ProductDTO(existingProduct);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductDTO productDTO
    ) {
        AppUser addedBy = appUserService.getAppUser(productDTO.addedById);
        if (addedBy == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        return new ResponseEntity<>(addedProductDTO, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable long id
    ) {
        Product existingProduct = productService.getProduct(id);
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
