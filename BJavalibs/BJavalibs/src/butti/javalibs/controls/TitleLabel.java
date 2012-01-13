package butti.javalibs.controls;

import java.awt.Font;

import javax.swing.JLabel;

public class TitleLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public TitleLabel(String text) {
		super(text);
		setFont(getFont().deriveFont(Font.BOLD));
	}
}
