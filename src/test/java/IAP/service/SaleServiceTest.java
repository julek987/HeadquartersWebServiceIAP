package IAP.service;

import IAP.configuration.TestConfig;
import IAP.exception.InvalidDataException;
import IAP.exception.ResourceNotFoundException;
import IAP.model.AppUser;
import IAP.model.Branch;
import IAP.model.Sale;
import IAP.repository.SaleRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test; // ✅ JUnit 4

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SaleServiceTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleRepository saleRepository;

    private Sale sale;

    @Before
    public void setup() {
        sale = new Sale();
        sale.setId(1L);
        sale.setBranch(new Branch());
        sale.setSaleDate(LocalDateTime.now());
        sale.setAnnotations("First sale of the day");
        sale.setCreatedAt(LocalDateTime.now());
        sale.setModifiedAt(LocalDateTime.now());

        when(saleRepository.save(sale)).thenReturn(sale);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
    }

    @Test
    public void testAddSale() throws InvalidDataException, ResourceNotFoundException {
        Sale result = saleService.addSale(sale);
        assertEquals(sale, result);
        verify(saleRepository).save(sale);
    }

    @Test
    public void testUpdateSale() throws InvalidDataException, ResourceNotFoundException {
        when(saleRepository.existsById(sale.getId())).thenReturn(true);
        when(saleRepository.save(sale)).thenReturn(sale);
        Sale result = saleService.updateSale(sale.getId(), sale);
        assertEquals(sale, result);
    }

//    @Test
//    public void testUpdateSale_NotFound() {
//        when(saleRepository.existsById(sale.getId())).thenReturn(false);
//        assertThrows(ResourceNotFoundException.class, () ->
//                saleService.updateSale(sale.getId(), sale));
//    }

    @Test
    public void testDeleteSale() throws ResourceNotFoundException {
        when(saleRepository.existsById(1L)).thenReturn(true);
        saleService.deleteSale(1L);
        verify(saleRepository).deleteById(1L);
    }

    @Test
    public void testDeleteSale_NotFound() {
        when(saleRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () ->
                saleService.deleteSale(1L));
    }

    @Test
    public void testListSales() {
        List<Sale> sales = List.of(sale);
        when(saleRepository.findAll()).thenReturn(sales);
        List<Sale> result = saleService.listSales();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetSale() throws ResourceNotFoundException {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        Sale result = saleService.getSale(1L);
        assertEquals(sale, result);
    }

    @Test
    public void testGetSale_NotFound() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                saleService.getSale(1L));
    }
}
