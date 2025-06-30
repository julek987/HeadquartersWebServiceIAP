package IAP.model.DTO;

import IAP.model.Sale;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class BranchSaleDTO {

    private Long id;

    @NotNull(message = "Sold by ID is required")
    private Long soldById;

    @Size(max = 500, message = "Annotations must be less than 500 characters")
    private String annotations;

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime saleDate;

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime modifiedAt;

    public BranchSaleDTO() {
    }

    public BranchSaleDTO(Sale sale) {
        this.id = sale.getId();
        this.soldById = 0L;
        this.saleDate = sale.getSaleDate();
        this.annotations = sale.getAnnotations();
        this.createdAt = sale.getCreatedAt();
        this.modifiedAt = sale.getModifiedAt();
    }

    @Override
    public String toString() {
        return String.format(
                "BranchSaleDTO{id=%s, soldById=%s, saleDate=%s, createdAt=%s, modifiedAt=%s, annotations='%s'}",
                id != null ? id : "null",
                soldById != null ? soldById : "null",
                saleDate != null ? saleDate : "null",
                createdAt != null ? createdAt : "null",
                modifiedAt != null ? modifiedAt : "null",
                annotations != null ? annotations : "null"
        );
    }

    // ===== getters and setters =====
    // they are needed for JSON deserialization

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getSoldById() {return soldById;}
    public void setSoldById(Long soldById) {this.soldById = soldById;}

    public String getAnnotations() {return annotations;}
    public void setAnnotations(String annotations) {this.annotations = annotations;}

    public LocalDateTime getSaleDate() {return saleDate;}
    public void setSaleDate(LocalDateTime saleDate) {this.saleDate = saleDate;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getModifiedAt() {return modifiedAt;}
    public void setModifiedAt(LocalDateTime modifiedAt) {this.modifiedAt = modifiedAt;}

}
