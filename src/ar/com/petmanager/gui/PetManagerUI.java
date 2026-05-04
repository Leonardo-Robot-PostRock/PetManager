package ar.com.petmanager.gui;

import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.gui.adoptionUI.AdoptionUI;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.gui.donorsUI.ContactDonorsUI;
import ar.com.petmanager.gui.homeUI.HomeUI;
import ar.com.petmanager.gui.ownerUI.OwnerUI;
import ar.com.petmanager.gui.petUI.PetUI;
import ar.com.petmanager.gui.utils.ImageUtils;
import ar.com.petmanager.gui.vetUI.VetsUI;
import ar.com.petmanager.service.DonorServiceImpl;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.PetServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Main frame de PetManager.
 * Shell principal con sidebar de navegación, header y panel de contenido con CardLayout.
 * Orquesta todas las vistas del sistema.
 */
public class PetManagerUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private HomeUI homePanel;
    private OwnerUI ownerPanel;
    private PetUI petPanel;
    private VetsUI vetsPanel;
    private ContactDonorsUI donorsPanel;
    private AdoptionUI adoptionPanel;

    private JLabel lblActiveSection;
    private JButton btnActive;

    // Servicios
    private final OwnerServiceImpl ownerService;
    private final VetServiceImpl vetService;
    private final PetServiceImpl petService;
    private final DonorServiceImpl donorService;

    public PetManagerUI() {
        // Inicializar servicios
        this.ownerService = new OwnerServiceImpl();
        this.vetService = new VetServiceImpl(ownerService);
        this.petService = new PetServiceImpl();
        this.donorService = new DonorServiceImpl();

        // Configurar ventana
        setTitle(UIConstants.APP_TITLE);
        setSize(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        // Layout principal
        cardLayout = new CardLayout();

        initializeViews();
        configureLayout();
        setVisible(true);
    }

    private void initializeViews() {
        homePanel = new HomeUI(panelName -> navigateTo(panelName));
        ownerPanel = new OwnerUI(ownerService, vetService);
        petPanel = new PetUI(petService);
        vetsPanel = new VetsUI(vetService);
        donorsPanel = new ContactDonorsUI(donorService);
        adoptionPanel = new AdoptionUI(petService, ownerService);
    }

    private void configureLayout() {
        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.COLOR_BACKGROUND);

        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);

        // Sidebar
        mainPanel.add(createSidebar(), BorderLayout.WEST);

        // Content
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        contentPanel.add(homePanel, "inicio");
        contentPanel.add(ownerPanel, "dueños");
        contentPanel.add(petPanel, "mascotas");
        contentPanel.add(vetsPanel, "veterinarias");
        contentPanel.add(donorsPanel, "donantes");
        contentPanel.add(adoptionPanel, "adopciones");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        navigateTo("inicio");
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.HEADER_HEIGHT));
        header.setBorder(new EmptyBorder(0, UIConstants.PADDING_LARGE, 0, UIConstants.PADDING_LARGE));

        // Logo y título
        JLabel lblLogo = new JLabel(UIConstants.APP_TITLE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);

        // Indicador de sección activa
        lblActiveSection = new JLabel("Inicio");
        lblActiveSection.setFont(UIConstants.FONT_BODY_BOLD);
        lblActiveSection.setForeground(Color.WHITE);

        header.add(lblLogo, BorderLayout.WEST);
        header.add(lblActiveSection, BorderLayout.CENTER);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(UIConstants.SIDEBAR_WIDTH, UIConstants.WINDOW_HEIGHT));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(new LineBorder(UIConstants.COLOR_BORDER));

        // Logo section
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        JLabel iconLabel = ImageUtils.createIconLabel(UIConstants.PATH_ICON_PET, 40, 40);
        logoPanel.add(iconLabel);
        logoPanel.setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, 0, UIConstants.PADDING_LARGE, 0));

        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_MEDIUM)));

        // Navigation buttons
        sidebar.add(createSidebarButton("Inicio", "inicio", true));
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(createSidebarButton("Dueños", "dueños", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(createSidebarButton("Mascotas", "mascotas", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(createSidebarButton("Veterinarias", "veterinarias", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(createSidebarButton("Donantes", "donantes", false));
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(createSidebarButton("Adopciones", "adopciones", false));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createSidebarButton(String label, String panelName, boolean isActive) {
        JButton btn = new JButton(label);
        btn.setFont(UIConstants.FONT_BODY);
        btn.setForeground(isActive ? UIConstants.COLOR_PRIMARY:UIConstants.COLOR_TEXT_SECONDARY);
        btn.setBackground(isActive ? UIConstants.COLOR_PRIMARY.brighter().brighter():Color.WHITE);
        btn.setBorder(new EmptyBorder(12, UIConstants.PADDING_LARGE, 12, UIConstants.PADDING_LARGE));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(UIConstants.SIDEBAR_WIDTH - 20, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn!=btnActive) {
                    btn.setBackground(UIConstants.COLOR_BORDER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn!=btnActive) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });

        btn.addActionListener(e -> navigateTo(panelName));

        if (isActive) {
            btnActive = btn;
        }

        return btn;
    }

    private void navigateTo(String panelName) {
        cardLayout.show(contentPanel, panelName);
        lblActiveSection.setText(capitalizeSectionName(panelName));

        // Refresh de datos
        switch (panelName) {
            case "inicio":
                List<Pet> pets = petService.getAll();
                long dogs = pets.stream().filter(p -> p instanceof ar.com.petmanager.domain.Dog).count();
                long cats = pets.stream().filter(p -> p instanceof ar.com.petmanager.domain.Cat).count();
                long perdidas = pets.stream().filter(p -> p.getStatus() == ar.com.petmanager.domain.PetStatus.PERDIDA).count();
                long fallecidas = pets.stream().filter(p -> p.getStatus() == ar.com.petmanager.domain.PetStatus.FALLECIDA).count();
                homePanel.updateStats(pets.size(), (int) dogs, (int) cats,
                        ownerService.getAll().size(), vetService.getAll().size(),
                        (int) perdidas, (int) fallecidas);
                break;
            case "mascotas":
                petPanel.showList();
                break;
            default:
                break;
        }
    }

    private String capitalizeSectionName(String panelName) {
        switch (panelName) {
            case "inicio":
                return "Inicio";
            case "dueños":
                return "Dueños";
            case "mascotas":
                return "Mascotas";
            case "veterinarias":
                return "Veterinarias";
            case "donantes":
                return "Donantes";
            case "adopciones":
                return "Adopciones";
            default:
                return panelName;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PetManagerUI::new);
    }
}