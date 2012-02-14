package butti.javalibs.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class BDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public BDialog() {
		this((Frame) null, false);
	}

	public BDialog(Frame owner) {
		this(owner, false);
	}

	public BDialog(Frame owner, boolean modal) {
		this(owner, null, modal);
	}

	public BDialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public BDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public BDialog(Dialog owner) {
		this(owner, false);
	}

	public BDialog(Dialog owner, boolean modal) {
		this(owner, null, modal);
	}

	public BDialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public BDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public BDialog(Window window) {
		super(window);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	protected JRootPane createRootPane() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				processWindowEvent(new WindowEvent(BDialog.this, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
