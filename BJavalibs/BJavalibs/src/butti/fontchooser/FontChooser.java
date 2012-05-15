package butti.fontchooser;

import java.awt.Component;
import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class FontChooser {

	public static Font chooseFont(Component parent, Font original) {
		FontChooserPanel fontChooser = new FontChooserPanel(original);

		EditorDialog dlg = EditorDialog.create(parent, "Schrift wählen", fontChooser);

		if (dlg.display()) {
			return fontChooser.getSelectedFont();
		}

		return null;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		System.out.println(chooseFont(null, null));

	}
}
