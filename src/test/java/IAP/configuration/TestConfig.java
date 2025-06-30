package IAP.configuration;

import IAP.repository.*;
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

    //                ----- Image -----
    @Bean
    public ImageRepository imageRepository() {
        return Mockito.mock(ImageRepository.class);
    }

    @Bean
    public ImageService imageService(ImageRepository repo) {
        return new ImageServiceImpl(repo);
    }

    //                ----- Order -----
    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository,  ProductRepository productRepository) {
        return new OrderServiceImpl(orderRepository, productRepository);
    }

    //                ----- Product -----
    @Bean
    public ProductRepository productRepository() {
        return Mockito.mock(ProductRepository.class);
    }

    @Bean
    public ProductService productService(ProductRepository repo) {
        return new ProductServiceImpl(repo);
    }

    //                ----- ProductChangeLog -----
    @Bean
    public ProductChangeLogRepository productChangeLogRepository() {
        return Mockito.mock(ProductChangeLogRepository.class);
    }

    @Bean
    public ProductChangeLogService productChangeLogService(ProductChangeLogRepository repo) {
        return new ProductChangeLogServiceImpl(repo);
    }

    //                ----- Sale -----
    @Bean
    public SaleRepository saleRepository() {
        return Mockito.mock(SaleRepository.class);
    }

    @Bean
    public SaleService saleService(SaleRepository saleRepository, BranchRepository branchRepository) {
        return new SaleServiceImpl(saleRepository,  branchRepository);
    }

}
