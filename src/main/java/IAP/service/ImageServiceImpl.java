package IAP.service;

import IAP.model.Image;
import IAP.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) { this.imageRepository = imageRepository; }

    @Override
    public void addImage(Image image) { imageRepository.save(image); }

    @Override
    public void updateImage(Image image) { imageRepository.save(image); }

    @Override
    public void deleteImage(long id) { imageRepository.deleteById(id); }

    @Override
    public List<Image> listImage() { return imageRepository.findAll(); }

    @Override
    public Image getImage(long id) { return imageRepository.findById(id); }

}
