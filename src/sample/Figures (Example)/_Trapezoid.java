package sample.Figures;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.Point;
import sample.Shape;

public class _Trapezoid extends Shape {
    private int currentQuantityPoints = 0;
    private int requiredQuantityOfPoints = 4;

    private Point[] point = new Point[requiredQuantityOfPoints];

    @Override
    public int howManyNeed() {
        return (this.requiredQuantityOfPoints - this.currentQuantityPoints);
    }

    @Override
    public boolean draw(Point point, Color penColor, Color fillColor, int Width, Canvas canvas) {
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
            gc.strokeLine(this.point[0].x, this.point[0].y, this.point[1].x, this.point[1].y);
            gc.strokeLine(this.point[1].x, this.point[1].y, this.point[2].x, this.point[2].y);
            gc.strokeLine(this.point[2].x, this.point[2].y, this.point[3].x, this.point[3].y);
            gc.strokeLine(this.point[3].x, this.point[3].y, this.point[0].x, this.point[0].y);
        }
        return flag;
    }
}