# Ledger CLI App

Hey its Mohamed Hassan and this is my Capstone 1 project. It's a command line app that keeps track of money coming in and going out. You can add deposits and payments, look at all your transactions, and run a few reports. Everything gets saved in a file called `transactions.csv`, so it's still there the next time you open the app.

## What it can do

**Home screen**
- D) Add Deposit: type in a description, vendor, and amount and it saves it
- P) Make Payment (Debit): same thing but the amount is saved as a negative
- L) Ledger: goes to the ledger screen
- X) Exit: closes the app (it keeps running until you pick this)

**Ledger screen** (newest transactions show first)
- A) All: shows every transaction
- D) Deposits: only the money that came in
- P) Payments: only the money that went out
- R) Reports: goes to the reports screen
- H) Home: goes back to the home screen

**Reports screen**
- 1 - Month To Date
- 2 - Previous Month
- 3 - Year To Date
- 4 - Previous Year
- 4 - Search by Vendor: type a vendor name and it shows all the transactions for that vendor
- 0 - Back: goes back to the ledger screen

If you type letters instead of a number for the amount, it says so and doesn't save anything.

## How the transactions are saved

Each transaction is one line in `transactions.csv` and the parts are split up with a `|`:

```
date|time|description|vendor|amount
2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
2023-04-15|11:15:00|Invoice 1001 paid|Joe|1500.00
```

## How to run it

1. Make sure you have Java installed. I used Java 21.
2. Clone the repo:
   ```
   git clone https://github.com/basicallyatwork/ledger-cli-app.git
   ```
3. Open the `ledger-cli-app` folder in IntelliJ.
4. Open `src/Main.java` and click the green run arrow next to `main`.
5. Click inside the Run window at the bottom and use the menu by typing a letter or number and pressing Enter.

Run it from the project folder (IntelliJ does this on its own) so the app can find `transactions.csv`.

## Files

- `src/Main.java`: the menus, the reports, and the code that reads and saves the file
- `src/Transaction.java`: the Transaction class (date, time, description, vendor, amount)
- `transactions.csv`: where the transactions are saved

## Code I want to show

Finding last month is kinda annoying in January, because the month before January is December of the year before. This part handles it:

```java
int month = today.getMonthValue() - 1;
int year = today.getYear();

//if this month is january, last month is december of last year
if (month == 0) {
    month = 12;
    year = year - 1;
}
```
