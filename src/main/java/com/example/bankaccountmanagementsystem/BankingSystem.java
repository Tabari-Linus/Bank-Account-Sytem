package com.example.bankaccountmanagementsystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BankingSystem {
    static List<BankAccount> accounts = new ArrayList<>();

    public static String createAccount(String accountHolderName, double initialBalance, String accountType) {
        String accountNumber = null;
        BankAccount account;
try{
        if (accounts.isEmpty()) {
            accountNumber = "0000000001";
        } else {
            int lastAccountNumber = Integer.parseInt(accounts.get(accounts.size() - 1).accountNumber);
            accountNumber = String.format("%010d", lastAccountNumber + 1);
        }

        try {
            if (accountType.equalsIgnoreCase("savings")) {
                account = new SavingsAccount(accountNumber, accountHolderName, initialBalance);
                System.out.println("Savings account created for : " + accountHolderName + " with account number: " + accountNumber);
            } else if (accountType.equalsIgnoreCase("current")) {
                account = new CurrentAccount(accountNumber, accountHolderName, initialBalance);
                System.out.println("Current account created for : " + accountHolderName + " with account number: " + accountNumber);
            } else if (accountType.equalsIgnoreCase("fixed")) {
                account = new FixedDepositAccount(accountNumber, accountHolderName, initialBalance,
                        LocalDate.now().plusYears(1), 5.0);
                System.out.println("Fixed deposit account created for : " + accountHolderName + " with account number: " + accountNumber);
            } else {
                throw new RuntimeException("Invalid account type specified.");

            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating account: " + e.getMessage());
        }
        System.out.println("Account created successfully!");

        // Add the account to the list of accounts
        accounts.add(account);
            return "Account created successfully!\n" +
                    "Account Number: " + accountNumber + "\n" +
                    "Account Holder: " + accountHolderName + "\n" +
                    "Account Type: " + accountType;

        } catch (Exception e) {
            throw new RuntimeException("Error creating account: " + e.getMessage());
        }
    }

    public BankAccount getAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty.");
        }
        if (accountNumber.length() != 10) {
            throw new IllegalArgumentException("Account number must be 10 digits long.");
        }
        try {
            Long.parseLong(accountNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Account number must contain only digits.");
        }

        for (BankAccount account : accounts) {
            if (account.accountNumber.equals(accountNumber)) {
                return account;
            }
        }
        throw new IllegalArgumentException("Account not found.");
    }

    public String viewAccountDetails(String accountNumber) {
        BankAccount account = getAccount(accountNumber);
        StringBuilder details = new StringBuilder();
        details.append("Account Details:\n")
                .append("Account number: ").append(account.accountNumber).append("\n")
                .append("Account holder: ").append(account.accountHolderName).append("\n")
                .append("Account balance: ").append(account.getAccountBalance()).append("\n")
                .append("Account type: ").append(account.getAccountType());

        if (account instanceof FixedDepositAccount) {
            details.append("\nMaturity date: ").append(((FixedDepositAccount) account).getMaturityDate());
        }

        return details.toString();
    }

    public String deposit(String accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        account.deposit(amount);
        return "Deposit of " + amount + " successful.\nNew balance: " + account.getAccountBalance();
    }

    public String withdraw(String accountNumber, double amount) {
        BankAccount account = getAccount(accountNumber);
        if (account.withdraw(amount)) {
            return "Withdrawal of " + amount + " successful.\nNew balance: " + account.getAccountBalance();
        }
        return "Withdrawal failed.";
    }

    public String getTransactionHistory(String accountNumber, int numberOfTransactions) {
        BankAccount account = getAccount(accountNumber);
        // This would need to be modified to return String instead of printing
        // You might need to change your BankAccount class's displayTransactionHistory method
        account.displayTransactionHistory(numberOfTransactions);
        return "Transaction history displayed"; // Modify as needed
    }

    public String applyInterest(String accountNumber, Double rate) {
        BankAccount account = getAccount(accountNumber);
        if (account instanceof SavingsAccount) {
            if (rate == null) {
                throw new IllegalArgumentException("Interest rate is required for savings account");
            }
            ((SavingsAccount) account).applyInterest(rate);
            return "Interest applied to savings account.\nNew balance: " + account.getAccountBalance();
        } else if (account instanceof FixedDepositAccount) {
            ((FixedDepositAccount) account).applyInterest();
            return "Interest applied to fixed deposit account.\nNew balance: " + account.getAccountBalance();
        } else {
            throw new IllegalArgumentException("Interest can only be applied to savings and fixed deposit accounts");
        }
    }
}