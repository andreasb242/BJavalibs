package butti.filechooser.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.Icon;

import butti.filechooser.icons.Icons;

/**
 * Gets the device detail information on Windows
 * 
 * @author Andreas Butti
 * 
 */
public class WindowsDevices {
	/**
	 * Not instanceable
	 */
	private WindowsDevices() {
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

		t.setName("Filechooser: WindowsDeviceListThread");
		
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
			File file = File.createTempFile("ListDevices", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objWMIService = GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\.\\root\\cimv2\")\n"
					+ "\n"
					+ "Set colDisks = objWMIService.ExecQuery(\"Select * from Win32_LogicalDisk\")\n"
					+ "\n"
					+ "For Each objDisk in colDisks\n"
					+ "  Wscript.Echo objDisk.DeviceID & objDisk.DriveType & \":\" & objDisk.VolumeName\n"
					+ "Next";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec(
					"cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				int pos = line.indexOf(":");
				String drive = line.substring(0, pos);
				int pos2 = line.indexOf(":", pos + 1);
				int type = 1;
				try {
					type = Integer.parseInt(line.substring(pos + 1, pos2));
				} catch (Exception e) {
					e.printStackTrace();
				}

				String name = line.substring(pos2 + 1);

				String typeName = drive + ":\\";

				for (DirItem a : root) {
					if (a.getPath().equals(typeName)) {
						a.setName(name);
						a.setIcon(getIcon(drive, type));
						devices.add(a);
						break;
					}
				}
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
	private static Icon getIcon(String drive, int type) {
		switch (type) {
		case 2:// Removable drive
			if ("A".equals(drive) || "B".equals(drive)) {
				return Icons.get("floppy.png");
			}
			return Icons.get("sd.png");
		case 4:// Network disk
			return Icons.get("network.png");
		case 5:// Compact disk
			return Icons.get("cdrom.png");
		case 6:// RAM disk.
			return Icons.get("memory.png");
		default:
		case 1: // No root directory. Drive type could not be determined
		case 3: // Local hard disk
			return Icons.get("hdd.png");
		}
	}
}
