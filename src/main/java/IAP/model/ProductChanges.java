package IAP.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductChanges {
    private Change<String> productName;
    private Change<Float> price;
    private Change<Integer> width;
    private Change<Integer> depth;
    private Change<Integer> height;


    // getters and setters :)

    public Change<String> getProductName() { return productName; }
    public void setProductName(Change<String> productName) { this.productName = productName; }

    public Change<Float> getPrice() { return price; }
    public void setPrice(Change<Float> price) { this.price = price; }

    public Change<Integer> getWidth() { return width; }
    public void setWidth(Change<Integer> width) { this.width = width; }

    public Change<Integer> getDepth() { return depth; }
    public void setDepth(Change<Integer> depth) { this.depth = depth; }

    public Change<Integer> getHeight() { return height; }
    public void setHeight(Change<Integer> height) { this.height = height; }

    @Override
    public String toString() {
        return "ProductChanges{" +
                "productName=" + productName +
                ", price=" + price +
                ", width=" + width +
                ", depth=" + depth +
                ", height=" + height +
                '}';
    }


    // helper methods :O

    // if any field changed
    public boolean hasAnyChanges() {
        return hasChanged(productName) ||
                hasChanged(price) ||
                hasChanged(width) ||
                hasChanged(depth) ||
                hasChanged(height);
    }

    // Return a readable map of all actual changes
    public Map<String, String> getChangedFieldsSummary() {
        Map<String, String> changes = new HashMap<>();

        addIfChanged(changes, "productName", productName);
        addIfChanged(changes, "price", price);
        addIfChanged(changes, "width", width);
        addIfChanged(changes, "depth", depth);
        addIfChanged(changes, "height", height);

        return changes;
    }

    // Check if a change exists
    private <T> boolean hasChanged(Change<T> change) {
        return change != null && !Objects.equals(change.getBefore(), change.getAfter());
    }

    // Add changed field to the summary map
    private <T> void addIfChanged(Map<String, String> map, String fieldName, Change<T> change) {
        if (hasChanged(change)) {
            map.put(fieldName, change.getBefore() + " → " + change.getAfter());
        }
    }
}
