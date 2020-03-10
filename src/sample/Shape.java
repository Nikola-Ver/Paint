package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.io.Serializable;

// Abstract class
public abstract class Shape implements Serializable {
    public int HowManyNeed() { return -1; };
    public boolean Draw(Point point, Color penColor, Color fillColor, int Width, Canvas canvas) { return true; };
}