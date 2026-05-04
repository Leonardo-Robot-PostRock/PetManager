package ar.com.petmanager.gui.petUI;

import ar.com.petmanager.domain.Cat;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
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
 * Panel de listado de mascotas con filtros por tipo (Todos, Perros, Gatos).
 */
public class PetUI extends BasePanel {

    private final PetService petService;
    private final PetDetailPanel detailPanel;
    private final CardLayout cardLayout;
    private final JPanel cards;

    private JTable tblPets;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilter;

    public PetUI(PetService petService) {
        this.petService = petService;
        this.cardLayout = new CardLayout();
        this.cards = new JPanel(cardLayout);
        this.detailPanel = new PetDetailPanel(petService);

        initializeComponents();
        configureLayout();
        configureListeners();
        loadTableData();
    }

    private void initializeComponents() {
        cmbFilter = new JComboBox<>(new String[]{"Todos", "Perros", "Gatos"});
        cmbFilter.setFont(UIConstants.FONT_BODY);
        cmbFilter.setPreferredSize(new Dimension(130, 32));

        tblPets = new JTable();
        setupTable();

        cards.setOpaque(false);
        cards.add(createListPanel(), "lista");
        cards.add(detailPanel, "detalle");
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);
        cardLayout.show(cards, "lista");
    }

    private void setupTable() {
        String[] columns = {"ID", "Nombre", "Tipo", "Edad", "Peso", "Raza", "Enfermo", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPets.setModel(tableModel);
        tblPets.setFont(UIConstants.FONT_BODY);
        tblPets.setRowHeight(35);
        tblPets.setGridColor(UIConstants.COLOR_BORDER);
        tblPets.setShowVerticalLines(false);
        tblPets.setSelectionBackground(UIConstants.COLOR_CARD_PETS.brighter());

        JTableHeader header = tblPets.getTableHeader();
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setBackground(UIConstants.COLOR_CARD_PETS);
        header.setForeground(UIConstants.COLOR_WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tblPets.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        // Header con título y filtro
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Listado de Mascotas");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIConstants.PADDING_SMALL, 0));
        filterPanel.setOpaque(false);
        JLabel lblFilter = new JLabel("Filtrar por:");
        lblFilter.setFont(UIConstants.FONT_BODY);
        lblFilter.setForeground(UIConstants.COLOR_TEXT_SECONDARY);
        filterPanel.add(lblFilter);
        filterPanel.add(cmbFilter);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);
        headerPanel.setBorder(new EmptyBorder(0, 0, UIConstants.PADDING_LARGE, 0));

        // Tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        // Barra de acciones
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        actionBar.setOpaque(false);

        JButton btnViewDetail = createButton("Ver Detalle", UIConstants.COLOR_ACCENT);
        JButton btnDelete = createButton("Eliminar", UIConstants.COLOR_ERROR);
        JButton btnAdd = createButton("Agregar Mascota", UIConstants.COLOR_SUCCESS);

        actionBar.add(btnAdd);
        actionBar.add(btnViewDetail);
        actionBar.add(btnDelete);

        JScrollPane scrollPane = new JScrollPane(tblPets);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(actionBar, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    private void configureListeners() {
        cmbFilter.addActionListener(e -> filterPets());

        tblPets.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showPetDetail();
                }
            }
        });
    }

    private void filterPets() {
        String filter = (String) cmbFilter.getSelectedItem();
        List<Pet> allPets = petService.getAll();
        tableModel.setRowCount(0);

        for (Pet pet : allPets) {
            boolean include = false;
            if ("Todos".equals(filter)) include = true;
            else if ("Perros".equals(filter) && pet instanceof ar.com.petmanager.domain.Dog) include = true;
            else if ("Gatos".equals(filter) && pet instanceof Cat) include = true;

            if (include) {
                addPetToTable(pet);
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Pet> pets = petService.getAll();
        for (Pet pet : pets) {
            addPetToTable(pet);
        }
    }

    private void addPetToTable(Pet pet) {
        String tipo = pet instanceof ar.com.petmanager.domain.Dog ? "Perro" : "Gato";
        String sick = pet.isSick() ? "Sí" : "No";
        String status = pet.getStatus() != null ? pet.getStatus().getLabel() : "Activa";
        tableModel.addRow(new Object[]{
                pet.getId(), pet.getName(), tipo, pet.getAge(),
                String.format("%.1f kg", pet.getWeight()), pet.getRace(), sick, status
        });
    }

    private void showPetDetail() {
        int row = tblPets.getSelectedRow();
        if (row == -1) {
            showError("Seleccioná una mascota de la tabla.");
            return;
        }

        long petId = (long) tableModel.getValueAt(row, 0);
        Pet pet = petService.getById((int) petId);
        if (pet != null) {
            detailPanel.setPet(pet);
            cardLayout.show(cards, "detalle");
        } else {
            showError("Mascota no encontrada.");
        }
    }

    public void showList() {
        loadTableData();
        cardLayout.show(cards, "lista");
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(UIConstants.COLOR_WHITE);
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}