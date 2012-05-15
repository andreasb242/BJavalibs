package butti.fontchooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import butti.javalibs.gui.ButtonFactory;

public class EditorDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private boolean ok = false;

	public EditorDialog(Window parent, String title, Component contents) {
		super(parent);
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		add(contents, BorderLayout.CENTER);

		JButton btCancel = ButtonFactory.createButton("Abbrechen", false);
		btCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton btOk = ButtonFactory.createButton("OK", true);
		btOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ok = true;
				dispose();
			}
		});

		int width = Math.max(btOk.getPreferredSize().width, btCancel.getPreferredSize().width);
		btOk.setPreferredSize(new Dimension(width, btOk.getPreferredSize().height));
		btCancel.setPreferredSize(new Dimension(width, btCancel.getPreferredSize().height));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(btCancel);
		buttonPanel.add(btOk);

		add(buttonPanel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(parent);
		setModal(true);
	}

	public static EditorDialog create(Component parent, String title, Component contents) {
		while (parent != null && !(parent instanceof Window)) {
			parent = parent.getParent();
		}
		return new EditorDialog((Window) parent, title, contents);
	}

	public boolean display() {
		setVisible(true);
		return ok;
	}
}
