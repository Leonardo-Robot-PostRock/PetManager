package ar.com.petmanager.gui.petUI;

import ar.com.petmanager.domain.Cat;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.PetStatus;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.service.PetService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Panel de detalle de mascota.
 * Muestra foto/avatar, información completa, y acciones
 * (editar, eliminar, dar en adopción).
 */
public class PetDetailPanel extends BasePanel {

    private final PetService petService;
    private Pet currentPet;

    private JLabel lblName;
    private JLabel lblType;
    private JLabel lblAge;
    private JLabel lblWeight;
    private JLabel lblRace;
    private JLabel lblSick;
    private JLabel lblStatus;
    private JComboBox<PetStatus> cmbStatus;
    private JTextArea txtDescription;

    private JPanel avatarPanel;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnAdopt;
    private JButton btnBack;

    private final Runnable onBackToList;

    public PetDetailPanel(PetService petService, Runnable onBack) {
        this.petService = petService;
        this.onBackToList = onBack;

        initializeComponents();
        configureLayout();
        configureListeners();
    }

    private void initializeComponents() {
        lblName = new JLabel();
        lblType = new JLabel();
        lblAge = new JLabel();
        lblWeight = new JLabel();
        lblRace = new JLabel();
        lblSick = new JLabel();
        lblStatus = new JLabel();
        cmbStatus = new JComboBox<>(PetStatus.values());

        txtDescription = new JTextArea();
        txtDescription.setEditable(false);
        txtDescription.setFont(UIConstants.FONT_BODY);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBackground(UIConstants.COLOR_WHITE);

        avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(150, 150));
        avatarPanel.setOpaque(false);

        btnEdit = createButton("Editar", UIConstants.COLOR_ACCENT);
        btnDelete = createButton("Eliminar", UIConstants.COLOR_ERROR);
        btnAdopt = createButton("Dar en Adopción", UIConstants.COLOR_CARD_OWNER);
        btnBack = createButton("← Volver", UIConstants.COLOR_TEXT_SECONDARY);
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        // Panel superior: back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(btnBack);

        // Panel central: foto + info
        JPanel centerPanel = new JPanel(new BorderLayout(UIConstants.PADDING_LARGE, 0));
        centerPanel.setOpaque(false);

        centerPanel.add(createAvatarSection(), BorderLayout.WEST);
        centerPanel.add(createInfoSection(), BorderLayout.CENTER);

        // Panel inferior: acciones
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, UIConstants.PADDING_LARGE, 0));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, 0, 0, 0));
        actionPanel.add(btnEdit);
        actionPanel.add(btnAdopt);
        actionPanel.add(btnDelete);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createAvatarSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 0, UIConstants.PADDING_LARGE));
        panel.add(avatarPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createInfoSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE)
        ));

        lblName.setFont(UIConstants.FONT_TITLE);
        lblName.setForeground(UIConstants.COLOR_CARD_PETS);

        panel.add(lblName);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_LARGE)));
        panel.add(createInfoRow("Tipo:", lblType));
        panel.add(createInfoRow("Edad:", lblAge));
        panel.add(createInfoRow("Peso:", lblWeight));
        panel.add(createInfoRow("Raza:", lblRace));
        panel.add(createInfoRow("Salud:", lblSick));
        panel.add(createComboRow("Estatus:", cmbStatus));
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_LARGE)));

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(UIConstants.FONT_BODY_BOLD);
        lblDesc.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setPreferredSize(new Dimension(300, 80));
        scrollDesc.setBorder(new LineBorder(UIConstants.COLOR_BORDER, 1));

        panel.add(lblDesc);
        panel.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_SMALL)));
        panel.add(scrollDesc);

        return panel;
    }

    private JPanel createInfoRow(String label, JLabel value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        lbl.setPreferredSize(new Dimension(60, 25));

        value.setFont(UIConstants.FONT_BODY);
        value.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        row.add(lbl);
        row.add(value);

        return row;
    }

    private JPanel createComboRow(String label, JComboBox<?> combo) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        lbl.setPreferredSize(new Dimension(60, 25));

        combo.setFont(UIConstants.FONT_BODY);
        combo.setPreferredSize(new Dimension(140, 28));

        row.add(lbl);
        row.add(combo);
        row.add(lblStatus);

        return row;
    }

    /**
     * Carga los datos de una mascota en el panel de detalle.
     */
    public void setPet(Pet pet) {
        this.currentPet = pet;

        lblName.setText(pet.getName());
        lblType.setText(pet instanceof ar.com.petmanager.domain.Dog ? "Perro" : "Gato");
        lblAge.setText(pet.getAge() + " años");
        lblWeight.setText(String.format("%.1f kg", pet.getWeight()));
        lblRace.setText(pet.getRace());
        lblSick.setText(pet.isSick() ? "Enfermo" : "Saludable");
        lblSick.setForeground(pet.isSick() ? UIConstants.COLOR_ERROR : UIConstants.COLOR_SUCCESS);
        lblStatus.setText(pet.getStatus() != null ? pet.getStatus().getLabel() : "Activa");
        cmbStatus.setSelectedItem(pet.getStatus() != null ? pet.getStatus() : PetStatus.ACTIVA);
        txtDescription.setText(pet.getDescription());

        updateAvatar(pet);
    }

    private void updateAvatar(Pet pet) {
        avatarPanel.removeAll();
        String initials = pet.getName();
        Color bgColor = pet instanceof ar.com.petmanager.domain.Dog
                ? UIConstants.COLOR_CARD_PETS
                : UIConstants.COLOR_CARD_OWNER;

        avatarPanel.add(ar.com.petmanager.gui.utils.ImageUtils.createAvatarPlaceholder(initials, 140, bgColor));
        avatarPanel.revalidate();
        avatarPanel.repaint();
    }

    private void configureListeners() {
        btnBack.addActionListener(e -> navigateBack());

        btnEdit.addActionListener(e -> {
            cmbStatus.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Editá el estado y demás campos, luego confirmá.");
        });

        btnDelete.addActionListener(e -> {
            if (currentPet == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, UIConstants.MSG_CONFIRM_DELETE,
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                petService.deleteById((int) currentPet.getId());
                JOptionPane.showMessageDialog(this, UIConstants.MSG_DELETE_SUCCESS);
                navigateBack();
            }
        });

        btnAdopt.addActionListener(e -> {
            if (currentPet == null) return;
            JOptionPane.showMessageDialog(this,
                    "Esta mascota será marcada como disponible para adopción.",
                    "Dar en Adopción", JOptionPane.INFORMATION_MESSAGE);
        });

        cmbStatus.addActionListener(e -> {
            if (currentPet != null && cmbStatus.getSelectedItem() != null) {
                PetStatus newStatus = (PetStatus) cmbStatus.getSelectedItem();
                currentPet.setStatus(newStatus);
                petService.update(currentPet);
                lblStatus.setText(newStatus.getLabel());
            }
        });
        cmbStatus.setEnabled(false);
    }

    private void navigateBack() {
        onBackToList.run();
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setBackground(bgColor);
        btn.setForeground(UIConstants.COLOR_WHITE);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}