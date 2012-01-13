package butti.filechooser.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.Icon;

import butti.filechooser.icons.Icons;

/**
 * Gets the device detail information on Unix (mount)
 * 
 * @author Andreas Butti
 * 
 */
public class UnixDevices {
	/**
	 * Not instanceable
	 */
	public UnixDevices() {
	}

	/**
	 * List the detailinformations
	 * 
	 * @param root
	 *            The root
	 * @param devices
	 *            The devicelist
	 */
	public static void listDevices(final Root root,
			final Vector<DirItem> devices) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				doListDevices(root, devices);
			}
		});

		
		t.setName("Dirchooser: UnixDeviceListThread");
		
		t.start();
	}

	/**
	 * Gets the informations in a thread
	 * 
	 * @param root
	 *            The root
	 * @param devices
	 *            The devicelist
	 */
	private static void doListDevices(Root root, Vector<DirItem> devices) {
		try {
			Process p = Runtime.getRuntime().exec("mount");
			BufferedReader input = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				int pos = line.indexOf(" on ");
				if (pos == -1) {
					continue;
				}
				// String device = line.substring(0, pos);

				int pos2 = line.indexOf(" type ");
				if (pos2 == -1) {
					pos2 = line.length();
				}
				String mountpoint = line.substring(pos + 4, pos2);

				int pos3 = line.indexOf("(");
				if (pos3 == -1) {
					pos3 = line.length();
				}
				String type = line.substring(pos2 + 6, pos3 - 1);

				DirItem it = root.getFolder(mountpoint);
				if (it != null) {
					it.setIcon(getIcon(type));
					devices.add(it);
				}

				// System.out.println("device: " + device);
				// System.out.println("mountpoint: " + mountpoint);
				// System.out.println("type: " + type);
				//
				// System.out.println("-----------");

			}
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the icon of a device
	 * 
	 * @param drive
	 *            The drive letter
	 * @param type
	 *            The type ID
	 * @return The icon
	 */
	private static Icon getIcon(String type) {
		return Icons.get("hdd.png");
	}
}
