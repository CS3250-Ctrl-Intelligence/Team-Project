import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * 
 *The GUI aims to allow easy connection to an open-source relational database management system(MySQL).
 *Linked buttons with database, JTable is implemented to display entries from database 
 *and users are able to load data to the GUI
 * @author Tho Le
 * @version 1.3.1
 */
public class Table {

	JFrame frmAdmin;
	/** Text field components*/
	private JTextField productIDField;
	private JTextField quantityField;
	private JTextField wholesaleCostField;
	private JTextField salePriceField;
	private JTextField supplierIDField;
	private JPanel mainPanel = new JPanel();
	
	/** Item table component */
	
	private JTable table= new JTable();
	private DefaultTableModel tableModel= new DefaultTableModel();

	/** Button components */
	private JButton createButton = new JButton("Create");
	private JButton updateButton = new JButton("Update");
	private JButton deleteButton = new JButton("Delete");
	private JButton clearButton = new JButton("Clear");
	private JButton readButton = new JButton("Read");
	private JButton connectButton = new JButton("Connect to database");
	
	/** Connection to database */
	private Database db = new Database();
	private JTextField searchField;
	
	
	private int x,y;
	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {

	//}

	/**
	 * Create the application.
	 */
	public Table() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmAdmin = new JFrame();
		frmAdmin.setBounds(100, 100, 1137, 795);
		frmAdmin.getContentPane().setLayout(null);//set layout to absolute
		frmAdmin.setUndecorated(true);//remove default title bar
		
		mainPanel.setBackground(new Color(255, 240, 245));
		mainPanel.setBounds(0, 0, 1136, 795);
		frmAdmin.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		//Call methods implemented to add basic components to gui
		addLabelComponents();
		addFieldComponents();
		
		//Add scrolling functionality to table
		JScrollPane table_scrollPane = new JScrollPane();
		table_scrollPane.setBounds(29, 77, 1081, 638);
		mainPanel.add(table_scrollPane);
		
		//Add custom title bar
		JPanel titlePanel = new JPanel();
		
		//Add draggable functionality on title bar when mouse is clicked and hold
		titlePanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int xx = e.getXOnScreen();
				int yy = e.getYOnScreen();
				frmAdmin.setLocation(xx-x,yy-y);
			}
		});
		titlePanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				x=e.getX();
				y= e.getY();
			}
		});
		titlePanel.setBackground(new Color(139, 0, 139));
		titlePanel.setBounds(0, 0, 1136, 29);
		mainPanel.add(titlePanel);
		titlePanel.setLayout(null);
		
		// Custom close label to exit the gui
		JLabel closeLabel = new JLabel("");
		closeLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		closeLabel.setBounds(1111, 7, 20, 20);
		titlePanel.add(closeLabel);
		closeLabel.setIcon(new ImageIcon("C:\\Users\\thole\\Desktop\\Java\\LA for CS1050\\databasegui\\src\\Close_icon_20x20.png"));
		
		//When user clicked on a row, the information will be input to the textfield to change
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				int i = table.getSelectedRow();
				productIDField.setText(tableModel.getValueAt(i,0).toString());
				quantityField.setText(tableModel.getValueAt(i,1).toString());
				wholesaleCostField.setText(tableModel.getValueAt(i,2).toString());
				salePriceField.setText(tableModel.getValueAt(i,3).toString());
				supplierIDField.setText(tableModel.getValueAt(i,4).toString());
			}
		});
		
		
		table.setBackground(new Color(230, 230, 250));
		table_scrollPane.setViewportView(table);
		tableModel = new DefaultTableModel();
		Object [] column = {"Product ID", "Quantity", "Wholesale Cost", "Sale Price", "Supplier ID"};
		Object [] row = new Object[5];
		tableModel.setColumnIdentifiers(column);
		table.setModel(tableModel);
		
		
		
		//-----------------------------Add Button and Functionalities--------------------------------
		addButtonComponents();
	
		//when the Create button is clicked, item is added to table and datase
		//no check for duplication
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
			        //check for empty input
					if(productIDField.getText().equals("") || quantityField.getText().equals("") || wholesaleCostField.getText().equals("") 
							|| salePriceField.getText().equals("") || supplierIDField.getText().equals("") ) {
						JOptionPane.showMessageDialog(null,"Please fill complete information");
					}
					else {		
						db.Insert(productIDField.getText(), Integer.parseInt(quantityField.getText()), Float.parseFloat(wholesaleCostField.getText()), Float.parseFloat(salePriceField.getText()), supplierIDField.getText());		//Calls Insert method from database class
						//Add item to table	
						row[0]= productIDField.getText();
						row[1] = quantityField.getText();
						row[2]= wholesaleCostField.getText();
						row[3] = salePriceField.getText();
						row[4]= supplierIDField.getText();
						tableModel.addRow(row);
						
						JOptionPane.showMessageDialog(null,"Item with the Product ID: "+ productIDField.getText() +" has been successfully added");
						clearField();
					}
			    }
		});
		
		//when the Update button is clicked, item's quantity,wholesale cost, and sale price is updated
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
					int i = table.getSelectedRow();
					if(i>=0) {
						//Update item on table
						tableModel.setValueAt(productIDField.getText(), i, 0);
						tableModel.setValueAt(quantityField.getText(), i, 1);
						tableModel.setValueAt(wholesaleCostField.getText(), i, 2);
						tableModel.setValueAt(salePriceField.getText(), i, 3);
						tableModel.setValueAt(supplierIDField.getText(), i, 4);
						JOptionPane.showMessageDialog(null,"Item with the Product ID: "+ productIDField.getText() +" has been successfully updated");
						
						//Update item in database
						db.Update(productIDField.getText(), Integer.parseInt(quantityField.getText()), Float.parseFloat(wholesaleCostField.getText()), Float.parseFloat(salePriceField.getText()));		//Calls Update method from database class
					}
					else {
						JOptionPane.showMessageDialog(null,"Please Select an Item first");
					}
			}
		});
		
		//when the Delete button is clicked, the item(row) is deleted
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
					int i = table.getSelectedRow();
					if (i >=0) {
						//Delete item from table
						tableModel.removeRow(i);
						//Delelte item from database
						db.Delete(productIDField.getText()); // calls Delete method from database class
						JOptionPane.showMessageDialog(null,"Item with the Product ID: "+ productIDField.getText() +" has been successfully d");
					}
					else
						JOptionPane.showMessageDialog(null,"Please select a row to be deleted");
			}
		});
	
		//when the Clear buttonm is clicked, the informations in text fields are cleared
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearField();
			}
		});
		
		//When the Connect button is clicked, load items from database to table
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				db.importCSV(); // Calls import method from database.java to import csv

				try {
					//Load items from database
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Inventory","root","password");
					String sql= "select * from Inventory.items order by product_id";
					Statement selectStatement = con.createStatement();
					ResultSet results = selectStatement.executeQuery(sql);
					
					while(results.next()) {
						//Add items to table
				        row[0]= results.getString("product_id");
						row[1] = results.getInt("quantity");
						row[2]= results.getDouble("wholesale_cost");
						row[3] = results.getDouble("sale_price");
						row[4]= results.getString("supplier_id");
						tableModel.addRow(row);
					}
					 con.close();
				}
		        catch (SQLException ev) {
		            ev.printStackTrace();
		        }
			}
		});
		
		//When the Read button is clicked, load item with given product id from database to table
		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if(productIDField.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"Please fill complete information");
				}
				else {
					//Load item with specifed product id to table
					tableModel.addRow(db.Read(productIDField.getText())); // calls Read method from database class and loads it to the table
					searchBar(productIDField.getText());
				}
   }
});
	}
	/**
     * Diplay labels for text field which allow users to know what information they're entering
     */
	private void addLabelComponents() {
		
		JLabel productIDLabel = new JLabel("Product ID: ");
		productIDLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		productIDLabel.setBounds(66, 48, 75, 15);
		mainPanel.add(productIDLabel);
		
		JLabel quantityLabel = new JLabel("Quantity: ");
		quantityLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		quantityLabel.setBounds(289, 48, 62, 15);
		mainPanel.add(quantityLabel);
		
		JLabel wholesaleCostLabel = new JLabel("Wholesale Cost: ");
		wholesaleCostLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		wholesaleCostLabel.setBounds(483, 48, 101, 15);
		mainPanel.add(wholesaleCostLabel);
		
		JLabel salePriceLabel = new JLabel("Sale Price: ");
		salePriceLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		salePriceLabel.setBounds(699, 48, 66, 15);
		mainPanel.add(salePriceLabel);
		
		JLabel supplierIDLabel = new JLabel("Supplier ID: ");
		supplierIDLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		supplierIDLabel.setBounds(893, 48, 76, 15);
		mainPanel.add(supplierIDLabel);
		
		JLabel searchLabel = new JLabel("Enter Product ID:");
		searchLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		searchLabel.setBounds(624, 751, 108, 15);
		mainPanel.add(searchLabel);
		
		JLabel lblNewLabel = new JLabel("Search:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBounds(806, 723, 50, 16);
		mainPanel.add(lblNewLabel);
	}
	
	/**
     * Diplay text field components that accepts user's inputs
     */
	private void addFieldComponents() {
		
		productIDField = new JTextField();
		productIDField.setBounds(151, 45, 125, 20);
		productIDField.setColumns(10);
		mainPanel.add(productIDField);
		
		quantityField = new JTextField();
		quantityField.setBounds(361, 46, 87, 20);
		quantityField.setColumns(10);
		mainPanel.add(quantityField);
		
		wholesaleCostField = new JTextField();
		wholesaleCostField.setBounds(594, 46, 86, 20);
		wholesaleCostField.setColumns(10);
		mainPanel.add(wholesaleCostField);
		
		salePriceField = new JTextField();
		salePriceField.setBounds(775, 46, 86, 20);
		salePriceField.setColumns(10);
		mainPanel.add(salePriceField);
		
		supplierIDField = new JTextField();
		supplierIDField.setBounds(979, 46, 106, 20);
		supplierIDField.setColumns(10);
		mainPanel.add(supplierIDField);
		
		searchField = new JTextField();
		searchField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String search = searchField.getText();
				searchBar(search);
			}
		});
		searchField.setBounds(742, 745, 175, 28);
		mainPanel.add(searchField);
		searchField.setColumns(10);
	}

	/**
     * Diplay button components to implement C.R.U.D operations
     */
	private void addButtonComponents() {
		createButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		createButton.setIcon(new ImageIcon("C:\\Users\\brian\\OneDrive\\Desktop\\Create_icon_20x20.png")); // Adds Create image to Create button
		createButton.setBounds(66, 725, 106, 39);
		mainPanel.add(createButton);
		
		readButton.setIcon(new ImageIcon("C:\\Users\\brian\\OneDrive\\Desktop\\Read_icon_20x20.png"));     // Adds Read image to Read button
		readButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		readButton.setBounds(180, 725, 106, 39);
		mainPanel.add(readButton);
		
		updateButton.setIcon(new ImageIcon("C:\\Users\\brian\\OneDrive\\Desktop\\Update_icon_20x20.png")); // Adds Update image to Update button
		updateButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		updateButton.setBounds(292, 725, 106, 39);
		mainPanel.add(updateButton);
		
		deleteButton.setIcon(new ImageIcon("C:\\Users\\brian\\OneDrive\\Desktop\\Delete_icon_20x20.png")); // Adds Delete image to Delete button
		deleteButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		deleteButton.setBounds(403, 725, 106, 39);
		mainPanel.add(deleteButton);
		
		clearButton.setIcon(new ImageIcon("C:\\Users\\brian\\OneDrive\\Desktop\\Clear_icon_20x20.png"));  // Adds Clear image to Clear button
		clearButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		clearButton.setBounds(514, 725, 106, 39);
		mainPanel.add(clearButton);
		
		connectButton.setBounds(940, 726, 133, 39);
		mainPanel.add(connectButton);
	}

	/**
     * Implement search bar function to accept user input to be searched for in the database 
     */
	
	private void searchBar(String str) {
		tableModel= (DefaultTableModel)table.getModel();
		TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(tableModel);
		table.setRowSorter(trs);
		trs.setRowFilter(RowFilter.regexFilter(str));
	}
	 /**
     * Clear all contents in text fields
     */
	private void clearField() {
		productIDField.setText("");
		quantityField.setText("");
		wholesaleCostField.setText("");
		salePriceField.setText("");
		supplierIDField.setText("");
	}
}
