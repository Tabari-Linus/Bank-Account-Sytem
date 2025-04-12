package com.example.bankaccountmanagementsystem;

import java.time.LocalDate;

public class FixedDepositAccount extends BankAccount {

    private LocalDate maturityDate;
    private double interestRate;

    boolean interestApplied = false;

    public FixedDepositAccount(String accountNumber, String accountHolderName,
                               double initialBalance, LocalDate maturityDate, double interestRate) {
        super(accountNumber, accountHolderName, initialBalance);
        this.maturityDate = maturityDate;
        this.interestRate = interestRate;
        super.accountType = "Fixed Deposit Account";
    }

    @Override
    public boolean withdraw(double amount) {
//        if (LocalDate.now().isBefore(maturityDate)) {
//            System.out.println("Withdrawal denied. Account not matured for withdrawal. Locked until " + maturityDate);
//
//            return false;
//        } else if (amount <= 0) {
//            System.out.println("Amount should not be less than zero.");
//            return false;
//        } else if (amount > accountBalance) {
//            System.out.println("Insufficient funds in the account.");
//            return false;
//        }

        if (!interestApplied) {
            applyInterest();
            interestApplied = true;
        }
        accountBalance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }

    public void applyInterest() {
        if (LocalDate.now().isAfter(maturityDate)) {
            double interest = accountBalance * interestRate / 100;
            accountBalance += interest;
            addTransaction("Interest", interest);
        }
    }

    @Override
    public void deposit(double amount) {
//        if (amount <= 0) {
//            System.out.println("Amount to deposit must be positive");
//            return;
//        }
        accountBalance += amount;
        addTransaction("Deposit", amount);
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }
}
