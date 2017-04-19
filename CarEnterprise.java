/**
 * Dana Cunningham
 * CSE241
 * Project
  */

import java.util.Scanner;
import java.util.InputMismatchException;
import java.sql.*;
import java.util.ArrayList;

import static ConnSingleton.getConn;
import static ScannerSingleton.getScan;

////////////////////////////// SINGLETONS //////////////////////////////
/**
 * Uses the singleton pattern in order to create a global connection. 
 */
class ConnSingleton {
    private static Connection conn = null;

    /** 
     * getConn: Gets the connection 
     * @return Connection - returns the global connection
     */
    static Connection getConn() throws java.lang.ClassnOtFoundException {   
        if (conn == null)
            initConn();
        return conn;
    }
    
    /**
     * initConn(): used to establish the connection with SQL Developer 
     */
    private static void initConn() throws java.lang.ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        // Get the user's Oracle signin information
        // This will be passed to the CarEnterprise task
        while(true) {
            System.out.println("enter Oracle user id:");
            String usnm = scan.nextLine();
            System.out.println("enter Oracle password for " + usnm + ":");
            String pwd = scan.nextLine();

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
        
                conn = DriverManager.getConnection("jdbc:oracle:thin:@edgar0.cse.lehigh.edu:1521:cse241", usnm, pwd);
                break;
            } catch (SQLException e) {
                System.out.println("Incorrect username/password.");
            }	
        }
    }
}

/**
 * Uses the singleton pattern to create a global scanner. 
 */
class ScannerSingleton {
    private static Scanner scan = null;

    /**
     * getScan(): Gets the scanner
     * @return Scanner - the global scanner.
    */
    static Scanner getScan() {
        if (scan == null)
            initScan();
        return scan;
    }

    /**
     * initScan(): This method is used to establish the Scanner 
    */
    private static void initScan() {
        scan = new Scanner(System.in);
    }
}



////////////////////////////// CARENTERPRISE //////////////////////////////
/**
 * This is the CarEnterprise class for the project. It contains 
 * interfaces for different users.  
 */
public class CarEnterprise {

    ////////////////////////////// METHODS //////////////////////////////
    /**
    * printLocations(): Prints all the locations a user can pickup and dropoff a car.
    */
    void printLocations() {
        String getStmt = "SELECT * FROM location";
        PreparedStatement stmt = getConn().prepareStatement(getStmt);
        Result rs = stmt.executeQuery();

        while (rs.next()) {
            String lid = rs.getString("lID");
            String street = rs.getString("street");
            String city = rs.getString("city");
            String state = rs.getString("state");
            int zip_code = Integer.parseInt(rs.getString("zip_code"));

            System.out.printf("%-8s%-20s%-15s%-5s$-8\n", lid, street, city, state, zip_code);
        }
    }

    /**
    * inLocations(): Checks whether a user's input is a valid lID.
    * @param input - the user's input.
    * @return boolean - whether or not the input is valid. 
    */
    boolean inLocations(String input) {
        boolean result = false;

        String getStmt = "SELECT lID FROM location";
        PreparedStatement stmt = getConn().prepareStatement(getStmt);
        Result rs = stmt.executeQuery();

        while (rs.next()) {
            if(input.equals(rs.getString("lID"))) {
                result = true;
                break;
            }
        }
        
        return result;
    }

    /**
    * getReservationDate(): Get the customer's pickup or dropoff date.
    * @param afterThisDate: The date the customer's input should be on or after. Already in (YYYY-MM-DD) format.
    * @return Date - the date the customer wants to pickup/dropoff the Car.
    */
    Date getReservationDate(Date afterThisDate) {
        Date mydate = null;
        String input = "";

        while (true) {
            input = getScan().nextLine();
            try {
                SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-DD");
                mydate = ft.parse(input);
                
                if(mydate >= afterThisDate) {
                    return mydate;
                }
                else {
                    System.out.println("Error: Please enter a date on/after " + afterThisDate.toString());
                    return getReservationDate(afterThisDate);
                }
            } catch (ParseException ex) {
                System.out.println("Error: Your date format is wrong.");
            }
        }
    }

