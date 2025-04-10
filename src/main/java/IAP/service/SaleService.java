package IAP.service;

import IAP.model.Sale;
import java.util.List;

public interface SaleService {
    void addSale(Sale sale);
    void updateSale(Sale sale);
    void deleteSale(long id);
    List<Sale> listSales();
    Sale getSale(long id);
}
