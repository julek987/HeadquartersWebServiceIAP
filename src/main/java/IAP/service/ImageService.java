package IAP.service;

import IAP.model.Branch;
import IAP.model.Image;

import java.util.List;

public interface ImageService {

    public void addImage(Image image);
    public void updateImage(Image image);
    public void deleteImage(long id);
    public List<Image> listImage();
    public Image getImage(long id);

}
