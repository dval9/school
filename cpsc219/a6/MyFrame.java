/**
 *Tom Crowfoot
 *10037477
 *CPSC 219 Assignment 6
 *Version FINAL
 */

 //importing a bunch of stuff
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MyFrame extends JFrame{
	private JLabel contactsLabel;
	private JLabel entryLabel;
	private JLabel blank;
	private JButton addButton;
	private JButton remButton;
	private JList contactList;
	private JScrollPane scrollPane;
	private JTextField enterField;
	private GridBagConstraints c;
	private int fileLength;
	public static final int MAX_VISIBLE_ROWS=5;
	
	@SuppressWarnings("unchecked")	// to prevent some unchecked error
	MyFrame(String s){
		super(s);
		addWindowListener(new MyWindowListener());	// adding window listener
		countFileLength();
		contactsLabel=new JLabel("Email contacts");
		entryLabel=new JLabel("Enter new email below");
		addButton=new JButton("<-Add");
		remButton=new JButton("Rem->");
		remButton.addActionListener(new ActionListener(){	// abstract listener removes contact from list
			public void actionPerformed(ActionEvent e){
				try{
					JButton aButton=(JButton)e.getSource();
					MyFrame f=(MyFrame)aButton.getRootPane().getParent();
					DefaultListModel aModel=(DefaultListModel)contactList.getModel();
					f.setTitle("Removing contact "+aModel.get(contactList.getSelectedIndex()));
					Thread.sleep(1000);
					aModel.removeElementAt(contactList.getSelectedIndex());	// removing selected element in list
					f.setTitle("Email contacts");
					contactList.setSelectedIndex(0);
					fileLength--;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		addButton.addActionListener(new ActionListener(){	// abstract listener adds contact to list
			public void actionPerformed(ActionEvent e){
				try{
					JButton aButton=(JButton)e.getSource();
					MyFrame f=(MyFrame)aButton.getRootPane().getParent();
					DefaultListModel aModel=(DefaultListModel)contactList.getModel();
					if(enterField.getText().equals("")){
						f.setTitle("Cannot add empty contact.");	// does not allow empty things added
						Thread.sleep(1000);
					}
					else{
						f.setTitle("Adding contact "+enterField.getText());	// adds if not empty
						Thread.sleep(1000);
						aModel.addElement(new String(enterField.getText()));
						fileLength++;
						enterField.setText("");
					}
					f.setTitle("Email contacts");
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		contactList=new JList(new DefaultListModel());
		readFile();	// populates contactList
		contactList.setVisibleRowCount(MAX_VISIBLE_ROWS);	// sets visable contacts to 5
		contactList.setSelectedIndex(0);	// sets first contact selected
		enterField=new JTextField();
		enterField.addActionListener(new ActionListener(){	// abstract listener adds contact to list
			public void actionPerformed(ActionEvent e){
				try{
					JTextField aTextField=(JTextField)e.getSource();
					MyFrame f=(MyFrame)aTextField.getRootPane().getParent();
					DefaultListModel aModel=(DefaultListModel)contactList.getModel();
					if(enterField.getText().equals("")){
						f.setTitle("Cannot add empty contact.");	// does not allow empty things added
						Thread.sleep(1000);
					}
					else{
						f.setTitle("Adding contact "+enterField.getText());	// adds if not empty
						Thread.sleep(1000);
						aModel.addElement(new String(enterField.getText()));
						fileLength++;
						enterField.setText("");
					}
					f.setTitle("Email contacts");					
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		setLayout(new GridBagLayout());
		c=new GridBagConstraints();
		scrollPane=new JScrollPane(contactList);	// adds scroll bar to jlist
		addComponent(contactsLabel,0,0,1,1,GridBagConstraints.HORIZONTAL);
		addComponent(entryLabel,2,0,1,1,GridBagConstraints.HORIZONTAL);
		addComponent(addButton,1,1,1,1,GridBagConstraints.HORIZONTAL);
		addComponent(remButton,1,2,1,1,GridBagConstraints.HORIZONTAL);
		addComponent(enterField,2,1,1,1,GridBagConstraints.HORIZONTAL);
		addComponent(scrollPane,0,1,1,2,GridBagConstraints.VERTICAL);
	}
	
	// adds components to the frame
	public void addComponent(Component name,int x,int y,int w,int h,int fill){
		c.gridx=x;
		c.gridy=y;
		c.gridwidth=w;
		c.gridheight=h;
		c.fill=fill;
		c.insets=new Insets(0,10,0,0);	// padding on right side
		add(name,c);
	}
	
	// checks amount of initial file lines
	public void countFileLength(){
		try{
			fileLength=0;
			FileReader fr=new FileReader("contacts.txt");
			BufferedReader br=new BufferedReader(fr);
			while(br.readLine()!=null)	//counting lines in initial file
				fileLength++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// prints the contacts to the list in window from the file
	@SuppressWarnings("unchecked")	// to prevent some unchecked error
	public void readFile(){
		try{
			FileReader fr=new FileReader("contacts.txt");
			BufferedReader br=new BufferedReader(fr);
			DefaultListModel aModel=(DefaultListModel)contactList.getModel();
			for(int i=0;i<fileLength;i++)	// writing complete file to contactList
				aModel.addElement(new String(br.readLine()));
			fr.close();
			for(int j=0;j<fileLength;j++){	// removing blank entries from initial file
				if(aModel.get(j).equals("")){
					aModel.removeElementAt(j);
					fileLength--;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// called by MyWindowListener, saves contacts to file "updated_contacts.txt"
	public void writeFile(){
		try{
			FileWriter fw=new FileWriter("updated_contacts.txt");
			PrintWriter pw=new PrintWriter(fw);
			DefaultListModel aModel=(DefaultListModel)contactList.getModel();
			for(int i=0;i<fileLength;i++)
				pw.println(aModel.get(i));	// prints each line from contactList to file
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// saves the list to a file "updated_contacts.txt" and closes frame
	private class MyWindowListener extends WindowAdapter{	//nested class
        public void windowClosing(WindowEvent e){
			writeFile();
			JFrame f=(JFrame)e.getWindow();
			f.setTitle("Saving contacts...");	// changing title
			try{
				Thread.sleep(1000);	// waiting so user can see title change
			}catch(Exception ex){
				ex.printStackTrace();
			}
			f.setVisible(false);
			f.dispose();
		}
	}
}