package butti.javalibs.controls.listcontrol.searchmodules;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import butti.javalibs.controls.searchcontrol.SearchField;



/**
 * Suchfeld über den ganzen Text
 * 
 * @author Andreas Butti
 * 
 */
public class Textsearch extends AbstractSearchModul {
	private SearchField control;

	/**
	 * Erstellt eine Volltextsuche
	 * 
	 * @param name
	 *            Der Name des Feldes
	 */
	public Textsearch(String name) {
		control = new SearchField(name);
		control.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				fireSearchChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				fireSearchChanged();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				fireSearchChanged();
			}
		});
	}

	@Override
	public JComponent getComponent() {
		return control;
	}

	@Override
	public Object getValue() {
		return control.getText();
	}

	@Override
	public boolean showField(Object object) {
		return object.toString().toLowerCase().contains(control.getText().toLowerCase());
	}

	@Override
	public void setFilter(Object filter) {
		control.setText(filter.toString());
	}
}
