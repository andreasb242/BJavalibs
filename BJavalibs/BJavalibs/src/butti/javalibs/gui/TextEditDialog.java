package butti.javalibs.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import butti.javalibs.controls.TitleLabel;

public class TextEditDialog extends BDialog {
	private static final long serialVersionUID = 1L;

	private GridBagManager gbm;
	private TitleLabel lbDescription = new TitleLabel("Description");
	private JTextField txtText = new JTextField();
	private JLabel lbError = new JLabel(" ");
	private String text = null;

	private Color defaultTextBackground;

	public TextEditDialog(String description, TextChecker checker) {
		this(null, description, checker);
	}

	public TextEditDialog(Window win, String description, final TextChecker checker) {
		super(win);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		
		setDescription(description);

		gbm = new GridBagManager(this);

		gbm.setX(0).setWeightY(0).setWidth(3).setY(0).setComp(lbDescription);
		gbm.setX(0).setWeightY(0).setWidth(3).setY(1).setComp(txtText);
		gbm.setX(0).setWeightY(0).setWidth(3).setY(2).setComp(lbError);

		JButton btCancel = ButtonFactory.createButton("Abbrechne", false);
		btCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		gbm.setX(0).setY(10).setComp(new JLabel());// dummy

		gbm.setX(1).setY(10).setWeightY(0).setFill(GridBagConstraints.NONE).setWeightX(0).setComp(btCancel);

		JButton btOK = ButtonFactory.createButton("Speichern", true);
		btOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				text = txtText.getText();
				dispose();
			}
		});
		gbm.setX(2).setY(10).setWeightY(0).setFill(GridBagConstraints.NONE).setWeightX(0).setComp(btOK);

		txtText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				checkText();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkText();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				checkText();
			}

			private void checkText() {
				String txt = txtText.getText();
				if (checker.isValidText(txt)) {
					lbError.setText(" ");
					txtText.setBackground(defaultTextBackground);
				} else {
					lbError.setText(checker.getErrorDescription());
					txtText.setBackground(new Color(0xFF9B9B));
				}
			}
		});

		defaultTextBackground = txtText.getBackground();

		pack();
		setLocationRelativeTo(win);
	}

	public void setText(String text) {
		this.txtText.setText(text);
	}

	/**
	 * @return The inserted text, or <code>null</code> if canceled
	 */
	public String getText() {
		return text;
	}

	public void setDescription(String description) {
		lbDescription.setText(description);
	}

	public interface TextChecker {
		public boolean isValidText(String text);

		public String getErrorDescription();
	}

	public static void main(String[] args) {
		TextEditDialog dlg = new TextEditDialog("test", new TextChecker() {

			@Override
			public boolean isValidText(String text) {
				if ("test".equals(text)) {
					return false;
				}
				return true;
			}

			@Override
			public String getErrorDescription() {
				return "Fehler #123";
			}
		});
		dlg.setTitle("Bla");
		dlg.setVisible(true);

		System.exit(0);
	}
}
