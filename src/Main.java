import panels.CartesianCoordinatePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;

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
                double x = Double.parseDouble(xField.getText());
                double y = Double.parseDouble(yField.getText());

                mainPanel.addPoint(x, y);
                mainPanel.setTableModel(tableModel);
                mainPanel.updateTable();

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

        createCurveButton.addActionListener(e -> {
            mainPanel.setCreateCurve(true);
            mainPanel.repaint();
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