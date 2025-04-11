package IAP.controller;

import IAP.model.Address;
import IAP.model.AppUser;
import IAP.model.Branch;
import IAP.model.DTO.BranchDTO;
import IAP.service.AddressService;
import IAP.service.AppUserService;
import IAP.service.BranchService;
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
@RequestMapping("/api/branch")
public class BranchController {
    private final BranchService branchService;
    private final AddressService addressService;
    private final AppUserService appUserService;

    @Autowired
    public BranchController(
            BranchService branchService,
            AddressService addressService,
            AppUserService appUserService
    ) {
        this.branchService = branchService;
        this.addressService = addressService;
        this.appUserService = appUserService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BranchDTO>> listBranches() {
        List<Branch> listBranch = branchService.listBranch();
        List<BranchDTO> listBranchDTO = new ArrayList<>(0);
        for (Branch branch : listBranch)
            listBranchDTO.add(new BranchDTO(branch));

        return new ResponseEntity<>(listBranchDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BranchDTO> getBranch(@PathVariable long id) {
        Branch branch = branchService.getBranch(id);
        if (branch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BranchDTO branchDTO = new BranchDTO(branch);
        return new  ResponseEntity<>(branchDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BranchDTO> addBranch(
            @RequestBody BranchDTO branchDTO
    ) {
        Address existingAddress = addressService.getAddress(branchDTO.addressId);
        if (existingAddress == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppUser existingManager = appUserService.getAppUser(branchDTO.managerId);
        if (existingManager == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Branch newBranch = new  Branch();

        newBranch.setActive(branchDTO.active);
        newBranch.setName(branchDTO.name);
        newBranch.setAddress(existingAddress);
        newBranch.setManager(existingManager);
        newBranch.setCreatedAt(LocalDateTime.now());
        newBranch.setModifiedAt(LocalDateTime.now());

        branchService.addBranch(newBranch);
        BranchDTO savedBranchDTO = new BranchDTO(newBranch);
        return new ResponseEntity<>(savedBranchDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchDTO> updateBranch(
            @PathVariable long id,
            @RequestBody BranchDTO branchDTO
    ) {
        Branch existingBranch = branchService.getBranch(id);
        if (existingBranch == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Address existingAddress = addressService.getAddress(branchDTO.addressId);
        if (existingAddress == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppUser existingManager = appUserService.getAppUser(branchDTO.managerId);
        if (existingManager == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        existingBranch.setActive(branchDTO.active);
        existingBranch.setName(branchDTO.name);
        existingBranch.setAddress(existingAddress);
        existingBranch.setManager(existingManager);
        existingBranch.setModifiedAt(LocalDateTime.now());

        branchService.updateBranch(existingBranch);
        BranchDTO updatedBranchDTO = new BranchDTO(existingBranch);
        return new ResponseEntity<>(updatedBranchDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable long id) {
        branchService.deleteBranch(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
