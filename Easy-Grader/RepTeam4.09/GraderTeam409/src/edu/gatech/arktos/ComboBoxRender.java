package edu.gatech.arktos;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class ComboBoxRender extends JLabel implements ListCellRenderer{
	public ComboBoxRender(){
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}
	
	  public Component getListCellRendererComponent(
              JList list,
              Object value,
              int index,
              boolean isSelected,
              boolean cellHasFocus) {
		  setText(((Student)value).getName());
		  
		  return this;
	  }

}
