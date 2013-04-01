package edu.gatech.arktos.testcases;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import edu.gatech.arktos.MainFrame;
import edu.gatech.arktos.Student;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test UI for Grade Tool.
 * @author jielu
 *
 */
public class UITest extends TestCase {
	MainFrame frame = null;

	@Before
	public void setUp() throws Exception {
		frame = new MainFrame();
		frame.setVisible(true);
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		frame.dispose();
		super.tearDown();
	}

	/**
	 * Test all required components(Header banner, Student List ComboBox, Information Area, Save Button) 
	 * are represented with correct name in UI. 
	 */
	@Test
	public void testUIComponents() {
		try {
			JLabel header = (JLabel) UITestUtils.getChildByName(frame, "header");
			assertNotNull("Can't get header banner!!", header);
			assertEquals("CS6300 GRADING TOOL", header.getText().trim());
			Thread.sleep(2000);
			
			// Need verify at least one item is selected by default
			JComboBox studentsBox = (JComboBox) UITestUtils.getChildByName(frame, "studentsList");
			assertNotNull("Can't get combo box for students list!!", studentsBox);
			assertTrue(studentsBox.getSelectedIndex() > -1);
			Thread.sleep(2000);
			
			// Need verify text area is not empty since at least one student item has been selected.
			JTextArea infoArea = (JTextArea) UITestUtils.getChildByName(frame, "infoArea");
			assertNotNull("Can't get text area for student information!!", infoArea);
			assertTrue(!infoArea.getText().equals(""));
			Thread.sleep(2000);
			
			JButton saveBtn = (JButton) UITestUtils.getChildByName(frame, "saveButton");
			assertNotNull("Can't get save button!", saveBtn);
			assertEquals("Save Student", saveBtn.getText());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * Test the number of students listed in combo box is correct.
	 */
	@Test
	public void testStudentsListNumber(){
		try{
			JComboBox studentsBox = (JComboBox) UITestUtils.getChildByName(frame, "studentsList");
			assertNotNull("Can't get combobox for students list!!", studentsBox);
			assertEquals(14, studentsBox.getItemCount());	
		}catch(Exception e){
			fail(e.toString());
		}
	}
	
	/**
	 * Test given student in db can be found in students list combo box. 
	 */
	@Test
	public void testStudentInList(){
		try{
			JComboBox studentsBox = (JComboBox) UITestUtils.getChildByName(frame, "studentsList");
			assertNotNull("Can't get combobox for students list!!", studentsBox);
			
			Student st = new Student();
			st.setName("Cynthia Faast");
			st.setGtid("901234514");
			
			boolean found = false;
			for(int i=0; i<studentsBox.getItemCount(); i++){
				Student s = (Student) studentsBox.getItemAt(i);
				if ((s.getName().compareTo("Cynthia Faast") == 0) && (s.getGtid().compareTo("901234514") == 0)) {
					found = true;
					break;
				}
			}
			assertTrue(found);
		}catch(Exception e){
			fail(e.toString());
		}
	}
	
	/**
	 * Test the student's whole information can be displayed correctly in the text area.
	 */
	@Test
	public void testStudentInformation(){
		try{
			JComboBox studentsBox = (JComboBox) UITestUtils.getChildByName(frame, "studentsList");
			assertNotNull("Can't get combobox for students list!!", studentsBox);
			
			Student s = new Student();
			s.setName("Shevon Wise");
			s.setGtid("901234504");
			studentsBox.setSelectedItem(s);
			Thread.sleep(4000);
			
			//Verify text area can display the correct student information in correct format.
			String expectedStudentInfo = "NAME: Shevon Wise\n"
					                   + "GTID: 901234504\n"
					                   + "EMAIL: sw@gatech.edu\n"
					                   + "ATTENDANCE: 100\n"
					                   + "PROJECT DETAILS:\n"
					                   + "- P1\n"
					                   + "  Team Grade: 93\n"
					                   + "  Project Average Grade: 93\n"
					                   + "  Individual Contribution: 9.25\n"
					                   + "- P2\n"
					                   + "  Team Grade: 86\n"
					                   + "  Project Average Grade: 92\n"
					                   + "  Individual Contribution: 7.50\n"
					                   + "- P3\n"
					                   + "  Team Grade: 96\n"
					                   + "  Project Average Grade: 99\n"
					                   + "  Individual Contribution: 8.25\n"
					                   + "ASSIGNMENT DETAILS:\n"
					                   + "- Assignment 1\n"
					                   + "  Grade: 100\n"
					                   + "  Assignment Average Grade: 99\n"
					                   + "- Assignment 2\n"
					                   + "  Grade: 100\n"
					                   + "  Assignment Average Grade: 100\n"
					                   + "- Assignment 3\n"
					                   + "  Grade: 40\n"
					                   + "  Assignment Average Grade: 77\n"
					;
			JTextArea infoArea = (JTextArea) UITestUtils.getChildByName(frame, "infoArea");
			assertNotNull("Can't get text area for student information!!", infoArea);
			assertEquals(expectedStudentInfo, infoArea.getText());		
		}catch(Exception e){
			fail(e.toString());
		}
	}

	/**
	 * Test save student information function can work correctly
	 */
	@Test
	public void testSaveStudentInfo(){
		BufferedReader in = null;
		try{
			JComboBox studentsBox = (JComboBox) UITestUtils.getChildByName(frame, "studentsList");
			assertNotNull("Can't get combobox for students list!!", studentsBox);
			
			Student s = new Student();
			s.setName("Shevon Wise");
			s.setGtid("901234504");
			studentsBox.setSelectedItem(s);
			Thread.sleep(4000);
			
			String expectedStudentInfo = "NAME: Shevon Wise\n"
					                   + "GTID: 901234504\n"
					                   + "EMAIL: sw@gatech.edu\n"
					                   + "ATTENDANCE: 100\n"
					                   + "PROJECT DETAILS:\n"
					                   + "- P1\n"
					                   + "  Team Grade: 93\n"
					                   + "  Project Average Grade: 93\n"
					                   + "  Individual Contribution: 9.25\n"
					                   + "- P2\n"
					                   + "  Team Grade: 86\n"
					                   + "  Project Average Grade: 92\n"
					                   + "  Individual Contribution: 7.50\n"
					                   + "- P3\n"
					                   + "  Team Grade: 96\n"
					                   + "  Project Average Grade: 99\n"
					                   + "  Individual Contribution: 8.25\n"
					                   + "ASSIGNMENT DETAILS:\n"
					                   + "- Assignment 1\n"
					                   + "  Grade: 100\n"
					                   + "  Assignment Average Grade: 99\n"
					                   + "- Assignment 2\n"
					                   + "  Grade: 100\n"
					                   + "  Assignment Average Grade: 100\n"
					                   + "- Assignment 3\n"
					                   + "  Grade: 40\n"
					                   + "  Assignment Average Grade: 77\n"
					;
			
			JButton saveBtn = (JButton) UITestUtils.getChildByName(frame, "saveButton");
			assertNotNull("Can't get save button!", saveBtn);
			saveBtn.doClick();
			
			//Verify file for the student is generated and content is correct.
			String expectedFileName = "Shevon Wise.txt";
		    in = new BufferedReader(new InputStreamReader(new FileInputStream(expectedFileName)));
			StringBuilder fileContent = new StringBuilder();
			String line = null;
			while((line = in.readLine()) != null){
				fileContent.append(line + "\n");
			}
			assertEquals(expectedStudentInfo, fileContent.toString());
			
		}catch(Exception e){
			fail(e.toString());
		}finally{
			try{
				if(in != null)
					in.close();
			}catch(Exception e){
				fail(e.toString());
			}
		}
	}
}
