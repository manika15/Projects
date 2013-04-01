
package edu.gatech.arktos;


import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;

public class MainFrame extends JFrame implements ActionListener{
    public static boolean RIGHT_TO_LEFT = false;
    private JLabel headerLbl;
    private JLabel errorMsgLbl;
    private JComboBox combobox;
    private JButton saveBtn;
    private JTextArea textArea;
    private Session session;
    private GradesDB db;
    
    public MainFrame(){
    	super("CS6300 Grading Tool");
    	super.setMinimumSize(new Dimension(500, 400));
    	try{
    		session = new Session();
    		session.login(Constants.USERNAME, Constants.PASSWORD);
    		db = session.getDBByName(Constants.GRADES_DB);
    	}catch(Exception e){
    		errorMsgLbl.setText(e.toString());
    	}
    	
    	addComponentsToPane(getContentPane());
    }
    
    public void addComponentsToPane(Container pane) {
        
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
        
        JPanel headerPane = new JPanel();
        headerLbl = new JLabel("CS6300 GRADING TOOL");
        headerLbl.setName("header");
        headerPane.add(headerLbl);
        pane.add(headerPane, BorderLayout.PAGE_START);
       
        JPanel leftPane = new JPanel();
        HashSet<Student> students = new HashSet<Student>();
        try{
        	students = db.getStudents();
        }catch(Exception e){
        	errorMsgLbl.setText(e.toString());
        }
        
        combobox = new JComboBox(students.toArray());
    	combobox.setName("studentsList");
        if(students != null){
    		ComboBoxRender render = new ComboBoxRender();
    		combobox.setRenderer(render);
    		combobox.addActionListener(this);
        }
      
        leftPane.add(combobox);
        saveBtn = new JButton("Save Student");
        saveBtn.setName("saveButton");
        leftPane.add(saveBtn);
        saveBtn.addActionListener(this);
        pane.add(leftPane, BorderLayout.LINE_START);
        
        //Make the center component big, since that's the
        //typical usage of BorderLayout.
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setName("infoArea");
        JScrollPane rightPane = new JScrollPane(textArea);
        rightPane.setPreferredSize(new Dimension(200, 100));
        pane.add(rightPane, BorderLayout.CENTER);
        
        errorMsgLbl = new JLabel();
        pane.add(errorMsgLbl, BorderLayout.PAGE_END);
      
        //Display the information for the student selected by defaul
		Student s = (Student)combobox.getSelectedItem();
		textArea.setText(s.toString());
		
		
    }
    
	public void actionPerformed(ActionEvent e){
		if(e.getSource() instanceof JComboBox){
			JComboBox cb = (JComboBox)e.getSource();
			Student s = (Student)cb.getSelectedItem();
			textArea.setText(s.toString());
		}else if(e.getSource() instanceof JButton){
			Student s = (Student)combobox.getSelectedItem();
			try {
				s.saveInfoToFile();
				errorMsgLbl.setText("Student " + s.getName() + " has been saved successfully!");
			} catch (Exception e1) {
				errorMsgLbl.setText(e1.toString());
			}
			
		}
		
		
	}

   
    
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	MainFrame frame = new MainFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
