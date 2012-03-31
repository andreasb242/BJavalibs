package butti.javalibs.gui.splitmenuitem.ui;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

public class NimbusSplitMenuitemUI extends BaseSplitMenuitemUI {
	public NimbusSplitMenuitemUI() {
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();

		selectionForeground = UIManager.getColor("MenuItem[MouseOver].textForeground");
		selectionForeground = new Color(selectionForeground.getRGB());
		
		selectionBackground = UIManager.getColor("nimbusSelectionBackground");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new NimbusSplitMenuitemUI();
	}
}
