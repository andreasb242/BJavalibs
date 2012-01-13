package butti.filechooser;

import javax.swing.UIManager;

import butti.filechooser.DirChooser;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		DirChooser c = new DirChooser(null, "C:\\ATM2000\\BuildWithPHP\\config");
		if (c.showDirectoryDialog()) {
			System.out.println("ordner gewählt: " + c.getSelectedFolder());
		}

		System.out.println("beendet");
		System.exit(0);
	}

}
