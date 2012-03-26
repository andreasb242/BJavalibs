package butti.javalibs.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JWindow;

import butti.javalibs.controls.TitleLabel;

public class TextEditDialog extends BDialog {
	private static final long serialVersionUID = 1L;

	private GridBagManager gbm;
	private TitleLabel lbDescription = new TitleLabel("Description");
	private JTextField txtText = new JTextField();
	private String text = null;

	public TextEditDialog(String description) {
		this(null, description);
	}

	public TextEditDialog(JWindow win, String description) {
		super(win);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);

		gbm = new GridBagManager(this, true);

		gbm.setX(0).setWeightY(0).setWidth(3).setY(0).setComp(lbDescription);
		gbm.setX(0).setWeightY(1).setWidth(3).setY(0).setComp(txtText);

		JButton btCancel = ButtonFactory.createButton("Abbrechne", false);
		btCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		gbm.setX(1).setY(2).setWeightY(0).setWeightX(0).setComp(btCancel);

		JButton btOK = ButtonFactory.createButton("Speichern", false);
		btOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				text = txtText.getText();
				dispose();
			}
		});
		gbm.setX(1).setY(2).setWeightY(0).setWeightX(0).setComp(btOK);
	}

	public void setDescription(String description) {
		lbDescription.setText(description);
	}
}
