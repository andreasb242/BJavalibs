package butti.filechooser.model;

import java.io.File;

import javax.swing.tree.DefaultTreeModel;

import butti.filechooser.DirChooserLoader;
import butti.filechooser.icons.Icons;

/**
 * The root of the DirChooser
 * 
 * @author Andreas Butti
 * 
 */
public class Root extends DirItem {

	public Root(boolean showHidden, DirChooserLoader loader, DefaultTreeModel model) {
		super(null, Icons.get("system.png"), showHidden, loader, model);
	}

	public void initRoots() {
		for (File f : File.listRoots()) {
			addDevice(f);
		}
	}

	private void addDevice(File f) {
		DirItem it = new DirItem(f, Icons.get("hdd.png"), showHidden, loader, model);
		add(it);
		it.setName("");
	}

	@Override
	public String toString() {
		return "Computer";
	}

	@Override
	public DirItem getFolder(String folder) {
		if (folder == null || folder.length() == 0) {
			return null;
		}

		char lastChar = folder.charAt(folder.length() - 1);

		if (lastChar == '\\' && folder.length() > 3 || lastChar == '/') {
			folder = folder.substring(0, folder.length() - 1);
		}

		for (DirItem it : this) {
			DirItem found = it.getFolder(folder);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
}
