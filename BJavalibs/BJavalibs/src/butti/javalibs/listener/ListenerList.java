package butti.javalibs.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * A Listener handler for ActionListener
 * 
 * @author Andreas Butti
 */
public class ListenerList implements ActionListener {
	private Vector<ActionListener> listener = new Vector<ActionListener>();

	public ListenerList() {
	}

	public void addListener(ActionListener listener) {
		if (listener == null) {
			throw new NullPointerException("listener == null");
		}
		this.listener.add(listener);
	}

	public boolean removeListener(ActionListener listener) {
		return this.listener.remove(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (ActionListener l : this.listener) {
			l.actionPerformed(e);
		}
	}
}
