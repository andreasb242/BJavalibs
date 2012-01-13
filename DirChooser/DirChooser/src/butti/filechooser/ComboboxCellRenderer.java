package butti.filechooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import butti.filechooser.model.DirItem;

/**
 * Displays an icon in the Combobox
 * 
 * @author Andreas Butti
 * 
 */
public class ComboboxCellRenderer implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

	private JLabel empty = new JLabel();
	
	// Device
	private JPanel panel;
	private JLabel lbPath;
	private JLabel lbName;

	// Default folder
	private JLabel lbFolder = new JLabel();

	private static final Color selectedBackground = UIManager
			.getColor("Tree.selectionBackground");
	private static final Color background = UIManager
			.getColor("Tree.textBackground");

	private static final Color selectedForeground = UIManager
			.getColor("Tree.selectionForeground");
	private static final Color foreground = UIManager
			.getColor("Tree.textForeground");

	/**
	 * Default constructor
	 * 
	 * @param isOpaque
	 *            true if this component should be opaque
	 */
	public ComboboxCellRenderer() {
		panel = new JPanel();
		panel.setOpaque(true);

		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		lbPath = new JLabel();
		lbPath.setFont(lbPath.getFont().deriveFont(Font.BOLD));

		lbName = new JLabel();
		lbName.setForeground(Color.GRAY);

		panel.add(lbPath);
		panel.add(lbName);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof DirItem) {
			DirItem item = (DirItem) value;

			if (item.getName() != null) {
				lbPath.setIcon(item.getIcon(false));
				lbPath.setText(item.getPath());
				lbName.setText(" " + item.getName());
				if (isSelected) {
					panel.setBackground(selectedBackground);
					lbPath.setForeground(selectedForeground);
					lbName.setForeground(Color.YELLOW);
				} else {
					panel.setBackground(background);
					lbPath.setForeground(foreground);
					lbName.setForeground(Color.GRAY);
				}
				return panel;
			} else {
				lbFolder.setIcon(item.getIcon(false));
				lbFolder.setText(item.toString());
				if (isSelected) {
					lbFolder.setBackground(selectedBackground);
					lbFolder.setForeground(selectedForeground);
				} else {
					lbFolder.setBackground(background);
					lbFolder.setForeground(foreground);
				}
				return lbFolder;
			}
		}

		// should never be reached!
		return empty;
	}
}
