//package view;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class ProductInputFrame extends JFrame {
//    private JTextField productCodeField;
//    private JTextField productNameField;
//    private JTextField productQuantityField;
//    private TonKhoView parent;
//    private String warehouseCode;
//
//    public ProductInputFrame(String warehouseCode, TonKhoView parent) {
//        this.warehouseCode = warehouseCode;
//        this.parent = parent;
//
//        setTitle("Thêm Sản Phẩm");
//        setSize(300, 200);
//        setLocationRelativeTo(parent);
//
//        JPanel panel = new JPanel(new GridLayout(4, 2));
//
//        panel.add(new JLabel("Mã Sản Phẩm:"));
//        productCodeField = new JTextField();
//        panel.add(productCodeField);
//
//        panel.add(new JLabel("Tên Sản Phẩm:"));
//        productNameField = new JTextField();
//        panel.add(productNameField);
//
//        panel.add(new JLabel("Số Lượng:"));
//        productQuantityField = new JTextField();
//        panel.add(productQuantityField);
//
//        JButton addButton = new JButton("Thêm");
//        addButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String productCode = productCodeField.getText();
//                String productName = productNameField.getText();
//                String productQuantityStr = productQuantityField.getText();
//                try {
//                    int productQuantity = Integer.parseInt(productQuantityStr);
//                    parent.saveProduct(warehouseCode, productCode, productName, productQuantity);
//                    dispose();
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(ProductInputFrame.this, "Vui lòng nhập số lượng hợp lệ.", "Dữ Liệu Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        panel.add(addButton);
//
//        add(panel);
//    }
//}
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductInputFrame extends JFrame {
    private JTextField productCodeField;
    private JTextField productNameField;
    private JTextField productQuantityField;
    private TonKhoView parent;
    private String warehouseCode;

    public ProductInputFrame(String warehouseCode, TonKhoView parent) {
        this.warehouseCode = warehouseCode;
        this.parent = parent;

        setTitle("Thêm Sản Phẩm");
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Mã Sản Phẩm:"));
        productCodeField = new JTextField();
        panel.add(productCodeField);

        panel.add(new JLabel("Tên Sản Phẩm:"));
        productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("Số Lượng:"));
        productQuantityField = new JTextField();
        panel.add(productQuantityField);

        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productCode = productCodeField.getText();
                String productName = productNameField.getText();
                String productQuantityStr = productQuantityField.getText();
                try {
                    int productQuantity = Integer.parseInt(productQuantityStr);
                    parent.saveProduct(warehouseCode, productCode, productName, productQuantity);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductInputFrame.this, "Vui lòng nhập số lượng hợp lệ.", "Dữ Liệu Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(addButton);

        add(panel);
    }
}
