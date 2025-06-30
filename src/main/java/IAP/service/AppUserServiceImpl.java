package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.AppUser;
import IAP.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUser addAppUser(AppUser appUser) throws InvalidDataException {
        validateAppUser(appUser);

        if (appUserRepository.existsByLogin(appUser.getLogin())) {
            throw new InvalidDataException("Login already exists");
        }

        if (appUserRepository.existsByEmail(appUser.getEmail())) {
            throw new InvalidDataException("Email already exists");
        }

        // Don't set timestamps here - let the controller handle them
        // appUser.setCreatedAt(LocalDateTime.now());
        // appUser.setModifiedAt(LocalDateTime.now());

        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser updateAppUser(long id, AppUser appUser)
            throws ResourceNotFoundException, InvalidDataException {
        validateAppUser(appUser);

        AppUser existingUser = appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!existingUser.getLogin().equals(appUser.getLogin()) &&
                appUserRepository.existsByLogin(appUser.getLogin())) {
            throw new InvalidDataException("Login already taken by another user");
        }

        if (!existingUser.getEmail().equals(appUser.getEmail()) &&
                appUserRepository.existsByEmail(appUser.getEmail())) {
            throw new InvalidDataException("Email already taken by another user");
        }

        // Update fields from the passed appUser object
        existingUser.setFirstName(appUser.getFirstName());
        existingUser.setLastName(appUser.getLastName());
        existingUser.setEmail(appUser.getEmail());
        existingUser.setPhoneNumber(appUser.getPhoneNumber());
        existingUser.setLogin(appUser.getLogin());
        existingUser.setRole(appUser.getRole());
        existingUser.setAddress(appUser.getAddress());
        existingUser.setBranch(appUser.getBranch());

        // Only update password if it's provided and different
        if (StringUtils.hasText(appUser.getPassword()) &&
                !appUser.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(appUser.getPassword());
        }

        existingUser.setModifiedAt(appUser.getModifiedAt()); // Use the timestamp from controller

        return appUserRepository.save(existingUser);
    }

    @Override
    public void deleteAppUser(long id) throws ResourceNotFoundException {
        if (!appUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        appUserRepository.deleteById(id);
    }

    @Override
    public List<AppUser> listAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser getAppUser(long id) throws ResourceNotFoundException {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<AppUser> listManagers() throws ResourceNotFoundException {
        return appUserRepository.findByRole(0)
                .orElseThrow(() -> new ResourceNotFoundException("Managers not found"));
    }

    @Override
    public List<AppUser> listDirectors(long id) throws ResourceNotFoundException {
        return appUserRepository.findByRole(1)
                .orElseThrow(() -> new ResourceNotFoundException("Directors not found"));
    }

    private void validateAppUser(AppUser appUser) throws InvalidDataException {
        if (!StringUtils.hasText(appUser.getFirstName())) {
            throw new InvalidDataException("First name is required");
        }

        if (!StringUtils.hasText(appUser.getLastName())) {
            throw new InvalidDataException("Last name is required");
        }

        if (!StringUtils.hasText(appUser.getEmail())) {
            throw new InvalidDataException("Email is required");
        }

        if (!StringUtils.hasText(appUser.getLogin())) {
            throw new InvalidDataException("Login is required");
        }

        // Password validation - allow empty password for updates
        if (appUser.getPassword() != null && appUser.getPassword().trim().isEmpty()) {
            throw new InvalidDataException("Password cannot be empty");
        }
    }
}