package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Sex;
import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.service.OwnerService;
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
 * Panel de Gestión de Clientes y Dueños.
 * Permite agregar, editar y eliminar dueños, asignar mascotas
 * y vincular veterinarias preferidas.
 */
public class OwnerUI extends BasePanel {

    private final OwnerService ownerService;
    private final VetService vetService;

    // Formulario
    private JTextField txtDni;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtPhone;
    private JTextField txtStreet;
    private JTextField txtCity;
    private JComboBox<Vet> cmbPreferredVet;
    private JComboBox<Sex> cmbSex;

    // Tabla
    private JTable tblOwners;
    private DefaultTableModel tableModel;

    // Navegación lista ↔ detalle
    private final CardLayout cardLayout;
    private final JPanel cards;
    private final OwnerDetailPanel detailPanel;

    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnViewDetail;

    public OwnerUI(OwnerService ownerService, VetService vetService) {
        this.ownerService = ownerService;
        this.vetService = vetService;
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(cardLayout);
        this.detailPanel = new OwnerDetailPanel(() -> cardLayout.show(cards, "lista"));
        initializeComponents();
        configureLayout();
        configureListeners();
        loadTableData();
    }

    private void initializeComponents() {
        txtDni = createTextField();
        txtName = createTextField();
        txtSurname = createTextField();
        txtPhone = createTextField();
        txtStreet = createTextField();
        txtCity = createTextField();
        cmbPreferredVet = new JComboBox<>();
        cmbSex = new JComboBox<>(Sex.values());

        btnSave = createButton("Guardar", UIConstants.COLOR_SUCCESS);
        btnUpdate = createButton("Actualizar", UIConstants.COLOR_ACCENT);
        btnDelete = createButton("Eliminar", UIConstants.COLOR_ERROR);
        btnClear = createButton("Limpiar", UIConstants.COLOR_TEXT_SECONDARY);
        btnViewDetail = createButton("Ver Detalle", UIConstants.COLOR_CARD_PETS);

        tblOwners = new JTable();
        setupTable();
    }

