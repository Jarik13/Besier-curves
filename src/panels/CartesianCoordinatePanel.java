package panels;

import managers.BezierCurveManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CartesianCoordinatePanel extends JPanel {
    private int scale = 50;
    private BezierCurveManager manager = new BezierCurveManager();

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
    }

    public void addPoint(int x, int y) {
        manager.addPoint(x, y);
        repaint();
    }

    public void clearPoints() {
        manager.clearPoints();
        repaint();
    }

    public List<Point> getPoints() {
        return manager.getPoints();
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

        g2d.setColor(Color.RED);
        for (Point p : manager.getPoints()) {
            int x = p.x * scale + centerX;
            int y = centerY - p.y * scale;
            g2d.fillOval(x - 3, y - 3, 6, 6);
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
