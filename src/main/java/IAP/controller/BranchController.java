package IAP.controller;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import IAP.model.AppUser;
import IAP.model.Branch;
import IAP.model.DTO.BranchDTO;
import IAP.service.AddressService;
import IAP.service.AppUserService;
import IAP.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/branch")
public class BranchController {

    private final BranchService branchService;
    private final AddressService addressService;
    private final AppUserService appUserService;

    @Autowired
    public BranchController(BranchService branchService, AddressService addressService, AppUserService appUserService) {
        this.branchService = branchService;
        this.addressService = addressService;
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<List<BranchDTO>> listBranches() {
        List<Branch> branches = branchService.listBranch();
        List<BranchDTO> branchDTOs = branches.stream()
                .map(BranchDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(branchDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBranch(@PathVariable long id) {
        try {
            Branch branch = branchService.getBranch(id);
            if (branch == null) {
                throw new ResourceNotFoundException("Branch with ID " + id + " not found");
            }
            return ResponseEntity.ok(new BranchDTO(branch));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addBranch(@Valid @RequestBody BranchDTO branchDTO) {
        try {
            Address address = addressService.getAddress(branchDTO.addressId);
            if (address == null) {
                throw new ResourceNotFoundException("Address with ID " + branchDTO.addressId + " not found");
            }

            AppUser manager = appUserService.getAppUser(branchDTO.managerId);
            if (manager == null) {
                throw new ResourceNotFoundException("Manager with ID " + branchDTO.managerId + " not found");
            }

            Branch newBranch = new Branch();
            newBranch.setActive(branchDTO.active);
            newBranch.setName(branchDTO.name);
            newBranch.setAddress(address);
            newBranch.setManager(manager);
            newBranch.setCreatedAt(LocalDateTime.now());
            newBranch.setModifiedAt(LocalDateTime.now());

            branchService.addBranch(newBranch);
            BranchDTO savedBranchDTO = new BranchDTO(newBranch);
            return new ResponseEntity<>(savedBranchDTO, HttpStatus.CREATED);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBranch(@PathVariable long id, @Valid @RequestBody BranchDTO branchDTO) {
        try {
            Branch existingBranch = branchService.getBranch(id);
            if (existingBranch == null) {
                throw new ResourceNotFoundException("Branch with ID " + id + " not found");
            }

            Address address = addressService.getAddress(branchDTO.addressId);
            if (address == null) {
                throw new ResourceNotFoundException("Address with ID " + branchDTO.addressId + " not found");
            }

            AppUser manager = appUserService.getAppUser(branchDTO.managerId);
            if (manager == null) {
                throw new ResourceNotFoundException("Manager with ID " + branchDTO.managerId + " not found");
            }

            existingBranch.setActive(branchDTO.active);
            existingBranch.setName(branchDTO.name);
            existingBranch.setAddress(address);
            existingBranch.setManager(manager);
            existingBranch.setModifiedAt(LocalDateTime.now());

            branchService.updateBranch(existingBranch);
            BranchDTO updatedBranchDTO = new BranchDTO(existingBranch);
            return new ResponseEntity<>(updatedBranchDTO, HttpStatus.OK);
        } catch (ResourceNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBranch(@PathVariable long id) {
        try {
            branchService.deleteBranch(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
