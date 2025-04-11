package IAP.model.DTO;

import IAP.model.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ProductDTO {
    public long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must be less than 200 characters")
    public String productName;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    public float price;

    @NotNull(message = "Width is required")
    public int width;

    @NotNull(message = "Depth is required")
    public int depth;

    @NotNull(message = "Height is required")
    public int height;

    public long addedById;
    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;
    public LocalDateTime archivedAt;

    public ProductDTO() {}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.width = product.getWidth();
        this.depth = product.getDepth();
        this.height = product.getHeight();
        this.addedById = product.getAddedBy().getId();
        this.createdAt = product.getCreatedAt();
        this.modifiedAt = product.getModifiedAt();
        this.archivedAt = product.getArchivedAt();
    }
}
