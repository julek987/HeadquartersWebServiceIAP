package IAP.controller;

import IAP.model.Product;
import IAP.model.ProductChangeLog;
import IAP.service.ProductChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/productChangeLog")
public class ProductChangeLogController {

    private ProductChangeLogService productChangeLogService;

    @Autowired
    public ProductChangeLogController(ProductChangeLogService productChangeLogService) {
        this.productChangeLogService = productChangeLogService;
    }

    @PostMapping
    public ResponseEntity<ProductChangeLog> addProduct(@RequestBody ProductChangeLog productChangeLog) {
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        productChangeLog.setCreatedAt(now);

        productChangeLogService.addProductChangeLog(productChangeLog);

        return new ResponseEntity<>(productChangeLog, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductChangeLog>> listProductChangeLogs() {
        List<ProductChangeLog> productChangeLogs = productChangeLogService.listProductChangeLog();
        return new ResponseEntity<>(productChangeLogs, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductChangeLog> getProduct(@PathVariable long id) {
        ProductChangeLog productChangeLog = productChangeLogService.getProductChangeLog(id);
        return new ResponseEntity<>(productChangeLog, HttpStatus.OK);
    }

}
