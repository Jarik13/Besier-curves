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

    public Point2D.Double calculateBezierPointParametric(double t, int scale, int centerX, int centerY) {
        int n = points.size() - 1;
        double x = 0, y = 0;

        for (int i = 0; i <= n; i++) {
            double basis = binomialCoefficient(n, i) * Math.pow(1 - t, n - i) * Math.pow(t, i);
            x += basis * points.get(i).x;
            y += basis * points.get(i).y;
        }

        x = x * scale + centerX;
        y = centerY - y * scale;

        return new Point2D.Double(x, y);
    }

    public double bernsteinPolynomial(int i, int n, double t) {
        return binomialCoefficient(n, i) * Math.pow(1 - t, n - i) * Math.pow(t, i);
    }

    private double binomialCoefficient(int n, int i) {
        if (i == 0 || i == n) {
            return 1;
        }

        double num = 1;
        double denom = 1;

        for (int j = 1; j <= i; j++) {
            num *= (n - j + 1);
            denom *= j;
        }

        return num / denom;
    }
}
