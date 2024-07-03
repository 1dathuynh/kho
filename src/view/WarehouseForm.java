package view;

import java.awt.*;
import javax.swing.*;
import model.Kho;

public class WarehouseForm extends JPanel {
    private JTextField codeField;
    private JTextField nameField;

    public WarehouseForm(Kho kho) {
        setLayout(new GridLayout(2, 2));

        add(new JLabel("Mã kho:"));
        codeField = new JTextField();
        if (kho != null) {
            codeField.setText(kho.getMaKho());
            codeField.setEditable(false);
        }
        add(codeField);

        add(new JLabel("Tên kho:"));
        nameField = new JTextField();
        if (kho != null) {
            nameField.setText(kho.getTenKho());
        }
        add(nameField);
    }

    public Kho getWarehouse() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã kho và tên kho không được để trống.");
            return null;
        }

        return new Kho(code, name);
    }
}
