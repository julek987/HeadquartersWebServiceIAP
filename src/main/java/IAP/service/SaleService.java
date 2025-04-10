package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Sale;
import java.util.List;

public interface SaleService {
    Sale addSale(Sale sale) throws InvalidDataException, ResourceNotFoundException;
    Sale updateSale(long id, Sale sale) throws InvalidDataException, ResourceNotFoundException;
    void deleteSale(long id) throws ResourceNotFoundException;
    List<Sale> listSales();
    Sale getSale(long id) throws ResourceNotFoundException;
}