/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utilities;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author V
 */
public class DatabaseManager {
    private String url = "jdbc:mysql://localhost:3306/";
    Connection con = null;
    Statement cursor = null;
    ResultSet sqlVar = null;
    
    public DatabaseManager() {
        try {
            DatabaseSetup newDB = new DatabaseSetup("cafetheadb","","vr00T4rc3()->");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void createDatabase(String dbName) {
        try {
            cursor.execute("create database "+dbName);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteAll(String tableName) {
        String query = "DELETE FROM "+tableName;
        try {
            cursor.execute(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void dropDatabase(String dbName) {
        try {
            cursor.execute("drop database "+dbName);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Deleted "+dbName);
        }
    }
    
    public void deleteOne(String tableName, String id) {
        String idname = getConfigure(tableName);
        String query = "DELETE FROM "+tableName+" WHERE "+idname+" = "+id;
        try {
            System.out.println(query);
            cursor.execute(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertOne(String tableName, String values) {
        try {
            //get column names
            String query = "INSERT INTO "+tableName+" (";
            
            ResultSet columnNameSet = cursor.executeQuery("SELECT * FROM "+tableName);
            ResultSetMetaData queryMetaData = columnNameSet.getMetaData();
            int colCount = queryMetaData.getColumnCount();
            
            //i is set to two to skip the primary key that is auto_incremented/generated
            for (int i=2;i<=colCount;i++) {
                query += queryMetaData.getColumnName(i)+", ";
            }
            query = query.substring(0, query.length()-1);
            query = query+") values (";
            
            String temp = getConfigure(tableName);
            System.out.println(temp+values+")");
            cursor.executeUpdate(temp+values+")", cursor.RETURN_GENERATED_KEYS);
            ResultSet resultSet = cursor.getGeneratedKeys();
            
            if (resultSet.next()) {
                int primaryId = resultSet.getInt(1);
                System.out.println("Primary ID of the inserted row: " + primaryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateById(String tableName, int idVal) {
        try {
            String idName = getConfigure(tableName);
            String startStatement = "update "+tableName+" set ";
            //String betweenStatement = updateConfigure(tableName);
            
            String betweenStatement  = "";
            //
            ResultSet resultSet = cursor.executeQuery("SELECT * FROM "+tableName);

            // Get the metadata of the ResultSet
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // Get the number of columns in the ResultSet
            int columnCount = resultSetMetaData.getColumnCount();
              // Loop through all columns of the current row
            for (int i = 1; i <= columnCount; i++) {
                betweenStatement += resultSetMetaData.getColumnName(i) + " = ?, ";
            }
            betweenStatement = betweenStatement.substring(0, betweenStatement.length() - 2);
            //
            String endStatement = " where "+idName+" = "+idVal;
            String query = startStatement+betweenStatement+endStatement;
            PreparedStatement ps = con.prepareStatement(query);
            
            //this entire switch statement heavily relies on the gui component
            ps.setInt(1, 2);
            Calendar cldr = null;
            Date date = null;
            switch (tableName) {
                case "menuitem":
                    ps.setString(2, "getComponent");
                    ps.setFloat(3, 10.0f);
                    ps.setFloat(4, 10.0f);
                    break;
                case "supplier":
                    ps.setString(2, "getComponent");
                    ps.setInt(2, 10);
                    break;
                case "resource":
                    ps.setString(2, "getComponent");
                    ps.setFloat(3, 10.0f);
                    ps.setInt(4, 100);
                    ps.setInt(5, 10);
                    ps.setInt(6, 1);
                    break;
                case "customer":
                    ps.setString(2, "getComponent");
                    ps.setString(3, "fromGUI");
                    ps.setInt(4, 123456789); //integer too large, welp
                    ps.setString(5, "tightCoupling");
                    break;
                case "service":
                    ps.setString(2, "getComponent");
                    ps.setFloat(3, 10.0f);
                    ps.setFloat(4, 10.0f);
                    ps.setString(5, "fromGUI");
                    break;
                case "credentials":
                    ps.setString(2, "getComponent");
                    ps.setString(3, "fromGUI");
                    break;
                case "orderrequest":
                    ps.setInt(2, 10);
                    ps.setInt(3, 11);
                    ps.setInt(4, 12);
                    ps.setString(5, "getComponent");
                    //declaration of calendar and date
                    cldr = Calendar.getInstance();
                    cldr.set(2023, Calendar.JANUARY, 1);//date desired ; year month day
                    date = cldr.getTime();
                    ps.setDate(6, new java.sql.Date(date.getTime()));
                    ps.setString(7, "getComboBox"); //hmmm hope this works
                    ps.setString(8, "fromGUI");
                    ps.setFloat(9, 10.0f);
                    break;
                case "receipt":
                    cldr = Calendar.getInstance();
                    cldr.set(2023, Calendar.JANUARY, 1, 12, 0, 0);//date desired ; year month day hour minute second
                    date = cldr.getTime();
                    ps.setDate(2, new java.sql.Date(date.getTime()));
                    ps.setInt(3, 10);
                    break;
                default:
                    System.out.println("Error");
                    break;
            }
            //
            System.out.println(query);
            ps.executeUpdate();
            System.out.println(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void selectAll(String tableName) {
        try {
            ResultSet resultSet = cursor.executeQuery("SELECT * from "+tableName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // Get the number of columns in the ResultSet
            int columnCount = resultSetMetaData.getColumnCount();

            // Loop through all rows of the ResultSet
            while (resultSet.next()) {
              // Loop through all columns of the current row
              for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
              }
              System.out.println();
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }   
    
    public void selectId(String tableName, String idVal) {
        try {
            String idName = getConfigure(tableName);
            String query = "SELECT * FROM "+tableName+" WHERE "+idName+ " = "+idVal;
            ResultSet resultSet = cursor.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // Get the number of columns in the ResultSet
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
              // Loop through all columns of the current row
              for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
              }
              System.out.println();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
            
    public void selectQuery(String tableName, String statement, String statementVal) {
        try {
            String query = "SELECT * FROM "+tableName+" WHERE "+statement+ " = "+statementVal;
            ResultSet resultSet = cursor.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // Get the number of columns in the ResultSet
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
              // Loop through all columns of the current row
              for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
              }
              System.out.println();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void sortById(String tableName, boolean asc) {
        try {
            String idname = getConfigure(tableName);
            String query = "";
            if (asc) {
                 query = "SELECT * FROM "+tableName+" ORDER BY "+idname+" ";
            }
            else {
                query = "SELECT * FROM "+tableName+" ORDER BY "+idname+" DESC";
            }
            ResultSet resultSet = cursor.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // Get the number of columns in the ResultSet
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
              // Loop through all columns of the current row
              for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
              }
              System.out.println();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void sortByQuery(String tableName, String columnName, boolean asc) {
        try {
            String query = " ";
            if (asc) {
                 query = "SELECT * FROM "+tableName+" ORDER BY "+columnName+" ";
            }
            else {
                query = "SELECT * FROM "+tableName+" ORDER BY "+columnName+" DESC";
            }
            ResultSet resultSet = cursor.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // Get the number of columns in the ResultSet
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
              // Loop through all columns of the current row
              for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + " ");
              }
              System.out.println();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//    private String getConfigure2(String tableName) {//obsolete 
//        String statement = "INSERT INTO "+tableName+" ";
//        String text = "";
//        try {
//            switch (tableName) {
//                case "menuitem":
//                    text = "(menuitemname, menuitemcost) values (";
//                    break;
//                case "supplier":
//                    text = "(suppliername, suppliercontact) values (";
//                    break;
//                case "resource":
//                    text = "(resourcename, resourcecost, resourcequantity, resourcedeductionaverage, supplierid) values (";
//                    break;
//                case "customer":
//                    text = "(customerfirstname, customerlastname, customercontact, customeraddress) values (";
//                    break;
//                case "service":
//                    text = "(servicename, servicecost, resourcedeductionaverage, servicedescription) values (";
//                    break;
//                case "credentials":
//                    text = "(username, password) values (";
//                    break;
//                case "orderrequest":
//                    text = "(serviceid, productids, customerid, userid, datetime, deliverymode, totalcost) values (";
//                    break;
//                case "receipt":
//                    text = "(receiptdatetime, orderid) values (";
//                    break;
//                default:
//                    System.out.println("Out of bounds");
//                    break;
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//                
//        return statement+text;
//    }
    
    private String getConfigure(String tableName) {//obsolete
        String text = "";
        try {
            switch (tableName) {
                case "menuitem":
                    text = "menuitemid";
                    break;
                case "supplier":
                    text = "supplierid";
                    break;
                case "resource":
                    text = "resourceid";
                    break;
                case "customer":
                    text = "customerid";
                    break;
                case "service":
                    text = "serviceid";
                    break;
                case "credentials":
                    text = "userid";
                    break;
                case "orderrequest":
                    text = "orderid";
                    break;
                case "receipt":
                    text = "receiptid";
                    break;
                default:
                    System.out.println("Out of bounds");
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
    
//    private String updateConfigure(String tableName) {//obsolete
//        String text = "";
//        try {
//            switch (tableName) {
//                case "menuitem":
//                    text = "menuitemid = ?, menuitemname = ?, menuitemcost = ?, resourceDeductionAverage =?";
//                    break;
//                case "supplier":
//                    text = "supplierid = ?, suppliername = ?, suppliercontact = ?";
//                    break;
//                case "resource":
//                    text = "resourceid = ?, resourcename = ?, resourcecost = ?, resourcequantity = ?, resourcedeductionaverage = ?, supplierid = ?";
//                    break;
//                case "customer":
//                    text = "customerid = ?, customerfirstname = ?, customerlastname = ?, customercontact = ?, customeraddress = ?";
//                    break;
//                case "service":
//                    text = "serviceid = ?, servicename = ?, servicecost = ?, resourcedeductionaverage = ?, servicedescription = ?";
//                    break;
//                case "credentials":
//                    text = "userid = ?, username = ?, password = ?";
//                    break;
//                case "orderrequest"://ignore, do not allow edit, instead create a new one but deleting the old one simultaneously.
//                    text = "orderid = ?,  serviceids = ?, customerid = ?, userid = ?, productids = ?, scheddatetime = ?, deliverymode = ?, serviceinstruction = ?, totalcost = ?";
//                    break;
//                case "receipt":
//                    text = "receiptid = ?, receiptdatetime = ?, orderid = ?";
//                    break;
//                default:
//                    System.out.println("Out of bounds");
//                    break;
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return text;
//    }
    
    public void useDatabase(String dbName) {
        try {
            cursor.execute("use database "+dbName);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void closeConnections() {
        try {
            sqlVar.close();
            cursor.close();
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void commitChanges() {
        try {
           con.commit(); 
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private String regexQuery(String query) {
        return null;
    }
    
    public class DatabaseSetup {
        /**
        *This constructor establishes a connection to a local mysql database.
        * 
        *@param db       database name to use and access
        *@param user     mysql localhost username
        *@param password mysql user password
        */
        public DatabaseSetup(String db, String user, String password) {
            if (user.isBlank()) {
                user = "root";
            }
            try {
                con = DriverManager.getConnection(url,user,password);
                System.out.print("Connection established!");
                cursor = con.createStatement();
                sqlVar = cursor.executeQuery("show databases");
                Boolean flag = false;
                while (sqlVar.next()) {
                    String dbName = sqlVar.getString("Database");
                    if (dbName.equals(db)) {
                        flag=true;
                    }
                }
                if (flag) {
                    System.out.println(db+" exists!");
                    cursor.execute("use "+db);
                    System.out.println("using "+db);
                }
                else {
                    initiateTables(db,user,password);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private void initiateTables(String db, String user, String pass) {  
            final String[] dbTables = {
                "CREATE TABLE MENUITEM (menuItemID INT AUTO_INCREMENT PRIMARY KEY,menuItemName VARCHAR(50) NOT NULL,menuItemCost FLOAT(10) NOT NULL,resourceDeductionAverage FLOAT(10))",
                "CREATE TABLE SUPPLIER (supplierID INT AUTO_INCREMENT PRIMARY KEY,supplierName VARCHAR(50) NOT NULL,supplierContact INT(15) NOT NULL)",
                "CREATE TABLE RESOURCE (resourceID INT AUTO_INCREMENT PRIMARY KEY,resourceName VARCHAR(50) NOT NULL,resourceCost FLOAT(10) NOT NULL,resourceQuantity INT NOT NULL,resourceDeductionAverage INT NOT NULL,supplierID INT NOT NULL,CONSTRAINT FK_SUP FOREIGN KEY (supplierID) REFERENCES SUPPLIER(supplierID))",
                "CREATE TABLE CUSTOMER (customerID INT AUTO_INCREMENT PRIMARY KEY, customerFirstName VARCHAR(50) NOT NULL, customerLastName VARCHAR(50) NOT NULL, customerContact INT(15) NOT NULL, customerAddress VARCHAR(100) NOT NULL)",
                "CREATE TABLE SERVICE (serviceID INT AUTO_INCREMENT PRIMARY KEY, serviceName VARCHAR(20) NOT NULL, serviceCost FLOAT(10) NOT NULL, resourceDeductionAverage FLOAT(10), serviceDescription VARCHAR(200) NOT NULL)",
                "CREATE TABLE CREDENTIALS (userID INT AUTO_INCREMENT PRIMARY KEY, userName VARCHAR(20) NOT NULL, password VARCHAR(20) NOT NULL)",
                "CREATE TABLE ORDERREQUEST (orderID INT AUTO_INCREMENT PRIMARY KEY, serviceID INT NOT NULL, CONSTRAINT FK_SERVICE FOREIGN KEY (serviceID) REFERENCES SERVICE(serviceId), customerID INT NOT NULL, CONSTRAINT FK_CUST FOREIGN KEY (customerID) REFERENCES CUSTOMER(customerID), userID INT NOT NULL, CONSTRAINT FK_USER FOREIGN KEY (userID) REFERENCES CREDENTIALS(userID), productids VARCHAR(200), scheddatetime DATETIME, deliveryMode CHAR(20) NOT NULL, CONSTRAINT CHK_DELIVERY CHECK(deliveryMode IN ('Dine In', 'Take Out', 'Delivery', 'Pick Up', 'N/A')), serviceInstruction VARCHAR(150), totalCost FLOAT(10) NOT NULL, CONSTRAINT CHK_TOTAL CHECK(totalCost > 0))",
                "CREATE TABLE RECEIPT (receiptID INT AUTO_INCREMENT PRIMARY KEY, receiptDateTime DATETIME NOT NULL, orderID INT NOT NULL, CONSTRAINT FK_ORDER FOREIGN KEY (orderID) REFERENCES ORDERREQUEST(orderID))"
            };
            try {
                cursor.execute("create database "+db);
                con = DriverManager.getConnection(url+db, user, pass);
                cursor = con.createStatement();
                for (String query: dbTables) {
                    cursor.execute(query);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println("Created "+db);
            }

        }
    }
    
    public static void main(String args[]) {
        DatabaseManager cafeTheaDb = new DatabaseManager();
        //Test case
        //cafeTheaDb.insertOne("menuitem", "'pineapple pizza large', 690");
        //cafeTheaDb.insertOne("menuitem", "'peperroni pizza large', 575");//to parse
        //cafeTheaDb.deleteOne("menuitem", "1");
        //cafeTheaDb.dropDatabase("cafetheadb");
        //cafeTheaDb.selectAll("menuitem");
        cafeTheaDb.selectId("menuitem", "3");
        //cafeTheaDb.selectQuery("menuitem", "menuitemcost", "75");
        //cafeTheaDb.sortById("menuitem", false);
        //cafeTheaDb.sortByQuery("menuitem", "menuitemname", true);
        //cafeTheaDb.updateById("menuitem", 2);
    }
}
