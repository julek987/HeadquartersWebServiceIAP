package IAP.utils;

import IAP.model.ProductChanges;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ProductChangesConverter implements AttributeConverter<ProductChanges, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ProductChanges attribute) {
        if (attribute == null) return null;
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing ProductChanges", e);
        }
    }

    @Override
    public ProductChanges convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return mapper.readValue(dbData, ProductChanges.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deserializing ProductChanges", e);
        }
    }
}
