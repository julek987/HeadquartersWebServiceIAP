package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.BranchSaleDTO;
import IAP.model.Sale;
import IAP.repository.BranchRepository;
import IAP.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository    saleRepository;
    private final BranchRepository  branchRepository;

    @Autowired
    public SaleServiceImpl(
            SaleRepository      saleRepository,
            BranchRepository    branchRepository) {
        this.saleRepository     = saleRepository;
        this.branchRepository   = branchRepository;
    }

    @Override
    public Sale addSale(Sale sale) throws InvalidDataException, ResourceNotFoundException {
        validateSale(sale);
        sale.setCreatedAt(LocalDateTime.now());
        sale.setModifiedAt(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    @Override
    public Sale updateSale(long id, Sale sale) throws InvalidDataException, ResourceNotFoundException {
        validateSale(sale);
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        existingSale.setBranch(sale.getBranch());
        existingSale.setSaleDate(sale.getSaleDate());
        existingSale.setAnnotations(sale.getAnnotations());
        existingSale.setModifiedAt(LocalDateTime.now());

        return saleRepository.save(existingSale);
    }

    @Override
    public void deleteSale(long id) throws ResourceNotFoundException {
        if (!saleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sale not found with id: " + id);
        }
        saleRepository.deleteById(id);
    }

    @Override
    public List<Sale> listSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSale(long id) throws ResourceNotFoundException {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
    }

    private void validateSale(Sale sale) throws InvalidDataException {
        if (sale.getBranch() == null) {
            throw new InvalidDataException("Branch is required");
        }
        if (sale.getSaleDate() == null) {
            throw new InvalidDataException("Sale date is required");
        }
    }

    @Override
    public boolean existsByBranchIdAndBranchSaleId(Long branchId, Long branchSaleId) {
        return saleRepository.existsByBranchIdAndBranchSaleId(branchId, branchSaleId);
    }

    @Override
    public Sale getByBranchIdAndBranchSaleId(Long branchId, Long branchSaleId) {
        return saleRepository.findByBranchIdAndBranchSaleId(branchId, branchSaleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
    }

    @Override
    public Sale saveFromDTO(BranchSaleDTO dto, Long branchId) {
        Sale sale = new Sale();
        sale.setBranch(branchRepository.findById(branchId).orElseThrow());
        sale.setSaleDate(dto.getSaleDate());
        sale.setAnnotations(dto.getAnnotations());
        sale.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        sale.setModifiedAt(LocalDateTime.now());
        sale.setBranchSaleId(dto.getId()); // must exist in entity

        return saleRepository.save(sale);
    }

}