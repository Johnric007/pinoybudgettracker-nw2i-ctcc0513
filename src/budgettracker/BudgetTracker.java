package budgettracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BudgetTracker {
    private static double budget = 0.0;
    private static double balance = 0.0;
    private static Map<String, ArrayList<String>> monthTransactions = new HashMap<>();
    private static Map<String, ArrayList<Double>> monthAmounts = new HashMap<>();
    private static Map<String, Double> monthBalances = new HashMap<>();
    private static Map<String, Double> monthBudgets = new HashMap<>();
    private static JPanel transactionsPanel;
    private static JLabel balanceLabel;
    private static JTextField budgetText;
    private static JTextField descriptionText;
    private static JTextField amountText;
    private static JButton setBudgetButton;
    private static JButton addButton;
    private static JButton saveButton;
    private static JButton restartButton;
    private static JButton showRecordsButton;
    private static JPanel recordsPanel;
    private static JComboBox<String> monthComboBox;
    private static String selectedMonth = "";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pinoy Budget Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Month picker
        JLabel monthLabel = new JLabel("Select Month:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(monthLabel, gbc);

        // Combo box for month selection
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(monthComboBox, gbc);

        // Budget input section
        JLabel budgetLabel = new JLabel("Monthly Budget:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(budgetLabel, gbc);

        budgetText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(budgetText, gbc);

        setBudgetButton = new JButton("Set Budget");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(setBudgetButton, gbc);

        // Transaction description input
        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(descriptionLabel, gbc);

        descriptionText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(descriptionText, gbc);

        // Transaction amount input
        JLabel amountLabel = new JLabel("Amount:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(amountLabel, gbc);

        amountText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(amountText, gbc);

        // Add and save buttons
        addButton = new JButton("Add Transaction");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        balanceLabel = new JLabel("Balance: ₱0.0");
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(balanceLabel, gbc);

        restartButton = new JButton("Restart Budget");
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(restartButton, gbc);

        showRecordsButton = new JButton("Show Records");
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(showRecordsButton, gbc);

        // Bottom panel to hold transactions and the save button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Transactions panel
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(transactionsPanel);
        scrollPane.setPreferredSize(new Dimension(350, 300));
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        // Save button placed in the bottom panel
        saveButton = new JButton("Save Transaction");
        bottomPanel.add(saveButton, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.CENTER);

        // Records panel for transaction history
        recordsPanel = new JPanel();
        recordsPanel.setLayout(new BoxLayout(recordsPanel, BoxLayout.Y_AXIS));
        JScrollPane recordsScrollPane = new JScrollPane(recordsPanel);
        recordsScrollPane.setPreferredSize(new Dimension(350, 200));

        // Action Listeners
        setBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    selectedMonth = monthComboBox.getSelectedItem().toString();
                    budget = Double.parseDouble(budgetText.getText());
                    balance = budget;
                    monthBudgets.put(selectedMonth, budget);
                    monthBalances.put(selectedMonth, balance);
                    balanceLabel.setText("Balance: ₱" + balance);
                    budgetText.setEditable(false);  // Disable editing of budget once set
                    setBudgetButton.setEnabled(false);
                    monthComboBox.setEnabled(false);  // Disable month selection once budget is set
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the budget.");
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String description = descriptionText.getText();
                    double amount = Double.parseDouble(amountText.getText());
                    if (amount > balance) {
                        JOptionPane.showMessageDialog(frame, "Not enough budget to cover this transaction.");
                    } else {
                        balance -= amount;
                        monthBalances.put(selectedMonth, balance);  // Update balance for the selected month
                        balanceLabel.setText("Balance: ₱" + balance);
                        descriptionText.setText("");
                        amountText.setText("");
                        // Add the transaction to the transactions panel
                        JLabel transactionLabel = new JLabel(description + ": ₱" + amount);
                        transactionsPanel.add(transactionLabel);
                        transactionsPanel.revalidate();
                        transactionsPanel.repaint();
                        // Store the transaction temporarily for the selected month
                        monthTransactions.computeIfAbsent(selectedMonth, k -> new ArrayList<>()).add(description + ": ₱" + amount);
                        monthAmounts.computeIfAbsent(selectedMonth, k -> new ArrayList<>()).add(amount);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the amount.");
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the transactions for the selected month
                StringBuilder record = new StringBuilder();
                double totalSpent = 0.0;
                for (int i = 0; i < monthTransactions.get(selectedMonth).size(); i++) {
                    String transaction = monthTransactions.get(selectedMonth).get(i);
                    double amount = monthAmounts.get(selectedMonth).get(i);
                    record.append(transaction).append("\n");
                    totalSpent += amount;
                }

                double remainingBalance = monthBalances.get(selectedMonth);
                record.append("Total Spent: ₱").append(totalSpent).append("\n");
                record.append("Remaining Balance: ₱").append(remainingBalance).append("\n");

                // Add the record to the records panel
                recordsPanel.add(new JLabel("<html><b>Month: " + selectedMonth + "</b></html>"));
                recordsPanel.add(new JLabel(record.toString()));
                recordsPanel.add(new JLabel("<hr>"));
                recordsPanel.revalidate();
                recordsPanel.repaint();

                // Clear the transactions panel and reset for the new month
                transactionsPanel.removeAll();
                transactionsPanel.revalidate();
                transactionsPanel.repaint();
                JOptionPane.showMessageDialog(frame, "Transactions saved for " + selectedMonth + ".");
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset only the current month's budget and transactions
                budgetText.setEditable(true);
                balanceLabel.setText("Balance: ₱0.0");
                transactionsPanel.removeAll();
                transactionsPanel.revalidate();
                transactionsPanel.repaint();
                // Do not clear records, only reset current budget
                monthTransactions.clear();
                monthAmounts.clear();
                monthBalances.clear();
                monthBudgets.clear();
                monthComboBox.setEnabled(true);
                setBudgetButton.setEnabled(true);
                monthComboBox.setSelectedIndex(0);
                JOptionPane.showMessageDialog(frame, "Current budget has been reset.");
            }
        });

        showRecordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, new JScrollPane(recordsPanel), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setVisible(true);
    }
}
