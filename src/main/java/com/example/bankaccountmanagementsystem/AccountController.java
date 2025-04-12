package com.example.bankaccountmanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.example.bankaccountmanagementsystem.BankAccount.getAccount;

public class AccountController {

    @FXML
    private ComboBox<String> accountTypeComboBox;
    @FXML
    private TextField accountNumberField;
    @FXML
    private TextField accountHolderField;
    @FXML
    private TextField initialBalanceField;
    @FXML
    private TextField maturityDateField;
    @FXML
    private TextField interestRateField;
    @FXML
    private TextField transactionAmountField;

    @FXML
    private TextField nTransactions;


    @FXML
    private TextArea transactionHistoryArea;

    private BankAccount currentAccount;
    static List<BankAccount> accounts = new ArrayList<>();

    @FXML
    private void initialize() {
        accountTypeComboBox.getItems().addAll("Savings", "Current", "Fixed Deposit");
        accountTypeComboBox.getSelectionModel().selectFirst();

        // Dynamically show/hide fields based on account type selection
        accountTypeComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("Fixed Deposit")) {
                    maturityDateField.setVisible(true);
                    interestRateField.setVisible(true);
                } else {
                    maturityDateField.setVisible(false);
                    interestRateField.setVisible(false);
                }
            }
        });
    }

    @FXML
    private void handleCreateAccount() {
        String accountType = accountTypeComboBox.getValue();
        String accountHolder = accountHolderField.getText();
        double initialBalance = 0;
        String accountNumber;

        // Validation: Ensure Account Holder is not empty
        if (accountHolder.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Account holder name cannot be empty.");
            return;
        }

        // Validation: Ensure Initial Balance is a valid positive number
        try {
            initialBalance = Double.parseDouble(initialBalanceField.getText());
            if (initialBalance <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Initial balance must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid initial balance.");
            return;
        }

        // Generate account number
        if (accounts.isEmpty()) {
            accountNumber = "0000000001";
        } else {
            int lastAccountNumber = Integer.parseInt(((BankAccount) accounts.get(accounts.size() - 1)).accountNumber);
            accountNumber = String.format("%010d", lastAccountNumber + 1);
        }

        // Account creation logic
        switch (accountType) {
            case "Savings":
                currentAccount = new SavingsAccount(accountNumber, accountHolder, initialBalance);
                break;
            case "Current":
                currentAccount = new CurrentAccount(accountNumber, accountHolder, initialBalance);
                break;
            case "Fixed Deposit":
                // Validation: Ensure Maturity Date is valid
                LocalDate maturityDate = null;
                try {
                    maturityDate = LocalDate.parse(maturityDateField.getText());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid maturity date (YYYY-MM-DD).");
                    return;
                }

                // Validation: Ensure Interest Rate is a valid positive number
                double interestRate = 0;
                try {
                    interestRate = Double.parseDouble(interestRateField.getText());
                    if (interestRate <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Interest rate must be greater than zero.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid interest rate.");
                    return;
                }

                currentAccount = new FixedDepositAccount(accountNumber, accountHolder, initialBalance, maturityDate, interestRate);
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a valid account type.");
                return;
        }

        // Display account creation confirmation
        String accountInfo = "Account Created Successfully\n\n" +
                "Account number: " + currentAccount.accountNumber
                + "\nAccount holder: " + currentAccount.accountHolderName
                + "\nAccount balance: " + currentAccount.getBalance()
                + "\nAccount type: " + currentAccount.getAccountType();
        transactionHistoryArea.setText(accountInfo);

        // Add the account to the list of accounts
        accounts.add(currentAccount);
    }

    @FXML
    private void handleDeposit() {
        String accountNum;
        double amount;

        // Validation: Ensure Amount is a valid positive number
        try {
            accountNum = String.valueOf(accountNumberField.getText());
            if (accountNum.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Account Number Cannot be Empty.");
                return;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid deposit amount.");
            return;
        }

        // Validation: Ensure Amount is a valid positive number
        try {
            amount = Double.parseDouble(transactionAmountField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Deposit amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid deposit amount.");
            return;
        }
        currentAccount = getAccount(accountNum);

        if (currentAccount != null) {
            currentAccount.deposit(amount);
            transactionHistoryArea.setText("Balance: GHC" + currentAccount.getBalance());
            updateTransactionHistory("Deposit: GHC" + amount);
        }
    }

    @FXML
    private void handleWithdraw() {
        String accountNum;
        double amount;

        // Validation: Ensure Amount is a valid positive number
        try {
            accountNum = String.valueOf(accountNumberField.getText());
            if (accountNum.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Account Number Cannot be Empty.");
                return;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid withdraw amount.");
            return;
        }

        try {
            amount = Double.parseDouble(transactionAmountField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Withdraw amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid withdraw amount.");
            return;
        }
        currentAccount = getAccount(accountNum);
        if (currentAccount instanceof FixedDepositAccount){
            LocalDate maturityDate = ((FixedDepositAccount) currentAccount).getMaturityDate();
            if (LocalDate.now().isBefore(maturityDate)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Withdrawal denied. Account not matured for withdrawal. Locked until " + maturityDate);
                return ;
            }
        }

        if (currentAccount != null && currentAccount.withdraw(amount)) {
            transactionHistoryArea.setText("Balance: GHC" + currentAccount.getAccountBalance());
            updateTransactionHistory("Withdrawal: GHC" + amount);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Insufficient balance or invalid operation.");
        }
    }

    @FXML
    private void handleShowBalance() {
        String accountNum;

        // Validation: Ensure Amount is a valid positive number
        try {
            accountNum = String.valueOf(accountNumberField.getText());
            if (accountNum.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Account Number Cannot be Empty. to show transaction");
                return;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid account number.");
            return;
        }
        currentAccount = getAccount(accountNum);
        if (currentAccount != null) {
            transactionHistoryArea.setText("Balance: GHC" + currentAccount.getAccountBalance());
        }
    }

    @FXML
    private void handleShowTransactions() {
        int n = 0;


        // Validation: Ensure Amount is a valid positive number
        try {
            n = Integer.parseInt(nTransactions.getText());
            if (n <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Number of transactions should greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid Integer.");
            return;
        }

        String accountNum;

        // Validation: Ensure Amount is a valid positive number
        try {
            accountNum = String.valueOf(accountNumberField.getText());
            if (accountNum.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Account Number Cannot be Empty. to show transaction");
                return;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid account number.");
            return;
        }
        currentAccount = getAccount(accountNum);
        if (currentAccount != null) {
            StringBuilder history = new StringBuilder();
            for (String transaction : currentAccount.getTransaction(n)) {
                history.append(transaction).append("\n");
            }
            transactionHistoryArea.setText(history.toString());
        }
    }

    private void updateTransactionHistory(String transaction) {

    }

//    private void refreshTransactionTable() {
//        accounts
//        TransactionHistorylist current = currentAccount.transactionHead;
//        while (current != null) {
//            transactions.add(current);
//            current = current.next;
//        }
//        transactionTable.setItems(transactions);
//    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
