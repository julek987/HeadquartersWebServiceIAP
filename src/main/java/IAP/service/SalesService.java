package IAP.service;

import IAP.model.Sales;
import java.util.List;

public interface SalesService {
    void addSale(Sales sale);
    void updateSale(Sales sale);
    void deleteSale(long id);
    List<Sales> listSales();
    Sales getSale(long id);
}
