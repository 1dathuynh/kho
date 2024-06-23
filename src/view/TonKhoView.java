package view;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TonKhoView extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private JTextArea productListTextArea;
    private DefaultTableModel tableModel;
    private Map<String, Map<String, Product>> warehouseProducts = new HashMap<>();
    private String selectedProductCode;

    public TonKhoView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 901, 600);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        RoundedPanel jPanelMenu = new RoundedPanel(50, Color.WHITE);
        jPanelMenu.setLayout(new BorderLayout(0, 0));
        contentPane.add(jPanelMenu, BorderLayout.WEST);

        JLabel labelHouse = new JLabel("");
        labelHouse.setHorizontalAlignment(SwingConstants.CENTER);
        Image img = new ImageIcon(this.getClass().getResource("/House.png")).getImage();
        labelHouse.setIcon(new ImageIcon(img));
        jPanelMenu.add(labelHouse, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());

        addMenuItem(panel, "Đơn Hàng", "/icon_DonHang.png");
        addMenuItem(panel, "Tồn Kho", "/icon_TonKho.png");
        addMenuItem(panel, "Nhập/Xuất", "/icon_NhapXuat.png");
        addMenuItem(panel, "Vị Trí Kho", "/icon_ViTri.png");
        addMenuItem(panel, "Quản Trị", "/icon_QuanTri.png");
        addMenuItem(panel, "Thống kê", "/icon_ThongKe.png");

        panel.add(Box.createVerticalStrut(10));
        panel.add(Box.createVerticalGlue());

        jPanelMenu.add(panel, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        tablePanel.add(buttonPanel, BorderLayout.NORTH);

        String[] columnNames = {"Mã Kho", "Tên Khu Vực", "Ghi Chú Hàng Tồn Kho"};
        Object[][] data = {
            {"WH001", "Khu vực 1", "Notes for Khu Vuc 1"},
            {"WH002", "Khu vực 2", "Notes for Khu Vuc 2"},
            {"WH003", "Khu vực 3", "Notes for Khu Vuc 3"},
            {"WH004", "Khu vực 4", "Notes for Khu Vuc 4"}
        };

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Thông Tin Kho Hàng", null, tablePanel, null);

        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());

        JLabel productLabel = new JLabel("Danh Sách Sản Phẩm Hiện Có Tại Khu Vực:");
        productPanel.add(productLabel, BorderLayout.NORTH);

        productListTextArea = new JTextArea();
        productListTextArea.setEditable(false);
        productListTextArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int caretPosition = productListTextArea.getCaretPosition();
                    int lineNumber = productListTextArea.getDocument().getDefaultRootElement().getElementIndex(caretPosition);
                    String line = null;
                    try {
                        int start = productListTextArea.getLineStartOffset(lineNumber);
                        int end = productListTextArea.getLineEndOffset(lineNumber);
                        line = productListTextArea.getText(start, end - start).trim();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (line != null && line.startsWith("Code: ")) {
                        selectedProductCode = line.substring(6, line.indexOf(", Name:")).trim();
                    }
                }
            }
        });

        JScrollPane productScrollPane = new JScrollPane(productListTextArea);
        productScrollPane.setPreferredSize(new Dimension(200, 0));
        productPanel.add(productScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Sản Phẩm", null, productPanel, null);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String warehouseCode = (String) table.getValueAt(selectedRow, 0);
                        updateProductList(warehouseCode);
                    }
                }
            }
        });

        addButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String warehouseCode = (String) table.getValueAt(selectedRow, 0);
                ProductInputFrame inputFrame = new ProductInputFrame(warehouseCode, this);
                inputFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kho trước.", "Kho không được chọn", JOptionPane.WARNING_MESSAGE);
            }
        });

        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
    }

    public void saveProduct(String warehouseCode, String productCode, String productName, int productQuantity) {
        warehouseProducts.putIfAbsent(warehouseCode, new HashMap<>());
        Map<String, Product> products = warehouseProducts.get(warehouseCode);
        if (products.containsKey(productCode)) {
            products.get(productCode).addQuantity(productQuantity);
        } else {
            products.put(productCode, new Product(productCode, productName, productQuantity));
        }
        updateProductList(warehouseCode);
    }

    private void updateProductList(String warehouseCode) {
        Map<String, Product> products = warehouseProducts.get(warehouseCode);
        productListTextArea.setText("Sản phẩm của kho " + warehouseCode + ":\n");
        selectedProductCode = null;
        if (products != null) {
            for (Product product : products.values()) {
                productListTextArea.append("Code: " + product.getCode() + ", Name: " + product.getName() + ", Quantity: " + product.getQuantity() + "\n");
            }
        }
    }

    private void editProduct() {
        if (selectedProductCode == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để chỉnh sửa.", "Không có sản phẩm được chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String warehouseCode = (String) table.getValueAt(selectedRow, 0);
            Map<String, Product> products = warehouseProducts.get(warehouseCode);
            if (products != null && products.containsKey(selectedProductCode)) {
                Product product = products.get(selectedProductCode);
                String newProductName = JOptionPane.showInputDialog(this, "Nhập tên mới cho sản phẩm:", product.getName());
                String newProductQuantityStr = JOptionPane.showInputDialog(this, "Nhập số lượng mới cho sản phẩm:", product.getQuantity());
                try {
                    int newProductQuantity = Integer.parseInt(newProductQuantityStr);
                    product.setName(newProductName);
                    product.setQuantity(newProductQuantity);
                    updateProductList(warehouseCode);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteProduct() {
        if (selectedProductCode == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa.", "Không có sản phẩm được chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String warehouseCode = (String) table.getValueAt(selectedRow, 0);
            Map<String, Product> products = warehouseProducts.get(warehouseCode);
            if (products != null) {
                products.remove(selectedProductCode);
                updateProductList(warehouseCode);
            }
        }
    }

    private void addMenuItem(JPanel panel, String text, String iconPath) {
        JLabel label = new JLabel(text);
        Image img = new ImageIcon(this.getClass().getResource(iconPath)).getImage();
        Image scaledImg = img.getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImg));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(label);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TonKhoView frame = new TonKhoView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

//package view;
//
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import javax.swing.table.DefaultTableModel;
//import java.util.HashMap;
//import java.util.Map;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//public class TonKhoView extends JFrame {
//
//    private JPanel contentPane;
//    private JTable table;
//    private JTextArea productListTextArea;
//    private DefaultTableModel tableModel;
//    private Map<String, Map<String, Product>> warehouseProducts = new HashMap<>();
//    private String selectedProductCode;
//
//    public TonKhoView() {
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 901, 600);
//        contentPane = new JPanel();
//        setContentPane(contentPane);
//        contentPane.setLayout(new BorderLayout(0, 0));
//
//        RoundedPanel jPanelMenu = new RoundedPanel(50, Color.WHITE);
//        jPanelMenu.setLayout(new BorderLayout(0, 0));
//        contentPane.add(jPanelMenu, BorderLayout.WEST);
//
//        JLabel labelHouse = new JLabel("");
//        labelHouse.setHorizontalAlignment(SwingConstants.CENTER);
//        Image img = new ImageIcon(this.getClass().getResource("/House.png")).getImage();
//        labelHouse.setIcon(new ImageIcon(img));
//        jPanelMenu.add(labelHouse, BorderLayout.NORTH);
//
//        JPanel panel = new JPanel();
//        panel.setOpaque(false);
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.add(Box.createVerticalGlue());
//
//        addMenuItem(panel, "Đơn Hàng", "/icon_DonHang.png");
//        addMenuItem(panel, "Tồn Kho", "/icon_TonKho.png");
//        addMenuItem(panel, "Nhập/Xuất", "/icon_NhapXuat.png");
//        addMenuItem(panel, "Vị Trí Kho", "/icon_ViTri.png");
//        addMenuItem(panel, "Quản Trị", "/icon_QuanTri.png");
//        addMenuItem(panel, "Thống kê", "/icon_ThongKe.png");
//
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(Box.createVerticalGlue());
//
//        jPanelMenu.add(panel, BorderLayout.CENTER);
//
//        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//        contentPane.add(tabbedPane, BorderLayout.CENTER);
//
//        JPanel tablePanel = new JPanel();
//        tablePanel.setLayout(new BorderLayout());
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//
//        JButton addButton = new JButton("Thêm");
//        JButton editButton = new JButton("Sửa");
//        JButton deleteButton = new JButton("Xóa");
//
//        buttonPanel.add(addButton);
//        buttonPanel.add(editButton);
//        buttonPanel.add(deleteButton);
//
//        tablePanel.add(buttonPanel, BorderLayout.NORTH);
//
//        String[] columnNames = {"Mã Kho", "Tên Khu Vực", "Ghi Chú Tồn Kho"};
//        Object[][] data = {
//            {"WH001", "Khu Vực 1", "Ghi chú cho khu vực 1"},
//            {"WH002", "Khu Vực 2", "Ghi chú cho khu vực 2"},
//            {"WH003", "Khu Vực 3", "Ghi chú cho khu vực 3"},
//            {"WH004", "Khu Vực 4", "Ghi chú cho khu vực 4"}
//        };
//
//        tableModel = new DefaultTableModel(data, columnNames);
//        table = new JTable(tableModel);
//        JScrollPane scrollPane = new JScrollPane(table);
//        tablePanel.add(scrollPane, BorderLayout.CENTER);
//
//        tabbedPane.addTab("Thông Tin Kho", null, tablePanel, null);
//
//        JPanel productPanel = new JPanel();
//        productPanel.setLayout(new BorderLayout());
//
//        JLabel productLabel = new JLabel("Danh Sách Sản Phẩm Có Trong Khu Vực:");
//        productPanel.add(productLabel, BorderLayout.NORTH);
//
//        productListTextArea = new JTextArea();
//        productListTextArea.setEditable(false);
//        productListTextArea.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 1) {
//                    int caretPosition = productListTextArea.getCaretPosition();
//                    int lineNumber = productListTextArea.getDocument().getDefaultRootElement().getElementIndex(caretPosition);
//                    String line = null;
//                    try {
//                        int start = productListTextArea.getLineStartOffset(lineNumber);
//                        int end = productListTextArea.getLineEndOffset(lineNumber);
//                        line = productListTextArea.getText(start, end - start).trim();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    if (line != null && line.startsWith("Mã: ")) {
//                        selectedProductCode = line.substring(5, line.indexOf(", Tên:")).trim();
//                        System.out.println("Selected product code: " + selectedProductCode);
//                    }
//                }
//            }
//        });
//
//        JScrollPane productScrollPane = new JScrollPane(productListTextArea);
//        productScrollPane.setPreferredSize(new Dimension(200, 0));
//        productPanel.add(productScrollPane, BorderLayout.CENTER);
//
//        tabbedPane.addTab("Sản Phẩm", null, productPanel, null);
//
//        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent event) {
//                if (!event.getValueIsAdjusting()) {
//                    int selectedRow = table.getSelectedRow();
//                    if (selectedRow != -1) {
//                        String warehouseCode = (String) table.getValueAt(selectedRow, 0);
//                        updateProductList(warehouseCode);
//                    }
//                }
//            }
//        });
//
//        addButton.addActionListener(e -> {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow != -1) {
//                String warehouseCode = (String) table.getValueAt(selectedRow, 0);
//                ProductInputFrame inputFrame = new ProductInputFrame(warehouseCode, this);
//                inputFrame.setVisible(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn một kho trước.", "Chưa Chọn Kho", JOptionPane.WARNING_MESSAGE);
//            }
//        });
//
//        editButton.addActionListener(e -> editProduct());
//        deleteButton.addActionListener(e -> deleteProduct());
//    }
//
//    public void saveProduct(String warehouseCode, String productCode, String productName, int productQuantity) {
//        warehouseProducts.putIfAbsent(warehouseCode, new HashMap<>());
//        Map<String, Product> products = warehouseProducts.get(warehouseCode);
//        if (products.containsKey(productCode)) {
//            products.get(productCode).addQuantity(productQuantity);
//        } else {
//            products.put(productCode, new Product(productCode, productName, productQuantity));
//        }
//        updateProductList(warehouseCode);
//    }
//
//    private void updateProductList(String warehouseCode) {
//        Map<String, Product> products = warehouseProducts.get(warehouseCode);
//        productListTextArea.setText("Sản phẩm cho kho " + warehouseCode + ":\n");
//        selectedProductCode = null;
//        if (products != null) {
//            for (Product product : products.values()) {
//                productListTextArea.append("Mã: " + product.getCode() + ", Tên: " + product.getName() + ", Số lượng: " + product.getQuantity() + "\n");
//            }
//        }
//    }
//
//    private void editProduct() {
//        if (selectedProductCode == null) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để sửa.", "Chưa Chọn Sản Phẩm", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            String warehouseCode = (String) table.getValueAt(selectedRow, 0);
//            Map<String, Product> products = warehouseProducts.get(warehouseCode);
//            if (products != null && products.containsKey(selectedProductCode)) {
//                Product product = products.get(selectedProductCode);
//                String newProductName = JOptionPane.showInputDialog(this, "Nhập tên mới cho sản phẩm:", product.getName());
//                String newProductQuantityStr = JOptionPane.showInputDialog(this, "Nhập số lượng mới cho sản phẩm:", product.getQuantity());
//                try {
//                    int newProductQuantity = Integer.parseInt(newProductQuantityStr);
//                    product.setName(newProductName);
//                    product.setQuantity(newProductQuantity);
//                    updateProductList(warehouseCode);
//                    System.out.println("Product edited: " + selectedProductCode);
//                } catch (NumberFormatException e) {
//                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ.", "Dữ Liệu Không Hợp Lệ", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//    }
//
//    private void deleteProduct() {
//        if (selectedProductCode == null) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sản phẩm để xóa.", "Chưa Chọn Sản Phẩm", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            String warehouseCode = (String) table.getValueAt(selectedRow, 0);
//            Map<String, Product> products = warehouseProducts.get(warehouseCode);
//            if (products != null) {
//                products.remove(selectedProductCode);
//                updateProductList(warehouseCode);
//                System.out.println("Product deleted: " + selectedProductCode);
//            }
//        }
//    }
//
//    private void addMenuItem(JPanel panel, String text, String iconPath) {
//        JLabel label = new JLabel(text);
//        Image img = new ImageIcon(this.getClass().getResource(iconPath)).getImage();
//        Image scaledImg = img.getScaledInstance(36, 36, Image.SCALE_SMOOTH);
//        label.setIcon(new ImageIcon(scaledImg));
//        label.setHorizontalAlignment(SwingConstants.CENTER);
//        label.setFont(new Font("Tahoma", Font.PLAIN, 16));
//        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        panel.add(label);
//    }
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    TonKhoView frame = new TonKhoView();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//}



