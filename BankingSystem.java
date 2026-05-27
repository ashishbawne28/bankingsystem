public class BankingSystem {
    public static void main(String[] args) {

        // 1. Test Saving Account
        System.out.println("--- Testing Saving Account ---");
        SavingAccount sa = new SavingAccount("Ashish", 1000.0, 0.05); // Changed balance to double
        System.out.println("Initial Balance: " + sa.getBalance());

        sa.applyInterest();
        System.out.println("Final Balance: " + sa.getBalance()); // Expected: 1050.0

        System.out.println(); // Space

        // 2. Test Checking Account
        System.out.println("--- Testing Checking Account ---");
        BankAccount ac = new CheckingAccount("Audi", 500.0, 2.0); // Changed balance to double
        System.out.println("Initial Balance: " + ac.getBalance());

        ac.withdraw(100.0); // Withdraws 100 + 2 fee.
        System.out.println("Final Balance: " + ac.getBalance()); // Expected: 398.0
    }
}

class BankAccount {
    private String accountHolder;
    private double balance;

    public BankAccount(String accountHolder, double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    // Fixed spelling: deposit
    public void deposit(double amount) {
        if (amount > 0) {
            balance = balance + amount;
            System.out.println(accountHolder + " deposited: $" + amount);
        } else {
            System.out.println("Amount should be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount >= 0 && amount <= balance) {
            balance = balance - amount;
            System.out.println(accountHolder + " successfully withdrew: $" + amount);
        } else {
            // Fixed the misleading error message
            System.out.println("Error: Insufficient funds or invalid amount for " + accountHolder);
        }
    }

    // Abstract placeholder removed since it's not strictly needed here,
    // but kept empty if you want dynamic dispatch from parent references.
    public void applyInterest() {
        // Base accounts don't have interest
    }

    // Getters and Setters
    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

class SavingAccount extends BankAccount {
    private double interestRate;

    // Fixed: changed balance parameter type from int to double
    public SavingAccount(String accountHolder, double balance, double interestRate) {
        super(accountHolder, balance);
        this.interestRate = interestRate;
    }

    @Override
    public void applyInterest() {
        double interest = interestRate * getBalance();
        deposit(interest); // Using corrected method name
        System.out.println("Interest added: $" + interest);
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}

class CheckingAccount extends BankAccount {
    private double transactionfee;

    // Fixed: changed balance parameter type from int to double
    public CheckingAccount(String accountHolder, double balance, double transactionfee) {
        super(accountHolder, balance);
        this.transactionfee = transactionfee;
    }

    @Override
    public void withdraw(double amount) {
        double totalAmt = amount + transactionfee;

        // Check against totalAmt instead of just amount
        if (totalAmt <= getBalance()) {
            // CRITICAL FIX: Pass totalAmt to the parent class so the fee is actually deducted!
            super.withdraw(totalAmt);
            System.out.println("Transaction fee applied: $" + transactionfee);
        } else {
            System.out.println("Error: Insufficient funds to cover amount and fee for " + getAccountHolder());
        }
    }
}