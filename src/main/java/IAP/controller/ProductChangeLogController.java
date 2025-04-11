package IAP.controller;

import IAP.model.AppUser;
import IAP.model.DTO.ProductChangeLogDTO;
import IAP.model.Product;
import IAP.model.ProductChangeLog;
import IAP.service.AppUserService;
import IAP.service.ProductChangeLogService;
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
@RequestMapping("/api/productChangeLog")
public class ProductChangeLogController {

    private final ProductService productService;
    private final AppUserService appUserService;
    private final ProductChangeLogService productChangeLogService;

    @Autowired
    public ProductChangeLogController(ProductChangeLogService productChangeLogService, ProductService productService, AppUserService appUserService) {
        this.productChangeLogService = productChangeLogService;
        this.productService = productService;
        this.appUserService = appUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductChangeLogDTO>> listProductChangeLogs() {
        List<ProductChangeLog> listProductChangeLog = productChangeLogService.listProductChangeLog();
        List<ProductChangeLogDTO> listProductChangeLogDTO = new ArrayList<>(0);
        for (ProductChangeLog productChangeLog : listProductChangeLog)
            listProductChangeLogDTO.add(new ProductChangeLogDTO(productChangeLog));

        return new ResponseEntity<>(listProductChangeLogDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductChangeLogDTO> getProduct(@PathVariable long id) {
        ProductChangeLog productChangeLog = productChangeLogService.getProductChangeLog(id);
        if (productChangeLog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductChangeLogDTO productChangeLogDTO = new ProductChangeLogDTO(productChangeLog);
        return new ResponseEntity<>(productChangeLogDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductChangeLogDTO> addProduct(
            @RequestBody ProductChangeLogDTO productChangeLogDTO
    ) {
        Product existingProduct = productService.getProduct(productChangeLogDTO.productId);
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppUser existingAppUser = appUserService.getAppUser(productChangeLogDTO.changedById);
        if (existingAppUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    }

}
