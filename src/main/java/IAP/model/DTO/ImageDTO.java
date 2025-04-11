package IAP.model.DTO;

import IAP.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ImageDTO {

    public Long id;

    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotBlank(message = "URL is required")
    @Size(max = 255, message = "URL must be less than 255 characters")
    public String url;

    public int showOrder;

    public LocalDateTime createdAt;
    public LocalDateTime modifiedAt;

    public ImageDTO() {}

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.productId = image.getProduct().getId();
        this.url = image.getUrl();
        this.showOrder = image.getShowOrder();
        this.createdAt = image.getCreatedAt();
        this.modifiedAt = image.getModifiedAt();
    }
}
