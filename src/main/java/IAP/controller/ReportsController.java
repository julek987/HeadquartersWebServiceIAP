package IAP.controller;

import IAP.model.DTO.BranchSalesReportDTO;
import IAP.model.DTO.TotalSalesOverviewReportDTO;
import IAP.model.Product;
import IAP.service.OrderService;
import IAP.service.ProductService;
import IAP.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final SaleService saleService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public ReportsController(
            SaleService saleService,
            OrderService orderService,
            ProductService productService) {
        this.saleService = saleService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/{branch_id}/{year}-{month}")
    public ResponseEntity<BranchSalesReportDTO> getBranchReport(
            @PathVariable("branch_id") Long branchId,
            @PathVariable Integer year,
            @PathVariable Integer month) {

        Map<Long, BranchSalesReportDTO.ProductSalesOverview> productSales = new HashMap<>();
        productSales.put(1L,   new BranchSalesReportDTO.ProductSalesOverview(15L, 34d));
        productSales.put(11L,  new BranchSalesReportDTO.ProductSalesOverview(22L, 123.44d));
        productSales.put(5L,   new BranchSalesReportDTO.ProductSalesOverview(1L, 294.77d));
        productSales.put(3L,   new BranchSalesReportDTO.ProductSalesOverview(19L, 2312.59d));

        BranchSalesReportDTO report = new BranchSalesReportDTO(16L, productSales);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/overview/{year}-{month}")
    public ResponseEntity<TotalSalesOverviewReportDTO> getOverviewReport(
            @PathVariable Integer year,
            @PathVariable Integer month) {

        Map<Long, TotalSalesOverviewReportDTO.BranchSalesOverview>  sales = new HashMap<>();
        sales.put(
                1L,
                new TotalSalesOverviewReportDTO.BranchSalesOverview(
                        10L, 123L, 1500d
                )
        );
        sales.put(
                2L,
                new TotalSalesOverviewReportDTO.BranchSalesOverview(
                        50L, 554L, 55432.94d
                )
        );
        sales.put(
                3L,
                new TotalSalesOverviewReportDTO.BranchSalesOverview(
                        3L, 50L, 10500.50d
                )
        );

        return ResponseEntity.ok(new TotalSalesOverviewReportDTO(sales));
    }

}
