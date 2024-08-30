package ar.com.petmanager.gui.ownerUI;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Vet;
import ar.com.petmanager.service.OwnerServiceImpl;
import ar.com.petmanager.service.VetServiceImpl;

import javax.swing.*;
import java.awt.*;

public class OwnerDetailPanel extends OwnerPanelBase {
    private final OwnerServiceImpl ownerService;
    private Owner currentOwner;
    private OwnerUI ownerUI;
    private JButton btnEdit, btnSave, btnCancel, btnDelete;

    public OwnerDetailPanel(OwnerServiceImpl ownerService, VetServiceImpl vetService) {
        super(vetService);
        this.ownerService = ownerService;

        setPanelTitle("Detalle del Dueño y edición");

        initializeButtons();
        arrangeButtonPanel();
        setFieldsEditable(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        loadVets();
    }

    private void initializeButtons() {
        btnEdit = new JButton("Editar");
        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        btnDelete = new JButton("Eliminar");

        btnEdit.addActionListener(e -> {
            setFieldsEditable(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        });

        btnSave.addActionListener(e -> saveOwner());

        btnCancel.addActionListener(e -> cancelEdit());

        btnDelete.addActionListener(e -> deleteOwner());
    }

    private void arrangeButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnDelete);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(buttonPanel, gbc);
    }

    public void setOwner(Owner owner) {
        this.currentOwner = owner;
        if (owner != null) {
            txtDni.setText(String.valueOf(owner.getDni()));
            txtName.setText(owner.getName());
            txtSurname.setText(owner.getSurname());
            txtPhone.setText(String.valueOf(owner.getPhone()));
            txtStreet.setText(owner.getAddress().getStreet());
            txtCity.setText(owner.getAddress().getCity());
            selectPreferredVet.setSelectedItem(owner.getPreferredVet());
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtName.setEditable(editable);
        txtSurname.setEditable(editable);
        txtPhone.setEditable(editable);
        txtStreet.setEditable(editable);
        txtCity.setEditable(editable);
        selectPreferredVet.setEnabled(editable);
    }

    private void saveOwner() {
        if (currentOwner != null) {
            try {
                currentOwner.setName(txtName.getText());
                currentOwner.setSurname(txtSurname.getText());
                currentOwner.setPhone(Integer.parseInt(txtPhone.getText()));
                currentOwner.getAddress().setStreet(txtStreet.getText());
                currentOwner.getAddress().setCity(txtCity.getText());

                Vet selectedVet = (Vet) selectPreferredVet.getSelectedItem();
                currentOwner.setPreferredVet(selectedVet);

                ownerService.update(currentOwner);
                JOptionPane.showMessageDialog(this, "Dueño actualizado exitosamente.");
                setFieldsEditable(false);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(false);
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
                showOwnerList();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: Teléfono debe ser un número.");
            }
        }
    }

    private void cancelEdit() {
        setFieldsEditable(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
    }

    private void deleteOwner() {
        if (currentOwner != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar este dueño?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ownerService.deleteById(currentOwner.getDni());
                JOptionPane.showMessageDialog(this, "Dueño eliminado exitosamente.");
                showOwnerList();
            }
        }
    }

    private void showOwnerList() {
        if (ownerUI != null) {
            ownerUI.updateTableData();
            ownerUI.showOwnerList();
        }
    }

    public void setOwnerUI(OwnerUI ownerUI) {
        this.ownerUI = ownerUI;
    }
}