    /**
    * printVehicles(): Prints all the vehicles a user can rent at a certain date and location. 
    * @param start_lID - the ID of the location the customer will pick up the vehicle.
    * @param start_date - the Date the customer will pick up the vehicle. Already in (YYYY-MM-DD) format.
    * @param end_date - the Date the customer will dropoff the vehicle. Already in (YYYY-MM-DD) format.
    */
    void printVehicles(String start_lID, Date start_date, Date end_date) {
        // Get vehicles that are located_at the pickup location
        String getStmt = "SELECT * FROM located_at WHERE lID=?";
        PreparedStatement stmt = getConn().prepareStatement(getStmt);
        stmt.setString(1, start_lID);
        ResultSet rs = stmt.executeQuery();

        ArrayList<String> vehiclesInLoc = new ArrayList<String>();
        ArrayList<String> vehiclesAvailable = new ArrayList<String>();

        while(rs.next()) {
            String vehicle = rs.getString("vID");
            vehiclesInLoc.add(vehicle);
        }

        // Get vehicles from that location that are available at that time
        for(int i=0; i<vehiclesInLoc.size(); i++) {
            // get vID that are taken
            getStmt = "SELECT * FROM rental natural join reservation WHERE vID=? and ((? BETWEEN start_date AND end_date) or (? BETWEEN start_date and end_date))";
            PreparedStatement stmt2 = getConn().prepareStatement(getStmt);
            stmt2.setString(1, vehiclesInLoc.get(i));
            stmt2.setString(2, start_date);
            stmt2.setString(3, end_date);
            ResultSet rs2 = stmt2.executeQuery();

            if(rs.next() == false) { // if the query returns empty, that means the vehicle is available
                vehiclesAvailable.add(vehiclesInLoc.get(i));
            }
        }

        // Print out the list
        for(int i=0; i<vehiclesAvailable.size(); i++) {

        }
    }




////////////////////////// BEGINNING ///////////////////////////////
    /**
     * whichInterface: ask if the user is a customer, employee, or manager.
     */
    void whichInterface() {
        
        System.out.println("Welcome to Hurts Rent-a-Lemon!");
        System.out.println("Are you a customer, employee, or manager?");
        
        while (true) {
            System.out.println("Please type one of the following: customer, employee, manager");
            String input = getScan().nextLine();
            
            if(input.equals("customer") || input.equals("employee") || input.equals("manager")) {
                if(input.equals("customer")) {
                    customerInterface();
                }
                
                if(input.equals("employee")) {
                    employeeInterface();
                }
                
                if(input.equals("manager")) {
                    managerInterface();
                }

                if(input.equals("quit")) {
                    System.out.println("Goodbye!");
                    break;
                }   
            }
            else {
                System.out.println("Error: You entered invalid input. Please try again.");
            }
        }
    }


////////////////////////////// INTERFACES //////////////////////////////
    /**
     * customerInterface: ask the customer for pickup and dropoff location, pickup and dropoff date, vehicle, extra ammenities, and customer info. 
    */
    void customerInterface() {
        String input = "";

        String start_lID;
        String end_lID;
        Date start_date;
        Date end_date;
        
        String vID;

        System.out.println("Welcome to Hurts Rent-a-Lemon: Rent a painfully bad, cheap car.");
        
        // Pickup Location
        System.out.println("Please choose a pickup location from the following list:");
        while (true) {
            printLocations();
            System.out.println("Print the corresponding pickup lID of your choice.");
            
            input = getScan().nextLine();
            if(inLocations(input)) {
                start_lID = input;
                break;
            }
            else {
                System.out.println("Error: You did not enter a valid lID. Please try again.");
            }
        }

        // Dropoff Location
        while (true) {
            System.out.println("Would you like to choose a different dropoff location? (yes/no) ");
            input = getScan().nextLine();

            if(input.equals("yes") || input.equals("no")) {

                if(input.equals("no")) {
                    end_lID = start_lID;
                }
                else {
                    System.out.println("Please choose a dropoff location from the following list:");
                    while (true) {
                        printLocations();
                        System.out.println("Print the corresponding dropoff lID of your choice: ");
                        input = getScan().nextLine();
                        
                        if(inLocations(input)) {
                            end_lID = input;
                            break;
                        }
                        else {
                            System.out.println("Error: You did not enter a valid lID. Please try again.");
                        }
                    }
                }

                break;
            }
            else {
                System.out.println("Error: Please enter yes or no.");
            }
        }

        // Pickup Date
        System.out.println("Please enter the date you wish to pickup your car. (YYYY-MM-DD)");
        SimpleDateFormat ft = new SimpleDateFormat("YYYY-MM-DD");
        start_date = getReservationDate(ft.format(new Date())); // get a start date that is >= the current date

        // Dropoff Date
        System.out.println("Please enter the date you wish to dropoff your car. (YYYY-MM-DD)");
        end_date = getReservationDate(start_date); // get an end date that is >= start date

        // Vehicle
        System.out.println("Please pick a vehicle from the following list:");
        while (true) {
            printVehicles(start_lID, start_date, end_date);
            System.out.println("Print the corresponding vID of your choice.");

            input = getScan().nextLine();
            if(inVehicles(input, start_lID, start_date, end_date)) {
                vID = input;
                break;
            }
            else {
                System.out.println("Error: You did not enter a valid vID. Please try again.");
            }
        }

        // Ammenities

        // Coupons

        // Customer Info



        // Create registration

        // Confirm
    }

    void employeeInterface() {

    }

    void managerInterface() {

    }



    
    /**
     * The main method for this project.
     */
    public static void main(String[] args) {
        whichInterface();
    }
}