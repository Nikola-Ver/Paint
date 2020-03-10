package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;


public class Main extends Application {
    private ArrayList<Shape> shapes = new ArrayList<Shape>();
    private String nameOfFigure;
    private int currentQuantityOfShapes = 0;

    private void SaveArrayList() {
        File file = new File("Paint3d-UltraProVersion-ForTrueMan.ArrayList");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(shapes);
            objectStream.close();
        } catch (Exception exp) { return; };
    }

    private void OpenArrayList(Canvas canvas, MenuItem menuItem) {
        File file = new File("Paint3d-UltraProVersion-ForTrueMan.ArrayList");
        try {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0,0,4000,4000);
            menuItem.setText("New shape");
            this.currentQuantityOfShapes = 0;
            FileInputStream fileStream = new FileInputStream(file);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);
            Object obj = objectStream.readObject();
            shapes = (ArrayList<Shape>) obj;
            Point point = new Point(0,0);
            shapes.forEach(element -> {
                this.currentQuantityOfShapes++;
                if (shapes.get(this.currentQuantityOfShapes - 1).HowManyNeed() == 0) {
                    element.Draw(point, Color.BLACK, Color.BLACK, 5, canvas);
                } else {
                    shapes.remove(this.currentQuantityOfShapes - 1);
                    this.currentQuantityOfShapes--;
                }
            });
            objectStream.close();
        } catch (Exception exp) { return; };
    }

    private void Clicked(MouseEvent event, Canvas canvas, MenuItem menuItem, Color penColor, Color fillColor)  {
        Point point = new Point(event.getX(), event.getY());
        if (
            this.currentQuantityOfShapes == 0 ||
            shapes.get(this.currentQuantityOfShapes - 1).Draw(point, Color.BLACK, Color.BLACK, 5, canvas)
            ) {
            try {
                Object nameOfClass = Class.forName(this.nameOfFigure).getConstructor().newInstance();
                Shape newShape = (Shape) nameOfClass;
                shapes.add(newShape);
                this.currentQuantityOfShapes++;
                shapes.get(this.currentQuantityOfShapes - 1).Draw(point, Color.BLACK, Color.BLACK, 5, canvas);
            } catch (Exception exp) { return; };
        }
        if (shapes.get(this.currentQuantityOfShapes - 1).HowManyNeed() > 0) {
            menuItem.setText(shapes.get(this.currentQuantityOfShapes - 1).HowManyNeed() + " need points");
        } else {
            menuItem.setText("New shape");
        }
    }

    private void NameOfFigure(String _nameOfFigure, MenuItem menuItem) {
        this.nameOfFigure = "sample.Figures." + _nameOfFigure.replaceAll(".java($)","");
        if (this.currentQuantityOfShapes != 0 && shapes.get(this.currentQuantityOfShapes - 1).HowManyNeed() != 0) {
            menuItem.setText("New shape");
            shapes.remove(this.currentQuantityOfShapes - 1);
            this.currentQuantityOfShapes--;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, 500, 400);
        Canvas canvas = new Canvas(5000, 5000);
        root.getChildren().add(canvas);

        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuOpen = new MenuItem("Open");
        menuFile.getItems().add(menuOpen);
        MenuItem menuSave = new MenuItem("Save");
        menuFile.getItems().add(new SeparatorMenuItem());
        menuFile.getItems().add(menuSave);

        Color penColor = Color.BLACK;
        Color fillColor = Color.BLACK;

        Menu menuNeedPoints = new Menu("Select a shape");
        menuSave.setOnAction(event -> this.SaveArrayList());
        menuOpen.setOnAction(event -> this.OpenArrayList(canvas, menuNeedPoints));

        Menu menuFigures = new Menu("Shapes");
        File folder = new File("./src/sample/Figures");
        for (File file : folder.listFiles()) {
            MenuItem menuItem = new MenuItem(file.getName().replaceAll(".java($)",""));
            menuItem.setOnAction(event -> this.NameOfFigure(file.getName(), menuNeedPoints));
            menuFigures.getItems().add(menuItem);
        }
        menuBar.getMenus().add(menuFile);
        menuBar.getMenus().add(menuFigures);
        menuBar.getMenus().add(menuNeedPoints);
        root.getChildren().add(menuBar);
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.setOnMouseClicked(mouseEvent -> this.Clicked(mouseEvent, canvas, menuNeedPoints, penColor, fillColor));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
