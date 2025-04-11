package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Image;
import IAP.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public void addImage(Image image) throws InvalidDataException {
        validateImage(image);
        imageRepository.save(image);
    }

    @Override
    public void updateImage(Image image) throws ResourceNotFoundException, InvalidDataException {
        validateImage(image);

        if (!imageRepository.existsById(image.getId())) {
            throw new ResourceNotFoundException("Image not found with id: " + image.getId());
        }

        imageRepository.save(image);
    }

    @Override
    public void deleteImage(long id) throws ResourceNotFoundException {
        if (!imageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Image not found with id: " + id);
        }
        imageRepository.deleteById(id);
    }

    @Override
    public List<Image> listImage() {
        return imageRepository.findAll();
    }

    @Override
    public Image getImage(long id) throws ResourceNotFoundException {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
    }

    private void validateImage(Image image) throws InvalidDataException {
        // Assuming we check for file name, size, or other specific attributes
        if (image == null) {
            throw new InvalidDataException("Image data cannot be null");
        }
    }
}
