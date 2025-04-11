package IAP.service;

import IAP.model.Product;
import IAP.model.ProductChangeLog;

import java.util.List;

public interface ProductChangeLogService {
    public void addProductChangeLog(ProductChangeLog productChangeLog);
    public void updateProductChangeLog(ProductChangeLog productChangeLog);
    public void deleteProductChangeLog(long id);
    public List<ProductChangeLog> listProductChangeLog();
    public ProductChangeLog getProductChangeLog(long id);
}
