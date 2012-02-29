package butti.filechooser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import butti.filechooser.model.DirItem;
import butti.filechooser.model.FilechooserModel;
import butti.filechooser.model.Root;
import butti.filechooser.model.UnixDevices;
import butti.filechooser.model.WindowsDevices;

/**
 * A dirchooser, lists all available directories on the system
 * 
 * @author Andreas Butti
 * 
 */
public class DirChooser {
	/**
	 * The Dialog
	 */
	private JDialog dialog;

	/**
	 * If a director is selected or canceled
	 */
	private boolean directorySelected = false;

	/**
	 * The tree with the folders
	 */
	private JTree tree;

	/**
	 * The treemodel
	 */
	private FilechooserModel model;

	/**
	 * The drive combobox
	 */
	private JComboBox cbDrives;

	/**
	 * The selected path
	 */
	private JTextField txtPath;

	/**
	 * Loads the childfolder
	 */
	private DirChooserLoader loader = new DirChooserLoader();

	/**
	 * The root folder of the system
	 */
	private Root root;

	/**
	 * Creates a dirchooser
	 */
	public DirChooser() {
		this(null, false, null);
	}

	/**
	 * Creates a dirchooser
	 * 
	 * @param parent
	 *            The parent window
	 * @param selectedFolder
	 *            The selected folder
	 */
	public DirChooser(Window parent, String selectedFolder) {
		this(parent, false, selectedFolder);
	}

	/**
	 * Creates a dirchooser
	 * 
	 * @param parent
	 *            The parent window
	 * @param showHidden
	 *            Displays hidden folders
	 * @param selectedFolder
	 *            The selected folder
	 */
	public DirChooser(Window parent, boolean showHidden, String selectedFolder) {
		dialog = new JDialog(parent);
		dialog.setModal(true);
		
		dialog.setTitle("Ordner wählen");

		dialog.setLayout(new BorderLayout());
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		initCenterPanel(showHidden);
		initBottom();

		dialog.setSize(325, 400);
		dialog.setLocationRelativeTo(parent);

		if (selectedFolder != null) {
			setSelectedFolder(selectedFolder);
		}

		model.enableEvents();
	}

	/**
	 * Init select and cancel button
	 */
	private void initBottom() {
		JButton select = new JButton("Wählen");
		select.setMnemonic('W');
		select.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				directorySelected = true;
				dialog.setVisible(false);
			}

		});

		JButton cancel = new JButton("Abbrechen");
		cancel.setMnemonic('A');
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}

		});

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		southPanel.add(cancel);
		southPanel.add(select);

		dialog.add(southPanel, BorderLayout.SOUTH);
	}

	/**
	 * Init tree
	 * 
	 * @param showHidden
	 *            Shows hidden files
	 */
	private void initCenterPanel(boolean showHidden) {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		model = new FilechooserModel(null);
		root = new Root(showHidden, loader, model);
		model.setRoot(root);
		root.initRoots();

		Vector<DirItem> devices = new Vector<DirItem>();

		// Init system specific devices
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows")) {
			WindowsDevices.listDevices(root, devices);
			// } else if (os.contains("linux")) {
			// } else if (os.contains("mac")) {
		} else {
			UnixDevices.listDevices(root, devices);
		}

		cbDrives = new JComboBox(devices);
		cbDrives.setRenderer(new ComboboxCellRenderer());

		cbDrives.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbDrives.getSelectedItem() != null) {
					setSelectedFolder(((DirItem) cbDrives.getSelectedItem())
							.getFile().getAbsolutePath());
				}
			}
		});

		centerPanel.add(cbDrives, BorderLayout.NORTH);

		tree = new JTree(model);
		tree.setCellRenderer(new FolderTreeCellRenderer());
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object object = e.getPath().getLastPathComponent();
				if (object instanceof DirItem) {
					DirItem selected = (DirItem) object;
					folderSelected(selected);
				}
			}
		});

		centerPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
		dialog.add(centerPanel, BorderLayout.CENTER);

		txtPath = new JTextField();

		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new BorderLayout());
		pathPanel.add(txtPath, BorderLayout.CENTER);
		pathPanel.add(new JLabel("Verzeichnis: "), BorderLayout.WEST);
		centerPanel.add(pathPanel, BorderLayout.SOUTH);
	}

	/**
	 * Sets the selected folder
	 * 
	 * @param folder
	 *            The folder
	 * @return
	 */
	private boolean setSelectedFolder(String folder) {
		// TODO funktioniert nicht immer / korrekt
		DirItem item = root.getFolder(folder);
		if (item != null) {
			Vector<Object> objectPath = new Vector<Object>();
			Object[] arr;
			while (item.getParent() != null) {
				objectPath.add(item);
				item = item.getParent();
			}
			objectPath.add(item);

			arr = new Object[objectPath.size()];
			int count = objectPath.size();
			for (int i = 0; i < count; i++) {
				arr[count - i - 1] = objectPath.get(i);
			}

			TreePath path = new TreePath(arr);

			tree.expandPath(path);
			tree.setSelectionPath(path);
			tree.scrollPathToVisible(path);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * A folder is selected
	 * 
	 * @param selected
	 *            The selected item
	 */
	protected void folderSelected(DirItem selected) {
		File file = selected.getFile();

		if (file == null) {
			txtPath.setText("");
		} else {
			txtPath.setText(file.getAbsolutePath());
		}
	}

	/**
	 * Displays the dirchooser dialog modal
	 * 
	 * @return true if a folder is selected
	 */
	public boolean showDirectoryDialog() {
		directorySelected = false;
		dialog.setVisible(true);

		loader.stop();

		return directorySelected;
	}

	/**
	 * Gets the selected if <code>showDirectoryDialog</code> return true
	 * 
	 * @return the selected path
	 */
	public String getSelectedFolder() {
		return txtPath.getText();
	}
}
