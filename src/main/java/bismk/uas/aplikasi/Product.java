package bismk.uas.aplikasi;

public class Product {
    private int productID;
    private String productName;
    private int supplierID;
    private String category;
    private double unitPrice;
    private int quantity;

    public Product(int productID, String productName, int supplierID, String category, double unitPrice, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.supplierID = supplierID;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Getters and setters for all fields
    // ...

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
