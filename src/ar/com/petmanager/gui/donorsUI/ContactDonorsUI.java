package ar.com.petmanager.gui.donorsUI;

import ar.com.petmanager.domain.Donor;
import ar.com.petmanager.domain.Sex;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.service.DonorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Panel de gestión de contacto con donadores.
 * Lista donantes con sus datos de contacto.
 */
public class ContactDonorsUI extends BasePanel {

    private final DonorService donorService;

    private JTable tblDonors;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblTotal;

    private JButton btnAdd;
    private JButton btnDelete;

    public ContactDonorsUI(DonorService donorService) {
        this.donorService = donorService;
        initializeComponents();
        configureLayout();
        configureListeners();
        loadData();
    }

    private void initializeComponents() {
        txtSearch = new JTextField();
        txtSearch.setFont(UIConstants.FONT_BODY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
        txtSearch.setPreferredSize(new Dimension(200, 32));

        lblTotal = new JLabel("Total de donadores registrados: 0");
        lblTotal.setFont(UIConstants.FONT_BODY_BOLD);
        lblTotal.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        btnAdd    = createStyledButton("Agregar Donante", UIConstants.COLOR_SUCCESS);
        btnDelete = createStyledButton("Eliminar",        UIConstants.COLOR_ERROR);

        tblDonors = new JTable();
        setupTable();
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Gestión de Contacto con Donadores");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.PADDING_SMALL, 0));
        searchPanel.setOpaque(false);
        JLabel lblSearch = new JLabel("Buscar:");
        lblSearch.setFont(UIConstants.FONT_BODY);
        lblSearch.setForeground(UIConstants.COLOR_TEXT_SECONDARY);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        headerPanel.setBorder(new EmptyBorder(0, 0, UIConstants.PADDING_LARGE, 0));

        // Tabla con barra de acciones
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        actionBar.setOpaque(false);
        actionBar.setBorder(new EmptyBorder(0, 0, UIConstants.PADDING_SMALL, 0));
        actionBar.add(btnAdd);
        actionBar.add(btnDelete);

        JScrollPane scrollPane = new JScrollPane(tblDonors);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(actionBar,  BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer con total en card
        JPanel totalCard = new JPanel(new BorderLayout());
        totalCard.setBackground(UIConstants.COLOR_WHITE);
        totalCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_CARD_DONORS, 1, true),
                new EmptyBorder(UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM)
        ));
        totalCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        totalCard.add(lblTotal, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel,  BorderLayout.CENTER);
        add(totalCard,   BorderLayout.SOUTH);
    }

    private void setupTable() {
        String[] columns = {"DNI", "Nombre", "Apellido", "Teléfono", "Dirección"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDonors.setModel(tableModel);
        tblDonors.setFont(UIConstants.FONT_BODY);
        tblDonors.setRowHeight(35);
        tblDonors.setGridColor(UIConstants.COLOR_BORDER);
        tblDonors.setShowVerticalLines(false);
        tblDonors.setSelectionBackground(new Color(80, 170, 130, 60));

        JTableHeader header = tblDonors.getTableHeader();
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setBackground(UIConstants.COLOR_CARD_DONORS);
        header.setForeground(UIConstants.COLOR_WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblDonors.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void configureListeners() {
        btnAdd.addActionListener(e -> showAddDonorDialog());
        btnDelete.addActionListener(e -> deleteDonor());

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { filterDonors(); }
            @Override public void removeUpdate(DocumentEvent e)  { filterDonors(); }
            @Override public void changedUpdate(DocumentEvent e) { filterDonors(); }
        });
    }

    private void filterDonors() {
        String query = txtSearch.getText().trim().toLowerCase();
        List<Donor> donors = donorService.getAll();
        tableModel.setRowCount(0);

        for (Donor donor : donors) {
            boolean matches = query.isEmpty()
                    || donor.getName().toLowerCase().contains(query)
                    || donor.getSurname().toLowerCase().contains(query)
                    || String.valueOf(donor.getDni()).contains(query);
            if (matches) addDonorToTable(donor);
        }

        lblTotal.setText("Total de donadores registrados: " + tableModel.getRowCount());
    }

    private void deleteDonor() {
        int row = tblDonors.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un donante de la tabla.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, UIConstants.MSG_CONFIRM_DELETE,
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int dni = (int) tableModel.getValueAt(row, 0);
        donorService.deleteById(dni);
        loadData();
    }

    private void showAddDonorDialog() {
        JPanel form = new JPanel(new GridLayout(0, 2, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));

        JTextField txtDni     = new JTextField();
        JTextField txtName    = new JTextField();
        JTextField txtSurname = new JTextField();
        JTextField txtPhone   = new JTextField();
        JTextField txtStreet  = new JTextField();
        JTextField txtCity    = new JTextField();
        JComboBox<Sex> cmbSex = new JComboBox<>(Sex.values());

        form.add(new JLabel("DNI:"));       form.add(txtDni);
        form.add(new JLabel("Nombre:"));    form.add(txtName);
        form.add(new JLabel("Apellido:"));  form.add(txtSurname);
        form.add(new JLabel("Teléfono:")); form.add(txtPhone);
        form.add(new JLabel("Calle:"));     form.add(txtStreet);
        form.add(new JLabel("Ciudad:"));    form.add(txtCity);
        form.add(new JLabel("Sexo:"));      form.add(cmbSex);

        int result = JOptionPane.showConfirmDialog(this, form,
                "Agregar Donante", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            int dni      = Integer.parseInt(txtDni.getText().trim());
            String name  = txtName.getText().trim();
            String surname = txtSurname.getText().trim();
            long phone   = Long.parseLong(txtPhone.getText().trim());
            String street = txtStreet.getText().trim();
            String city  = txtCity.getText().trim();

            if (name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y apellido son obligatorios.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Sex sex = (Sex) cmbSex.getSelectedItem();
            Donor donor = new Donor(dni, name, surname, phone, sex, street, city);
            donorService.add(donor);
            loadData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "DNI y teléfono deben ser números válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Donor> donors = donorService.getAll();
        for (Donor donor : donors) {
            addDonorToTable(donor);
        }
        lblTotal.setText("Total de donadores registrados: " + donors.size());
    }

    private void addDonorToTable(Donor donor) {
        String direccion = donor.getAddress().getStreet() + ", " + donor.getAddress().getCity();
        tableModel.addRow(new Object[]{
                donor.getDni(), donor.getName(), donor.getSurname(),
                donor.getPhone(), direccion
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(UIConstants.COLOR_WHITE);
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
