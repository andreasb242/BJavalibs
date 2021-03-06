package butti.javalibs.gui.splitmenuitem;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import butti.javalibs.gui.splitmenuitem.ui.BaseSplitMenuitemUI;
import butti.javalibs.gui.splitmenuitem.ui.SplitMenuItemUI;

public class SplitMenuitem extends AbstractButton {
	private static final long serialVersionUID = 1L;

	/**
	 * The UI class ID string.
	 */
	private static final String uiClassID = "SplitMenuItemUI";

	public static final String ADDITIONAL_ACTION_CHANGED = "additionalAction";

	private Vector<Action> additionalActions = new Vector<Action>();

	static {
		// look and feel change is currently not supported, because it anyway
		// don't work with all components

		String lnf = UIManager.getLookAndFeel().getClass().getName();
		if ("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel".equals(lnf)) {
			UIManager.put(uiClassID, "butti.javalibs.gui.splitmenuitem.ui.NimbusSplitMenuitemUI");			
		} else {
			UIManager.put(uiClassID, "butti.javalibs.gui.splitmenuitem.ui.BaseSplitMenuitemUI");
		}
	}

	public SplitMenuitem(String text) {
		setText(text);
		updateUI();
		doLayout();
	}

	/**
	 * Sets the new UI delegate.
	 * 
	 * @param ui
	 *            New UI delegate.
	 */
	public void setUI(SplitMenuItemUI ui) {
		super.setUI(ui);
	}

	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((SplitMenuItemUI) UIManager.getUI(this));
		} else {
			setUI(new BaseSplitMenuitemUI());
		}
	}

	@Override
	public SplitMenuItemUI getUI() {
		return (SplitMenuItemUI) ui;
	}

	/**
	 * Returns the name of the UI class that implements the L&F for this
	 * component.
	 * 
	 * @return The name of the UI class that implements the L&F for this
	 *         component.
	 * @see JComponent#getUIClassID
	 * @see UIDefaults#getUI
	 */
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	public void firePressed() {
		createActionListener().actionPerformed(new ActionEvent(this, 0, "pressed"));
	}

	public void addAction(Action additionalAction) {
		additionalActions.add(additionalAction);

		firePropertyChange(ADDITIONAL_ACTION_CHANGED, additionalAction, null);
	}

	public void removeAction(Action additionalAction) {
		additionalActions.remove(additionalAction);

		firePropertyChange(ADDITIONAL_ACTION_CHANGED, null, additionalAction);
	}

	public Vector<Action> getAdditionalActions() {
		return additionalActions;
	}
}
