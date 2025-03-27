package IAP.service;

import IAP.model.Sales;
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
    public void addSale(Sales sale) {
        salesRepository.save(sale);
    }

    @Override
    public void updateSale(Sales sale) {
        salesRepository.save(sale);
    }

    @Override
    public void deleteSale(long id) {
        salesRepository.deleteById(id);
    }

    @Override
    public List<Sales> listSales() {
        return salesRepository.findAll();
    }

    @Override
    public Sales getSale(long id) {
        return salesRepository.findById(id);
    }
}
