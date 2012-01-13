package butti.javalibs.util;

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

public class JEscapeDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public JEscapeDialog() {
		this((Frame) null, false);
	}

	public JEscapeDialog(Frame owner) {
		this(owner, false);
	}

	public JEscapeDialog(Window owner) {
		super(owner);
	}

	public JEscapeDialog(Frame owner, boolean modal) {
		this(owner, null, modal);
	}

	public JEscapeDialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public JEscapeDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	public JEscapeDialog(Dialog owner) {
		this(owner, false);
	}

	public JEscapeDialog(Dialog owner, boolean modal) {
		this(owner, null, modal);
	}

	public JEscapeDialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public JEscapeDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
	}

	protected JRootPane createRootPane() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				processWindowEvent(new WindowEvent(JEscapeDialog.this, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		return rootPane;
	}
}
