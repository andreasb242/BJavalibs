package butti.javalibs.controls.reorderable;

import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListModel;

public class ReorderableList<E> extends JList {
	private static final long serialVersionUID = 1L;

	public ReorderableList(ListModel dataModel) {
		super(dataModel);
		setDragEnabled(true);
		setDropMode(DropMode.INSERT);

		setTransferHandler(new ListDropHandler<E>(this));

		new DragListener<E>(this);
	}
	
	void movePosition(int pos, E object) {
		ReorderableModel model = (ReorderableModel) getModel();
		model.moveTo(pos, object);
	}
}
