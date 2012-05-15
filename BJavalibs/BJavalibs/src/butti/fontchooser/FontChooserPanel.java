package butti.fontchooser;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import butti.javalibs.gui.GridBagManager;

/**
 * @author Andreas Butti, based on JCommons FontChooser
 */
public class FontChooserPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	/**
	 * The font sizes that can be selected.
	 */
	public static final String[] SIZES = { "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "28", "36", "48", "72" };

	/**
	 * The list of fonts.
	 */
	private FontChooserComboBox cbFonts = new FontChooserComboBox();

	/**
	 * The list of sizes.
	 */
	private JComboBox cbSize = new JComboBox(SIZES);

	/**
	 * The checkbox that indicates whether the font is bold.
	 */
	private JCheckBox cbBold = new JCheckBox("Fett");

	/**
	 * The checkbox that indicates whether or not the font is italic.
	 */
	private JCheckBox cbItalic = new JCheckBox("Kursiv");

	/**
	 * The label with the example Text
	 */
	private FontPreview lbPreview = new FontPreview();

	/**
	 * Layout manager
	 */
	private GridBagManager gbm;

	/**
	 * The current font
	 */
	private Font font;

	/**
	 * Standard constructor - builds a FontChooserPanel initialised with the
	 * specified font.
	 * 
	 * @param font
	 *            the initial font to display.
	 */
	public FontChooserPanel(Font font) {
		gbm = new GridBagManager(this);

		JPanel pFont = new JPanel();
		pFont.setBorder(new TitledBorder("Schrift"));
		pFont.add(this.cbFonts);

		this.cbFonts.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				updateFont();
			}
		});

		JPanel pSize = new JPanel();
		pSize.setBorder(new TitledBorder("Grösse"));
		pSize.add(this.cbSize);

		this.cbSize.setEditable(true);
		this.cbSize.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				updateFont();
			}
		});

		JPanel pAttributes = new JPanel();
		pAttributes.setBorder(new TitledBorder("Attribute"));
		pAttributes.setLayout(new GridLayout(1, 0, 10, 10));

		pAttributes.add(this.cbBold);
		pAttributes.add(this.cbItalic);

		this.cbBold.addActionListener(this);
		this.cbItalic.addActionListener(this);

		JPanel pPreview = new JPanel();
		pPreview.setBorder(new TitledBorder("Vorschau"));
		pPreview.add(lbPreview);

		gbm.setX(0).setY(0).setComp(pFont);
		gbm.setX(1).setY(0).setComp(pSize);
		gbm.setX(0).setY(1).setWidth(2).setComp(pAttributes);
		gbm.setX(0).setY(2).setWidth(2).setComp(pPreview);

		if (font == null) {
			font = new Font("Serif", 0, 12);
		}

		setSelectedFont(font);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateFont();
	}

	/**
	 * Updates the preview and <code>this.font</code>
	 */
	private void updateFont() {
		this.font = new Font(getSelectedName(), getSelectedStyle(), getSelectedSize());
		this.lbPreview.setFont(this.font);
	}

	/**
	 * Returns a Font object representing the selection in the panel.
	 * 
	 * @return the font.
	 */
	public Font getSelectedFont() {
		return this.font;
	}

	/**
	 * Returns the selected name.
	 * 
	 * @return the name.
	 */
	public String getSelectedName() {
		return this.cbFonts.getSelectedFontName();
	}

	/**
	 * Returns the selected style.
	 * 
	 * @return the style.
	 */
	public int getSelectedStyle() {
		if (this.cbBold.isSelected() && this.cbItalic.isSelected()) {
			return Font.BOLD + Font.ITALIC;
		}
		if (this.cbBold.isSelected()) {
			return Font.BOLD;
		}
		if (this.cbItalic.isSelected()) {
			return Font.ITALIC;
		} else {
			return Font.PLAIN;
		}
	}

	/**
	 * Returns the selected size.
	 * 
	 * @return the size.
	 */
	public int getSelectedSize() {
		final String selected = (String) this.cbSize.getSelectedItem().toString();
		if (selected != null) {
			return Integer.parseInt(selected);
		} else {
			return 10;
		}
	}

	/**
	 * Initializes the contents of the dialog from the given font object.
	 * 
	 * @param font
	 *            the font from which to read the properties.
	 */
	public void setSelectedFont(final Font font) {
		if (font == null) {
			throw new NullPointerException();
		}

		this.font = font;

		this.cbBold.setSelected(font.isBold());
		this.cbItalic.setSelected(font.isItalic());

		this.cbSize.setSelectedItem(font.getSize());
		this.cbFonts.setSelectedItem(font.getName());
	}

}
