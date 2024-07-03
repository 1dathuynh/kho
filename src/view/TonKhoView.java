package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

import DAO.ProductDAO;
import model.SanPham;
import model.Kho;
import view.WarehouseForm;

public class TonKhoView extends JFrame {
    private JPanel contentPane;
    private JTable table;
    private JTextArea productListTextArea;
    private DefaultTableModel tableModel;
    private Map<String, Map<String, SanPham>> warehouseProducts = new HashMap<>();
    private String selectedProductCode;
    private ProductDAO productDAO;

    public TonKhoView() {
        productDAO = new ProductDAO();

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
        // icon
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
            {"K1", "apple", "Notes for apple"},
            {"K2", "realme", "Notes for realme"},
            {"K3", "oppo", "Notes for oppo"},
            {"K3", "oppo", "Notes for oppo"}
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

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null, "Chọn một kho hàng trước.");
                    return;
                }
                String warehouseCode = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                ProductInput productInput = new ProductInput(null);
                int result = JOptionPane.showConfirmDialog(null, productInput, "Thêm Sản Phẩm", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    SanPham sanPham = productInput.getProduct();
                    if (sanPham != null) {
                        productDAO.addProduct(sanPham, warehouseCode);
                        updateProductList(warehouseCode);
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedProductCode == null || table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null, "Chọn một sản phẩm để sửa.");
                    return;
                }
                String warehouseCode = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                Map<String, SanPham> sanPhams = warehouseProducts.get(warehouseCode);
                SanPham sanPham = sanPhams.get(selectedProductCode);
                ProductInput productInput = new ProductInput(sanPham);
                int result = JOptionPane.showConfirmDialog(null, productInput, "Sửa Sản Phẩm", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    SanPham updatedProduct = productInput.getProduct();
                    if (updatedProduct != null) {
                        productDAO.updateProduct(updatedProduct);
                        updateProductList(warehouseCode);
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedProductCode == null || table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null, "Chọn một sản phẩm để xóa.");
                    return;
                }
                String warehouseCode = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
                productDAO.deleteProduct(selectedProductCode, warehouseCode);
                updateProductList(warehouseCode);
            }
        });
    }

    private void updateProductList(String warehouseCode) {
        ArrayList<SanPham> sanPhams = (ArrayList<SanPham>) productDAO.getProductsByWarehouseCode(warehouseCode);
        warehouseProducts.put(warehouseCode, new HashMap<>());
        StringBuilder productList = new StringBuilder();
        for (SanPham sanPham : sanPhams) {
            productList.append("Mã Sản Phẩm: ").append(sanPham.getMaSanPham()).append(", Tên Sản Phẩm: ").append(sanPham.getTenSanPham()).append(", Số lượng tồn: ").append(sanPham.getGiaBan()).append("\n");
            warehouseProducts.get(warehouseCode).put(sanPham.getMaSanPham(), sanPham);
        }
        productListTextArea.setText(productList.toString());
    }

    private void addMenuItem(JPanel panel, String text, String iconPath) {
        JPanel menuItem = new JPanel();
        menuItem.setOpaque(false);
        menuItem.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel iconLabel = new JLabel();
        ImageIcon icon = new ImageIcon(this.getClass().getResource(iconPath));
        iconLabel.setIcon(icon);
        menuItem.add(iconLabel);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        menuItem.add(textLabel);

        panel.add(menuItem);
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
