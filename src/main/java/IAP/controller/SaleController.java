package IAP.controller;

import IAP.model.Branch;
import IAP.model.AppUser;
import IAP.model.Sale;
import IAP.model.DTO.SaleDTO;
import IAP.service.SaleService;
import IAP.service.BranchService;
import IAP.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final BranchService branchService;
    private final AppUserService appUserService;

    @Autowired
    public SaleController(SaleService saleService,
                          BranchService branchService,
                          AppUserService appUserService) {
        this.saleService = saleService;
        this.branchService = branchService;
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<SaleDTO> addSale(@RequestBody SaleDTO saleDTO) {
        Branch branch = branchService.getBranch(saleDTO.branchId);
        AppUser appUser = appUserService.getAppUser(saleDTO.appUserId);

        if (branch == null || appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Sale newSale = new Sale();
        newSale.setBranch(branch);
        newSale.setAppUser(appUser);
        newSale.setSaleDate(saleDTO.saleDate);
        newSale.setAnnotations(saleDTO.annotations);

        LocalDateTime now = LocalDateTime.from(Instant.now());
        newSale.setCreatedAt(now);
        newSale.setModifiedAt(now);

        saleService.addSale(newSale);
        return new ResponseEntity<>(new SaleDTO(newSale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDTO> updateSale(@PathVariable long id, @RequestBody SaleDTO saleDTO) {
        Sale existingSale = saleService.getSale(id);
        if (existingSale == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Branch branch = branchService.getBranch(saleDTO.branchId);
        AppUser appUser = appUserService.getAppUser(saleDTO.appUserId);

        if (branch == null || appUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingSale.setBranch(branch);
        existingSale.setAppUser(appUser);
        existingSale.setSaleDate(saleDTO.saleDate);
        existingSale.setAnnotations(saleDTO.annotations);
        existingSale.setModifiedAt(LocalDateTime.from(Instant.now()));

        saleService.updateSale(existingSale);
        return new ResponseEntity<>(new SaleDTO(existingSale), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable long id) {
        saleService.deleteSale(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SaleDTO>> listSales() {
        List<SaleDTO> sales = saleService.listSales()
                .stream()
                .map(SaleDTO::new)
                .toList();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleDTO> getSale(@PathVariable long id) {
        Sale sale = saleService.getSale(id);
        if (sale != null) {
            return new ResponseEntity<>(new SaleDTO(sale), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
