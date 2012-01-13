package butti.filechooser.model;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class FilechooserModel extends DefaultTreeModel {
	private static final long serialVersionUID = 1L;
	private boolean enableEvents = false;

	public FilechooserModel(TreeNode root) {
		super(root);
	}
	
	public synchronized void enableEvents() {
		enableEvents = true;
	}

	@Override
	public synchronized void nodeChanged(TreeNode node) {
		if (enableEvents) {
			super.nodeChanged(node);
		}
	}
}
