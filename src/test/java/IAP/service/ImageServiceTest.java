package IAP.service;

import IAP.configuration.TestConfig;
import IAP.model.Image;
import IAP.model.Product;
import IAP.repository.ImageRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test; // ✅ JUnit 4

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageRepository imageRepository;

    private Image image;

    @Before
    public void setup() {
        image = new Image();
        image.setId(1L);
        image.setUrl("https://example.com/image.jpg");
        image.setShowOrder(1);
        image.setProduct(new Product());
        image.setCreatedAt(LocalDateTime.now());
        image.setModifiedAt(LocalDateTime.now());

        when(imageRepository.save(image)).thenReturn(image);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    }

    @Test
    public void testAddImage() {
        imageService.addImage(image);
        verify(imageRepository).save(image);
    }

    @Test
    public void testUpdateImage() {
        when(imageRepository.existsById(image.getId())).thenReturn(true);
        imageService.updateImage(image);
        verify(imageRepository).save(image);
    }

    @Test
    public void testUpdateImage_NotFound() {
        when(imageRepository.existsById(image.getId())).thenReturn(false);
        assertThrows(RuntimeException.class, () -> imageService.updateImage(image));
    }

    @Test
    public void testDeleteImage() {
        when(imageRepository.existsById(1L)).thenReturn(true);
        imageService.deleteImage(1L);
        verify(imageRepository).deleteById(1L);
    }

    @Test
    public void testDeleteImage_NotFound() {
        when(imageRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> imageService.deleteImage(1L));
    }

    @Test
    public void testListImage() {
        List<Image> images = List.of(image);
        when(imageRepository.findAll()).thenReturn(images);
        List<Image> result = imageService.listImage();
        assertEquals(1, result.size());
    }

//    @Test
//    public void testGetImage() {
//        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
//        Image result = imageService.getImage(1L);
//        assertEquals(image, result);
//    }

//    @Test
//    public void testGetImage_NotFound() {
//        when(imageRepository.findById(1L)).thenReturn(Optional.empty());
//        Image result = imageService.getImage(1L);
//        assertNull(result);
//    }
}
