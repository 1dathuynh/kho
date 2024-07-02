package view;

import java.awt.*;
import javax.swing.*;
import model.Product;

public class ProductForm extends JPanel {
    private JTextField codeField;
    private JTextField nameField;
    private JTextField quantityField;

    public ProductForm(Product product) {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Mã sản phẩm:"));
        codeField = new JTextField();
        if (product != null) {
            codeField.setText(product.getmaSanPham());
            codeField.setEditable(false);
        }
        add(codeField);

        add(new JLabel("Tên sản phẩm:"));
        nameField = new JTextField();
        if (product != null) {
            nameField.setText(product.getTenSanPham());
        }
        add(nameField);

        add(new JLabel("Số lượng:"));
        quantityField = new JTextField();
        if (product != null) {
            quantityField.setText(String.valueOf(product.getsoLuongTon()));
        }
        add(quantityField);
    }

    public Product getProduct() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên.");
            return null;
        }

        return new Product(code, name, quantity);
    }
}
