package IAP.service;

import IAP.configuration.TestConfig;
import IAP.model.AppUser;
import IAP.model.Product;
import IAP.model.ProductChangeLog;
import IAP.model.objects.ProductChanges;
import IAP.repository.ProductChangeLogRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test; // ✅ JUnit 4

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ProductChangeLogServiceTest {

    @Autowired
    private ProductChangeLogService productChangeLogService;

    @Autowired
    private ProductChangeLogRepository productChangeLogRepository;

    private ProductChangeLog changeLog;

    @Before
    public void setup() {
        changeLog = new ProductChangeLog();
        changeLog.setId(1L);
        changeLog.setProduct(new Product());
        changeLog.setChangedBy(new AppUser());
        changeLog.setChangeReason("Price update");
        changeLog.setChanges(new ProductChanges()); // Assuming a no-arg constructor for test
        changeLog.setCreatedAt(LocalDateTime.now());

        when(productChangeLogRepository.save(changeLog)).thenReturn(changeLog);
        when(productChangeLogRepository.findById(1L)).thenReturn(Optional.of(changeLog));
    }

    @Test
    public void testAddProductChangeLog() {
        productChangeLogService.addProductChangeLog(changeLog);
        verify(productChangeLogRepository).save(changeLog);
    }

    @Test
    public void testUpdateProductChangeLog() {
        when(productChangeLogRepository.existsById(changeLog.getId())).thenReturn(true);
        productChangeLogService.updateProductChangeLog(changeLog);
        verify(productChangeLogRepository).save(changeLog);
    }

    @Test
    public void testUpdateProductChangeLog_NotFound() {
        when(productChangeLogRepository.existsById(changeLog.getId())).thenReturn(false);
        assertThrows(RuntimeException.class, () -> productChangeLogService.updateProductChangeLog(changeLog));
    }

    @Test
    public void testDeleteProductChangeLog() {
        when(productChangeLogRepository.existsById(1L)).thenReturn(true);
        productChangeLogService.deleteProductChangeLog(1L);
        verify(productChangeLogRepository).deleteById(1L);
    }

    @Test
    public void testDeleteProductChangeLog_NotFound() {
        when(productChangeLogRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> productChangeLogService.deleteProductChangeLog(1L));
    }

    @Test
    public void testListProductChangeLog() {
        List<ProductChangeLog> list = List.of(changeLog);
        when(productChangeLogRepository.findAll()).thenReturn(list);
        List<ProductChangeLog> result = productChangeLogService.listProductChangeLog();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetProductChangeLog() {
        when(productChangeLogRepository.findById(1L)).thenReturn(Optional.of(changeLog));
        ProductChangeLog result = productChangeLogService.getProductChangeLog(1L);
        assertEquals(changeLog, result);
    }

    @Test
    public void testGetProductChangeLog_NotFound() {
        when(productChangeLogRepository.findById(1L)).thenReturn(Optional.empty());
        ProductChangeLog result = productChangeLogService.getProductChangeLog(1L);
        assertNull(result);
    }
}
