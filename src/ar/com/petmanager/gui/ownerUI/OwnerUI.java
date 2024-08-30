package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import java.awt.*;

public class OwnerUI extends JPanel {

    private CardLayout cardLayout;
    private JPanel cards;
    private OwnerTableManager ownerTableManager;
    private OwnerDetailPanel ownerDetailPanel;
    private final OwnerServiceImpl ownerService;
    private final VetServiceImpl vetService;

    public OwnerUI(OwnerServiceImpl ownerService, VetServiceImpl vetService) {
        this.ownerService = ownerService;
        this.vetService = vetService;

        initializeComponents();
        configureLayout();
        configureListeners();

        updateTableData();
    }

    private void initializeComponents() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        ownerTableManager = new OwnerTableManager(ownerService);
        OwnerFormPanel ownerFormPanel = new OwnerFormPanel(ownerService, vetService);
        ownerDetailPanel = new OwnerDetailPanel(ownerService, vetService);

        cards.add(ownerFormPanel, "Agregar Dueño");
        cards.add(ownerTableManager, "Lista de Dueños");
        cards.add(ownerDetailPanel, "Detalle del Dueño");
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        add(createNavigationPanel(), BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
    }

    private void configureListeners() {
        ownerTableManager.getTblOwners().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && ownerTableManager.getTblOwners().getSelectedRow() != -1) {
                int selectedRow = ownerTableManager.getTblOwners().getSelectedRow();
                showOwnerDetails(selectedRow);
            }
        });
    }

    public void updateTableData() {
        ownerTableManager.updateTableData();
    }

    public void showOwnerDetails(int row) {
        int ownerId = (Integer) ownerTableManager.getTblOwners().getValueAt(row, 0);
        Owner owner = ownerService.getById(ownerId);
        if (owner != null) {
            ownerDetailPanel.setOwner(owner);
            cardLayout.show(cards, "Detalle del Dueño");
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el dueño con ID: " + ownerId, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showOwnerList() {
        updateTableData();
        cardLayout.show(cards, "Lista de Dueños");
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new FlowLayout());

        JButton btnAddOwner = new JButton("Agregar Dueño");
        JButton btnViewOwners = new JButton("Ver Lista de Dueños");

        btnAddOwner.addActionListener(e -> cardLayout.show(cards, "Agregar Dueño"));
        btnViewOwners.addActionListener(e -> showOwnerList());

        navigationPanel.add(btnAddOwner);
        navigationPanel.add(btnViewOwners);

        return navigationPanel;
    }
}
