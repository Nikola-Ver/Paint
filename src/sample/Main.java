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
    private int currentMenuItems = 2;

    private void saveArrayList() {
        File file = new File("Paint3d-UltraProVersion-ForTrueMan.ArrayList");
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(shapes);
            objectStream.close();
        } catch (Exception exp) { return; };
    }

    private void openArrayList(Canvas canvas, MenuItem menuItem) {
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
                if (shapes.get(this.currentQuantityOfShapes - 1).howManyNeed() == 0) {
                    element.draw(point, Color.BLACK, Color.BLACK, 5, canvas);
                } else {
                    shapes.remove(this.currentQuantityOfShapes - 1);
                    this.currentQuantityOfShapes--;
                }
            });
            objectStream.close();
        } catch (Exception exp) { return; };
    }

    private void clickedMenuFigures(Menu _menuFigures, Menu _menuNeedPoints) {
        while (this.currentMenuItems > 2) {
            _menuFigures.getItems().remove(1);
            this.currentMenuItems--;
        }

        File folder = new File("./out/production/OOP/sample/Figures");
        for (File file : folder.listFiles()) {
            MenuItem menuItem = new MenuItem(file.getName().replaceAll(".class($)",""));
            menuItem.setOnAction(event -> this.nameOfFigure(file.getName(), _menuNeedPoints));
            _menuFigures.getItems().add(menuItem);
            this.currentMenuItems++;
        }
    }

    private void clicked(MouseEvent event, Canvas canvas, MenuItem menuItem, Color penColor, Color fillColor)  {
        Point point = new Point(event.getX(), event.getY());
        if (
            this.currentQuantityOfShapes == 0 ||
            shapes.get(this.currentQuantityOfShapes - 1).draw(point, Color.BLACK, Color.BLACK, 5, canvas)
            ) {
            try {
                Object nameOfClass = Class.forName(this.nameOfFigure).getConstructor().newInstance();
                Shape newShape = (Shape) nameOfClass;
                shapes.add(newShape);
                this.currentQuantityOfShapes++;
                shapes.get(this.currentQuantityOfShapes - 1).draw(point, Color.BLACK, Color.BLACK, 5, canvas);
            } catch (Exception exp) { return; };
        }
        if (shapes.get(this.currentQuantityOfShapes - 1).howManyNeed() > 0) {
            menuItem.setText(shapes.get(this.currentQuantityOfShapes - 1).howManyNeed() + " need points");
        } else {
            menuItem.setText("New shape");
        }
    }

    private void nameOfFigure(String _nameOfFigure, MenuItem menuItem) {
        this.nameOfFigure = "sample.Figures." + _nameOfFigure.replaceAll(".class($)","");
        if (this.currentQuantityOfShapes != 0 && shapes.get(this.currentQuantityOfShapes - 1).howManyNeed() != 0) {
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
        menuSave.setOnAction(event -> this.saveArrayList());
        menuOpen.setOnAction(event -> this.openArrayList(canvas, menuNeedPoints));

        Menu menuFigures = new Menu("Shapes");
        MenuItem menuRefresh = new MenuItem("Refresh");
        menuFigures.getItems().add(menuRefresh);
        menuFigures.getItems().add(new SeparatorMenuItem());
        menuRefresh.setOnAction(event -> this.clickedMenuFigures(menuFigures, menuNeedPoints));
        menuBar.getMenus().add(menuFile);
        menuBar.getMenus().add(menuFigures);
        menuBar.getMenus().add(menuNeedPoints);
        root.getChildren().add(menuBar);
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.setOnMouseClicked(mouseEvent -> this.clicked(mouseEvent, canvas, menuNeedPoints, penColor, fillColor));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
