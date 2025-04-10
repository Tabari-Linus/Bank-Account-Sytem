package com.example.bankaccountmanagementsystem;

public class CurrentAccount extends BankAccount {

    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
        this.overdraftLimit = 500;
        super.accountType = "Current Account";
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }
        if (accountBalance - amount < -overdraftLimit) {
            System.out.println("Withdrawal denied. Amount to withdraw exceeds Overdraft limit.");
            return false;
        }
        accountBalance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount to deposit must be positive");
            return;
        }
        accountBalance += amount;
        addTransaction("Deposit ", amount);
    }

    public void setOverdraftLimit(double newLimit) {
        this.overdraftLimit = newLimit;
    }

}
