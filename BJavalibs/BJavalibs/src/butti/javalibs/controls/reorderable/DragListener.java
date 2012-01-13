package butti.javalibs.controls.reorderable;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JList;

class DragListener<E> implements DragSourceListener, DragGestureListener {
	private JList list;

	private DragSource ds = new DragSource();

	public DragListener(JList list) {
		this.list = list;
		ds.createDefaultDragGestureRecognizer(list, DnDConstants.ACTION_MOVE,
				this);

	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		ListTransferable transferable = new ListTransferable(list
				.getSelectedValue());
		ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable, this);
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	}

	public void dragExit(DragSourceEvent dse) {
	}

	public void dragOver(DragSourceDragEvent dsde) {
	}

	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	}
}