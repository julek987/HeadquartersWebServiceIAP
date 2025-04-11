package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.AppUser;
import IAP.model.DTO.ProductChangeLogDTO;
import IAP.model.Product;
import IAP.model.ProductChangeLog;
import IAP.model.objects.ProductChanges;
import IAP.service.AppUserService;
import IAP.service.ProductChangeLogService;
import IAP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productChangeLog")
public class ProductChangeLogController {

    private final ProductService productService;
    private final AppUserService appUserService;
    private final ProductChangeLogService productChangeLogService;

    @Autowired
    public ProductChangeLogController(ProductChangeLogService productChangeLogService,
                                      ProductService productService,
                                      AppUserService appUserService) {
        this.productChangeLogService = productChangeLogService;
        this.productService = productService;
        this.appUserService = appUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listProductChangeLogs() {
        try {
            List<ProductChangeLog> listProductChangeLog = productChangeLogService.listProductChangeLog();
            List<ProductChangeLogDTO> listProductChangeLogDTO = listProductChangeLog.stream()
                    .map(ProductChangeLogDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(listProductChangeLogDTO);
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
            ProductChangeLog productChangeLog = productChangeLogService.getProductChangeLog(id);
            if (productChangeLog == null) {
                throw new ResourceNotFoundException("ProductChangeLog with ID " + id + " not found");
            }
            return ResponseEntity.ok(new ProductChangeLogDTO(productChangeLog));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductChangeLogDTO productChangeLogDTO) {
        try {
            Product existingProduct = productService.getProduct(productChangeLogDTO.productId);
            if (existingProduct == null) {
                throw new ResourceNotFoundException("Product with ID " + productChangeLogDTO.productId + " not found");
            }

            AppUser existingAppUser = appUserService.getAppUser(productChangeLogDTO.changedById);
            if (existingAppUser == null) {
                throw new ResourceNotFoundException("AppUser with ID " + productChangeLogDTO.changedById + " not found");
            }

            ProductChangeLog productChangeLog = new ProductChangeLog();
            productChangeLog.setProduct(existingProduct);
            productChangeLog.setChangedBy(existingAppUser);
            productChangeLog.setChangeReason(productChangeLogDTO.changeReason);
            productChangeLog.setChanges(productChangeLogDTO.changes);
            productChangeLog.setCreatedAt(LocalDateTime.now());

            productChangeLogService.addProductChangeLog(productChangeLog);
            ProductChangeLogDTO newProductChangeLogDTO = new ProductChangeLogDTO(productChangeLog);
            return new ResponseEntity<>(newProductChangeLogDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
