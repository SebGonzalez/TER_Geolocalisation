package fr.ufr.science.geolocalisation.IHM;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class CheckListItem {

	private String label;
	private boolean isSelected = false;

	public CheckListItem(String label, boolean selected) {
		this.label = label;
		this.isSelected = selected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return label;
	}
}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean hasFocus) {
		setEnabled(list.isEnabled());
		setSelected(((CheckListItem) value).isSelected());
		setFont(list.getFont());
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		setText(value.toString());
		return this;
	}

}
