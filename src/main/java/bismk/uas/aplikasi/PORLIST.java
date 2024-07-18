package bismk.uas.aplikasi;

public class PORLIST {
    private int NO;
    private  int ProductID;
    private String ProductName;
    private String Supplier;
    private int Qty;
    private  Double Price;
    private String Amount;

    public PORLIST(int NO, int productID, String productName, String supplier, int qty, Double price, String amount) {
        this.NO = NO;
        ProductID = productID;
        ProductName = productName;
        Supplier = supplier;
        Qty = qty;
        Price = price;
        Amount = amount;
    }

    public int getNO() {
        return NO;
    }

    public void setNO(int NO) {
        this.NO = NO;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getSupplier() {
        return Supplier;
    }

    public void setSupplier(String supplier) {
        Supplier = supplier;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
