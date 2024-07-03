package model;

public class TonKho {
    private String maTonKho;
    private String maSanPham;
    private String maKho;
    private int soLuongTon;

    public TonKho(String maTonKho, String maSanPham, String maKho, int soLuongTon) {
        this.maTonKho = maTonKho;
        this.maSanPham = maSanPham;
        this.maKho = maKho;
        this.soLuongTon = soLuongTon;
    }

    public String getMaTonKho() {
        return maTonKho;
    }

    public void setMaTonKho(String MaTonKho) {
        this.maTonKho = MaTonKho;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String MaSanPham) {
        this.maSanPham = MaSanPham;
    }

    public String getMaKho() {
        return maKho;
    }

    public void setMaKho(String MaKho) {
        this.maKho = MaKho;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
