package IAP.model.DTO;

import IAP.model.Image;

import java.sql.Timestamp;

public class ImageDTO {
    public long         id;
    public long         productId;
    public String       url;
    public int          showOrder;
    public Timestamp    createdAt;
    public Timestamp    modifiedAt;

    public ImageDTO() {}

    public ImageDTO(Image image) {
        this.id         = image.getId();
        this.productId  = image.getProduct().getId();
        this.url        = image.getUrl();
        this.showOrder  = image.getShowOrder();
        this.createdAt  = image.getCreatedAt();
        this.modifiedAt = image.getModifiedAt();
    }

}