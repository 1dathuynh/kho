package model;

public class Product {
    private String maSanPham;
    private String tenSanPham;
    private int soLuongTon;

    public Product(String maSanPham, String tenSanPham, int soLuongTon) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuongTon = soLuongTon;
    }

    public String getmaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void settenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getsoLuongTon() {
        return soLuongTon;
    }

    public void setsoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
