package butti.javalibs.controls.listcontrol.searchmodules;

import javax.swing.JComponent;

/**
 * Dummy Search Filed, ohne Inhalt
 * 
 * @author Andreas Butti
 * 
 */
public class NoSearch extends AbstractSearchModul {
	private static final JComponent component = new JComponent() {
		private static final long serialVersionUID = 1L;
	};

	@Override
	public JComponent getComponent() {
		return component;
	}

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public boolean showField(Object object) {
		return true;
	}

	@Override
	public void setFilter(Object filter) {
	}
}
