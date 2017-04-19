
/**
 * This is the CarEnterprise class for the project. It contains interfaces
 * for different users and useful methods to create queries and return objects.
 */
public class CarEnterprise {

    private static ScannerSingleton scan;
    private static ConnSingleton conn;

    //////////////////////////// METHODS /////////////////////////////////////





    /////////////////////////// INTERFACES ///////////////////////////////////
    /**
     * Allows the user to specify whether they are a customer, employee, or manager
      */
    static void whichInterface() {
        System.out.println("Welcome to Hurts Rent-a-Lemon!");
        System.out.println("Are you a customer, employee, or manager?");

        while (true) {
            System.out.println("Please type one of the following: customer, employee, manager");
            String input = scan.getScan().nextLine();

            if (input.equals("customer") || input.equals("employee") || input.equals("manager")) {
                if (input.equals("customer")) {
                    customerInterface();
                }

                if (input.equals("employee")) {
                    employeeInterface();
                }

                if (input.equals("manager")) {
                    managerInterface();
                }

                if (input.equals("quit")) {
                    System.out.println("Goodbye!");
                    break;
                }
            } else {
                System.out.println("Error: You entered invalid input. Please try again.");
            }
        }
    }


    static void customerInterface() {

    }

    static void employeeInterface() {

    }

    static void managerInterface() {

    }



    /////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        whichInterface();
    }
}
