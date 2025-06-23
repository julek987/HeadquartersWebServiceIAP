package IAP.controller;

import IAP.model.DTO.BranchSalesReportDTO;
import IAP.model.DTO.TotalSalesOverviewReportDTO;
import IAP.model.Order;
import IAP.model.Product;
import IAP.model.Sale;
import IAP.service.OrderService;
import IAP.service.ProductService;
import IAP.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59);

        // Step 1: Get sales in month
        List<Sale> sales = saleService.listSales();
        List<Sale> filteredSales = sales.stream()
                .filter(sale -> {
                    LocalDateTime date = sale.getSaleDate();
                    return date != null && !date.isBefore(startOfMonth) && !date.isAfter(endOfMonth) && sale.getBranch().getId() == branchId;
                })
                .toList();

        // Step 2: Get all orders for those sales
        List<Order> allOrders = new ArrayList<>();
        filteredSales.forEach(sale -> allOrders.addAll(orderService.listOrderBySale(sale)));

        // Step 3: Aggregate by product
        Map<Long, BranchSalesReportDTO.ProductSalesOverview> productSales = new HashMap<>();

        for (Order order : allOrders) {
            Product product = order.getProduct();
            if (product == null) continue;

            Long productId = product.getId();
            Long quantity = order.getQuantitySold();
            double revenue = quantity * order.getSalePrice();

            productSales.merge(
                    productId,
                    new BranchSalesReportDTO.ProductSalesOverview(quantity, revenue),
                    (oldVal, newVal) -> new BranchSalesReportDTO.ProductSalesOverview(
                            oldVal.getQuantity() + newVal.getQuantity(),
                            oldVal.getTotalProductRevenue() + newVal.getTotalProductRevenue()
                    )
            );
        }

        // Step 5: Build report
        BranchSalesReportDTO report = new BranchSalesReportDTO((long) filteredSales.size() , productSales);
        return ResponseEntity.ok(report);
    }


    @GetMapping("/overview/{year}-{month}")
    public ResponseEntity<TotalSalesOverviewReportDTO> getOverviewReport(
            @PathVariable Integer year,
            @PathVariable Integer month) {

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth
                .with(TemporalAdjusters.lastDayOfMonth())
                .withHour(23).withMinute(59).withSecond(59);

        List<Sale> sales = saleService.listSales();

        // Filter sales in given month
        List<Sale> filteredSales = sales.stream()
                .filter(sale -> {
                    LocalDateTime date = sale.getSaleDate();
                    return date != null && !date.isBefore(startOfMonth) && !date.isAfter(endOfMonth);
                })
                .toList();

        Map<Long, TotalSalesOverviewReportDTO.BranchSalesOverview> reportMap = new HashMap<>();

        for (Sale sale : filteredSales) {
            Long branchId = sale.getBranch().getId();
            List<Order> orders = orderService.listOrderBySale(sale);

            long totalQuantity = orders.stream()
                    .mapToLong(Order::getQuantitySold)
                    .sum();

            double totalRevenue = orders.stream()
                    .mapToDouble(order -> order.getQuantitySold() * order.getSalePrice())
                    .sum();

            reportMap.merge(
                    branchId,
                    new TotalSalesOverviewReportDTO.BranchSalesOverview(1L, totalQuantity, totalRevenue),
                    (oldVal, newVal) -> new TotalSalesOverviewReportDTO.BranchSalesOverview(
                            oldVal.getTotalBranchSales() + newVal.getTotalBranchSales(),
                            oldVal.getTotalBranchProductsSold() + newVal.getTotalBranchProductsSold(),
                            oldVal.getTotalBranchRevenue() + newVal.getTotalBranchRevenue()
                    )
            );
        }

        return ResponseEntity.ok(new TotalSalesOverviewReportDTO(reportMap));
    }


}
