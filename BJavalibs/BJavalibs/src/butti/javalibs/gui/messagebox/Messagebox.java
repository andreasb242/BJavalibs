package butti.javalibs.gui.messagebox;


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import butti.javalibs.gui.GridBagManager;
import butti.javalibs.util.JEscapeDialog;


/**
 * Messagebox anzeigen
 * 
 * @author Andreas Butti
 */
public class Messagebox extends JEscapeDialog {
	private static final long serialVersionUID = 1L;

	protected GridBagManager gbm;

	private JLabel lbIcon;

	private JLabel lbMessage;

	private JPanel btPanel = new JPanel();

	private GridBagManager gbmButton;

	public static final int ERROR = 1;
	public static final int WARNING = 2;
	public static final int INFORMATION = 3;
	public static final int QUESTION = 4;

	private int nextButtonX = 0;

	private int returncode = -1;

	private static ImageIcon ERROR_ICON;
	private static ImageIcon WARNING_ICON;
	private static ImageIcon INFORMATION_ICON;
	private static ImageIcon QUESTION_ICON;

	static {
		ERROR_ICON = loadIcon("dialog-error.png");
		WARNING_ICON = loadIcon("dialog-warning.png");
		INFORMATION_ICON = loadIcon("dialog-information.png");
		QUESTION_ICON = loadIcon("dialog-question.png");
	}

	private static ImageIcon loadIcon(String file) {
		java.net.URL imgURL = Messagebox.class.getResource("icons/" + file);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("B-Messagebox: could not load icon " + file);
		}
		return null;
	}

	public Messagebox(Window parent, String title, String message, int type) {
		super(parent == null ? getDefaultParent() : parent);

		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		gbm = new GridBagManager(this);

		gbmButton = new GridBagManager(btPanel);

		ImageIcon icon = ERROR_ICON;
		switch (type) {
		case WARNING:
			icon = WARNING_ICON;
			break;
		case INFORMATION:
			icon = INFORMATION_ICON;
			break;
		case QUESTION:
			icon = QUESTION_ICON;
		}

		lbIcon = new JLabel(icon);
		lbMessage = new JLabel(message);

		gbm.setX(0).setHeight(3).setWeightX(0).setY(0).setComp(lbIcon);
		gbm.setX(1).setY(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.FIRST_LINE_START).setWeightY(0).setComp(lbMessage);

		gbm.setX(1).setHeight(3).setFill(GridBagConstraints.NONE).setWeightY(0).setAnchor(GridBagConstraints.LINE_END).setY(2).setInsets(new Insets(0, 0, 0, 0))
				.setComp(btPanel);
	}

	public JButton addButton(String message, int id) {
		return addButton(message, id, false);
	}

	public JButton addButton(String message, final int id, boolean defaultButton) {
		JButton bt1 = new JButton(message);
		if (defaultButton) {
			bt1.setFont(bt1.getFont().deriveFont(Font.BOLD));
			gbmButton.setX(nextButtonX++).setY(0).setFill(GridBagConstraints.NONE).setWeightX(0).setPreferedSize(120, 50).setComp(bt1);

		} else {
			gbmButton.setX(nextButtonX++).setY(0).setFill(GridBagConstraints.NONE).setWeightX(0).setPreferedSize(100, 30).setComp(bt1);
		}

		bt1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				returncode = id;
				setVisible(false);
			}
		});

		return bt1;
	}

	protected void calcSize() {
		setResizable(false);
		pack();
	}

	public int display() {
		calcSize();

		if (getParent() != null && getParent().isVisible()) {
			setLocationRelativeTo(getParent());
		} else {
			setLocationRelativeTo(getDefaultParent());
		}

		setModal(true);
		setVisible(true);

		dispose();

		return returncode;
	}

	private static void showMessagebox(Window container, String title, String message, int type) {
		Messagebox msgbox = new Messagebox(container, title, message, type);

		msgbox.addButton("OK", 0, true);

		msgbox.display();
	}

	public static void showInfo(Window parent, String title, String message) {
		showMessagebox(parent, title, message, Messagebox.INFORMATION);
	}

	public static void showWarning(Window parent, String title, String message) {
		showMessagebox(parent, title, message, Messagebox.WARNING);
	}

	public static void showError(Window container, String title, String message) {
		showMessagebox(container, title, message, Messagebox.ERROR);
	}

	/**
	 * 
	 * @param container
	 * @param message
	 * @return true wenn Löschen, false wenn abbrechen
	 */
	public static boolean showDeleteConfirm(Window container, String message) {
		Messagebox msgbox = new Messagebox(container, "Löschen bestätigen", message, Messagebox.QUESTION);

		msgbox.addButton("Abbrechen", 0);
		msgbox.addButton("Löschen", 1, true);

		return msgbox.display() == 1;
	}

	/**
	 * 
	 * @param container
	 * @param message
	 * @return true wenn die Datei überschrieben werden soll
	 */
	public static boolean showOverwrite(Window container, String message) {
		Messagebox msgbox = new Messagebox(container, "Speichern", message, Messagebox.QUESTION);

		msgbox.addButton("Abbrechen", 0);
		msgbox.addButton("Überschreiben", 1, true);

		return msgbox.display() == 1;
	}

	private static Window getDefaultParent() {
		for (Window w : Window.getWindows()) {
			if (w instanceof JFrame && w.isVisible()) {
				return w;
			}
		}
		for (Window w : Window.getWindows()) {
			if (w instanceof JDialog && w.isVisible()) {
				return w;
			}
		}

		for (Window w : Window.getWindows()) {
			if (w.isActive()) {
				return w;
			}
		}

		return null;
	}

}
