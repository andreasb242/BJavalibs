package butti.javalibs.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class ButtonFactory {
	private ButtonFactory() {
	}

	public static JButton createButton(String message, boolean defaultButton) {
		JButton bt1 = new JButton(message);

		int height = 30;
		if (defaultButton) {
			bt1.setFont(bt1.getFont().deriveFont(Font.BOLD));
			height = 50;
		}

		int width = (int) bt1.getPreferredSize().getWidth();
		bt1.setPreferredSize(new Dimension(width, height));
		return bt1;
	}

}
