package ar.com.petmanager.gui.donorsUI;

import ar.com.petmanager.domain.Donor;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.service.DonorService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

    public ContactDonorsUI(DonorService donorService) {
        this.donorService = donorService;
        initializeComponents();
        configureLayout();
        loadData();
    }

    private void initializeComponents() {
        txtSearch = new JTextField();
        txtSearch.setFont(UIConstants.FONT_BODY);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
        txtSearch.setPreferredSize(new Dimension(200, 32));

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

        // Tabla
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JScrollPane scrollPane = new JScrollPane(tblDonors);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Footer con total
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false);
        JLabel lblTotal = new JLabel("Total de donadores registrados: ");
        lblTotal.setFont(UIConstants.FONT_BODY_BOLD);
        lblTotal.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        footerPanel.add(lblTotal);
        footerPanel.setBorder(new EmptyBorder(UIConstants.PADDING_MEDIUM, 0, 0, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
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
        tblDonors.setSelectionBackground(UIConstants.COLOR_CARD_DONORS.brighter());

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

    private void loadData() {
        tableModel.setRowCount(0);
        List<Donor> donors = donorService.getAll();
        for (Donor donor : donors) {
            String direccion = donor.getAddress().getStreet() + ", " + donor.getAddress().getCity();
            tableModel.addRow(new Object[]{
                    donor.getDni(), donor.getName(), donor.getSurname(),
                    donor.getPhone(), direccion
            });
        }
    }
}