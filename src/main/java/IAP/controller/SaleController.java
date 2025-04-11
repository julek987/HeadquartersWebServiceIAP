package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.*;
import IAP.model.DTO.AppUserDTO;
import IAP.model.DTO.SaleDTO;
import IAP.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> addSale(@Valid @RequestBody SaleDTO saleDTO) {
        try {
            Branch branch = branchService.getBranch(saleDTO.branchId);
            AppUser appUser = appUserService.getAppUser(saleDTO.appUserId);

            Sale newSale = new Sale();
            newSale.setBranch(branch);
            newSale.setAppUser(appUser);
            newSale.setSaleDate(saleDTO.saleDate);
            newSale.setAnnotations(saleDTO.annotations);
            newSale.setCreatedAt(LocalDateTime.now());
            newSale.setModifiedAt(LocalDateTime.now());

            saleService.addSale(newSale);
            SaleDTO savedSaleDTO = new SaleDTO(newSale);
            return new ResponseEntity<>(savedSaleDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(@PathVariable long id, @Valid @RequestBody SaleDTO saleDTO) {
        try {
            Branch branch = branchService.getBranch(saleDTO.branchId);
            AppUser appUser = appUserService.getAppUser(saleDTO.appUserId);

            Sale existingSale = saleService.getSale(id);
            existingSale.setBranch(branch);
            existingSale.setAppUser(appUser);
            existingSale.setSaleDate(saleDTO.saleDate);
            existingSale.setAnnotations(saleDTO.annotations);
            existingSale.setModifiedAt(LocalDateTime.now());

            saleService.addSale(existingSale);
            SaleDTO updatedSaleDTO = new SaleDTO(existingSale);
            return new ResponseEntity<>(updatedSaleDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDataException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable long id) {
        try {
            saleService.deleteSale(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SaleDTO>> listSales() {
        List<Sale> sales = saleService.listSales();
        List<SaleDTO> saleDTOs = sales.stream()
                .map(SaleDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(saleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSale(@PathVariable long id) {
        try {
            Sale sale = saleService.getSale(id);
            return ResponseEntity.ok(new SaleDTO(sale));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}