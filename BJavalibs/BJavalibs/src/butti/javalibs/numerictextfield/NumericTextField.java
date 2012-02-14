package butti.javalibs.numerictextfield;

import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

public class NumericTextField extends JTextField implements
		NumericPlainDocument.InsertErrorListener {
	private static final long serialVersionUID = 1L;

	public NumericTextField() {
		this(null, 0, null);
	}

	public NumericTextField(String text, int columns, DecimalFormat format) {
		super(null, text, columns);

		NumericPlainDocument numericDoc = (NumericPlainDocument) getDocument();
		if (format != null) {
			numericDoc.setFormat(format);
		}

		numericDoc.addInsertErrorListener(this);
	}

	public NumericTextField(int columns, DecimalFormat format) {
		this(null, columns, format);
	}

	public NumericTextField(String text) {
		this(text, 0, null);
	}

	public NumericTextField(String text, int columns) {
		this(text, columns, null);
	}

	public void setFormat(DecimalFormat format) {
		((NumericPlainDocument) getDocument()).setFormat(format);
	}

	public DecimalFormat getFormat() {
		return ((NumericPlainDocument) getDocument()).getFormat();
	}

	public void formatChanged() {
		// Notify change of format attributes.
		setFormat(getFormat());
	}

	// Methods to get the field value
	public Long getLongValue() throws ParseException {
		return ((NumericPlainDocument) getDocument()).getLongValue();
	}

	public Double getDoubleValue() throws ParseException {
		return ((NumericPlainDocument) getDocument()).getDoubleValue();
	}

	public Number getNumberValue() throws ParseException {
		return ((NumericPlainDocument) getDocument()).getNumberValue();
	}

	// Methods to install numeric values
	public void setValue(Number number) {
		setText(getFormat().format(number));
	}

	public void setValue(long l) {
		setText(getFormat().format(l));
		;
	}

	public void setValue(double d) {
		setText(getFormat().format(d));
	}

	public void normalize() throws ParseException {
		// format the value according to the format string
		setText(getFormat().format(getNumberValue()));
	}

	// Override to handle insertion error
	public void insertFailed(NumericPlainDocument doc, int offset, String str,
			AttributeSet a) {
		// By default, just beep
		Toolkit.getDefaultToolkit().beep();
	}

	// Method to create default model
	protected Document createDefaultModel() {
		return new NumericPlainDocument();
	}
}