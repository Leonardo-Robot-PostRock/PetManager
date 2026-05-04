package ar.com.petmanager.gui.vetUI;

import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.service.VetService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de veterinarias.
 * Permite registrar, editar y eliminar veterinarias.
 */
public class VetsUI extends BasePanel {

    private final VetService vetService;

    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtStreet;
    private JTextField txtCity;

    private JTable tblVets;
    private DefaultTableModel tableModel;

    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public VetsUI(VetService vetService) {
        this.vetService = vetService;
        initializeComponents();
        configureLayout();
        configureListeners();
        loadTableData();
    }

    private void initializeComponents() {
        txtName = createTextField();
        txtPhone = createTextField();
        txtStreet = createTextField();
        txtCity = createTextField();

        btnSave = createButton("Guardar", UIConstants.COLOR_SUCCESS);
        btnUpdate = createButton("Actualizar", UIConstants.COLOR_ACCENT);
        btnDelete = createButton("Eliminar", UIConstants.COLOR_ERROR);
        btnClear = createButton("Limpiar", UIConstants.COLOR_TEXT_SECONDARY);

        tblVets = new JTable();
        setupTable();
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        // Header
        JLabel lblTitle = new JLabel("Veterinarias");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, UIConstants.PADDING_LARGE, 0));

        // Panel de formulario + tabla
        JPanel mainPanel = new JPanel(new BorderLayout(UIConstants.PADDING_LARGE, 0));
        mainPanel.setOpaque(false);

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(lblTitle, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Nombre y Teléfono
        addFormField(panel, gbc, "Nombre:", txtName, 0, 0, 0.3);
        addFormField(panel, gbc, "Teléfono:", txtPhone, 1, 0, 0.3);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_SMALL, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        panel.add(buttonPanel, gbc);

        // Fila 2: Calle y Ciudad
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblStreet = new JLabel("Calle:");
        lblStreet.setFont(UIConstants.FONT_BODY_BOLD);
        panel.add(lblStreet, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        txtStreet.setPreferredSize(new Dimension(180, 30));
        panel.add(txtStreet, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        JLabel lblCity = new JLabel("Ciudad:");
        lblCity.setFont(UIConstants.FONT_BODY_BOLD);
        panel.add(lblCity, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.3;
        txtCity.setPreferredSize(new Dimension(180, 30));
        panel.add(txtCity, gbc);

        return panel;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int col, int row, double weightx) {
        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.weightx = weightx;
        field.setPreferredSize(new Dimension(180, 30));
        field.setFont(UIConstants.FONT_BODY);
        panel.add(field, gbc);
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(UIConstants.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
        return tf;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JScrollPane scrollPane = new JScrollPane(tblVets);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupTable() {
        String[] columns = {"ID", "Nombre", "Teléfono", "Dirección"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblVets.setModel(tableModel);
        tblVets.setFont(UIConstants.FONT_BODY);
        tblVets.setRowHeight(30);
        tblVets.setGridColor(UIConstants.COLOR_BORDER);
        tblVets.setShowVerticalLines(false);
        tblVets.setSelectionBackground(UIConstants.COLOR_CARD_VETS.brighter());

        JTableHeader header = tblVets.getTableHeader();
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setBackground(UIConstants.COLOR_CARD_VETS);
        header.setForeground(UIConstants.COLOR_WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblVets.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void configureListeners() {
        btnSave.addActionListener(e -> saveVet());
        btnUpdate.addActionListener(e -> updateVet());
        btnDelete.addActionListener(e -> deleteVet());
        btnClear.addActionListener(e -> clearForm());

        tblVets.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblVets.getSelectedRow() != -1) {
                fillFormFromRow(tblVets.getSelectedRow());
            }
        });
    }

    private void saveVet() {
        String name = txtName.getText();
        if (name.isEmpty()) {
            showError("El nombre es obligatorio.");
            return;
        }

        try {
            int phone = txtPhone.getText().isEmpty() ? 0 : Integer.parseInt(txtPhone.getText());
            String street = txtStreet.getText();
            String city = txtCity.getText();

            Vet vet = new Vet(name, phone, street, city);
            vetService.add(vet);
            loadTableData();
            clearForm();
            info("Veterinaria guardada exitosamente.");
        } catch (NumberFormatException ex) {
            showError("Teléfono debe ser un número válido.");
        }
    }

    private void updateVet() {
        int selectedRow = tblVets.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccioná una veterinaria de la tabla.");
            return;
        }

        long vetId = (long) tableModel.getValueAt(selectedRow, 0);
        Vet vet = vetService.getById((int) vetId);
        if (vet == null) {
            showError("Veterinaria no encontrada.");
            return;
        }

        try {
            vet.setName(txtName.getText());
            vet.setPhone(Integer.parseInt(txtPhone.getText()));
            vet.getAddress().setStreet(txtStreet.getText());
            vet.getAddress().setCity(txtCity.getText());

            vetService.update(vet);
            loadTableData();
            clearForm();
            info("Veterinaria actualizada exitosamente.");
        } catch (NumberFormatException ex) {
            showError("Teléfono debe ser un número válido.");
        }
    }

    private void deleteVet() {
        int selectedRow = tblVets.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccioná una veterinaria de la tabla.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, UIConstants.MSG_CONFIRM_DELETE,
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        long vetId = (long) tableModel.getValueAt(selectedRow, 0);
        vetService.deleteById((int) vetId);
        loadTableData();
        clearForm();
        info("Veterinaria eliminada exitosamente.");
    }

    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtStreet.setText("");
        txtCity.setText("");
        tblVets.clearSelection();
    }

    private void fillFormFromRow(int row) {
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtPhone.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        String[] direccion = ((String) tableModel.getValueAt(row, 3)).split(",");
        txtStreet.setText(direccion[0].trim());
        txtCity.setText(direccion.length > 1 ? direccion[1].trim() : "");
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Vet> vets = vetService.getAll();
        for (Vet vet : vets) {
            String direccion = vet.getAddress().getStreet() + ", " + vet.getAddress().getCity();
            tableModel.addRow(new Object[]{vet.getIdVet(), vet.getName(), vet.getPhone(), direccion});
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(UIConstants.COLOR_WHITE);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void info(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}