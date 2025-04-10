package IAP.model.DTO;

import IAP.model.Order;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderDTO {

    public Long id;
    public Long branchId;
    public Long SaleId;
    public Long productId;
    public Integer quantitySold;
    public BigDecimal salePrice;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

    public OrderDTO() {}

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.branchId = order.getBranch().getId();
        this.SaleId = order.getSale().getId();
        this.productId = order.getProduct().getId();
        this.quantitySold = order.getQuantitySold();
        this.salePrice = order.getSalePrice();
        this.createdAt = order.getCreatedAt();
        this.modifiedAt = order.getModifiedAt();
    }
}
