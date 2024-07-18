package bismk.uas.aplikasi;

public class TransactionLIST {
    private int NO;
    private  int ProductID;
    private String ProductName;
    private String CustomerName;
    private int Qty;
    private  Double Price;
    private Double Amount;

    public TransactionLIST(int NO, int productID, String productName, String customerName, int qty, Double price, Double amount){
        this.NO = NO;
        ProductID = productID;
        ProductName = productName;
        CustomerName = customerName;
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

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
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

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }
}
