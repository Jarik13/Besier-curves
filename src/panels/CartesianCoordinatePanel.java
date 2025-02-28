package panels;

import managers.BezierCurveManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;

public class CartesianCoordinatePanel extends JPanel {
    private int scale = 50;
    private final BezierCurveManager manager = new BezierCurveManager();
    private boolean createCurve = false;
    private int draggedPointIndex = -1;
    private Point lastMousePos = null;
    private DefaultTableModel tableModel;

    public CartesianCoordinatePanel() {
        addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                scale += 5;
            } else if (rotation > 0 && scale > 25) {
                scale -= 5;
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clickPoint = e.getPoint();
                List<Point2D.Double> points = manager.getPoints();
                for (int i = 0; i < points.size(); i++) {
                    Point2D.Double p = points.get(i);
                    int x = (int) (p.x * scale + (double) getWidth() / 2);
                    int y = (int) ((double) getHeight() / 2 - p.y * scale);
                    if (Math.abs(clickPoint.x - x) < 15 && Math.abs(clickPoint.y - y) < 15) {
                        draggedPointIndex = i;
                        lastMousePos = clickPoint;
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastMousePos = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedPointIndex != -1 && lastMousePos != null) {
                    int dx = e.getX() - lastMousePos.x;
                    int dy = e.getY() - lastMousePos.y;

                    Point2D.Double draggedPoint = manager.getPoints().get(draggedPointIndex);

                    draggedPoint.x += ((double) dx / scale);
                    draggedPoint.y -= ((double) dy / scale);
                    updateTable();
                    lastMousePos = e.getPoint();
                    repaint();
                }
            }
        });
    }

    public void addPoint(double x, double y) {
        manager.addPoint(x, y);
        repaint();
    }

    public void clearPoints() {
        manager.clearPoints();
        repaint();
    }

    public int getScale() {
        return scale;
    }

    public BezierCurveManager getManager() {
        return manager;
    }

    public void setCreateCurve(boolean createCurve) {
        this.createCurve = createCurve;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void updateTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < manager.getPoints().size(); i++) {
            Point2D.Double p = manager.getPoints().get(i);
            String label = "P" + (i + 1);

            String xFormatted = String.format("%.2f", p.x);
            String yFormatted = String.format("%.2f", p.y);

            tableModel.addRow(new Object[]{label, xFormatted, yFormatted});
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));

        g2d.drawLine(0, centerY, width, centerY);
        g2d.drawLine(centerX, 0, centerX, height);

        drawArrow(g2d, width, centerY, true);
        drawArrow(g2d, centerX, 0, false);

        g2d.drawString("X", width - 20, centerY - 5);
        g2d.drawString("Y", centerX + 5, 15);

        drawGrid(g2d, width, height, centerX, centerY);

        for (int i = 0; i < manager.getPoints().size(); i++) {
            Point2D.Double p = manager.getPoints().get(i);
            int x = (int) (p.x * scale + centerX);
            int y = (int) (centerY - p.y * scale);

            if (i == 0 || i == manager.getPoints().size() - 1) {
                g2d.setColor(Color.BLACK);
                g2d.fillOval(x - 4, y - 4, 10, 10);
            } else {
                g2d.setColor(Color.BLUE);
            }

            g2d.fillOval(x - 4, y - 4, 8, 8);

            String pointLabel = "P" + (i + 1);
            g2d.drawString(pointLabel, x + 5, y - 5);
        }

        float[] dashPattern = {10, 10};
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0, dashPattern, 0));

        for (int i = 0; i < manager.getPoints().size() - 1; i++) {
            Point2D.Double p1 = manager.getPoints().get(i);
            Point2D.Double p2 = manager.getPoints().get(i + 1);

            int x1 = (int) (p1.x * scale + centerX);
            int y1 = (int) (centerY - p1.y * scale);
            int x2 = (int) (p2.x * scale + centerX);
            int y2 = (int) (centerY - p2.y * scale);

            g2d.setColor(Color.BLUE);
            g2d.drawLine(x1, y1, x2, y2);
        }

        if (createCurve && manager.getPoints().size() > 1) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));

            Point2D.Double prev = null;

            for (double t = 0; t <= 1; t += 0.001) {
                Point2D.Double bezierPoint = manager.calculateBezierPoint(t, scale, centerX, centerY);

                int x = (int) bezierPoint.x;
                int y = (int) bezierPoint.y;

                if (prev != null) {
                    g2d.drawLine((int) prev.x, (int) prev.y, x, y);
                }

                prev = bezierPoint;
            }
        }
    }

    private void drawArrow(Graphics2D g2d, int x, int y, boolean isXAxis) {
        int arrowSize = 10;
        if (isXAxis) {
            g2d.drawLine(x - arrowSize, y + arrowSize / 2, x, y);
            g2d.drawLine(x - arrowSize, y - arrowSize / 2, x, y);
        } else {
            g2d.drawLine(x - arrowSize / 2, y + arrowSize, x, y);
            g2d.drawLine(x + arrowSize / 2, y + arrowSize, x, y);
        }
    }

    private void drawGrid(Graphics2D g2d, int width, int height, int centerX, int centerY) {
        g2d.setStroke(new BasicStroke(1));

        for (int x = centerX + scale; x < width; x += scale) {
            g2d.drawLine(x, centerY - 5, x, centerY + 5);
            g2d.drawString(String.valueOf((x - centerX) / scale), x - 5, centerY + 20);
        }
        for (int x = centerX - scale; x > 0; x -= scale) {
            g2d.drawLine(x, centerY - 5, x, centerY + 5);
            g2d.drawString(String.valueOf((x - centerX) / scale), x - 10, centerY + 20);
        }

        for (int y = centerY - scale; y > 0; y -= scale) {
            g2d.drawLine(centerX - 5, y, centerX + 5, y);
            g2d.drawString(String.valueOf((centerY - y) / scale), centerX + 10, y + 5);
        }
        for (int y = centerY + scale; y < height; y += scale) {
            g2d.drawLine(centerX - 5, y, centerX + 5, y);
            g2d.drawString(String.valueOf((centerY - y) / scale), centerX + 10, y + 5);
        }
    }
}