package IAP.controller;

import IAP.model.Branch;
import IAP.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    private final BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) { this.branchService = branchService; }

    @PostMapping
    public ResponseEntity<Branch> addBranch(@RequestBody Branch branch) {

        Timestamp now = new Timestamp(System.currentTimeMillis() / 1000);

        branch.setCreatedAt(now);
        branch.setModifiedAt(now);

        branchService.addBranch(branch);

        return new ResponseEntity<>(branch, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable long id, @RequestBody Branch branch) {
        Branch existingBranch = branchService.getBranch(id);
        if (existingBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Preserve the createdAt field from the existing entity
        branch.setCreatedAt(existingBranch.getCreatedAt());
        branch.setModifiedAt(new Timestamp(System.currentTimeMillis()  / 1000));

        branch.setId(id);
        branchService.updateBranch(branch);
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable long id) {
        branchService.deleteBranch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Branch>> listBranches() {
        List<Branch> branch = branchService.listBranch();
        return new ResponseEntity<>(branch, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Branch> getBranch(@PathVariable long id) {
        Branch branch = branchService.getBranch(id);
        if (branch != null) {
            return new ResponseEntity<>(branch, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
