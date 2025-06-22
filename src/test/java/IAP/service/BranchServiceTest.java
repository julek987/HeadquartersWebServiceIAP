package IAP.service;

import IAP.configuration.TestConfig;
import IAP.exception.InvalidDataException;
import IAP.model.Address;
import IAP.model.AppUser;
import IAP.model.Branch;
import IAP.repository.BranchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class BranchServiceTest {

    @Autowired
    private BranchService branchService;

    @Autowired
    private BranchRepository branchRepository;

    private Branch branch;

    @Before
    public void setUp() {
        branch = new Branch();
        branch.setId(1L);
        branch.setActive(true);
        branch.setName("test");
        branch.setAddress(new Address());
        branch.setManager(new AppUser());
        branch.setCreatedAt(LocalDateTime.now());
        branch.setModifiedAt(LocalDateTime.now());

        when(branchRepository.save(branch)).thenReturn(branch);
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
    }

    @Test
    public void testAddBranch() throws InvalidDataException {
        when(branchRepository.save(branch)).thenReturn(branch);
        branchService.addBranch(branch);
        verify(branchRepository).save(branch);
    }

    @Test
    public void testUpdateBranch() {
        when(branchRepository.existsById(branch.getId())).thenReturn(true);
        branchService.updateBranch(branch);
        verify(branchRepository).save(branch);
    }

//    @Test
//    public void testUpdateBranch_NotFound() {
//        when(branchRepository.existsById(branch.getId())).thenReturn(false);
//        assertThrows(RuntimeException.class, () -> branchService.updateBranch(branch));
//    }

    @Test
    public void testDeleteBranch() {
        when(branchRepository.existsById(1L)).thenReturn(true);
        branchService.deleteBranch(1L);
        verify(branchRepository).deleteById(1L);
    }

    @Test
    public void testDeleteBranch_NotFound() {
        when(branchRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> branchService.deleteBranch(1L));
    }


    @Test
    public void testListBranch() {
        List<Branch> branches = List.of(branch);
        when(branchRepository.findAll()).thenReturn(branches);
        List<Branch> result = branchService.listBranch();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetBranch() {
        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        Branch result = branchService.getBranch(1L);
        assertEquals(branch, result);
    }

//    @Test
//    public void testGetBranch_NotFound() {
//        when(branchRepository.findById(1L)).thenReturn(Optional.empty());
//        Branch result = branchService.getBranch(1L);
//        assertNull(result);
//    }


}
