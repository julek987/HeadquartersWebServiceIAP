package IAP.controller;

import IAP.model.Sales;
import IAP.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Sales> addSale(@RequestBody Sales sale) {
        salesService.addSale(sale);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sales> updateSale(@PathVariable long id, @RequestBody Sales sale) {
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
    public ResponseEntity<List<Sales>> listSales() {
        List<Sales> sales = salesService.listSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sales> getSale(@PathVariable long id) {
        Sales sale = salesService.getSale(id);
        if (sale != null) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
