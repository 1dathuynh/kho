package model;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private int giaBan;

    public SanPham(String maSanPham, String tenSanPham, int giaBan) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String MaSanPham) {
        this.maSanPham = MaSanPham;
    }

    public String gettenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int giaBan) {
        this.giaBan = giaBan;
    }
}
