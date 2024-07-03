package DAO;

import java.sql.*;
import java.util.*;

import model.Kho;
import model.SanPham;
import model.TonKho;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        // Kết nối tới database
        String url = "jdbc:mysql://localhost:3306/QuanLyKhoHang";
        String username = "root";
        String password = "0935542587"; // Thay đổi mật khẩu của bạn ở đây
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fetch quantity from ChiTietNhap
    public int fetchQuantityFromChiTietNhap(String productCode, String warehouseCode) {
        int quantity = 0;
        String query = "SELECT SUM(SoLuongNhap) AS totalQuantity FROM ChiTietNhap WHERE MaSanPham = ? AND MaKho = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, productCode);
            stmt.setString(2, warehouseCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                quantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException e) {
            System.out.println("Error in fetchQuantityFromChiTietNhap: " + e.getMessage());
            e.printStackTrace();
        }
        return quantity;
    }

    // Method to update TonKho based on fetched quantity
    public void updateTonKho(String productCode, String warehouseCode) {
        int quantity = fetchQuantityFromChiTietNhap(productCode, warehouseCode);
        String query = "UPDATE TonKho SET SoLuongTon = ? WHERE MaSanPham = ? AND MaKho = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, quantity);
            stmt.setString(2, productCode);
            stmt.setString(3, warehouseCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in updateTonKho: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add other necessary DAO methods, e.g., for fetching all warehouses, adding a warehouse, etc.
    public List<Kho> getAllWarehouses() {
        List<Kho> warehouses = new ArrayList<>();
        String query = "SELECT * FROM Kho";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Kho kho = new Kho(rs.getString("MaKho"), rs.getString("TenKho"));
                warehouses.add(kho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return warehouses;
    }

    public void addWarehouse(Kho kho) {
        String query = "INSERT INTO Kho (MaKho, TenKho) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kho.getMaKho());
            stmt.setString(2, kho.getTenKho());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SanPham> getProductsByWarehouseCode(String warehouseCode) {
        List<SanPham> sanPhams = new ArrayList<>();
        String query = "SELECT * FROM SanPham INNER JOIN TonKho ON SanPham.MaSanPham = TonKho.MaSanPham WHERE TonKho.MaKho = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, warehouseCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SanPham sanPham = new SanPham(rs.getString("MaSanPham"), rs.getString("TenSanPham"), rs.getDouble("SoLuongTon"), 0);
                sanPhams.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPhams;
    }

    public void addProduct(SanPham sanPham, String warehouseCode) {
        String query = "INSERT INTO SanPham (MaSanPham, TenSanPham) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, sanPham.getMaSanPham());
            stmt.setString(2, sanPham.getTenSanPham());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "INSERT INTO TonKho (MaTonKho, MaSanPham, MaKho, SoLuongTon) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, sanPham.getMaSanPham());
            stmt.setString(3, warehouseCode);
            stmt.setDouble(4, sanPham.getGiaBan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(SanPham sanPham) {
        String query = "UPDATE SanPham SET TenSanPham = ? WHERE MaSanPham = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, sanPham.getTenSanPham());
            stmt.setString(2, sanPham.getMaSanPham());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "UPDATE TonKho SET SoLuongTon = ? WHERE MaSanPham = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setDouble(1, sanPham.getGiaBan());
            stmt.setString(2, sanPham.getMaSanPham());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(String productCode, String warehouseCode) {
        String query = "DELETE FROM SanPham WHERE MaSanPham = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, productCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "DELETE FROM TonKho WHERE MaSanPham = ? AND MaKho = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, productCode);
            stmt.setString(2, warehouseCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
