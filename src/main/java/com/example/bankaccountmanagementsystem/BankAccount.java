package com.example.bankaccountmanagementsystem;

import java.util.List;

import static com.example.bankaccountmanagementsystem.AccountController.accounts;

public abstract class BankAccount implements BankingOperations {

    protected String accountNumber;
    protected String accountHolderName;
    protected double accountBalance;
    protected TransactionHistoryList transactionHistory;
    protected String accountType = "Bank Account";

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountBalance = initialBalance;
        this.transactionHistory = new TransactionHistoryList();
    }

    @Override
    public abstract void deposit(double amount);

    @Override
    public abstract boolean withdraw(double amount);

    @Override
    public double getAccountBalance() {
        return accountBalance;
    }

    @Override
    public void addTransaction(String type, double amount) {
        String transactionString = String.format("Account holder: %s with account number %s made a %s of: %.2f",
                accountHolderName, accountNumber, type, amount);
        transactionHistory.add(transactionString);
    }

    @Override
    public void displayTransactionHistory(int n) {
        List<String> lastN = transactionHistory.getLastN(n);
        lastN.forEach(System.out::println);
    }

//    public String[] getTransactionHistory(){
//        List<String> lastN = transactionHistory.getTransaction(this.accountNumber);
//        return null;
//    }

    protected String getAccountType() {
        return accountType;
    }


    public double getBalance() {
        return accountBalance;
    }

    public List<String> getTransaction(int n) {
        List<String> lastN = transactionHistory.getLastN(n);
        return  lastN;
    }

    public static BankAccount getAccount(String accountNumber) {
//        try {
//
//            if (accountNumber.isEmpty()) {
//                System.out.println("Account number cannot be empty.");
//                return null;
//            }
//            if (accountNumber.length() != 10) {
//                System.out.println("Account number must be 10 digits long.");
//                return null;
//            }
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//            return null;
//        }
        // Search for the account in the list of accounts
        for (BankAccount account : accounts) {
            if (account.accountNumber.equals(accountNumber)) {
                return account;

            }
        }
        System.out.println("Account not found.");
        return null;
    }
}