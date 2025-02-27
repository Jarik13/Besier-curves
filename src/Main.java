import panels.CartesianCoordinatePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Besier curves");
        initializeUI(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private static void initializeUI(JFrame frame) {
        CartesianCoordinatePanel mainPanel = new CartesianCoordinatePanel();
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        String[] columnNames = {"Point", "X", "Y"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable pointTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(pointTable);

        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel xLabel = new JLabel("X:");
        JTextField xField = new JTextField(5);
        JLabel yLabel = new JLabel("Y:");
        JTextField yField = new JTextField(5);
        JButton addButton = new JButton("Add Point");
        JButton clearButton = new JButton("Clear");
        JButton createCurveButton = new JButton("Create Curve");

        addButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());

                mainPanel.addPoint(x, y);

                Point point = mainPanel.getPoints().getLast();
                String label = "P" + mainPanel.getPoints().size();
                tableModel.addRow(new Object[]{label, point.x, point.y});

                xField.setText("");
                yField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid integers for X and Y.");
            }
        });

        clearButton.addActionListener(e -> {
            mainPanel.clearPoints();
            tableModel.setRowCount(0);
        });

        inputPanel.add(xLabel);
        inputPanel.add(xField);
        inputPanel.add(yLabel);
        inputPanel.add(yField);
        inputPanel.add(addButton);
        inputPanel.add(clearButton);
        inputPanel.add(createCurveButton);

        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(contentPanel, BorderLayout.WEST);
        frame.add(inputPanel, BorderLayout.NORTH);
    }
}