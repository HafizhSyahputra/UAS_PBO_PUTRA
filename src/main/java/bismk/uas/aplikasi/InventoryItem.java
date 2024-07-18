package bismk.uas.aplikasi;

    public class InventoryItem {
        private final String productName;
        private final String supplierName;
        private final int quantityInStock;
        private final double unitPrice;
        private final int qtyOut;

        public InventoryItem(String productName, String supplierName, int quantityInStock, double unitPrice, int qtyOut) {
            this.productName = productName;
            this.supplierName = supplierName;
            this.quantityInStock = quantityInStock;
            this.unitPrice = unitPrice;
            this.qtyOut = qtyOut; // Inisialisasi property qtyOut
        }

        public String getProductName() {
            return productName;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public int getQuantityInStock() {
            return quantityInStock;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public int getQtyOut() {
            return qtyOut;
        }
    }

