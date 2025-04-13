package IAP.configuration;

import IAP.repository.AddressRepository;
import IAP.repository.AppUserRepository;
import IAP.repository.BranchRepository;
import IAP.service.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {


    //                ----- Address -----
    @Bean
    public AddressRepository addressRepository() {
        return Mockito.mock(AddressRepository.class);
    }

    @Bean
    public AddressService addressService(AddressRepository repo) {
        return new AddressServiceImpl(repo);
    }

    //                ----- AppUser -----
    @Bean
    public AppUserRepository appUserRepository() {
        return Mockito.mock(AppUserRepository.class);
    }

    @Bean
    public AppUserService appUserService(AppUserRepository repo) {
        return new AppUserServiceImpl(repo);
    }

    //                ----- Branch -----
    @Bean
    public BranchRepository branchRepository() {
        return Mockito.mock(BranchRepository.class);
    }

    @Bean
    public BranchService branchService(BranchRepository repo) {
        return new BranchServiceImpl(repo);
    }

}
