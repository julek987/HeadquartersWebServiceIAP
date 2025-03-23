package IAP.controller;

import IAP.model.Branch;
import IAP.model.Image;
import IAP.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) { this.imageService = imageService; }

    @PostMapping
    public ResponseEntity<Image> addBranch(@RequestBody Image image) {
        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);
        image.setCreatedAt(now);
        image.setModifiedAt(now);

        imageService.addImage(image);

        return new ResponseEntity<>(image, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateBranch(@PathVariable long id, @RequestBody Image image) {
        Image existingImage = imageService.getImage(id);
        if (existingImage == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Preserve the createdAt field from the existing entity
        image.setCreatedAt(existingImage.getCreatedAt());
        image.setModifiedAt(new Timestamp(System.currentTimeMillis()  / 1000));
        image.setId(id);

        imageService.updateImage(image);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable long id) {
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Image>> listBranches() {
        List<Image> images = imageService.listImage();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Image> getBranch(@PathVariable long id) {
        Image image = imageService.getImage(id);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
