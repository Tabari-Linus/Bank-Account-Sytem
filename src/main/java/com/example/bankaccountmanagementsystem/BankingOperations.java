package com.example.bankaccountmanagementsystem;

// Creating an interface for banking activities
interface BankingOperations {

    void deposit(double amount);

    boolean withdraw(double amount);

    double getAccountBalance();

    void addTransaction(String type, double amount);

    void displayTransactionHistory(int n);

    String[] getTransactionHistory();
}

