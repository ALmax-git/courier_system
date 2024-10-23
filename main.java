import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CourierSystem {
    private JFrame frame;
    private JPanel panel;
    private JPanel container;
    private JButton addButton;
    private JButton retrieveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextArea displayArea;
    private JTextField receiverField;
    private JTextField statusField;
    private JTextField senderField;
    private JTextField trackingField;
    private JLabel trackingLabel;
    private JLabel senderLabel;
    private JLabel receiverLabel;
    private JLabel statusLabel;

    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement retrieveStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;

    public CourierSystem() {
        // Connect to the database
       // try {
       //     connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CourierDB", "root", ""); // Update with your MySQL credentials
       // } catch (SQLException e) {
       //     e.printStackTrace();
       // }
try {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Ensure the driver is loaded
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CourierDB", "root", ""); // MySQL credentials
        System.out.println("Database connected successfully.");
    } catch (SQLException e) {
        System.err.println("Connection failed: " + e.getMessage());
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        System.err.println("JDBC Driver not found: " + e.getMessage());
        e.printStackTrace();
    }
        frame = new JFrame("Courier System");
        panel = new JPanel();
        container = new JPanel();
        addButton = new JButton("Add Package");
        retrieveButton = new JButton("Retrieve Data");
        updateButton = new JButton("Update Package");
        deleteButton = new JButton("Delete Package");
        displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        trackingLabel = new JLabel("Tracking Number:");
        trackingField = new JTextField(10);
        senderLabel = new JLabel("Sender:");
        senderField = new JTextField(10);
        receiverLabel = new JLabel("Receiver:");
        receiverField = new JTextField(10);
        statusLabel = new JLabel("Status:");
        statusField = new JTextField(10);

        addButton.setFont(new Font("Arial", Font.BOLD, 16));
        retrieveButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        displayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        trackingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        trackingField.setFont(new Font("Arial", Font.PLAIN, 14));
        senderLabel.setFont(new Font("Arial", Font.BOLD, 16));
        senderField.setFont(new Font("Arial", Font.PLAIN, 14));
        receiverLabel.setFont(new Font("Arial", Font.BOLD, 16));
        receiverField.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add ActionListener to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPackage();
            }
        });

        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrieveData();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePackage();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePackage();
            }
        });

        // Configure layout and add components
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(trackingLabel);
        inputPanel.add(trackingField);
        inputPanel.add(senderLabel);
        inputPanel.add(senderField);
        inputPanel.add(receiverLabel);
        inputPanel.add(receiverField);
        inputPanel.add(statusLabel);
        inputPanel.add(statusField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(retrieveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        container.add(panel);

        frame.add(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void addPackage() {
        String trackingNumber = JOptionPane.showInputDialog(frame, "Enter Tracking Number:");
        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            String sender = JOptionPane.showInputDialog(frame, "Enter Sender:");
            String receiver = JOptionPane.showInputDialog(frame, "Enter Receiver:");
            String status = JOptionPane.showInputDialog(frame, "Enter Status:");

            try {
                insertStatement = connection.prepareStatement("INSERT INTO Packages (trackingNumber, sender, receiver, status) VALUES (?, ?, ?, ?)");
                insertStatement.setString(1, trackingNumber);
                insertStatement.setString(2, sender);
                insertStatement.setString(3, receiver);
                insertStatement.setString(4, status);
                insertStatement.executeUpdate();

                statusField.setText(status);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Implement other methods (retrieveData, updatePackage, deletePackage)
    public void retrieveData() {
    // Method implementation for retrieving data from the database
    try {
        retrieveStatement = connection.prepareStatement("SELECT * FROM Packages");
        ResultSet resultSet = retrieveStatement.executeQuery();
        displayArea.setText("");  // Clear the display area before showing data
        while (resultSet.next()) {
            String trackingNumber = resultSet.getString("trackingNumber");
            String sender = resultSet.getString("sender");
            String receiver = resultSet.getString("receiver");
            String status = resultSet.getString("status");
            // Displaying retrieved data in the text area
            displayArea.append("Tracking Number: " + trackingNumber + "\n");
            displayArea.append("Sender: " + sender + "\n");
            displayArea.append("Receiver: " + receiver + "\n");
            displayArea.append("Status: " + status + "\n\n");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

public void updatePackage() {
    String trackingNumber = trackingField.getText();
    String sender = senderField.getText();
    String receiver = receiverField.getText();
    String status = statusField.getText();

    try {
        updateStatement = connection.prepareStatement("UPDATE Packages SET sender = ?, receiver = ?, status = ? WHERE trackingNumber = ?");
        updateStatement.setString(1, sender);
        updateStatement.setString(2, receiver);
        updateStatement.setString(3, status);
        updateStatement.setString(4, trackingNumber);
        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(frame, "Package updated successfully!");
            displayUpdatedData(trackingNumber);  // Call another method to display updated data
        } else {
            JOptionPane.showMessageDialog(frame, "No package found with the given tracking number.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
public void displayUpdatedData(String trackingNumber) {
    try {
        // Prepare a statement to retrieve the updated package based on the tracking number
        retrieveStatement = connection.prepareStatement("SELECT * FROM Packages WHERE trackingNumber = ?");
        retrieveStatement.setString(1, trackingNumber);
        ResultSet resultSet = retrieveStatement.executeQuery();

        displayArea.setText("");  // Clear the display area before showing updated data
        if (resultSet.next()) {
            String sender = resultSet.getString("sender");
            String receiver = resultSet.getString("receiver");
            String status = resultSet.getString("status");

            // Display updated package data in the text area
            displayArea.append("Tracking Number: " + trackingNumber + "\n");
            displayArea.append("Sender: " + sender + "\n");
            displayArea.append("Receiver: " + receiver + "\n");
            displayArea.append("Status: " + status + "\n\n");
        } else {
            JOptionPane.showMessageDialog(frame, "No package found with the given tracking number.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

public void deletePackage() {
    String trackingNumber = trackingField.getText();

    int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this package?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            deleteStatement = connection.prepareStatement("DELETE FROM Packages WHERE trackingNumber = ?");
            deleteStatement.setString(1, trackingNumber);
            int rowsDeleted = deleteStatement.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(frame, "Package deleted successfully!");
                displayArea.setText("");  // Clear the display area after deletion
            } else {
                JOptionPane.showMessageDialog(frame, "No package found with the given tracking number.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


    public static void main(String[] args) {
        new CourierSystem();
    }
}
