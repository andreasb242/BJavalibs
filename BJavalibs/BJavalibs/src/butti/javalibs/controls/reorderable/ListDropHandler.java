package butti.javalibs.controls.reorderable;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.TransferHandler;

class ListDropHandler<E> extends TransferHandler {
	private static final long serialVersionUID = 1L;
	private ReorderableList<E> list;

	public ListDropHandler(ReorderableList<E> list) {
		this.list = list;
	}

	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDataFlavorSupported(ListTransferable.LIST_DATAFLOVER)) {
			return false;
		}
		JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
		if (dl.getIndex() == -1) {
			return false;
		} else {
			return true;
		}
	}

	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}

		Transferable transferable = support.getTransferable();
		E object;
		try {
			object = extracted(transferable);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
		int dropTargetIndex = dl.getIndex();

		list.movePosition(dropTargetIndex, object);
		
		return true;
	}

	@SuppressWarnings("unchecked")
	private E extracted(Transferable transferable)
			throws UnsupportedFlavorException, IOException {
		return (E) transferable
				.getTransferData(ListTransferable.LIST_DATAFLOVER);
	}
}