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

        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setModifiedAt(LocalDateTime.now());
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

        existingUser.setFirstName(appUser.getFirstName());
        existingUser.setLastName(appUser.getLastName());
        existingUser.setEmail(appUser.getEmail());
        existingUser.setPhoneNumber(appUser.getPhoneNumber());
        existingUser.setLogin(appUser.getLogin());
        existingUser.setPassword(appUser.getPassword());
        existingUser.setModifiedAt(LocalDateTime.now());

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

        if (!StringUtils.hasText(appUser.getPassword())) {
            throw new InvalidDataException("Password is required");
        }
    }
}