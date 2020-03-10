package sample;

import java.io.Serializable;

public class Point implements Serializable {
    public double x = 0;
    public double y = 0;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Point() {
        this.x = 0;
        this.y = 0;
    }
}