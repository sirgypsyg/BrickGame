package com.example.breakout_game;


import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static java.lang.Math.abs;

public class Ball extends GraphicsItem {
    public Point2D moveVector = new Point2D(1, -1).normalize();
    private double velocity = 500;

    public enum CrushType{ NoCrush, HorizontalCrush, VerticalCrush}
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

    public Ball() {
        x = 1;
        y = 1;// nie ma znaczenia, punkt startowy ustala paddle
        width = height = canvasHeight * .015; // wielkosc pilki
    }
    @Override
    public void draw(GraphicsContext graphicsContext) {  // Wygląd piłki
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(x, y, width, height);
    }

    public void setPosition(Point2D point) {
        this.x = point.getX() - width/2;
        this.y = point.getY() - height/2;
    }

    public void updatePosition(double diff) {
        x += moveVector.getX() * velocity * diff;
        y += moveVector.getY() * velocity * diff;
    }
    public void bounceFromPaddle(double paddleX){
        double angle = abs(paddleX -x) / 45;
        System.out.println(angle  + "\n");

        System.out.println(angle  + "\n");

        if (angle == 0) moveVector = new Point2D(1, -1).normalize();
        else
            moveVector = new Point2D(angle, -moveVector.getY()).normalize();
        System.out.println(moveVector.getX()  + "\n");
        System.out.println(moveVector.getY()  + "\n");
    }
    public void bounceHorizontally(){
        moveVector = new Point2D(-moveVector.getX(), moveVector.getY());
    }
    public void bounceVertically(){
        moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
    }
}

