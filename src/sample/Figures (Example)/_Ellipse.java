package sample.Figures;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Point;
import sample.Shape;

public class _Ellipse extends Shape {
    private int currentQuantityPoints = 0;
    private int requiredQuantityOfPoints = 3;

    private Point[] point = new Point[requiredQuantityOfPoints];

    @Override
    public int HowManyNeed() {
        return (this.requiredQuantityOfPoints - this.currentQuantityPoints);
    }

    @Override
    public boolean Draw(Point point, Color penColor, Color fillColor, int Width, Canvas canvas) {
        boolean flag = false;
        if (this.currentQuantityPoints == this.requiredQuantityOfPoints) {
            flag = true;
        } else {
            this.point[this.currentQuantityPoints] = new Point(point);
            this.currentQuantityPoints++;
        }

        if (this.currentQuantityPoints == this.requiredQuantityOfPoints) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(Width);
            gc.setStroke(penColor);
            gc.setFill(fillColor);
            gc.strokeOval(this.point[0].x, this.point[0].y, this.point[1].x - this.point[0].x, this.point[2].y - this.point[0].y);
            gc.fillOval(this.point[0].x, this.point[0].y, this.point[1].x - this.point[0].x, this.point[2].y - this.point[0].y);
        }
        return flag;
    }
}