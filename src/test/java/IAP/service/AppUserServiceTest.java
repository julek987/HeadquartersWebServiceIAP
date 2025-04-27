package IAP.service;

import IAP.configuration.TestConfig;
import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.Address;
import IAP.model.AppUser;
import IAP.model.Branch;
import IAP.repository.AddressRepository;
import IAP.repository.AppUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AppUserServiceTest {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AddressRepository addressRepository;

    private AppUser appUser;
    @Autowired
    private AppUserRepository appUserRepository;

    @Before
    public void setUp() {
        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setBranch(new Branch());
        appUser.setBranchUserId(1);

        appUser.setActive(true);
        appUser.setFirstName("firstName");
        appUser.setMiddleName("middleName");
        appUser.setLastName("LastName");
        appUser.setEmail("test@test.com");
        appUser.setPhoneNumber("1234567890");

        appUser.setAddress(new Address());
        appUser.setRole(4);

        appUser.setLogin("test-login");
        appUser.setPassword("test-password");

        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setModifiedAt(LocalDateTime.now());

        when(appUserRepository.save(appUser)).thenReturn(appUser);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(appUser));
    }

    @Test
    public void testAddAppUser() throws InvalidDataException {
        when(appUserRepository.save(appUser)).thenReturn(appUser);
        AppUser result = appUserService.addAppUser(appUser);
        assertEquals(appUser, result);
    }

    @Test
    public void testUpdateAppUser() throws InvalidDataException, ResourceNotFoundException {
        when(appUserRepository.existsById(appUser.getId())).thenReturn(true);
        when(appUserRepository.save(appUser)).thenReturn(appUser);
        AppUser result = appUserService.updateAppUser(appUser.getId(), appUser);
        assertEquals(appUser, result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateAppUser_NotFound() throws InvalidDataException, ResourceNotFoundException {
        when(appUserRepository.existsById(appUser.getId())).thenReturn(false);
        appUserService.updateAppUser(appUser.getId(), appUser);
    }

    @Test
    public void testDeleteAppUser() throws ResourceNotFoundException {
        when(appUserRepository.existsById(1L)).thenReturn(true);
        appUserService.deleteAppUser(1L);
        verify(appUserRepository).deleteById(1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteAppUser_NotFound() throws ResourceNotFoundException {
        when(appUserRepository.existsById(1L)).thenReturn(false);
        appUserService.deleteAppUser(1L);
    }

    @Test
    public void testListAppUsers() {
        List<AppUser> users = List.of(appUser);
        when(appUserRepository.findAll()).thenReturn(users);
        List<AppUser> result = appUserService.listAppUsers();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetAppUser() throws ResourceNotFoundException {
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(appUser));
        AppUser result = appUserService.getAppUser(1L);
        assertEquals(appUser, result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetAppUser_NotFound() throws ResourceNotFoundException {
        when(appUserRepository.findById(1L)).thenReturn(Optional.empty());
        appUserService.getAppUser(1L);
    }


}
