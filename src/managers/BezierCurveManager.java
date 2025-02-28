package managers;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BezierCurveManager {
    List<Point2D.Double> points = new ArrayList<>();

    public void addPoint(double x, double y) {
        points.add(new Point2D.Double(x, y));
    }

    public void clearPoints() {
        points.clear();
    }

    public List<Point2D.Double> getPoints() {
        return points;
    }

    public Point2D.Double calculateBezierPoint(double t, int scale, int centerX, int centerY) {
        List<Point2D.Double> tempPoints = new ArrayList<>();
        for (Point2D.Double p : points) {
            double scaledX = p.x * scale + centerX;
            double scaledY = centerY - p.y * scale;
            tempPoints.add(new Point2D.Double(scaledX, scaledY));
        }

        while (tempPoints.size() > 1) {
            List<Point2D.Double> nextPoints = new ArrayList<>();
            for (int i = 0; i < tempPoints.size() - 1; i++) {
                Point2D.Double p0 = tempPoints.get(i);
                Point2D.Double p1 = tempPoints.get(i + 1);
                double x = (1 - t) * p0.x + t * p1.x;
                double y = (1 - t) * p0.y + t * p1.y;
                nextPoints.add(new Point2D.Double(x, y));
            }
            tempPoints = nextPoints;
        }

        return tempPoints.get(0);
    }
}
