package IAP.model.DTO;

import IAP.model.Product;

import java.util.Map;

public class BranchSalesReportDTO {

    public static class ProductSalesOverview {
        private Long    quantity;
        private Double  totalProductRevenue;

        public ProductSalesOverview(Long quantity, Double totalProductRevenue) {
            this.quantity = quantity;
            this.totalProductRevenue = totalProductRevenue;
        }

        public Long getQuantity() {
            return quantity;
        }

        public Double getTotalProductRevenue() {
            return totalProductRevenue;
        }
    }

    private Long                            salesCount;
    //  <product_id, (object)ProductSalesOverview>
    private Map<Long, ProductSalesOverview> productSalesOverview;
    private Long                            productsSoldCount;
    private Double                          totalRevenue;

    public BranchSalesReportDTO(Long salesCount, Map<Long, ProductSalesOverview> productSalesOverview) {
        this.salesCount = salesCount;
        this.productSalesOverview = productSalesOverview;
        this.productsSoldCount = productSalesOverview.values().stream()
                .mapToLong(ProductSalesOverview::getQuantity)
                .sum();
        this.totalRevenue = productSalesOverview.values().stream()
                .mapToDouble(ProductSalesOverview::getTotalProductRevenue)
                .sum();
    }

    public Long getSalesCount() {
        return salesCount;
    }

    public Map<Long, ProductSalesOverview> getProductSalesOverview() {
        return productSalesOverview;
    }

    public Long getProductsSoldCount() {
        return productsSoldCount;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }
}

