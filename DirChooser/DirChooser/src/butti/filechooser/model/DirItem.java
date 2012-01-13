package butti.filechooser.model;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import butti.filechooser.DirChooserLoader;
import butti.filechooser.icons.Icons;

/**
 * Display items in the tree
 * 
 * @author Andreas Butti
 * 
 */
public class DirItem implements TreeNode, Iterable<DirItem> {
	/**
	 * The items for the current folder
	 */
	private Vector<DirItem> items = new Vector<DirItem>();

	/**
	 * The parent item of the current folder
	 */
	private DirItem parent = null;

	/**
	 * The open icon
	 */
	protected Icon iconOpen;

	/**
	 * The close icon
	 */
	protected Icon iconClosed;

	/**
	 * The model to fire events
	 */
	protected DefaultTreeModel model;

	/**
	 * True if the folder details have been loaded
	 */
	private boolean loaded = false;

	/**
	 * The children have been loaded
	 */
	private boolean childrenLoaded;

	/**
	 * The file to display in the tree
	 */
	private File file;

	/**
	 * The name of the device
	 */
	private String name;

	/**
	 * Folders in this folder
	 */
	private File[] childFiles;

	/**
	 * Should hidden folders be displayed?
	 */
	protected boolean showHidden;

	/**
	 * If the folder is leaf (has no children)
	 */
	private boolean isLeaf = false;

	/**
	 * The loader thread
	 */
	protected DirChooserLoader loader;

	/**
	 * Default closed folder icon
	 */
	private static Icon folder = Icons.get("folder.png");

	/**
	 * Default open folder icon
	 */
	private static Icon folderOpen = Icons.get("folder_open.png");

	/**
	 * Creates a new folder item
	 * 
	 * @param file
	 *            The file to display
	 * @param iconOpen
	 *            The icon if opened in the tree
	 * @param iconClosed
	 *            The icon if closed in the tree
	 * @param showHidden
	 *            Displays hidden files
	 */
	protected DirItem(File file, Icon iconOpen, Icon iconClosed,
			boolean showHidden, DirChooserLoader loader, DefaultTreeModel model) {
		this.file = file;
		this.iconOpen = iconOpen;
		this.iconClosed = iconClosed;
		this.showHidden = showHidden;
		this.loader = loader;
		this.model = model;

		addLoadingTask();
	}

	protected void addLoadingTask() {
		loader.addInvokeLater(this);
	}

	public synchronized void loadChildren() {
		if (childrenLoaded) {
			return;
		} else {
			childrenLoaded = true;
		}

		if (file != null) {
			Vector<File> folders = new Vector<File>();
			File[] files = file.listFiles();

			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						if (!showHidden && !f.isHidden()) {
							folders.add(f);
						}
					}
				}

			}

			childFiles = folders.toArray(new File[] {});

			if (childFiles.length == 0) {
				isLeaf = true;
			}
			fireItemChanged();
		} else {
			childFiles = new File[] {};
		}
	}

	/**
	 * Creates a new folder item
	 * 
	 * @param file
	 *            The file to display
	 * @param icon
	 *            The icon to display in the tree
	 * @param showHidden
	 *            Displays hidden files
	 * 
	 */
	protected DirItem(File file, Icon icon, boolean showHidden,
			DirChooserLoader loader, DefaultTreeModel model) {
		this(file, icon, icon, showHidden, loader, model);
	}

	@Override
	public Enumeration<DirItem> children() {
		return items.elements();
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public DirItem getChildAt(int childIndex) {
		return items.elementAt(childIndex);
	}

	@Override
	public int getChildCount() {
		loadFiles();
		return items.size();
	}

	/**
	 * Loads the filelist in the tree
	 */
	protected synchronized void loadFiles() {
		if (childFiles == null) {
			loadChildren();
		}
		if (loaded) {
			return;
		} else {
			loaded = true;
		}
		for (File f : childFiles) {
			add(new DirItem(f, folder, folderOpen, showHidden, loader, model));
		}
	}

	@Override
	public int getIndex(TreeNode node) {
		return items.indexOf(node);
	}

	@Override
	public DirItem getParent() {
		return parent;
	}

	/**
	 * Sets the parent of a treeitem
	 * 
	 * @param parent
	 *            The parent item
	 */
	public void setParent(DirItem parent) {
		this.parent = parent;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * Adds an item to the current folder
	 * 
	 * @param it
	 *            The item
	 */
	public void add(DirItem it) {
		it.setParent(this);
		items.add(it);
	}

	/**
	 * Gets the icon
	 * 
	 * @param expanded
	 *            If the treeitem is expanded
	 * @return The icon
	 */
	public Icon getIcon(boolean expanded) {
		if (expanded) {
			return iconOpen;
		} else {
			return iconClosed;
		}
	}

	@Override
	public Iterator<DirItem> iterator() {
		return items.iterator();
	}

	/**
	 * Fires the item changed
	 */
	protected void fireItemChanged() {
		if (SwingUtilities.isEventDispatchThread()) {
			model.nodeChanged(this);
		} else {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					try {// Multithreading problem...
						model.nodeChanged(DirItem.this);
					} catch (Exception e) {
					}
				}
			});
		}
	}

	/**
	 * Sets the model
	 * 
	 * @param model
	 *            The model
	 */
	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}

	/**
	 * Gets the file
	 * 
	 * @return The displayed file
	 */
	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return file.getName();
	}

	/**
	 * Get the absolute path
	 * 
	 * @return The path of the folder
	 */
	public String getPath() {
		return file.getAbsolutePath();
	}

	/**
	 * Gets the name
	 * 
	 * @return The device name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 *            The name of the device
	 */
	public void setName(String name) {
		this.name = name;
		fireItemChanged();
	}

	/**
	 * Sets the icon
	 * 
	 * @param icon
	 *            The icon
	 */
	public void setIcon(Icon icon) {
		this.iconClosed = icon;
		this.iconOpen = icon;
		fireItemChanged();
	}

	/**
	 * Gets the folder specified!
	 * 
	 * @param folder
	 *            The folder
	 */
	public DirItem getFolder(String folder) {
		String path = file.getAbsolutePath();

		if (path.equals(folder)) {
			return this;
		}

		if (folder.startsWith(path)) {
			if (!loaded) {
				loadFiles();
			}
			if (!childrenLoaded) {
				loadChildren();
			}

			for (DirItem it : this) {
				DirItem found = it.getFolder(folder);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}
}
