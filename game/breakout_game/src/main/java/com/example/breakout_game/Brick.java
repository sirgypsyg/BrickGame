package com.example.breakout_game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GraphicsItem{
    private static int gridRows;
    private static int gridCols;
    private Color color;
    public Point2D getLeftUpperCorner(){
        return new Point2D(getX(), getY());
    }
    public Point2D getLeftLowerCorner(){
        return new Point2D(getX(), getY() + getHeight());
    }
    public Point2D getRightUpperCorner(){
        return new Point2D(getX() + getWidth(), getY());
    }
    public Point2D getRightLowerCorner(){
        return new Point2D(getX() + getWidth(), getY() + getHeight());
    }
    public static void setGrid(int gridRows, int gridCols) {
        Brick.gridRows = gridRows;
        Brick.gridCols = gridCols;
    }
    public static int getGridRows() {
        return gridRows;
    }
    public static int getGridCols() {
        return gridCols;
    }
    private int row;
    private int col;
    public Brick(int col, int row, Color color) {
        width = canvasWidth/gridCols;
        height = canvasHeight/gridRows;
        x = col * width; //kolejne x w rzedzie + grubosc cegly
        y = 0 + row * height;
        this.color = color;
    }
    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, width, height);

        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.setLineWidth(2.0);
        graphicsContext.strokeRect(x, y, width, height);
    }
}
