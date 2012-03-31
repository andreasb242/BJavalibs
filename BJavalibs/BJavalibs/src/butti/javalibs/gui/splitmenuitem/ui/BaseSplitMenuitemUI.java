package butti.javalibs.gui.splitmenuitem.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;

import butti.javalibs.gui.splitmenuitem.JFlatButton;
import butti.javalibs.gui.splitmenuitem.SplitMenuitem;

public class BaseSplitMenuitemUI extends SplitMenuItemUI implements ActionListener {
	protected SplitMenuitem menuitem;
	protected JLabel lbMain = new JLabel();

	protected Color selectionBackground;
	protected Color selectionForeground;
	protected Color defaultForeground = null;
	protected boolean hover = false;

	private Vector<JButton> additionalButtons = new Vector<JButton>();

	protected MouseListener mouseListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			menuitem.firePressed();
			closeMenu();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			hover = true;
			lbMain.setForeground(selectionForeground);

			menuitem.repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			hover = false;

			lbMain.setForeground(defaultForeground);

			menuitem.repaint();
		}

	};

	private PropertyChangeListener propertyListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (AbstractButton.TEXT_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
				lbMain.setText((String) evt.getNewValue());
			} else if (AbstractButton.ICON_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
				lbMain.setIcon((Icon) evt.getNewValue());
			} else if (SplitMenuitem.ADDITIONAL_ACTION_CHANGED.equals(evt.getPropertyName())) {
				updateAdditionalActions();
			}
		}
	};

	public BaseSplitMenuitemUI() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.plaf.ComponentUI#createUI(javax.swing.JComponent)
	 */
	public static ComponentUI createUI(JComponent c) {
		return new BaseSplitMenuitemUI();
	}

	@Override
	public void installUI(JComponent c) {
		this.menuitem = (SplitMenuitem) c;
		installDefaults();
		installComponents();
		installListeners();

		c.setLayout(createLayoutManager());
	}

	@Override
	public void uninstallUI(JComponent c) {
		c.setLayout(null);
		uninstallListeners();
		uninstallComponents();
		uninstallDefaults();

		this.menuitem = null;
	}

	private void uninstallDefaults() {
	}

	private void uninstallComponents() {
		this.menuitem.remove(lbMain);
		for (JButton b : additionalButtons) {
			this.menuitem.remove(b);
			b.removeActionListener(this);
		}
	}

	protected void updateAdditionalActions() {
		for (JButton b : additionalButtons) {
			this.menuitem.remove(b);
			b.removeActionListener(this);
		}

		additionalButtons.clear();

		for (Action a : menuitem.getAdditionalActions()) {
			JButton b = createButton(a);
			this.menuitem.add(b);
			additionalButtons.add(b);
			b.addActionListener(this);
		}
	}

	private JButton createButton(Action a) {
		return new JFlatButton(a);
	}

	private void uninstallListeners() {
		this.menuitem.removePropertyChangeListener(propertyListener);
		this.menuitem.removeMouseListener(mouseListener);
	}

	private void installDefaults() {
		menuitem.setMargin(new Insets(0, 0, 0, 0));
		menuitem.setBorder(new EmptyBorder(2, 10, 2, 10));

		defaultForeground = UIManager.getColor("Menu.foreground");
		selectionForeground = UIManager.getColor("Menu.selectionForeground");
		selectionBackground = UIManager.getColor("Menu.selectionBackground");
		
		lbMain.setForeground(defaultForeground);
	}

	private void installComponents() {
		this.menuitem.add(lbMain);
		lbMain.setText(this.menuitem.getText());
		lbMain.setIcon(this.menuitem.getIcon());
		updateAdditionalActions();
	}

	private void installListeners() {
		this.menuitem.addPropertyChangeListener(propertyListener);
		this.menuitem.addMouseListener(mouseListener);
	}

	private LayoutManager createLayoutManager() {
		return new LayoutManager() {

			@Override
			public void removeLayoutComponent(Component comp) {
			}

			@Override
			public Dimension preferredLayoutSize(Container parent) {
				Insets margin = menuitem.getInsets();

				int w = margin.left + margin.right;
				int h = margin.top + margin.bottom;

				Dimension s = lbMain.getPreferredSize();
				w += s.width;
				h += s.height;

				for (JButton b : additionalButtons) {
					s = b.getPreferredSize();
					w += s.width + 2;
					h = Math.max(h, s.height + margin.top + margin.bottom);
				}

				return new Dimension(w, h);
			}

			@Override
			public Dimension minimumLayoutSize(Container parent) {
				return preferredLayoutSize(parent);
			}

			@Override
			public void layoutContainer(Container parent) {
				Insets margin = menuitem.getInsets();

				int x = menuitem.getWidth() - margin.right;
				int height = parent.getHeight();

				for (JButton b : additionalButtons) {
					Dimension s = b.getPreferredSize();
					x -= s.width;
					b.setBounds(x, 0, s.width, height);
					x -= 2;
				}

				lbMain.setBounds(margin.left, margin.top, x - margin.left, height - margin.top - margin.bottom);
			}

			@Override
			public void addLayoutComponent(String name, Component comp) {
			}
		};
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		if (hover) {
			g.setColor(selectionBackground);
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		closeMenu();
	}

	protected void closeMenu() {
		if (menuitem.getParent() instanceof JPopupMenu) {
			JPopupMenu m = (JPopupMenu) menuitem.getParent();
			m.setVisible(false);
		}
	}
}
