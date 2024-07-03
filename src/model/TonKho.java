package model;

public class TonKho {
    private String maTonKho;
    private String maSanPham;
    private String maKho;
    private static int soLuongTon;  // Không còn là biến static

    public TonKho(String maTonKho, String maSanPham, String maKho, int soLuongTon) {
        this.maTonKho = maTonKho;
        this.maSanPham = maSanPham;
        this.maKho = maKho;
        this.soLuongTon = soLuongTon;
    }

    public String getMaTonKho() {
        return maTonKho;
    }

    public void setMaTonKho(String maTonKho) {
        this.maTonKho = maTonKho;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String maKho) {
        this.maKho = maKho;
    }

    public static int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
