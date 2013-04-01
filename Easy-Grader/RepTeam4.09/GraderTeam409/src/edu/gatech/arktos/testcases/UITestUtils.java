package edu.gatech.arktos.testcases;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JMenu;

/**
 * This is the utils class to help validate components in Swing UI
 * @author jielu
 *
 */
public class UITestUtils {
	public static Component getChildByName(Component parent, String name){
		if (name.equals(parent.getName())){
			return parent;
		}
		
		if (parent instanceof Container){
			Component[] children = (parent instanceof JMenu) ?
					               ((JMenu)parent).getMenuComponents() :
					               ((Container)parent).getComponents();
			for(int i=0; i<children.length; i++){
				Component child = getChildByName(children[i], name);
				if(child != null){
					return child;
				}
			}
		}
		
		return null;		
	}

}
