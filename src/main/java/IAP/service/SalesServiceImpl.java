package IAP.service;

import IAP.model.Sale;
import IAP.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;

    @Autowired
    public SalesServiceImpl(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    @Override
    public void addSale(Sale sale) {
        salesRepository.save(sale);
    }

    @Override
    public void updateSale(Sale sale) {
        salesRepository.save(sale);
    }

    @Override
    public void deleteSale(long id) {
        salesRepository.deleteById(id);
    }

    @Override
    public List<Sale> listSales() {
        return salesRepository.findAll();
    }

    @Override
    public Sale getSale(long id) {
        return salesRepository.findById(id);
    }
}
