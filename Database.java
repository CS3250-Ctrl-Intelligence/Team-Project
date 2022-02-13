import java.util.*;
import java.sql.*;
import java.io.*;

/*
This class creates a database using MySQL from an imported CSV file.
It allows to Create, Read, Update, and Delete items directly from the database created. 
*/
public class Database {

    final String jdbc = "jdbc:mysql://localhost:3306/Inventory";   // Address to MySQL database
    final String dbUsername = "root";                              // Database's Username
    final String dbPassword = "password";                          // Database's Password
    String sql;                                                    // Initiates a String for SQL commands
    Connection con;                                                // Initiates a Connection to connect to database
    PreparedStatement statement;                                   // Initiates PreparedStatement for sql variable
    
    public static void main(String[] args) {
        
        Database database = new Database();
        database.importCSV();
    }


    // Method that imports a csv file into MySQL Database
    public void importCSV() {

        String csvFilePath = "C:\\Users\\brian\\Documents\\College\\CS 3250\\Project\\inventory_team1.csv";     // csv file address
        int batchSize = 20;
 
        try {
            // Connect to database
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            con.setAutoCommit(false);
            sql = "INSERT INTO Items (Product_ID, Quantity, Wholesale_Cost, Sale_Price, Supplier_ID)" + "VALUES (?, ?, ?, ?, ?)";   // SQL statement to insert data to database
            statement = con.prepareStatement(sql);
 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); 
            
            // Loops through CSV file line by line
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");                        // Split data by commas
                String product_id= data[0];
                int quantity = Integer.valueOf(data[1]);
                float wholesale_cost = Float.valueOf(data[2]);
                float sale_price = Float.valueOf(data[3]);
                String supplier_id = data[4];
                
                // Inserts each category to the database
                statement.setString(1, product_id);
                statement.setInt(2, quantity);
                statement.setFloat(3, wholesale_cost);
                statement.setFloat(4, sale_price);
                statement.setString(5, supplier_id);
                statement.addBatch();

                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
 
            // execute the remaining queries
            statement.executeBatch();
            con.commit();
            con.close();
        } 
        catch (IOException ex) {
            System.err.println(ex);
        } 
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // This method adds a new item to the database.
    public void Insert(String product_id, int qty, float wholesale_cost, float sale_price, String supplier_id) {

        try {
            // Connect to database
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "INSERT INTO Items (Product_ID, Quantity, Wholesale_Cost, Sale_Price, Supplier_ID)" + "VALUES (?, ?, ?, ?, ?)";   // SQL statement to insert data to database
            statement = con.prepareStatement(sql);

            // Inserts each category into database to their respective categories
            statement.setString(1, product_id);
            statement.setInt(2, qty);
            statement.setFloat(3, wholesale_cost);
            statement.setFloat(4, sale_price);
            statement.setString(5, supplier_id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("A new item has been added to the inventory successfully.");
            }
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* This method allows for us to search the database by Product ID and Read the information tied with it.
       Additionally, it returns a list of objects in order to implement these list into the GUI (Table.java). */
    public Object [] Read(String product_id) {

        Object [] row = new Object[5];      // List of objects to store the data retrieved by searching product ID

        try {
            // Connect to the database
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "SELECT * FROM Items WHERE Product_ID= '" + product_id + "'"; // SQL statement to perform the search in the database
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);

            // Adds each category result that matches with the product ID to the list of objects
            while (result.next()) {
                row[0]= result.getString("product_id");
                row[1] = result.getInt("quantity");
                row[2]= result.getDouble("wholesale_cost");
                row[3] = result.getDouble("sale_price");
                row[4]= result.getString("supplier_id");
            }
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return row;
    }


    // Method that updates an item within the database. Currently it is search by Product ID and updates the quantity, wholesale, and sale fields.
    public void Update(String product_id, int qty, float wholesale_cost, float sale_price) {

        try {
            // Connect to database
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "UPDATE Items SET Quantity=?, Wholesale_Cost=?, Sale_Price=? WHERE Product_ID=?";     // SQL statement to update specified fields within the database
            statement = con.prepareStatement(sql);

            // Inserts new data to the respective fields
            statement.setInt(1, qty);
            statement.setFloat(2, wholesale_cost);
            statement.setFloat(3, sale_price);
            statement.setString(4, product_id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("The item's information has been updated.");
            }
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method that deletes an item within the database. It is searched by Product ID
    public void Delete(String product_ID) {

        try {
            // Connect to database
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "DELETE FROM Items WHERE Product_ID=?";       // SQL statement to delete items from the database
            statement = con.prepareStatement(sql);

            // Deletes entire row that matches this product ID
            statement.setString(1, product_ID);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("The item's information has been deleted.");
            }
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
