package IAP.controller;

import IAP.model.Sale;
import IAP.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    @Autowired
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping
    public ResponseEntity<Sale> addSale(@RequestBody Sale sale) {
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        sale.setCreatedAt(now);
        sale.setModifiedAt(now);

        salesService.addSale(sale);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable long id, @RequestBody Sale sale) {
        Sale existingSale = salesService.getSale(id);
        if (existingSale == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        sale.setCreatedAt(existingSale.getCreatedAt());
        sale.setModifiedAt(new Timestamp(System.currentTimeMillis()  / 1000));
        sale.setId(id);

        salesService.updateSale(sale);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable long id) {
        salesService.deleteSale(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sale>> listSales() {
        List<Sale> sales = salesService.listSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sale> getSale(@PathVariable long id) {
        Sale sale = salesService.getSale(id);
        if (sale != null) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
