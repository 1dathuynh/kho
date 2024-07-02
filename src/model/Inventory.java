package model;
//
public class Inventory {
    private String productCode;
    private String productName;
    private String warehouseCode;
    private int quantity;

    public Inventory(String productCode, String productName, String warehouseCode, int quantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.warehouseCode = warehouseCode;
        this.quantity = quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}