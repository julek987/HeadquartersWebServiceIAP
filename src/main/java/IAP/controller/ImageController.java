package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.DTO.ImageDTO;
import IAP.model.Image;
import IAP.model.Product;
import IAP.service.ImageService;
import IAP.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;
    private final ProductService productService;

    @Autowired
    public ImageController(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> listImages() {
        List<Image> images = imageService.listImage();
        List<ImageDTO> imageDTOs = images.stream()
                .map(ImageDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImage(@PathVariable long id) {
        try {
            Image image = imageService.getImage(id);
            if (image == null) {
                throw new ResourceNotFoundException("Image with ID " + id + " not found");
            }
            return ResponseEntity.ok(new ImageDTO(image));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addImage(@Valid @RequestBody ImageDTO imageDTO) {
        try {
            Product existingProduct = productService.getProduct(imageDTO.productId);
            if (existingProduct == null) {
                throw new ResourceNotFoundException("Product with ID " + imageDTO.productId + " not found");
            }

            Image newImage = new Image();
            newImage.setUrl(imageDTO.url);
            newImage.setProduct(existingProduct);
            newImage.setShowOrder(imageDTO.showOrder);
            newImage.setCreatedAt(LocalDateTime.now());
            newImage.setModifiedAt(LocalDateTime.now());

            imageService.addImage(newImage);
            ImageDTO savedImageDTO = new ImageDTO(newImage);
            return new ResponseEntity<>(savedImageDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImage(@PathVariable long id, @Valid @RequestBody ImageDTO imageDTO) {
        try {
            Image existingImage = imageService.getImage(id);
            if (existingImage == null) {
                throw new ResourceNotFoundException("Image with ID " + id + " not found");
            }

            Product existingProduct = productService.getProduct(imageDTO.productId);
            if (existingProduct == null) {
                throw new ResourceNotFoundException("Product with ID " + imageDTO.productId + " not found");
            }

            existingImage.setUrl(imageDTO.url);
            existingImage.setProduct(existingProduct);
            existingImage.setShowOrder(imageDTO.showOrder);
            existingImage.setModifiedAt(LocalDateTime.now());

            imageService.updateImage(existingImage);
            ImageDTO updatedImageDTO = new ImageDTO(existingImage);
            return new ResponseEntity<>(updatedImageDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
