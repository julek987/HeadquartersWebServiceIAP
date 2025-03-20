package IAP.service;

import IAP.model.AppUser;
import IAP.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    private AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void addAppUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public void updateAppUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    @Override
    public void deleteAppUser(long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public List<AppUser> listAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser getAppUser(long id) {
        return appUserRepository.findById(id);
    }
}
