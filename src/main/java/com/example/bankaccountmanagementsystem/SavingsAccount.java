package com.example.bankaccountmanagementsystem;

public class SavingsAccount extends BankAccount {

    final double MINBALANCE = 50;

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
        super.accountType = "Savings Account";
    }

    @Override
    public boolean withdraw(double amount) {
//        if (amount <= 0) {
//            System.out.println("Amount to withdraw must be positive");
//            return false;
//        }
//        if (accountBalance - amount < MINBALANCE) {
//            System.out.println("Withdrawal denied. Insufficient balance.");
//            return false;
//        }
        accountBalance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }

    @Override
    public void deposit(double amount) {
//        if (amount <= 0) {
//            System.out.println("Amount to deposit must be positive");
//            return;
//        }
        accountBalance += amount;
        addTransaction("Deposit ", amount);
    }

    public void applyInterest(double rate) {
        double interest = accountBalance * rate / 100;
        accountBalance += interest;
        addTransaction("Interest", interest);
    }

    @Override
    protected String getAccountType() {
        return accountType;
    }

}
