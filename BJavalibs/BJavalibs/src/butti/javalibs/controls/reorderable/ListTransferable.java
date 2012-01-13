package butti.javalibs.controls.reorderable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ListTransferable implements Transferable {
	public static final DataFlavor LIST_DATAFLOVER;

	static {
		DataFlavor flover = null;
		try {
			flover = new DataFlavor(ListTransferable.class, "List Item");
		} catch (Exception e) {
			e.printStackTrace();
		}
		LIST_DATAFLOVER = flover;
	}

	private Object object;

	public ListTransferable(Object object) {
		this.object = object;
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		
		if(!LIST_DATAFLOVER.equals(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}

		return object;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {LIST_DATAFLOVER};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return LIST_DATAFLOVER.equals(flavor);
	}

}
