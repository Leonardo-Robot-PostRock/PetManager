package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Dog;
import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.gui.base.BasePanel;
import ar.com.petmanager.gui.constants.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Panel de detalle de dueño.
 * Muestra avatar, info completa, y resumen de mascotas.
 */
public class OwnerDetailPanel extends BasePanel {

    private Owner currentOwner;

    private JLabel lblName;
    private JLabel lblDni;
    private JLabel lblPhone;
    private JLabel lblAddress;
    private JLabel lblSex;
    private JLabel lblVet;
    private JLabel lblPetCount;
    private JLabel lblPetTypes;
    private JTextArea txtPetNames;

    private JPanel avatarPanel;
    private JButton btnBack;

    private final Runnable onBackToList;

    public OwnerDetailPanel(Runnable onBack) {
        this.onBackToList = onBack;

        initializeComponents();
        configureLayout();
        configureListeners();
    }

    private void initializeComponents() {
        lblName = new JLabel();
        lblDni = new JLabel();
        lblPhone = new JLabel();
        lblAddress = new JLabel();
        lblSex = new JLabel();
        lblVet = new JLabel();
        lblPetCount = new JLabel();
        lblPetTypes = new JLabel();

        txtPetNames = new JTextArea();
        txtPetNames.setEditable(false);
        txtPetNames.setFont(UIConstants.FONT_BODY);
        txtPetNames.setLineWrap(true);
        txtPetNames.setWrapStyleWord(true);
        txtPetNames.setBackground(UIConstants.COLOR_WHITE);

        avatarPanel = new JPanel();
        avatarPanel.setPreferredSize(new Dimension(150, 150));
        avatarPanel.setOpaque(false);

        btnBack = createButton("← Volver", UIConstants.COLOR_TEXT_SECONDARY);
    }

    private void configureLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(btnBack);

        JPanel centerPanel = new JPanel(new BorderLayout(UIConstants.PADDING_LARGE, 0));
        centerPanel.setOpaque(false);

        centerPanel.add(createAvatarSection(), BorderLayout.WEST);
        centerPanel.add(createInfoSection(), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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

        // Card: info del dueño
        JPanel ownerCard = new JPanel();
        ownerCard.setLayout(new BoxLayout(ownerCard, BoxLayout.Y_AXIS));
        ownerCard.setOpaque(false);
        ownerCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE)
        ));

        lblName.setFont(UIConstants.FONT_TITLE);
        lblName.setForeground(UIConstants.COLOR_CARD_OWNER);

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nameRow.setOpaque(false);
        nameRow.add(lblName);
        ownerCard.add(nameRow);
        ownerCard.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_LARGE)));
        ownerCard.add(createInfoRow("DNI:", lblDni));
        ownerCard.add(createInfoRow("Teléfono:", lblPhone));
        ownerCard.add(createInfoRow("Dirección:", lblAddress));
        ownerCard.add(createInfoRow("Sexo:", lblSex));
        ownerCard.add(createInfoRow("Vet. Preferida:", lblVet));

        wrapper.add(ownerCard);
        wrapper.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_LARGE)));

        // Card: mascotas
        wrapper.add(createPetsCard());

        return wrapper;
    }

    private JPanel createPetsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.COLOR_BORDER, 1, true),
                new EmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE,
                        UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE)
        ));

        JLabel lblPets = new JLabel("Mascotas");
        lblPets.setFont(UIConstants.FONT_SUBTITLE);
        lblPets.setForeground(UIConstants.COLOR_CARD_OWNER);
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerRow.setOpaque(false);
        headerRow.add(lblPets);
        card.add(headerRow);
        card.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_MEDIUM)));
        card.add(createInfoRow("Cantidad:", lblPetCount));
        card.add(createInfoRow("Tipos:", lblPetTypes));

        JLabel lblNames = new JLabel("Nombres:");
        lblNames.setFont(UIConstants.FONT_BODY_BOLD);
        lblNames.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        JPanel namesRow = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        namesRow.setOpaque(false);
        namesRow.add(lblNames);

        JScrollPane scrollNames = new JScrollPane(txtPetNames);
        scrollNames.setPreferredSize(new Dimension(300, 60));
        scrollNames.setBorder(new LineBorder(UIConstants.COLOR_BORDER, 1));

        card.add(namesRow);
        card.add(Box.createRigidArea(new Dimension(0, UIConstants.PADDING_SMALL)));
        card.add(scrollNames);

        return card;
    }

    private JPanel createInfoRow(String label, JLabel value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, UIConstants.PADDING_MEDIUM, 0));
        row.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY_BOLD);
        lbl.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        lbl.setPreferredSize(new Dimension(110, 25));

        value.setFont(UIConstants.FONT_BODY);
        value.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        row.add(lbl);
        row.add(value);
        return row;
    }

    public void setOwner(Owner owner) {
        this.currentOwner = owner;

        lblName.setText(owner.getName() + " " + owner.getSurname());
        lblDni.setText(String.valueOf(owner.getDni()));
        lblPhone.setText(String.valueOf(owner.getPhone()));
        lblAddress.setText(owner.getAddress().getStreet() + ", " + owner.getAddress().getCity());
        lblSex.setText(owner.getSex() != null ? owner.getSex().name() : "—");
        lblVet.setText(owner.getPreferredVet() != null
                ? owner.getPreferredVet().getName() + " — " + owner.getPreferredVet().getAddress().getCity()
                : "Sin asignar");

        // Mascotas
        List<Pet> pets = owner.getPets();
        if (pets == null || pets.isEmpty()) {
            lblPetCount.setText("0");
            lblPetTypes.setText("—");
            txtPetNames.setText("No tiene mascotas.");
        } else {
            lblPetCount.setText(String.valueOf(pets.size()));
            long dogs = pets.stream().filter(p -> p instanceof Dog).count();
            long cats = pets.size() - dogs;
            lblPetTypes.setText(dogs + " Perro" + (dogs != 1 ? "s" : "")
                    + ", " + cats + " Gato" + (cats != 1 ? "s" : ""));

            StringBuilder names = new StringBuilder();
            for (Pet pet : pets) {
                names.append("• ").append(pet.getName())
                        .append(" (").append(pet instanceof Dog ? "Perro" : "Gato").append(")\n");
            }
            txtPetNames.setText(names.toString().trim());
        }

        updateAvatar(owner);
    }

    private void updateAvatar(Owner owner) {
        avatarPanel.removeAll();
        String initials = owner.getName().substring(0, 1) + owner.getSurname().substring(0, 1);
        avatarPanel.add(ar.com.petmanager.gui.utils.ImageUtils.createAvatarPlaceholder(
                initials, 140, UIConstants.COLOR_CARD_OWNER));
        avatarPanel.revalidate();
        avatarPanel.repaint();
    }

    private void configureListeners() {
        btnBack.addActionListener(e -> onBackToList.run());
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
