package DAO;

import java.sql.*;
import java.util.*;

import model.SanPham;
import model.TonKho;
//c
import model.Kho;
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
    public List<Kho> getAllWarehouses() {
        List<Kho> warehouseList = new ArrayList<>();
        String query = "SELECT * FROM Kho";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String warehouseCode = rs.getString("MaKho");
                String warehouseName = rs.getString("TenKho");
                warehouseList.add(new Kho(warehouseCode, warehouseName));
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllWarehouses: " + e.getMessage());
            e.printStackTrace();
        }
        return warehouseList;
    }

    public void addWarehouse(Kho kho) {
        String query = "INSERT INTO Kho (MaKho, TenKho) VALUES (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, kho.getMaKho());
            stmt.setString(2, kho.getTenKho());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in addWarehouse: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public List<SanPham> getProductsByWarehouseCode(String warehouseCode) {
        List<SanPham> productList = new ArrayList<>();
        String query = "SELECT sp.MaSanPham, sp.TenSanPham, tk.SoLuongTon " +
                       "FROM SanPham sp " +
                       "JOIN TonKho tk ON sp.MaSanPham = tk.MaSanPham " +
                       "WHERE tk.MaKho = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, warehouseCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String productCode = rs.getString("MaSanPham");
                String productName = rs.getString("TenSanPham");
                int quantity = rs.getInt("SoLuongTon");
                productList.add(new SanPham(productCode, productName, quantity, quantity));
            }
        } catch (SQLException e) {
            System.out.println("Error in getProductsByWarehouseCode: " + e.getMessage());
            e.printStackTrace();
        }
        return productList;
    }

    public void addProduct(SanPham sanPham, String warehouseCode) {
        String queryProduct = "INSERT INTO SanPham (MaSanPham, TenSanPham) VALUES (?, ?)";
        String queryStock = "INSERT INTO TonKho (MaTonKho, MaSanPham, MaKho, SoLuongTon) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmtProduct = connection.prepareStatement(queryProduct);
            stmtProduct.setString(1, sanPham.getMaSanPham());
            stmtProduct.setString(2, sanPham.getTenSanPham());
            stmtProduct.executeUpdate();

            PreparedStatement stmtStock = connection.prepareStatement(queryStock);
            stmtStock.setString(1, generateUniqueCode()); // Generate a unique code for MaTonKho
            stmtStock.setString(2, sanPham.getMaSanPham());
            stmtStock.setString(3, warehouseCode);
            stmtStock.setDouble(4, sanPham.getGiaBan());
            stmtStock.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in addProduct: " + e.getMessage());
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
            System.out.println("Error in updateProduct: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteProduct(String productCode, String warehouseCode) {
        String queryStock = "DELETE FROM TonKho WHERE MaSanPham = ? AND MaKho = ?";
        String queryProduct = "DELETE FROM SanPham WHERE MaSanPham = ?";
        try {
            PreparedStatement stmtStock = connection.prepareStatement(queryStock);
            stmtStock.setString(1, productCode);
            stmtStock.setString(2, warehouseCode);
            stmtStock.executeUpdate();

            PreparedStatement stmtProduct = connection.prepareStatement(queryProduct);
            stmtProduct.setString(1, productCode);
            stmtProduct.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in deleteProduct: " + e.getMessage());
            e.printStackTrace();
        }
    }
    


    private String generateUniqueCode() {
        // Generate a unique code for MaTonKho, e.g., using UUID
        return UUID.randomUUID().toString().substring(0, 3);
    }
    
}