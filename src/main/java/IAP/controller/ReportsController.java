package IAP.controller;

import IAP.service.OrderService;
import IAP.service.ProductService;
import IAP.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final SaleService saleService;
    private final OrderService orderService;

    @Autowired
    public ReportsController(
            SaleService saleService, OrderService orderService,
            ProductService productService
    ) {
        this.saleService = saleService;
        this.orderService = orderService;
    }



}
