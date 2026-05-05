package ar.com.petmanager.gui.petUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.PetStatus;
import ar.com.petmanager.domain.Sex;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;
import ar.com.petmanager.service.OwnerService;
import ar.com.petmanager.service.PetService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel de detalle de mascota.
 * Muestra foto/avatar, información completa, y acciones
 * (editar, eliminar, dar en adopción).
 */
public class PetDetailPanel extends BasePanel {

    private final PetService petService;
    private final OwnerService ownerService;
    private Pet currentPet;

    private JLabel lblName;
    private JLabel lblType;
    private JTextField txtAge;
    private JTextField txtWeight;
    private JLabel lblRace;
    private JCheckBox chkSick;
    private JComboBox<PetStatus> cmbStatus;
    private JTextArea txtDescription;
    private JLabel lblOwners;

    private JPanel avatarPanel;
    private JButton btnEdit;
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnAddOwner;
    private JButton btnDelete;
    private JButton btnAdopt;
    private JButton btnBack;

    private final Runnable onBackToList;

    public PetDetailPanel(PetService petService, OwnerService ownerService, Runnable onBack) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.onBackToList = onBack;

        initializeComponents();
        configureLayout();
        configureListeners();
    }

    private void initializeComponents() {
        lblName = new JLabel();
        lblType = new JLabel();
        txtAge = new JTextField();
        txtAge.setEditable(false);
        txtAge.setFont(UIConstants.FONT_BODY);
        txtAge.setPreferredSize(new Dimension(120, 28));
        txtWeight = new JTextField();
        txtWeight.setEditable(false);
        txtWeight.setFont(UIConstants.FONT_BODY);
        txtWeight.setPreferredSize(new Dimension(120, 28));
        lblRace = new JLabel();
        chkSick = new JCheckBox("Está enfermo/a");
        chkSick.setEnabled(false);
        chkSick.setFont(UIConstants.FONT_BODY);
        chkSick.setOpaque(false);
        cmbStatus = new JComboBox<>(PetStatus.values());

        txtDescription = new JTextArea();
        txtDescription.setEditable(false);
        txtDescription.setFont(UIConstants.FONT_BODY);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBackground(UIConstants.COLOR_WHITE);

        lblOwners = new JLabel();
        lblOwners.setFont(UIConstants.FONT_BODY);
        lblOwners.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(150, 150));
        avatarPanel.setOpaque(false);

        btnEdit = createButton("Editar", UIConstants.COLOR_ACCENT);
        btnSave = createButton("Guardar", UIConstants.COLOR_SUCCESS);
        btnSave.setVisible(false);
        btnCancel = createButton("Cancelar", UIConstants.COLOR_TEXT_SECONDARY);
        btnCancel.setVisible(false);
        btnAddOwner = createButton("+ Dueño", UIConstants.COLOR_CARD_OWNER);
        btnAddOwner.setVisible(false);
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
        actionPanel.add(btnSave);
        actionPanel.add(btnCancel);
        actionPanel.add(btnAddOwner);
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
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(380, Integer.MAX_VALUE));

        // Card: info principal
        JPanel infoCard = createBorderedCard();
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoCard.setMaximumSize(new Dimension(380, 320));

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nameRow.setOpaque(false);
        lblName.setFont(UIConstants.FONT_TITLE);
        lblName.setForeground(UIConstants.COLOR_CARD_PETS);
        nameRow.add(lblName);
        infoCard.add(nameRow);
        infoCard.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_MEDIUM)));
        infoCard.add(createInfoRow("Tipo:", lblType));
        infoCard.add(createTextFieldRow("Edad:", txtAge));
        infoCard.add(createTextFieldRow("Peso:", txtWeight));
        infoCard.add(createInfoRow("Raza:", lblRace));
        infoCard.add(createCheckRow("Condición:", chkSick));
        infoCard.add(createComboRow("Estatus:", cmbStatus));

        wrapper.add(infoCard);
        wrapper.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_MEDIUM)));

        // Card: Dueños
        JPanel ownersCard = createBorderedCard();
        ownersCard.setLayout(new BoxLayout(ownersCard, BoxLayout.Y_AXIS));
        ownersCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        ownersCard.setMaximumSize(new Dimension(380, 120));

        JLabel lblOwnerTitle = new JLabel("Dueños:");
        lblOwnerTitle.setFont(UIConstants.FONT_BODY_BOLD);
        lblOwnerTitle.setForeground(UIConstants.COLOR_CARD_PETS);
        ownersCard.add(lblOwnerTitle);
        ownersCard.add(lblOwners);

        wrapper.add(ownersCard);
        wrapper.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_MEDIUM)));

        // Card: Descripción
        JPanel descCard = createBorderedCard();
        descCard.setLayout(new BorderLayout(0, UIConstants.PADDING_SMALL));
        descCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        descCard.setMaximumSize(new Dimension(380, 100));

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(UIConstants.FONT_BODY_BOLD);
        lblDesc.setForeground(UIConstants.COLOR_CARD_PETS);

        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setPreferredSize(new Dimension(300, 50));
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());

        descCard.add(lblDesc, BorderLayout.NORTH);
        descCard.add(scrollDesc, BorderLayout.CENTER);

        wrapper.add(descCard);

        return wrapper;
    }

    private JPanel createBorderedCard() {
        JPanel card = new JPanel();
        card.setBackground(UIConstants.COLOR_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM,
                        UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM)
        ));
        return card;
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

    private JPanel createTextFieldRow(String label, JTextField field) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        lbl.setPreferredSize(new Dimension(60, 25));

        row.add(lbl);
        row.add(field);

        return row;
    }

    private JPanel createCheckRow(String label, JCheckBox check) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        lbl.setPreferredSize(new Dimension(60, 25));

        row.add(lbl);
        row.add(check);

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

        return row;
    }

    /**
     * Carga los datos de una mascota en el panel de detalle.
     */
    public void setPet(Pet pet) {
        this.currentPet = pet;

        lblName.setText(pet.getName());
        lblType.setText(pet instanceof ar.com.petmanager.domain.Dog ? "Perro":"Gato");
        txtAge.setText(pet.getAge());
        txtWeight.setText(String.format("%.1f", pet.getWeight()));
        lblRace.setText(pet.getRace());
        chkSick.setSelected(pet.isSick());
        cmbStatus.setSelectedItem(pet.getStatus()!=null ? pet.getStatus():PetStatus.ACTIVA);
        txtDescription.setText(pet.getDescription());
        refreshOwnersLabel();

        updateAvatar(pet);
        setEditMode(false);
    }

    private void updateAvatar(Pet pet) {
        avatarPanel.removeAll();
        String initials = pet.getName();
        Color bgColor = pet instanceof ar.com.petmanager.domain.Dog
                ? UIConstants.COLOR_CARD_PETS
                :UIConstants.COLOR_CARD_OWNER;

        avatarPanel.add(ar.com.petmanager.gui.utils.ImageUtils.createAvatarPlaceholder(initials, 140, bgColor));
        avatarPanel.revalidate();
        avatarPanel.repaint();
    }

    private void configureListeners() {
        btnBack.addActionListener(e -> navigateBack());

        btnEdit.addActionListener(e -> setEditMode(true));

        btnCancel.addActionListener(e -> {
            if (currentPet!=null) {
                // Recargar desde BD para revertir cambios
                Pet fresh = petService.getById((int) currentPet.getId());
                if (fresh!=null) {
                    currentPet = fresh;
                }
                setEditMode(false);
            }
        });

        btnSave.addActionListener(e -> {
            if (currentPet==null) return;
            try {
                currentPet.setAge(txtAge.getText().trim());
                String weightText = txtWeight.getText().trim().replace(',', '.');
                currentPet.setWeight(Double.parseDouble(weightText));
                currentPet.setSick(chkSick.isSelected());
                currentPet.setStatus((PetStatus) cmbStatus.getSelectedItem());

                petService.update(currentPet);

                setEditMode(false);
                JOptionPane.showMessageDialog(this, "Mascota actualizada correctamente.");
            } catch (NumberFormatException ex) {
                showError("El peso debe ser un número válido (ej: 3.5 o 3,5).");
            } catch (RuntimeException ex) {
                showError("Error al guardar: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            if (currentPet==null) return;
            int confirm = JOptionPane.showConfirmDialog(this, UIConstants.MSG_CONFIRM_DELETE,
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm==JOptionPane.YES_OPTION) {
                petService.deleteById((int) currentPet.getId());
                JOptionPane.showMessageDialog(this, UIConstants.MSG_DELETE_SUCCESS);
                navigateBack();
            }
        });

        btnAdopt.addActionListener(e -> {
            if (currentPet == null) return;
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Quitar todos los dueños y marcar esta mascota como disponible para adopción?",
                    "Dar en Adopción", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                petService.removeAllOwnersFromPet(currentPet.getId());
                currentPet.setOwners(new ArrayList<>());
                refreshOwnersLabel();
                JOptionPane.showMessageDialog(this, "Mascota disponible para adopción.");
            }
        });

        btnAddOwner.addActionListener(e -> {
            if (currentPet==null) return;
            // Filtrar owners que ya tienen esta mascota
            List<Owner> allOwners = ownerService.getAll();
            List<Owner> currentOwners = currentPet.getOwners();
            List<Owner> available = new ArrayList<>();
            for (Owner o : allOwners) {
                if (currentOwners!=null && currentOwners.contains(o)) continue;
                available.add(o);
            }
            if (available.isEmpty()) {
                showError("No hay dueños disponibles para asignar.");
                return;
            }
            JComboBox<Owner> cmb = new JComboBox<>();
            for (Owner o : available) cmb.addItem(o);

            int result = JOptionPane.showConfirmDialog(this, cmb,
                    "Agregar Dueño", JOptionPane.OK_CANCEL_OPTION);
            if (result==JOptionPane.OK_OPTION) {
                Owner selected = (Owner) cmb.getSelectedItem();
                petService.addOwner(selected, currentPet);
                refreshOwnersLabel();
                JOptionPane.showMessageDialog(this,
                        "Dueño asignado correctamente.");
            }
        });

        cmbStatus.setEnabled(false);
    }

    private void setEditMode(boolean edit) {
        txtAge.setEditable(edit);
        txtWeight.setEditable(edit);
        chkSick.setEnabled(edit);
        cmbStatus.setEnabled(edit);
        txtDescription.setEditable(edit);

        btnEdit.setVisible(!edit);
        btnSave.setVisible(edit);
        btnCancel.setVisible(edit);
        btnAddOwner.setVisible(edit);
        btnAdopt.setVisible(!edit);
        btnDelete.setVisible(!edit);

        if (!edit && currentPet!=null) {
            // Refrescar campos por si hubo cambios cancelados
            txtAge.setText(currentPet.getAge());
            txtWeight.setText(String.format("%.1f", currentPet.getWeight()));
            chkSick.setSelected(currentPet.isSick());
            cmbStatus.setSelectedItem(currentPet.getStatus());
            txtDescription.setText(currentPet.getDescription());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void refreshOwnersLabel() {
        if (currentPet == null) return;
        List<Owner> owners = currentPet.getOwners();
        if (owners == null || owners.isEmpty()) {
            lblOwners.setText("Sin dueño asignado.");
        } else {
            StringBuilder sb = new StringBuilder("<html>");
            for (Owner o : owners) {
                String prefix = o.getSex() == Sex.FEMENINO ? "Dueña" : "Dueño";
                sb.append(prefix).append(": ").append(o.getName()).append(" ").append(o.getSurname()).append("<br>");
            }
            sb.append("</html>");
            lblOwners.setText(sb.toString());
        }
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