package ar.com.petmanager.gui;

import ar.com.petmanager.gui.adoptionUI.AdoptionUI;
import ar.com.petmanager.gui.ownerUI.OwnerUI;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import java.awt.*;

public class PetManagerUI extends JFrame {
    private JPanel contentPanel;
    private JButton btnHome;
    private JButton btnAdoption;
    private JButton btnViewPets;
    private JButton btnOwner;
    private JPanel sidebar;
    private JButton btnContactDonors;
    private JButton btnSearchVets;
    private OwnerServiceImpl ownerService;
    private VetServiceImpl vetService;

    public PetManagerUI() {
        setTitle("Pet Manager");
        setSize(800, 960);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        Dimension buttonSize = new Dimension(400, 200);

        btnHome = new JButton("Inicio");
        btnAdoption = new JButton("Adoptar");
        btnContactDonors = new JButton("Contactar Donantes");
        btnViewPets = new JButton("Ver mascotas");
        btnOwner = new JButton("Dueños");
        btnSearchVets = new JButton("Buscar Veterinarias");

        btnHome.setMaximumSize(buttonSize);
        btnAdoption.setMaximumSize(buttonSize);
        btnContactDonors.setMaximumSize(buttonSize);
        btnViewPets.setMaximumSize(buttonSize);
        btnOwner.setMaximumSize(buttonSize);
        btnSearchVets.setMaximumSize(buttonSize);

        btnHome.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdoption.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContactDonors.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnViewPets.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOwner.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSearchVets.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnHome);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnAdoption);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnViewPets);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnOwner);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnContactDonors);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSearchVets);
        sidebar.add(Box.createVerticalGlue());

        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());

        this.ownerService = new OwnerServiceImpl();
        this.vetService = new VetServiceImpl(ownerService);

        JPanel ownerPanel = new OwnerUI(ownerService, vetService);
        JPanel adoptionPanel = new AdoptionUI();

        contentPanel.add(adoptionPanel, "Adopciones");
        contentPanel.add(ownerPanel, "Dueños");

        btnViewPets.addActionListener(e -> switchPanel("Agregar Mascota"));
        btnAdoption.addActionListener(e -> switchPanel("Adopciones"));
        btnContactDonors.addActionListener(e -> switchPanel("Contactar Donantes"));
        btnHome.addActionListener(e -> switchPanel("Inicio"));
        btnOwner.addActionListener(e -> switchPanel("Dueños"));
        btnSearchVets.addActionListener(e -> switchPanel("Buscar Veterinarias"));

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void switchPanel(String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PetManagerUI::new);
    }
}
