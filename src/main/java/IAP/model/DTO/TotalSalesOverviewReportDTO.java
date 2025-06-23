package IAP.model.DTO;

import java.util.Map;

public class TotalSalesOverviewReportDTO {

    public static class BranchSalesOverview {
        private Long    totalBranchSales;
        private Long    totalBranchProductsSold;
        private Double  totalBranchRevenue;

        public BranchSalesOverview(
                Long totalBranchSales,
                Long totalBranchProductsSold,
                Double totalBranchRevenue) {
            this.totalBranchSales   = totalBranchSales;
            this.totalBranchProductsSold  = totalBranchProductsSold;
            this.totalBranchRevenue = totalBranchRevenue;
        }

        public Long getTotalBranchSales() {
            return totalBranchSales;
        }

        public Long getTotalBranchProductsSold() {
            return totalBranchProductsSold;
        }

        public Double getTotalBranchRevenue() {
            return totalBranchRevenue;
        }
    }

    //  <branch_id, (object)BranchSalesOverview>
    private Map<Long, BranchSalesOverview>  branchSalesOverview;
    private Long                            salesCount;
    private Long                            totalProductsSold;
    private Double                          totalRevenue;

    public TotalSalesOverviewReportDTO(
            Map<Long, BranchSalesOverview> branchSalesOverview) {
        this.branchSalesOverview = branchSalesOverview;
        this.salesCount = branchSalesOverview.values().stream()
                .mapToLong(BranchSalesOverview::getTotalBranchSales)
                .sum();
        this.totalProductsSold = branchSalesOverview.values().stream()
                .mapToLong(BranchSalesOverview::getTotalBranchProductsSold)
                .sum();
        this.totalRevenue = branchSalesOverview.values().stream()
                .mapToDouble(BranchSalesOverview::getTotalBranchRevenue)
                .sum();
    }

    public Map<Long, BranchSalesOverview> getBranchSalesOverview() {
        return branchSalesOverview;
    }

    public Long getSalesCount() {
        return salesCount;
    }

    public Long getTotalProductsSold() {
        return totalProductsSold;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

}
