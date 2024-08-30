package ar.com.petmanager.presentation;

import ar.com.petmanager.gui.PetManagerUI;

import javax.swing.*;

public class PetManagerMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(PetManagerUI::new);
	}
}
