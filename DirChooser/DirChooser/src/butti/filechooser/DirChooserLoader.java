package butti.filechooser;

import java.util.Stack;

import butti.filechooser.model.DirItem;

/**
 * Loading thread
 * 
 * @author Andreas Butti
 * 
 */
public class DirChooserLoader {
	/**
	 * The worker thread
	 */
	private Thread thread;

	/**
	 * Should run
	 */
	private boolean run = true;

	/**
	 * The stack wich have to be done
	 */
	private Stack<DirItem> stack = new Stack<DirItem>();

	/**
	 * Constructor
	 */
	public DirChooserLoader() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (run) {
					DirItem item = null;
					synchronized (stack) {
						if (!stack.empty()) {
							item = stack.pop();
						}
					}

					if (item != null) {
						item.loadChildren();
					} else {
						try {
							synchronized (thread) {
								thread.wait();
							}
						} catch (InterruptedException e) {
						}
					}
				}
			}
		});
		thread.setName("DirChooserLoader");

		thread.start();
	}

	/**
	 * Adds an intem to be loaded
	 * 
	 * @param item
	 *            The item to be loaded
	 */
	public void addInvokeLater(DirItem item) {
		synchronized (stack) {
			stack.add(0, item);
		}

		synchronized (thread) {
			// Starts the worker thread if waiting
			thread.notify();
		}
	}

	/**
	 * Stops the thread
	 */
	public void stop() {
		run = false;
		synchronized (thread) {
			// Starts the worker thread if waiting
			thread.notify();
		}
	}

}
