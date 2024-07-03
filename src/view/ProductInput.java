package view;

import java.awt.*;
import javax.swing.*;
import model.SanPham;

public class ProductInput extends JPanel {
    private JTextField codeField;
    private JTextField nameField;
    private JTextField quantityField;

    public ProductInput(SanPham sanPham) {
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Mã sản phẩm:"));
        codeField = new JTextField();
        if (sanPham != null) {
            codeField.setText(sanPham.getMaSanPham());
            codeField.setEditable(false);
        }
        add(codeField);

        add(new JLabel("Tên sản phẩm:"));
        nameField = new JTextField();
        if (sanPham != null) {
            nameField.setText(sanPham.getTenSanPham());
        }
        add(nameField);

        add(new JLabel("Số lượng:"));
        quantityField = new JTextField();
        if (sanPham != null) {
            quantityField.setText(String.valueOf(sanPham.getGiaBan()));
        }
        add(quantityField);
    }

    public SanPham getProduct() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên.");
            return null;
        }

        return new SanPham(code, name, quantity, quantity);
    }
}
