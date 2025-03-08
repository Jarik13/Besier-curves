import panels.CartesianCoordinatePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.FileWriter;

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
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        String[] columnNames = {"Point", "X", "Y"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable pointTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(pointTable);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel xLabel = new JLabel("X:");
        JTextField xField = new JTextField(5);
        JLabel yLabel = new JLabel("Y:");
        JTextField yField = new JTextField(5);
        JButton addButton = new JButton("Add Point");
        JButton clearButton = new JButton("Clear");
        JButton createCurveButton = new JButton("Create Curve");
        JCheckBox methodCheckBox = new JCheckBox("Use Matrix Formula");

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
            mainPanel.setUseMatrixMethod(methodCheckBox.isSelected());
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
        inputPanel.add(methodCheckBox);

        JPanel bernsteinPanel = new JPanel();
        bernsteinPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel iLabel = new JLabel("i (Index):");
        JTextField iField = new JTextField(5);
        JLabel tStartLabel = new JLabel("start:");
        JTextField tStartField = new JTextField(10);
        JLabel tEndLabel = new JLabel("end:");
        JTextField tEndField = new JTextField(10);
        JButton calculateButton = new JButton("Calculate Bernstein");

        calculateButton.addActionListener(e -> {
            try {
                int index = Integer.parseInt(iField.getText());
                double tStart = Double.parseDouble(tStartField.getText());
                double tEnd = Double.parseDouble(tEndField.getText());

                if (tStart < 0 || tStart > 1 || tEnd < 0 || tEnd > 1) {
                    JOptionPane.showMessageDialog(frame, "t must be between 0 and 1", "Invalid Range", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                FileWriter writer = new FileWriter("bernstein_results.txt");
                writer.write("t\t\tBernstein Polynomial\t\tBezier Point (X, Y)\n");

                for (double t = tStart; t <= tEnd; t += mainPanel.getStep()) {
                    double bernsteinValue = mainPanel.getManager().bernsteinPolynomial(index, mainPanel.getManager().getPoints().size() - 1, t);

                    Point2D.Double bezierPoint;
                    if (methodCheckBox.isSelected()) {
                        bezierPoint = mainPanel.getManager().calculateBezierPointMatrix(t, mainPanel.getScale(), mainPanel.getWidth() / 2, mainPanel.getHeight() / 2);
                    } else {
                        bezierPoint = mainPanel.getManager().calculateBezierPoint(t, mainPanel.getScale(), mainPanel.getWidth() / 2, mainPanel.getHeight() / 2);
                    }

                    writer.write(String.format("%.4f\t%.6f\t\t\t\t%.2f, %.2f\n", t, bernsteinValue, bezierPoint.x, bezierPoint.y));
                }

                writer.close();
                JOptionPane.showMessageDialog(frame, "Results saved to bernstein_results.txt");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input.");
            }
        });

        bernsteinPanel.add(iLabel);
        bernsteinPanel.add(iField);
        bernsteinPanel.add(tStartLabel);
        bernsteinPanel.add(tStartField);
        bernsteinPanel.add(tEndLabel);
        bernsteinPanel.add(tEndField);
        bernsteinPanel.add(calculateButton);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        leftPanel.add(inputPanel);
        leftPanel.add(bernsteinPanel);
        leftPanel.add(tablePanel);

        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
    }
}
