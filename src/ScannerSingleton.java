/**
 * Created by Dana on 4/19/2017.
 */

import java.util.Scanner;

/**
 * Uses the singleton pattern to create a global scanner.
 */
public class ScannerSingleton {
    private static Scanner scan = null;

    /**
     * getScan: Gets the scanner
     * @return Scanner - returns the global scanner
     */
    static Scanner getScan() {
        if (scan == null)
            initScan();
        return scan;
    }

    /**
     * initScan: used establish the scanner (System.in)
     */
    private static void initScan() {
        scan = new Scanner(System.in);
    }
}