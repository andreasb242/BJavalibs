package butti.filechooser;

import java.io.File;

import javax.swing.UIManager;

import butti.filechooser.DirChooser;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		DirChooser c;
		if(new File("C:\\").exists()) {
			c = new DirChooser(null, "C:\\");
		} else {
			c = new DirChooser(null, "/");
		}
		
		if (c.showDirectoryDialog()) {
			System.out.println("ordner gewählt: " + c.getSelectedFolder());
		}

		System.out.println("beendet");
		System.exit(0);
	}

}
