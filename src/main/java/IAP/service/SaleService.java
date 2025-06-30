package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.BranchSaleDTO;
import IAP.model.Sale;
import java.util.List;
import java.util.Optional;

public interface SaleService {
    Sale addSale(Sale sale) throws InvalidDataException, ResourceNotFoundException;
    Sale updateSale(long id, Sale sale) throws InvalidDataException, ResourceNotFoundException;
    void deleteSale(long id) throws ResourceNotFoundException;
    List<Sale> listSales();
    Sale getSale(long id) throws ResourceNotFoundException;
    boolean existsByBranchIdAndBranchSaleId(Long branchId, Long branchSaleId);
    Sale getByBranchIdAndBranchSaleId(Long branchId, Long branchSaleId);
    Sale saveFromDTO(BranchSaleDTO dto, Long branchId);
}