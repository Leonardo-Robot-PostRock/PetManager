package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.List;

public abstract class OwnerPanelBase extends JPanel {
    protected JTextField txtDni;
    protected JTextField txtName;
    protected JTextField txtSurname;
    protected JTextField txtPhone;
    protected JTextField txtStreet;
    protected JTextField txtCity;
    protected JComboBox<Vet> selectPreferredVet;

    protected VetServiceImpl vetService;

    public OwnerPanelBase(VetServiceImpl vetService) {
        this.vetService = vetService;
        initializeFields();
        setLayout(new GridBagLayout());
        arrangeComponents();
    }

    private void initializeFields() {
        txtDni = new JTextField(15);
        txtName = new JTextField(15);
        txtSurname = new JTextField(15);
        txtPhone = new JTextField(15);
        txtStreet = new JTextField(15);
        txtCity = new JTextField(15);
        selectPreferredVet = new JComboBox<>();
        selectPreferredVet.setPreferredSize(new Dimension(170, 20));
    }

    private void arrangeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndField("DNI:", txtDni, gbc, 0);
        addLabelAndField("Nombre:", txtName, gbc, 1);
        addLabelAndField("Apellido:", txtSurname, gbc, 2);
        addLabelAndField("Teléfono:", txtPhone, gbc, 3);
        addLabelAndField("Calle:", txtStreet, gbc, 4);
        addLabelAndField("Ciudad:", txtCity, gbc, 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Veterinario Preferido:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.WEST;
        add(selectPreferredVet, gbc);
    }

    private void addLabelAndField(String labelText, JTextField textField, GridBagConstraints gbc, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        add(textField, gbc);
    }

    protected void loadVets() {
        List<Vet> vets = vetService.getAll();
        DefaultComboBoxModel<Vet> model = new DefaultComboBoxModel<>(vets.toArray(new Vet[0]));
        selectPreferredVet.setModel(model);
    }

    protected void setPanelTitle(String title) {
        TitledBorder border = (BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        border.setTitleFont(new Font("Arial", Font.BOLD, 30));
        border.setTitleColor(Color.BLACK);

        setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 20, 20, 20),
                border
        ));
    }
}
