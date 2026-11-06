import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    // the 5 things every transaction has
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // makes the time always show seconds like 11:15:00
    // without this java cuts off the seconds when they are 00
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    // constructor, sets up a new transaction
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // getters so other classes can see the values
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    // turns the transaction into one line for the csv file
    // date|time|description|vendor|amount
    public String toCsvLine() {
        return date + "|" + time.format(timeFormat) + "|" + description + "|" + vendor + "|" + String.format("%.2f", amount);
    }

    // this is what prints on the screen when we show the ledger
    @Override
    public String toString() {
        return date + " | " + time.format(timeFormat) + " | " + description + " | " + vendor + " | " + String.format("%.2f", amount);
    }
}