import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.io.Serializable;

public class ExpenseTrackerApp {
    private List<Expense> expenses = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> expenseList = new JList<>(listModel);
    private Map<String, Double> categoryBudgets = new HashMap<>();

    private JTextField expenseNameField;
    private JTextField expenseAmountField;
    private JComboBox<String> categoryComboBox;
    private JTextField tagField;
    private JTextField budgetField;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton setBudgetButton;
    private JLabel budgetStatusLabel;

    public ExpenseTrackerApp() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Colors
        Color backgroundColor = new Color(220, 220, 220);
        Color buttonColor = new Color(60, 179, 113);
        Color textColor = Color.black;

        // Create and set up the expense input fields
        expenseNameField = new JTextField(20);
        expenseAmountField = new JTextField(10);

        // Create and set up category and tag fields
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("Select Category");
        categoryComboBox.addItem("Food");
        categoryComboBox.addItem("Transportation");
        categoryComboBox.addItem("Entertainment");
        categoryComboBox.addItem("Other");

        tagField = new JTextField(10);

        // Create and set up buttons
        addButton = new JButton("Add Expense");
        addButton.setBackground(buttonColor);
        addButton.setForeground(Color.black);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = expenseNameField.getText();
                double amount = Double.parseDouble(expenseAmountField.getText());
                String category = (String) categoryComboBox.getSelectedItem();
                String tags = tagField.getText();

                List<String> tagList = Arrays.asList(tags.split(","));

                Expense expense = new Expense(name, amount, new Date(), category, tagList);
                expenses.add(expense);
                listModel.addElement(expense.toString());

                expenseNameField.setText("");
                expenseAmountField.setText("");
                categoryComboBox.setSelectedIndex(0);
                tagField.setText("");
            }
        });

        editButton = new JButton("Edit Expense");
        editButton.setBackground(buttonColor);
        editButton.setForeground(Color.black);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = expenseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String newName = expenseNameField.getText();
                    double newAmount = Double.parseDouble(expenseAmountField.getText());
                    String newCategory = (String) categoryComboBox.getSelectedItem();
                    String newTags = tagField.getText();
                    List<String> newTagList = Arrays.asList(newTags.split(","));

                    Expense editedExpense = new Expense(newName, newAmount, new Date(), newCategory, newTagList);
                    expenses.set(selectedIndex, editedExpense);
                    listModel.set(selectedIndex, editedExpense.toString());
                }
            }
        });

        removeButton = new JButton("Remove Expense");
        removeButton.setBackground(buttonColor);
        removeButton.setForeground(Color.black);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = expenseList.getSelectedIndex();
                if (selectedIndex != -1) {
                    expenses.remove(selectedIndex);
                    listModel.remove(selectedIndex);
                }
            }
        });

        // Create and set up budget fields and components
        budgetField = new JTextField(10);
        setBudgetButton = new JButton("Set Budget");
        setBudgetButton.setBackground(buttonColor);
        setBudgetButton.setForeground(Color.black);
        budgetStatusLabel = new JLabel("Budget Status: No budget set");
        budgetStatusLabel.setForeground(textColor);

        // Set up action listener for setting budgets
        setBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                String budgetText = budgetField.getText();
                try {
                    double budgetAmount = Double.parseDouble(budgetText);
                    categoryBudgets.put(selectedCategory, budgetAmount);
                    budgetStatusLabel.setText("Budget Status: " + selectedCategory + " budget set to $" + budgetAmount);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Create and set up buttons for showing expenses and budget
        JButton showExpensesButton = new JButton("Show Expenses");
        showExpensesButton.setBackground(buttonColor);
        showExpensesButton.setForeground(Color.black);
        showExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExpenses();
            }
        });

        JButton showBudgetButton = new JButton("Show Budget");
        showBudgetButton.setBackground(buttonColor);
        showBudgetButton.setForeground(Color.black);
        showBudgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBudget();
            }
        });

        // Create a panel for the input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.setBackground(backgroundColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(expenseNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(expenseAmountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Tags (comma-separated):"), gbc);
        gbc.gridx = 1;
        inputPanel.add(tagField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(addButton, gbc);
        gbc.gridx = 1;
        inputPanel.add(editButton, gbc);

        // Create a panel for the list
        JPanel listPanel = new JPanel();
        listPanel.add(new JScrollPane(expenseList));

        // Create a panel for budget fields and components
        JPanel budgetPanel = new JPanel();
        budgetPanel.setLayout(new GridBagLayout());
        budgetPanel.setBackground(backgroundColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        budgetPanel.add(new JLabel("Set Budget for Category:"), gbc);
        gbc.gridx = 1;
        budgetPanel.add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        budgetPanel.add(new JLabel("Budget Amount: $"), gbc);
        gbc.gridx = 1;
        budgetPanel.add(budgetField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        budgetPanel.add(setBudgetButton, gbc);
        gbc.gridx = 1;
        budgetPanel.add(budgetStatusLabel, gbc);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(removeButton);
        buttonPanel.add(showExpensesButton);
        buttonPanel.add(showBudgetButton);
        buttonPanel.setBackground(backgroundColor);

        // Add the input, list, budget, and button panels to the frame
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(listPanel, BorderLayout.CENTER);
        frame.getContentPane().add(budgetPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Method to display expenses
    private void showExpenses() {
        JFrame expensesFrame = new JFrame("Expenses");
        expensesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        expensesFrame.setSize(400, 300);

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        for (Expense expense : expenses) {
            textArea.append(expense.toString() + "\n");
        }

        expensesFrame.getContentPane().add(scrollPane);
        expensesFrame.setVisible(true);
    }

    // Method to display budget
    private void showBudget() {
        JFrame budgetFrame = new JFrame("Budget");
        budgetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        budgetFrame.setSize(300, 100);

        JTextArea textArea = new JTextArea(3, 30);
        textArea.setEditable(false);

        for (Map.Entry<String, Double> entry : categoryBudgets.entrySet()) {
            String category = entry.getKey();
            Double budgetAmount = entry.getValue();
            textArea.append(category + " Budget: $" + budgetAmount + "\n");
        }

        budgetFrame.getContentPane().add(textArea);
        budgetFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseTrackerApp();
            }
        });
    }
}

class Expense implements Serializable {
    private String name;
    private double amount;
    private Date date;
    private String category;
    private List<String> tags;

    public Expense(String name, double amount, Date date, String category, List<String> tags) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return name + " - $" + amount + " - " + dateFormat.format(date) + " - Category: " + category + " - Tags: " + String.join(", ", tags);
    }
} 