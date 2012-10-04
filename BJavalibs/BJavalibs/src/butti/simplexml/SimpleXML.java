package butti.simplexml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A simple XML Parser e.g. for configuration file
 * 
 * @author Andreas Butti
 */
public class SimpleXML {
	/**
	 * The contents of this node
	 */
	private Node node;

	/**
	 * Creates a SimpleXML Object from an XML String
	 * 
	 * @param xmlContents
	 *            The XML String, something like <a><b>x</b><b>y</b></a>
	 * @throws SimpleXMLException
	 *             If an error occurs during parsing
	 */
	public SimpleXML(String xmlContents) throws SimpleXMLException {
		try {
			InputStream is = new ByteArrayInputStream(xmlContents.getBytes("UTF-8"));
			parse(is);
		} catch (UnsupportedEncodingException e) {
			throw new SimpleXMLException(e);
		}
	}

	/**
	 * Creates a SimpleXML Object from an input stream
	 * 
	 * @param in
	 *            The Inputstream
	 * @throws SimpleXMLException
	 *             If an error occurs during parsing
	 */
	public SimpleXML(InputStream in) throws SimpleXMLException {
		parse(in);
	}

	/**
	 * Creates a SimpleXML Object from an input stream
	 * 
	 * @param in
	 *            The Inputstream
	 * @throws SimpleXMLException
	 *             If an error occurs during parsing
	 */
	public SimpleXML(File file) throws SimpleXMLException, FileNotFoundException {
		this(new FileInputStream(file));
	}

	/**
	 * Internal Constructor, for Children
	 * 
	 * @param n
	 *            The node != null
	 */
	protected SimpleXML(Node n) {
		this.node = n;
	}

	/**
	 * @return The node name
	 */
	public String getNodeName() {
		return node.getNodeName();
	}

	/**
	 * Finds the rootnode of the document
	 * 
	 * @param document
	 *            The document
	 * @throws SimpleXMLException
	 *             If there is no root tag
	 */
	private void init(Document document) throws SimpleXMLException {
		Node root = null;

		NodeList rootNodes = document.getChildNodes();

		// Skip comments
		for (int i = 0; i < rootNodes.getLength(); i++) {
			Node n = rootNodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				root = n;
				break;
			}
		}

		if (root == null) {
			throw new SimpleXMLException("XML File does not contain a root");
		}

		this.node = root;
	}

	/**
	 * Parses an inputstream
	 * 
	 * @param is
	 *            The Inputstream
	 * @throws SimpleXMLException
	 *             If an error occurs during parsing
	 */
	private void parse(InputStream is) throws SimpleXMLException {
		try {
			init(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is));
		} catch (Exception e) {
			throw new SimpleXMLException(e);
		}
	}

	/**
	 * Returns all Children of this Node
	 * 
	 * @return The list is complete handed over
	 */
	public Vector<SimpleXML> children() {
		Vector<SimpleXML> data = new Vector<SimpleXML>();

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);

			if (n.getNodeType() == Node.ELEMENT_NODE) {
				data.add(new SimpleXML(n));
			}
		}
		return data;
	}

	/**
	 * Returns all Children named «nodeName»
	 * 
	 * @return The list is complete handed over
	 */
	public Vector<SimpleXML> children(String nodeName) {
		Vector<SimpleXML> data = new Vector<SimpleXML>();

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);

			if (n.getNodeType() == Node.ELEMENT_NODE && nodeName.equals(n.getNodeName())) {
				data.add(new SimpleXML(n));
			}
		}

		return data;
	}

	/**
	 * Return the Child named «nodeName»
	 * 
	 * @param nodeName
	 *            The Name of the Child
	 * @return The Child
	 * @throws SimpleXMLException
	 *             If count != 1
	 */
	public SimpleXML child(String nodeName) throws SimpleXMLException {
		SimpleXML data = null;

		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);

			if (n.getNodeType() == Node.ELEMENT_NODE && nodeName.equals(n.getNodeName())) {
				if (data != null) {
					throw new SimpleXMLException("Node name \"" + nodeName + "\" not unique, call children() instead of child()");
				}
				data = new SimpleXML(n);
			}
		}

		if (data == null) {
			throw new SimpleXMLException("Node name \"" + nodeName + "\" not found");
		}

		return data;
	}

	/**
	 * @return The text contents of this node
	 */
	public String text() {
		return node.getChildNodes().item(0).getTextContent();
	}

	/**
	 * Returns a named attribute
	 * 
	 * @param name
	 *            The name of the attribute
	 * @return The Text of the Attribute or <code>null</code> if not found
	 */
	public String attr(String name) {
		NamedNodeMap attr = node.getAttributes();
		Node n = attr.getNamedItem(name);
		if (n == null) {
			return null;
		}

		return n.getTextContent();
	}

	/**
	 * Returns a named attribute
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value if there is none or no valid attribute
	 * 
	 * @return The Text of the Attribute
	 */
	public String attr(String name, String defaultValue) {
		String attr = attr(name);
		if (attr == null) {
			return defaultValue;
		}
		return attr;

	}

	/**
	 * Returns a named attribute
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value if there is none or no valid attribute
	 * 
	 * @return The value of the Attribute
	 */
	public int attr(String name, int defaultValue) {
		try {
			String attr = attr(name);
			if (attr != null) {
				return Integer.parseInt(attr);
			}

		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	/**
	 * Returns a named attribute
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value if there is none or no valid attribute
	 * 
	 * @return The value of the Attribute
	 */
	public double attr(String name, double defaultValue) {
		try {
			String attr = attr(name);
			if (attr != null) {
				return Double.parseDouble(attr);
			}

		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	/**
	 * Returns a named attribute
	 * 
	 * @param name
	 *            The name of the attribute
	 * @param defaultValue
	 *            The default value if there is none or no valid attribute
	 * 
	 * @return The value of the Attribute
	 */
	public boolean attr(String name, boolean defaultValue) {
		try {
			String attr = attr(name);
			if (attr != null) {
				return Boolean.parseBoolean(attr);
			}

		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	public String attrS(String name) throws SimpleXMLException {
		String attr = attr(name);
		if (attr != null) {
			return attr;
		}
		throw new SimpleXMLException("Attribute \"" + name + "\" not set");
	}

	public int attrI(String name) throws SimpleXMLException {
		String attr = null;
		try {
			attr = attr(name);
			if (attr != null) {
				return Integer.parseInt(attr);
			}
			throw new SimpleXMLException("Attribute \"" + name + "\" not set");

		} catch (NumberFormatException e) {
			throw new SimpleXMLException("Attribute \"" + name + "\" not an int \"" + attr + "\"");
		}
	}

	public double attrD(String name) throws SimpleXMLException {
		String attr = null;
		try {
			attr = attr(name);
			if (attr != null) {
				return Double.parseDouble(attr);
			}
			throw new SimpleXMLException("Attribute \"" + name + "\" not set");

		} catch (NumberFormatException e) {
			throw new SimpleXMLException("Attribute \"" + name + "\" not a double \"" + attr + "\"");
		}
	}

	public boolean attrB(String name) throws SimpleXMLException {
		String attr = attr(name);
		if (attr != null) {
			return Boolean.parseBoolean(attr);
		}
		throw new SimpleXMLException("Attribute \"" + name + "\" not set");
	}

}