    private void configureLayout() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(UIConstants.PADDING_LARGE, 0));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        cards.setOpaque(false);
        cards.add(mainPanel, "lista");
        cards.add(detailPanel, "detalle");

        add(cards, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_LARGE)
        ));

        // Título
        JLabel lblTitle = new JLabel("Datos del Dueño");
        lblTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblTitle.setForeground(UIConstants.COLOR_CARD_OWNER);

        // Panel de campos
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, 0, UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM);
        gbc.anchor = GridBagConstraints.WEST;

        addField(fieldsPanel, gbc, "DNI:", txtDni, 0, 0);
        addField(fieldsPanel, gbc, "Nombre:", txtName, 1, 0);
        addField(fieldsPanel, gbc, "Apellido:", txtSurname, 2, 0);
        addField(fieldsPanel, gbc, "Teléfono:", txtPhone, 0, 1);
        addField(fieldsPanel, gbc, "Calle:", txtStreet, 1, 1);
        addField(fieldsPanel, gbc, "Ciudad:", txtCity, 2, 1);
        addField(fieldsPanel, gbc, "Sexo:", cmbSex, 0, 2);
        addField(fieldsPanel, gbc, "Veterinaria Preferida:", cmbPreferredVet, 0, 3);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_SMALL, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(fieldsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row, int col) {
        gbc.gridx = col * 2;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = col * 2 + 1;
        gbc.weightx = 1;
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

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(UIConstants.COLOR_WHITE);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JLabel lblTitle = new JLabel("Dueños Registrados");
        lblTitle.setFont(UIConstants.FONT_SUBTITLE);
        lblTitle.setForeground(UIConstants.COLOR_CARD_OWNER);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnViewDetail, BorderLayout.EAST);
        headerPanel.setBorder(new EmptyBorder(0, 0, UIConstants.PADDING_MEDIUM, 0));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupTable() {
        String[] columns = {"DNI", "Nombre", "Apellido", "Teléfono", "Dirección", "Vet Preferida"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOwners.setModel(tableModel);
        tblOwners.setFont(UIConstants.FONT_BODY);
        tblOwners.setRowHeight(30);
        tblOwners.setGridColor(UIConstants.COLOR_BORDER);
        tblOwners.setShowVerticalLines(false);
        tblOwners.setSelectionBackground(UIConstants.COLOR_CARD_OWNER.brighter());

        JTableHeader header = tblOwners.getTableHeader();
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setBackground(UIConstants.COLOR_CARD_OWNER);
        header.setForeground(UIConstants.COLOR_WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblOwners.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void configureListeners() {
        btnSave.addActionListener(e -> saveOwner());
        btnUpdate.addActionListener(e -> updateOwner());
        btnDelete.addActionListener(e -> deleteOwner());
        btnClear.addActionListener(e -> clearForm());
        btnViewDetail.addActionListener(e -> showOwnerDetail());

        tblOwners.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblOwners.getSelectedRow();
                if (row != -1) {
                    fillFormFromRow(row);
                } else {
                    btnSave.setEnabled(true);
                    txtDni.setEditable(true);
                }
            }
        });
    }

    private void saveOwner() {
        try {
            int dni = Integer.parseInt(txtDni.getText());
            String name = txtName.getText();
            String surname = txtSurname.getText();
            int phone = Integer.parseInt(txtPhone.getText());
            String street = txtStreet.getText();
            String city = txtCity.getText();

            if (name.isEmpty() || surname.isEmpty()) {
                showError("Nombre y Apellido son obligatorios.");
                return;
            }

            Sex sex = (Sex) cmbSex.getSelectedItem();
            Owner owner = new Owner(dni, name, surname, phone, sex, street, city);
            Vet preferredVet = (Vet) cmbPreferredVet.getSelectedItem();
            if (preferredVet != null) {
                owner.setPreferredVet(preferredVet);
            }

            ownerService.add(owner);
            loadTableData();
            clearForm();
        } catch (NumberFormatException ex) {
            showError("DNI y Teléfono deben ser números válidos.");
        } catch (RuntimeException ex) {
            showError("Error al guardar: " + ex.getMessage());
        }
    }

    private void updateOwner() {
        int selectedRow = tblOwners.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccioná un dueño de la tabla.");
            return;
        }

        try {
            int dni = (int) tableModel.getValueAt(selectedRow, 0);
            Owner owner = ownerService.getById(dni);
            if (owner == null) {
                showError("Dueño no encontrado.");
                return;
            }

            owner.setName(txtName.getText());
            owner.setSurname(txtSurname.getText());
            owner.setPhone(Long.parseLong(txtPhone.getText()));
            owner.getAddress().setStreet(txtStreet.getText());
            owner.getAddress().setCity(txtCity.getText());
            owner.setSex((Sex) cmbSex.getSelectedItem());

            Vet preferredVet = (Vet) cmbPreferredVet.getSelectedItem();
            owner.setPreferredVet(preferredVet);

            ownerService.update(owner);
            loadTableData();
            clearForm();
            info("Dueño actualizado exitosamente.");
        } catch (NumberFormatException ex) {
            showError("Teléfono debe ser un número válido.");
        } catch (RuntimeException ex) {
            showError("Error al actualizar: " + ex.getMessage());
        }
    }

    private void deleteOwner() {
        int selectedRow = tblOwners.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccioná un dueño de la tabla.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, UIConstants.MSG_CONFIRM_DELETE,
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int dni = (int) tableModel.getValueAt(selectedRow, 0);
        ownerService.deleteById(dni);
        loadTableData();
        clearForm();
        info("Dueño eliminado exitosamente.");
    }

    private void showOwnerDetail() {
        int row = tblOwners.getSelectedRow();
        if (row == -1) {
            showError("Seleccioná un dueño de la tabla.");
            return;
        }
        int dni = (int) tableModel.getValueAt(row, 0);
        Owner owner = ownerService.getById(dni);
        if (owner != null) {
            detailPanel.setOwner(owner);
            cardLayout.show(cards, "detalle");
        } else {
            showError("Dueño no encontrado.");
        }
    }

    private void clearForm() {
        txtDni.setText("");
        txtName.setText("");
        txtSurname.setText("");
        txtPhone.setText("");
        txtStreet.setText("");
        txtCity.setText("");
        cmbPreferredVet.setSelectedIndex(-1);
        cmbSex.setSelectedIndex(0);
        txtDni.setEditable(true);
        btnSave.setEnabled(true);
        tblOwners.clearSelection();
    }

    private void fillFormFromRow(int row) {
        int dni = (int) tableModel.getValueAt(row, 0);
        Owner owner = ownerService.getById(dni);
        if (owner == null) return;

        txtDni.setText(String.valueOf(owner.getDni()));
        txtName.setText(owner.getName());
        txtSurname.setText(owner.getSurname());
        txtPhone.setText(String.valueOf(owner.getPhone()));
        txtStreet.setText(owner.getAddress().getStreet());
        txtCity.setText(owner.getAddress().getCity());
        cmbPreferredVet.setSelectedItem(owner.getPreferredVet());
        cmbSex.setSelectedItem(owner.getSex());
        txtDni.setEditable(false);
        btnSave.setEnabled(false);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Owner> owners = ownerService.getAll();
        for (Owner owner : owners) {
            String direccion = owner.getAddress().getStreet() + ", " + owner.getAddress().getCity();
            String vetName = owner.getPreferredVet() != null ? owner.getPreferredVet().getName() : "—";
            tableModel.addRow(new Object[]{
                    owner.getDni(), owner.getName(), owner.getSurname(),
                    owner.getPhone(), direccion, vetName
            });
        }
        tblOwners.clearSelection();
        loadVets();
    }

    private void loadVets() {
        cmbPreferredVet.removeAllItems();
        cmbPreferredVet.addItem(null);   // opción "sin veterinaria"
        List<Vet> vets = vetService.getAll();
        for (Vet vet : vets) {
            cmbPreferredVet.addItem(vet);
        }
    }

    /**
     * Actualiza los datos de la tabla (compatibilidad con OwnerDetailPanel).
     */
    public void updateTableData() {
        loadTableData();
    }

    /**
     * Muestra la lista de dueños (compatibilidad con OwnerDetailPanel).
     */
    public void showOwnerList() {
        loadTableData();
    }

    /**
     * Retorna la tabla para que OwnerDetailPanel pueda acceder.
     */
    public JTable getTblOwners() {
        return tblOwners;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void info(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}