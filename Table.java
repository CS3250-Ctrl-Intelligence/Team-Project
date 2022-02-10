package databasegui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 *
 * @author Tho Le
 * @version 1.3.1
 */
public class Table {

	private JFrame frame;
	/** The text field */
	private JTextField productIDField;
	private JTextField quantityField;
	private JTextField wholesaleCostField;
	private JTextField salePriceField;
	private JTextField supplierIDField;
	
	/** The item table */
	private JTable table;
	private DefaultTableModel tableModel;

	/** Connection to database */
	private Connection con;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Table window = new Table();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1057, 738);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setJMenuBar(createMenuBar());
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(0, 0, 1041, 698);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel productIDLabel = new JLabel("Product ID: ");
		productIDLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		productIDLabel.setBounds(32, 24, 75, 15);
		panel.add(productIDLabel);
		
		JLabel quantityLabel = new JLabel("Quantity: ");
		quantityLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		quantityLabel.setBounds(236, 24, 62, 15);
		panel.add(quantityLabel);
		
		productIDField = new JTextField();
		productIDField.setBounds(122, 21, 86, 20);
		panel.add(productIDField);
		productIDField.setColumns(10);
		
		quantityField = new JTextField();
		quantityField.setBounds(308, 22, 86, 20);
		panel.add(quantityField);
		quantityField.setColumns(10);
		
		JLabel wholesaleCostLabel = new JLabel("Wholesale Cost: ");
		wholesaleCostLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		wholesaleCostLabel.setBounds(430, 24, 101, 15);
		panel.add(wholesaleCostLabel);
		
		wholesaleCostField = new JTextField();
		wholesaleCostField.setBounds(541, 22, 86, 20);
		panel.add(wholesaleCostField);
		wholesaleCostField.setColumns(10);
		
		JLabel salePriceLabel = new JLabel("Sale Price: ");
		salePriceLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		salePriceLabel.setBounds(646, 24, 66, 15);
		panel.add(salePriceLabel);
		
		salePriceField = new JTextField();
		salePriceField.setBounds(722, 22, 86, 20);
		panel.add(salePriceField);
		salePriceField.setColumns(10);
		
		JLabel supplierIDLabel = new JLabel("Supplier ID: ");
		supplierIDLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		supplierIDLabel.setBounds(840, 24, 76, 15);
		panel.add(supplierIDLabel);
		
		supplierIDField = new JTextField();
		supplierIDField.setBounds(926, 22, 86, 20);
		panel.add(supplierIDField);
		supplierIDField.setColumns(10);
		
		JScrollPane table_scrollPane = new JScrollPane();
		table_scrollPane.setBounds(32, 50, 980, 569);
		panel.add(table_scrollPane);
		
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//When user clicked on a row, the information will be input to the textfield to change
				int i = table.getSelectedRow();
				productIDField.setText(tableModel.getValueAt(i,0).toString());
				quantityField.setText(tableModel.getValueAt(i,1).toString());
				wholesaleCostField.setText(tableModel.getValueAt(i,2).toString());
				salePriceField.setText(tableModel.getValueAt(i,3).toString());
				supplierIDField.setText(tableModel.getValueAt(i,4).toString());
			}
		});
		table.setBackground(new Color(240, 255, 240));
		table_scrollPane.setViewportView(table);
		tableModel = new DefaultTableModel();
		Object [] column = {"Product ID", "Quantity", "Wholesale Cost", "Sale Price", "Supplier ID"};
		Object [] row = new Object[5];
		tableModel.setColumnIdentifiers(column);
		table.setModel(tableModel);
		
		
		
		//-----------------------------Create Button and Functionalities--------------------------------
		
		JButton createButton = new JButton("Create");
		
		//when the Create button is clicked, item is added to table and datase
		//no check for duplication
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				
				try {
					//Add item to database
			        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","student","student");
			        String sql = "INSERT INTO inventory_team1 (Product_ID, Quantity, Wholesale_Cost, Sale_Price, Supplier_ID)" + "VALUES (?, ?, ?, ?, ?)";
			        PreparedStatement statement = con.prepareStatement(sql);

			        statement.setString(1, productIDField.getText());
			        statement.setInt(2, Integer.parseInt(quantityField.getText()));
			        statement.setDouble(3, Double.parseDouble(wholesaleCostField.getText()));
			        statement.setDouble(4, Double.parseDouble(salePriceField.getText()));
			        statement.setString(5, supplierIDField.getText());

			        int rows = statement.executeUpdate();

			        if (rows > 0) {
			            System.out.println("A new item has been added to the inventory successfully.");
			        }
			      
			        con.close();
			        //check for empty input
					if(productIDField.getText().equals("") || quantityField.getText().equals("") || wholesaleCostField.getText().equals("") 
							|| salePriceField.getText().equals("") || supplierIDField.getText().equals("") ) {
						JOptionPane.showMessageDialog(null,"Please fill complete information");
					}
					else {		
						//Add item to table	
						row[0]= productIDField.getText();
						row[1] = quantityField.getText();
						row[2]= wholesaleCostField.getText();
						row[3] = salePriceField.getText();
						row[4]= supplierIDField.getText();
						tableModel.addRow(row);
						clearField();
						JOptionPane.showMessageDialog(null,"Successfully Added");
					}
			    }
			    catch (SQLException e) {
			        e.printStackTrace();
			    }
				
			}
			
		});
		createButton.setBounds(57, 631, 89, 23);
		panel.add(createButton);
		
		JButton updateButton = new JButton("Update");
		//when the Update button is clicked, item's quantity,wholesale cost, and sale price is updated
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					int i = table.getSelectedRow();
					if(i>=0) {
						//Update item on table
						tableModel.setValueAt(productIDField.getText(), i, 0);
						tableModel.setValueAt(quantityField.getText(), i, 1);
						tableModel.setValueAt(wholesaleCostField.getText(), i, 2);
						tableModel.setValueAt(salePriceField.getText(), i, 3);
						tableModel.setValueAt(supplierIDField.getText(), i, 4);
						JOptionPane.showMessageDialog(null,"Successfully Updated");
						
						//Update item in database
						con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","student","student");
			            String sql = "UPDATE inventory_team1 SET quantity=?, wholesale_cost=?, sale_price=? WHERE product_id='"+ productIDField.getText()+"'";
			            PreparedStatement statement = con.prepareStatement(sql);
			            statement.setInt(1, Integer.parseInt(quantityField.getText()));
			            statement.setDouble(2, Double.parseDouble(wholesaleCostField.getText()));
			            statement.setDouble(3, Double.parseDouble(salePriceField.getText()));

			            int rows = statement.executeUpdate();

			            if (rows > 0) {
			                System.out.println("The item's information has been updated.");
			            }
			            con.close();
					}
					else {
						JOptionPane.showMessageDialog(null,"Please Select an Item First");
					}
		            
		        }
		        catch (SQLException e) {
		            e.printStackTrace();
		        }
				
				
			}
		});
		updateButton.setBounds(255, 630, 89, 23);
		panel.add(updateButton);
		
		JButton deleteButton = new JButton("Delete");
		
		//when the Delete button is clicked, the item(row) is deleted
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				try {
					int i = table.getSelectedRow();
					if (i >=0) {
						//Delete item from table
						tableModel.removeRow(i);
						//Delelte item from database
						con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","student","student");
			            String sql = "DELETE FROM inventory_team1 WHERE product_id='"+productIDField.getText()+"'";
			            PreparedStatement statement = con.prepareStatement(sql);

			            int rows = statement.executeUpdate();

			            if (rows > 0) {
			                System.out.println("The item's information has been deleted.");
			            }
			            con.close();
						JOptionPane.showMessageDialog(null,"Selected item deleted");
						
					}
					else
						JOptionPane.showMessageDialog(null,"Please select a row to be deleted");
				
		        }
		        catch (SQLException e) {
		            e.printStackTrace();
		        }
		}
				
		});
		deleteButton.setBounds(354, 631, 89, 23);
		panel.add(deleteButton);
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clearField();
				
			}
		});
		clearButton.setBounds(453, 630, 89, 23);
		panel.add(clearButton);
		
		JButton connectButton = new JButton("Connect to database");
		
		//When the Connect button is clicked, load items from database to table
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Load items from database
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","student","student");
					String sql= "select product_id, quantity, wholesale_cost, sale_price, supplier_id from demo.inventory_team1 order by product_id";
					Statement selectStatement = con.createStatement();
					ResultSet results = selectStatement.executeQuery(sql);
					
					while(results.next()) {
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
		connectButton.setBounds(887, 631, 89, 23);
		panel.add(connectButton);
		
		JButton searchButton = new JButton("Read");
		
		//When the Read button is clicked, load item with given product id from database to table
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				 try {
					 	if(productIDField.getText().equals("")) {
					 		JOptionPane.showMessageDialog(null,"Please fill complete information");
					 	}
					 	else {
					 		//Load item with specifed product id to table
					 		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","student","student");
					 		String sql = "SELECT * FROM inventory_team1 WHERE product_id=" + "'" + productIDField.getText() + "'";
					 		PreparedStatement statement = con.prepareStatement(sql);
					 		ResultSet result = statement.executeQuery(sql);
					 		
					 		while (result.next()) {
					 			row[0]= result.getString("product_id");
					 			row[1] = result.getInt("quantity");
					 			row[2]= result.getDouble("wholesale_cost");
					 			row[3] = result.getDouble("sale_price");
					 			row[4]= result.getString("supplier_id");
					 			tableModel.addRow(row);
					 		}
					 		con.close();					 		
					 	}
			        }
			        catch (SQLException e) {
			            e.printStackTrace();
			        }
				
			}
		});
		searchButton.setBounds(156, 631, 89, 23);
		panel.add(searchButton);
		
	}
	 /**
     * Create Menu Bar
     */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu windowMenu = new JMenu("Window");
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem exportData = new JMenuItem("Export Data....");
		JMenuItem importData = new JMenuItem("Import Data....");
		JMenuItem exit = new JMenuItem("Exit");
		
		fileMenu.add(exportData);
		fileMenu.add(importData);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		
		
		
		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		
		return menuBar;
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
