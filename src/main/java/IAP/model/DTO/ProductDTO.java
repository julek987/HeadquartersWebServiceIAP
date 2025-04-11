package IAP.model.DTO;

import IAP.model.Product;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProductDTO {
    public long             id;
    public String           productName;
    public float            price;
    public int              width;
    public int              depth;
    public int              height;
    public long             addedById;
    public LocalDateTime    createdAt;
    public LocalDateTime    modifiedAt;
    public LocalDateTime    archivedAt;

    public ProductDTO() {}

    public ProductDTO(Product product) {
        this.id             = product.getId();
        this.productName    = product.getProductName();
        this.price          = product.getPrice();
        this.width          = product.getWidth();
        this.depth          = product.getDepth();
        this.height         = product.getHeight();
        this.addedById       = product.getAddedBy().getId();
        this.createdAt      = product.getCreatedAt();
        this.modifiedAt     = product.getModifiedAt();
        this.archivedAt     = product.getArchivedAt();
    }

}
