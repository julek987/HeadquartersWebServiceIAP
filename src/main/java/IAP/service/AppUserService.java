package IAP.service;

import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.AppUser;
import java.util.List;

public interface AppUserService {
    AppUser addAppUser(AppUser appUser) throws InvalidDataException;
    AppUser updateAppUser(long id, AppUser appUser) throws ResourceNotFoundException, InvalidDataException;
    void deleteAppUser(long id) throws ResourceNotFoundException;
    List<AppUser> listAppUsers();
    AppUser getAppUser(long id) throws ResourceNotFoundException;
}