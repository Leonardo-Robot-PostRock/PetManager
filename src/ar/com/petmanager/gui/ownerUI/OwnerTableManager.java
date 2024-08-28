package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.service.OwnerServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OwnerTableManager extends JPanel {

    private JTable tblOwners;
    private DefaultTableModel tableModel;
    private final OwnerServiceImpl ownerService;
    private JTextField txtSearch;

    public OwnerTableManager(OwnerServiceImpl ownerService) {
        this.ownerService = ownerService;
        initializeTable();
    }

    private void initializeTable() {
        String[] columnNames = {"DNI", "Nombre", "Teléfono", "Calle", "Ciudad"};
        this.tableModel = new DefaultTableModel(columnNames, 0);
        this.tblOwners = new JTable(tableModel);
        this.tblOwners.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.txtSearch = new JTextField(30);
        JButton btnSearch = new JButton("Buscar");

        btnSearch.addActionListener(e -> searchOwnerById());

        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Buscar por DNI: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.setPreferredSize(new Dimension(600, 40));

        add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(10,10,10,10));
        JScrollPane scrollPane = new JScrollPane((tblOwners));
        scrollPane.setPreferredSize(new Dimension(600,300));

        tablePanel.add(scrollPane,BorderLayout.CENTER);

        add(tablePanel,BorderLayout.CENTER);

        updateTableData();
    }

    public void updateTableData() {
        tableModel.setRowCount(0);
        List<Owner> owners = ownerService.getAll();
        for (Owner owner : owners) {
            Object[] rowData = {
                    owner.getDni(),
                    owner.getName(),
                    owner.getPhone(),
                    owner.getAddress().getStreet(),
                    owner.getAddress().getCity()
            };
            tableModel.addRow(rowData);
        }
    }

    private void searchOwnerById() {
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
            updateTableData();
            return;
        }

        try {
            int searchId = Integer.parseInt(searchText);
            Owner owner = ownerService.getById(searchId);

            tableModel.setRowCount(0);

            if (owner != null) {
                Object[] rowData = {
                        owner.getDni(),
                        owner.getName(),
                        owner.getPhone(),
                        owner.getAddress().getStreet(),
                        owner.getAddress().getCity(),
                };

                tableModel.addRow(rowData);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el dueño con DNI: " + searchId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTable getTblOwners() {
        return tblOwners;
    }
}
