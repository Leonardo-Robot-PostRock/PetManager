package ar.com.petmanager.gui.adoptionUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.gui.utils.ImageUtils;
import ar.com.petmanager.service.OwnerService;
import ar.com.petmanager.service.PetService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Panel de adopciones.
 * Muestra mascotas disponibles para adopción con sus datos
 * y permite seleccionar un dueño para adoptar.
 */
public class AdoptionUI extends BasePanel {

    private final PetService petService;
    private final OwnerService ownerService;

    private JTable tblAvailablePets;
    private DefaultTableModel tableModel;
    private JComboBox<Owner> cmbOwners;
    private JButton btnAdopt;

    public AdoptionUI(PetService petService, OwnerService ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;

        initializeComponents();
        configureLayout();
        configureListeners();
        loadData();
    }

    private void initializeComponents() {
        tblAvailablePets = new JTable();
        setupTable();

        cmbOwners = new JComboBox<>();
        cmbOwners.setFont(UIConstants.FONT_BODY);
        cmbOwners.setPreferredSize(new Dimension(250, 32));

        btnAdopt = createButton("Adoptar Mascota", UIConstants.COLOR_SUCCESS);
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Panel de Adopciones");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JLabel lblSubtitle = new JLabel("Mascotas disponibles para adopción");
        lblSubtitle.setFont(UIConstants.FONT_BODY);
        lblSubtitle.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.setBorder(new EmptyBorder(0, 0, UIConstants.PADDING_LARGE, 0));

        // Tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JScrollPane scrollPane = new JScrollPane(tblAvailablePets);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer con selector de dueño y botón adoptar
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, UIConstants.PADDING_LARGE, 0));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, 0, 0, 0));

        JLabel lblSelectOwner = new JLabel("Seleccioná dueño:");
        lblSelectOwner.setFont(UIConstants.FONT_BODY_BOLD);
        lblSelectOwner.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        footerPanel.add(lblSelectOwner);
        footerPanel.add(cmbOwners);
        footerPanel.add(btnAdopt);

        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void setupTable() {
        String[] columns = {"ID", "Nombre", "Tipo", "Edad", "Raza", "Peso"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblAvailablePets.setModel(tableModel);
        tblAvailablePets.setFont(UIConstants.FONT_BODY);
        tblAvailablePets.setRowHeight(35);
        tblAvailablePets.setGridColor(UIConstants.COLOR_BORDER);
        tblAvailablePets.setShowVerticalLines(false);
        tblAvailablePets.setSelectionBackground(UIConstants.COLOR_CARD_PETS.brighter());

        JTableHeader header = tblAvailablePets.getTableHeader();
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setBackground(UIConstants.COLOR_CARD_PETS);
        header.setForeground(UIConstants.COLOR_WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblAvailablePets.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void configureListeners() {
        btnAdopt.addActionListener(e -> adoptPet());
    }

    private void adoptPet() {
        int selectedRow = tblAvailablePets.getSelectedRow();
        if (selectedRow == -1) {
            showError("Seleccioná una mascota para adoptar.");
            return;
        }

        Owner selectedOwner = (Owner) cmbOwners.getSelectedItem();
        if (selectedOwner == null) {
            showError("Seleccioná un dueño.");
            return;
        }

        long petId = (long) tableModel.getValueAt(selectedRow, 0);
        Pet pet = petService.getById((int) petId);

        if (pet != null) {
            selectedOwner.adoptPet(pet);
            ownerService.update(selectedOwner);
            info("¡Adopción exitosa! " + pet.getName() + " fue adoptado por " + selectedOwner.getName());
            loadData();
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Pet> pets = petService.getAll();
        for (Pet pet : pets) {
            if (pet.getOwners() == null || pet.getOwners().isEmpty()) {
                String tipo = pet instanceof ar.com.petmanager.domain.Dog ? "Perro" : "Gato";
                tableModel.addRow(new Object[]{
                        pet.getId(), pet.getName(), tipo, pet.getAge(),
                        pet.getRace(), String.format("%.1f kg", pet.getWeight())
                });
            }
        }
        loadOwners();
    }

    public void refreshData() {
        loadData();
    }

    private void loadOwners() {
        cmbOwners.removeAllItems();
        List<Owner> owners = ownerService.getAll();
        for (Owner owner : owners) {
            cmbOwners.addItem(owner);
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(UIConstants.COLOR_WHITE);
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void info(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}