import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class BankAppGUI extends JFrame {
    private JTextField nameField, accNumField, amountField;
    private JTextArea outputArea;
    private static final String DATA_FILE = "accounts.txt";

    public BankAppGUI() {
        setTitle("Banking System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 80, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(100, 20, 200, 25);
        add(nameField);

        JButton createBtn = new JButton("Create Account");
        createBtn.setBounds(320, 20, 150, 25);
        add(createBtn);

        JLabel accNumLabel = new JLabel("Acc No:");
        accNumLabel.setBounds(20, 60, 80, 25);
        add(accNumLabel);

        accNumField = new JTextField();
        accNumField.setBounds(100, 60, 200, 25);
        add(accNumField);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(20, 100, 80, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(100, 100, 200, 25);
        add(amountField);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(320, 60, 150, 25);
        add(depositBtn);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(320, 100, 150, 25);
        add(withdrawBtn);

        JButton balanceBtn = new JButton("Balance Inquiry");
        balanceBtn.setBounds(20, 140, 200, 25);
        add(balanceBtn);

        outputArea = new JTextArea();
        outputArea.setBounds(20, 180, 450, 150);
        add(outputArea);

        createBtn.addActionListener(e -> createAccount());
        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
        balanceBtn.addActionListener(e -> checkBalance());
    }

    private void createAccount() {
        String name = nameField.getText();
        String accNo = "AC" + new Random().nextInt(9999);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(accNo + "," + name + ",0\n");
            outputArea.setText("Account created! Acc No: " + accNo);
        } catch (IOException ex) {
            outputArea.setText("Error creating account.");
        }
    }

    private void deposit() {
        modifyBalance(true);
    }

    private void withdraw() {
        modifyBalance(false);
    }

    private void modifyBalance(boolean isDeposit) {
        String accNo = accNumField.getText();
        double amount = Double.parseDouble(amountField.getText());
        File inputFile = new File(DATA_FILE);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(accNo)) {
                    found = true;
                    double balance = Double.parseDouble(parts[2]);
                    balance = isDeposit ? balance + amount : balance - amount;
                    writer.write(parts[0] + "," + parts[1] + "," + balance + "\n");
                } else {
                    writer.write(line + "\n");
                }
            }

            if (found) {
                outputArea.setText((isDeposit ? "Deposited" : "Withdrawn") + " successfully.");
                inputFile.delete();
                tempFile.renameTo(inputFile);
            } else {
                outputArea.setText("Account not found.");
            }
        } catch (IOException e) {
            outputArea.setText("Transaction error.");
        }
    }

    private void checkBalance() {
        String accNo = accNumField.getText();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(accNo)) {
                    outputArea.setText("Account: " + parts[1] + "\nBalance: " + parts[2]);
                    return;
                }
            }
            outputArea.setText("Account not found.");
        } catch (IOException e) {
            outputArea.setText("Error reading account.");
        }
    }
}