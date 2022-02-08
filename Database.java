import java.util.*;
import java.sql.*;
import java.io.*;

public class Database {

    final String jdbc = "jdbc:mysql://localhost:3306/Inventory";   // Address to MySQL database
    final String dbUsername = "root";                              // Database's Username
    final String dbPassword = "password";                          // Database's Password
    String sql;                                                    // Initiates a String for SQL commands
    Connection con;                                                // Initiates a Connection to connect to database
    PreparedStatement statement;                                   // Initiates PreparedStatement for sql variable
    
    public static void main(String[] args) {
        
        Database testing = new Database();
        testing.importCSV();
        Scanner keyboard = new Scanner(System.in);

        /* The below code is only use for testing the different functions using input from the user */

        // Insert Method user prompts
        System.out.println("Enter Product ID: ");
        String userProduct = keyboard.nextLine();
        System.out.println("Enter Quantity: ");
        int userQty = keyboard.nextInt();
        System.out.println("Enter Wholesale Cost: ");
        float userWholesale = keyboard.nextFloat();
        System.out.println("Enter Sale Price: ");
        float userSale = keyboard.nextFloat();
        keyboard.nextLine();
        System.out.println("Enter Supplier ID: ");
        String userSupplier = keyboard.nextLine();

        Database test = new Database();
        test.Insert(userProduct, userQty , userWholesale, userSale, userSupplier);
        test.Retrieve();


        //Retrieve Method user prompts
        System.out.println("Enter Product ID: ");
        String userProduct2 = keyboard.nextLine();
        System.out.println("Enter Quantity: ");
        int userQty2 = keyboard.nextInt();
        System.out.println("Enter Wholesale Cost: ");
        float userWholesale2 = keyboard.nextFloat();
        System.out.println("Enter Sale Price: ");
        float userSale2 = keyboard.nextFloat();

        test.Update(userProduct2, userQty2, userWholesale2, userSale2);


        // Delete Method user prompt
        
        keyboard.nextLine();
        System.out.println("Enter Product ID: ");
        String userProduct3 = keyboard.nextLine();
        testing.Delete(userProduct3);
        keyboard.close(); 
    }


    // Method that imports a csv file into MySQL Database
    public void importCSV() {

        String csvFilePath = "C:\\Users\\brian\\Documents\\College\\CS 3250\\Project\\inventory_team1.csv";
        int batchSize = 20;
 
        try {
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            con.setAutoCommit(false);
            sql = "INSERT INTO Items (Product_ID, Quantity, Wholesale_Cost, Sale_Price, Supplier_ID)" + "VALUES (?, ?, ?, ?, ?)";
            statement = con.prepareStatement(sql);
 
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine(); 
 
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String product_id= data[0];
                int quantity = Integer.valueOf(data[1]);
                float wholesale_cost = Float.valueOf(data[2]);
                float sale_price = Float.valueOf(data[3]);
                String supplier_id = data[4];
 
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


    // This method adds a new item to the database. Input is from user.
    public void Insert(String product_id, int qty, float wholesale_cost, float sale_price, String supplier_id) {

        try {
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "INSERT INTO Items (Product_ID, Quantity, Wholesale_Cost, Sale_Price, Supplier_ID)" + "VALUES (?, ?, ?, ?, ?)";
            statement = con.prepareStatement(sql);

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


    // Method that displays the entirety of the database
    public void Retrieve() {

        try {
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "SELECT * FROM Items";
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                String productID = result.getString("Product_ID");
                int Quantity = result.getInt("Quantity");
                float wholesale_cost= result.getFloat("Wholesale_Cost");
                float sale_price = result.getFloat("Sale_Price");
                String supplierID = result.getString("Supplier_ID");
                
                System.out.println(productID + ", " + Quantity + ", " + wholesale_cost + ", " + sale_price + ", " + supplierID);
            }
            con.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method that updates an item within the database. Currently it is search by Product ID and updates the quantity, wholesale, and sale fields.
    public void Update(String product_id, int qty, float wholesale_cost, float sale_price) {

        try {
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "UPDATE Items SET Quantity=?, Wholesale_Cost=?, Sale_Price=? WHERE Product_ID=?";
            statement = con.prepareStatement(sql);

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
            con = DriverManager.getConnection(jdbc ,dbUsername, dbPassword);
            sql = "DELETE FROM Items WHERE Product_ID=?";
            statement = con.prepareStatement(sql);
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
