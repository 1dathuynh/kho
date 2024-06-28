package DAO;

import java.sql.*;
import java.util.*;

import model.Product;

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

    public List<Product> getProductsByWarehouseCode(String warehouseCode) {
        List<Product> productList = new ArrayList<>();
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
                productList.add(new Product(productCode, productName, quantity));
            }
        } catch (SQLException e) {
            System.out.println("Error in getProductsByWarehouseCode: " + e.getMessage());
            e.printStackTrace();
        }
        return productList;
    }

    public void addProduct(Product product, String warehouseCode) {
        String queryProduct = "INSERT INTO SanPham (MaSanPham, TenSanPham) VALUES (?, ?)";
        String queryStock = "INSERT INTO TonKho (MaTonKho, MaSanPham, MaKho, SoLuongTon) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmtProduct = connection.prepareStatement(queryProduct);
            stmtProduct.setString(1, product.getCode());
            stmtProduct.setString(2, product.getName());
            stmtProduct.executeUpdate();

            PreparedStatement stmtStock = connection.prepareStatement(queryStock);
            stmtStock.setString(1, generateUniqueCode()); // Generate a unique code for MaTonKho
            stmtStock.setString(2, product.getCode());
            stmtStock.setString(3, warehouseCode);
            stmtStock.setInt(4, product.getQuantity());
            stmtStock.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in addProduct: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product) {
        String query = "UPDATE SanPham SET TenSanPham = ? WHERE MaSanPham = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCode());
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





















