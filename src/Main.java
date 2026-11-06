import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //app keeps running until the user picks X
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== HOME SCREEN =====");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");

            //trim and uppercase so all types of d work
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("D")) {
                addDeposit(scanner);
            } else if (choice.equals("P")) {
                makePayment(scanner);
            } else if (choice.equals("L")) {
                showLedger();
            } else if (choice.equals("X")) {
                System.out.println("Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid option, try again.");
            }
        }

        scanner.close();
    }

    //asks the user for deposit info and saves it to the csv
    public static void addDeposit(Scanner scanner) {
        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");

        try {
            double amount = Double.parseDouble(scanner.nextLine());

            //deposit should always be pos+
            if (amount < 0) {
                amount = amount * -1;
            }

            Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
            saveTransaction(transaction);
            System.out.println("Deposit saved!");
        }
        catch (Exception e) {
            System.out.println("That amount was not a number. Nothing was saved.");
        }
    }

    //asks the user for payment info and saves it to the csv
    public static void makePayment(Scanner scanner) {
        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Amount: ");

        try {
            double amount = Double.parseDouble(scanner.nextLine());

            //payment should always be neg-
            if (amount > 0) {
                amount = amount * -1;
            }

            Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
            saveTransaction(transaction);
            System.out.println("Payment saved!");
        }
        catch (Exception e) {
            System.out.println("That amount was not a number. Nothing was saved.");
        }
    }

    // temporary ledger, just shows everything for now
    public static void showLedger() {
        ArrayList<Transaction> transactions = loadTransactions();

        System.out.println();
        System.out.println("===== LEDGER =====");

        //newest transactions are at the end of the file
        //we loop backwards to show newest first
        for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(transactions.get(i));
        }
    }

    //reads transactions.csv and returns a list of Transaction objects
    public static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("transactions.csv"));
            String input;

            //read the header line first so we skip it
            bufReader.readLine();

            //read until there are no more lines
            while ((input = bufReader.readLine()) != null) {
                String[] tokens = input.split("\\|");

                LocalDate date = LocalDate.parse(tokens[0]);
                LocalTime time = LocalTime.parse(tokens[1]);
                String description = tokens[2];
                String vendor = tokens[3];
                double amount = Double.parseDouble(tokens[4]);

                transactions.add(new Transaction(date, time, description, vendor, amount));
            }

            bufReader.close();
        }
        catch (Exception e) {
            System.out.println("Error reading transactions.csv");
            e.printStackTrace();
        }

        return transactions;
    }

    //adds one transaction to the bottom of transactions.csv
    public static void saveTransaction(Transaction transaction) {
        try {
            //true means add to the file instead of overwriting it
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufWriter = new BufferedWriter(fileWriter);

            bufWriter.write(transaction.toCsvLine() + "\n");

            bufWriter.close();
        }
        catch (Exception e) {
            System.out.println("Error saving the transaction");
            e.printStackTrace();
        }
    }
}