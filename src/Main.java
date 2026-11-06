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
                showLedger(scanner);
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

    //ledger screen, stays here until the user picks H for home
    public static void showLedger(Scanner scanner) {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println();
            System.out.println("===== LEDGER =====");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            //load the file every time so new entries always show up
            ArrayList<Transaction> transactions = loadTransactions();

            if (choice.equals("A")) {
                displayTransactions(transactions);
            } else if (choice.equals("D")) {
                displayTransactions(getDeposits(transactions));
            } else if (choice.equals("P")) {
                displayTransactions(getPayments(transactions));
            } else if (choice.equals("R")) {
                showReports(scanner);
            } else if (choice.equals("H")) {
                inLedger = false;
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
    }

    //reports screen, stays here until the user picks 0 to go back to the ledger
    public static void showReports(Scanner scanner) {
        boolean inReports = true;

        while (inReports) {
            System.out.println();
            System.out.println("===== REPORTS =====");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            //load the file every time so new entries always show up
            ArrayList<Transaction> transactions = loadTransactions();

            if (choice.equals("1")) {
                displayTransactions(getMonthToDate(transactions));
            } else if (choice.equals("2")) {
                displayTransactions(getPreviousMonth(transactions));
            } else if (choice.equals("3")) {
                displayTransactions(getYearToDate(transactions));
            } else if (choice.equals("4")) {
                displayTransactions(getPreviousYear(transactions));
            } else if (choice.equals("5")) {
                System.out.print("Enter the vendor name: ");
                String vendor = scanner.nextLine().trim();
                displayTransactions(searchByVendor(transactions, vendor));
            } else if (choice.equals("0")) {
                inReports = false;
            } else {
                System.out.println("Invalid option, try again.");
            }
        }
    }

    //prints a list of transactions, one method so we dont repeat the code
    public static void displayTransactions(ArrayList<Transaction> transactions) {
        System.out.println();

        if (transactions.size() == 0) {
            System.out.println("No transactions found.");
        }

        //newest transactions are at the end of the file
        //we loop backwards to show newest first
        for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(transactions.get(i));
        }
    }

    //returns only the transactions with a positive amount
    public static ArrayList<Transaction> getDeposits(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> deposits = new ArrayList<Transaction>();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getAmount() > 0) {
                deposits.add(t);
            }
        }

        return deposits;
    }

    //returns only the transactions with a negative amount
    public static ArrayList<Transaction> getPayments(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> payments = new ArrayList<Transaction>();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getAmount() < 0) {
                payments.add(t);
            }
        }

        return payments;
    }

    //returns the transactions from this month and this year
    public static ArrayList<Transaction> getMonthToDate(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> results = new ArrayList<Transaction>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == today.getYear() && t.getDate().getMonthValue() == today.getMonthValue()) {
                results.add(t);
            }
        }

        return results;
    }

    //returns the transactions from last month
    public static ArrayList<Transaction> getPreviousMonth(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> results = new ArrayList<Transaction>();
        LocalDate today = LocalDate.now();

        int month = today.getMonthValue() - 1;
        int year = today.getYear();

        //if this month is january, last month is december of last year
        if (month == 0) {
            month = 12;
            year = year - 1;
        }

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == year && t.getDate().getMonthValue() == month) {
                results.add(t);
            }
        }

        return results;
    }

    //returns the transactions from this year
    public static ArrayList<Transaction> getYearToDate(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> results = new ArrayList<Transaction>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == today.getYear()) {
                results.add(t);
            }
        }

        return results;
    }

    //returns the transactions from last year
    public static ArrayList<Transaction> getPreviousYear(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> results = new ArrayList<Transaction>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getDate().getYear() == today.getYear() - 1) {
                results.add(t);
            }
        }

        return results;
    }

    //returns the transactions that match the vendor name, upper or lower case doesnt matter
    public static ArrayList<Transaction> searchByVendor(ArrayList<Transaction> transactions, String vendor) {
        ArrayList<Transaction> results = new ArrayList<Transaction>();

        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);

            if (t.getVendor().equalsIgnoreCase(vendor)) {
                results.add(t);
            }
        }

        return results;
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