package ar.com.petmanager.gui.homeUI;

import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.gui.utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Dashboard principal de PetManager.
 * Muestra estadísticas generales (total mascotas, perros, gatos, dueños, veterinarias)
 * y botones de acceso rápido a cada sección del sistema.
 */
public class HomeUI extends BasePanel {

    private JLabel lblTotalMascotas;
    private JLabel lblTotalPerros;
    private JLabel lblTotalGatos;
    private JLabel lblTotalDuenos;
    private JLabel lblTotalVets;
    private JLabel lblTotalPerdidas;
    private JLabel lblTotalFallecidas;

    private final Consumer<String> onNavigate;

    /**
     * @param onNavigate callback que recibe el nombre del panel al navegar (ej: "dueños", "mascotas")
     */
    public HomeUI(Consumer<String> onNavigate) {
        this.onNavigate = onNavigate;
        initializeComponents();
        configureLayout();
    }

    private void initializeComponents() {
        lblTotalMascotas = new JLabel("0");
        lblTotalPerros = new JLabel("0");
        lblTotalGatos = new JLabel("0");
        lblTotalDuenos = new JLabel("0");
        lblTotalVets = new JLabel("0");
        lblTotalPerdidas = new JLabel("0");
        lblTotalFallecidas = new JLabel("0");
    }

    private void configureLayout() {
        setLayout(new BorderLayout());

        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(UIConstants.PADDING_XLARGE, UIConstants.PADDING_XLARGE,
                UIConstants.PADDING_MEDIUM, UIConstants.PADDING_XLARGE));

        contentWrapper.add(createWelcomeHeader());
        contentWrapper.add(Box.createRigidArea(new Dimension(0, UIConstants.MARGIN_SECTION)));
        contentWrapper.add(createStatsPanel());
        contentWrapper.add(Box.createRigidArea(new Dimension(0, UIConstants.MARGIN_SECTION)));
        contentWrapper.add(createQuickActionsPanel());

        add(contentWrapper, BorderLayout.CENTER);
    }

    private JPanel createWelcomeHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblWelcome = new JLabel("¡Bienvenido!");
        lblWelcome.setFont(UIConstants.FONT_TITLE);
        lblWelcome.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JLabel lblSubtitle = new JLabel("Panel de Gestión de Mascotas");
        lblSubtitle.setFont(UIConstants.FONT_BODY);
        lblSubtitle.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(lblWelcome);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(lblSubtitle);

        header.add(textPanel, BorderLayout.WEST);

        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        header.setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH - UIConstants.SIDEBAR_WIDTH - 60, 80));
        return header;
    }

    private JPanel createStatsPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, UIConstants.PADDING_LARGE, UIConstants.PADDING_SMALL));
        row1.setOpaque(false);
        row1.add(createStatCard("Total Mascotas", lblTotalMascotas, UIConstants.COLOR_ACCENT));
        row1.add(createStatCard("Perros", lblTotalPerros, UIConstants.COLOR_CARD_PETS));
        row1.add(createStatCard("Gatos", lblTotalGatos, UIConstants.COLOR_CARD_OWNER));
        row1.add(createStatCard("Dueños", lblTotalDuenos, UIConstants.COLOR_CARD_DONORS));
        row1.add(createStatCard("Veterinarias", lblTotalVets, UIConstants.COLOR_CARD_VETS));

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, UIConstants.PADDING_LARGE, UIConstants.PADDING_SMALL));
        row2.setOpaque(false);
        row2.add(createStatCard("Perdidas", lblTotalPerdidas, UIConstants.COLOR_WARNING));
        row2.add(createStatCard("Fallecidas", lblTotalFallecidas, UIConstants.COLOR_ERROR));

        wrapper.add(row1);
        wrapper.add(row2);

        return wrapper;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(UIConstants.STAT_CARD_SIZE);
        card.setMaximumSize(UIConstants.STAT_CARD_SIZE);
        card.setBackground(UIConstants.COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        // Barra de color superior
        JPanel colorBar = new JPanel();
        colorBar.setBackground(color);
        colorBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        colorBar.setPreferredSize(new Dimension(UIConstants.STAT_CARD_SIZE.width - 20, 4));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.FONT_SMALL);
        lblTitle.setForeground(UIConstants.COLOR_TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(UIConstants.FONT_STAT_NUMBER);
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(colorBar);
        card.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_SMALL)));
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_SMALL)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(UIConstants.PADDING_MEDIUM, 0, UIConstants.PADDING_MEDIUM, 0));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        panel.add(createActionCard("Gestión de Clientes y Dueños",
                "Administrá dueños, datos de contacto y mascotas asociadas",
                UIConstants.COLOR_CARD_OWNER, "Dueños"));
        panel.add(createActionCard("Ver Mascotas",
                "Listado completo de mascotas. Filtros por tipo, búsqueda y detalle",
                UIConstants.COLOR_CARD_PETS, "Mascotas"));
        panel.add(createActionCard("Veterinarias",
                "Registro y consulta de veterinarias disponibles",
                UIConstants.COLOR_CARD_VETS, "Veterinarias"));
        panel.add(createActionCard("Gestión de Contacto con Donadores",
                "Listado de donantes, datos de contacto y comunicación",
                UIConstants.COLOR_CARD_DONORS, "Contactar Donantes"));

        return panel;
    }

    private JPanel createActionCard(String title, String description, Color color, String targetPanel) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM));
        card.setBackground(UIConstants.COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        // Icono
        JLabel iconLabel = ImageUtils.createIconLabel(UIConstants.PATH_ICON_PET, 32, 32);
        iconLabel.setVerticalAlignment(SwingConstants.TOP);

        // Texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.FONT_HEADING);
        lblTitle.setForeground(color);

        JLabel lblDesc = new JLabel("<html><p style='width:180px'>" + description + "</p></html>");
        lblDesc.setFont(UIConstants.FONT_SMALL);
        lblDesc.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(lblDesc);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        // Hacer la card clickeable
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onNavigate.accept(targetPanel);
            }
        });

        return card;
    }

    /**
     * Actualiza los valores de estadísticas en el dashboard.
     */
    public void updateStats(int totalMascotas, int totalPerros, int totalGatos, int totalDuenos, int totalVets, int perdidas, int fallecidas) {
        lblTotalMascotas.setText(String.valueOf(totalMascotas));
        lblTotalPerros.setText(String.valueOf(totalPerros));
        lblTotalGatos.setText(String.valueOf(totalGatos));
        lblTotalDuenos.setText(String.valueOf(totalDuenos));
        lblTotalVets.setText(String.valueOf(totalVets));
        lblTotalPerdidas.setText(String.valueOf(perdidas));
        lblTotalFallecidas.setText(String.valueOf(fallecidas));
    }
}