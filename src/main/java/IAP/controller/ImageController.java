package IAP.controller;

import IAP.model.DTO.ImageDTO;
import IAP.model.Image;
import IAP.model.Product;
import IAP.service.ImageService;
import IAP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;
    private final ProductService productService;

    @Autowired
    public ImageController(
            ImageService imageService,
            ProductService productService
    ) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ImageDTO>> listBranches() {
        List<Image> listImage = imageService.listImage();
        List<ImageDTO> listImageDTO = new ArrayList<>(0);
        for (Image image : listImage)
            listImageDTO.add(new ImageDTO(image));

        return new ResponseEntity<>(listImageDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageDTO> getBranch(@PathVariable long id) {
        Image image = imageService.getImage(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ImageDTO imageDTO = new ImageDTO(image);
        return new ResponseEntity<>(imageDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ImageDTO> addBranch(
            @RequestBody ImageDTO imageDTO
    ) {
        Product existingProduct = productService.getProduct(imageDTO.productId);
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Image image = new Image();
        image.setId(imageDTO.id);
        image.setUrl(imageDTO.url);
        image.setProduct(existingProduct);
        image.setShowOrder(imageDTO.showOrder);
        image.setCreatedAt(LocalDateTime.now());
        image.setModifiedAt(LocalDateTime.now());

        imageService.addImage(image);

        ImageDTO newImageDTO = new ImageDTO(image);
        return new ResponseEntity<>(newImageDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateBranch(
            @PathVariable long id,
            @RequestBody ImageDTO imageDTO
    ) {
        Image existingImage = imageService.getImage(id);
        if (existingImage == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Product existingProduct = productService.getProduct(imageDTO.productId);
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        existingImage.setUrl(imageDTO.url);
        existingImage.setProduct(existingProduct);
        existingImage.setShowOrder(imageDTO.showOrder);
        existingImage.setModifiedAt(LocalDateTime.now());

        imageService.updateImage(existingImage);

        ImageDTO updatedImageDTO = new ImageDTO(existingImage);
        return new ResponseEntity<>(updatedImageDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable long id) {
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}