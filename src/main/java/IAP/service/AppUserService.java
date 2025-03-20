package IAP.service;

import IAP.model.AppUser;

import java.util.List;

public interface AppUserService {

    public void addAppUser(AppUser appUser);
    public void updateAppUser(AppUser appUser);
    public void deleteAppUser(long id);
    public List<AppUser> listAppUsers();
    public AppUser getAppUser(long id);

}
