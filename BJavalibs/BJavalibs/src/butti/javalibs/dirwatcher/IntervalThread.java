package butti.javalibs.dirwatcher;

/**
 * @author: bachi
 */
public abstract class IntervalThread implements Runnable{

	private boolean running;
	private int interval;
	private Thread thread;
	
	public IntervalThread(int interval) {
		this.running = false;
		this.interval = interval;
		this.thread = null;
	}
	
	public void start() {
		if (thread != null) {
			running = false;
			while (thread.getState() != Thread.State.TERMINATED) {
				try  {
					thread.join();
				} catch (InterruptedException e) {
					//
				}
			}
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		while (running) {
			try {
				doInterval();
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				 //
			}
		}
	}

	public abstract void doInterval();
}
