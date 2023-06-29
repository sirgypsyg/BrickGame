package com.example.breakout_game;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas {
    private GraphicsContext graphicsContext;
    private Paddle paddle;
    private Ball ball;
    private final List<Brick> bricks = new ArrayList<>();
    private int temp  = 0;
    private Ball.CrushType checkCollision(){
        //mozna zoptymalizowac na pewno;

        for (int i = 0; i < bricks.size(); ++i) {
            // Check for collision with the brick's sides
            if ((ball.getLeftUpperCorner().getX() >=  bricks.get(i).getLeftUpperCorner().getX() && ball.getLeftUpperCorner().getX() <= bricks.get(i).getRightUpperCorner().getX())
                    || (ball.getRightUpperCorner().getX() >=  bricks.get(i).getLeftUpperCorner().getX() && ball.getRightUpperCorner().getX() <=  bricks.get(i).getRightUpperCorner().getX())) {
                if (ball.getLeftUpperCorner().getY() <=  bricks.get(i).getLeftUpperCorner().getY() && ball.getRightLowerCorner().getY() >=  bricks.get(i).getLeftLowerCorner().getY()) {
                    bricks.remove(i);
                    return Ball.CrushType.HorizontalCrush;
                }
            }

            // Check for collision with the brick's top/bottom
            if ((ball.getLeftLowerCorner().getY() >=  bricks.get(i).getLeftUpperCorner().getY() && ball.getLeftLowerCorner().getY() <=  bricks.get(i).getLeftLowerCorner().getY())
                    || (ball.getRightLowerCorner().getY() >=  bricks.get(i).getLeftUpperCorner().getY() && ball.getRightLowerCorner().getY() <=  bricks.get(i).getLeftLowerCorner().getY())) {
                if (ball.getLeftUpperCorner().getX() <=  bricks.get(i).getRightUpperCorner().getX() && ball.getRightUpperCorner().getX() >=  bricks.get(i).getLeftUpperCorner().getX()) {
                    bricks.remove(i);
                    return Ball.CrushType.VerticalCrush;
                }
            }
        }

        return Ball.CrushType.NoCrush;

    }
    private boolean gameRunning = false;
    public void loadLevel() {
        Brick.setGrid(20, 10);

        for (int i = 0; i < Brick.getGridCols(); ++i) {
            for (int j = 2; j < 8; ++j) {
                Color color = getColorForRowIndex(j);
                bricks.add(new Brick(i, j, color));
            }
        }
    }
    private Color getColorForRowIndex(int rowIndex) {
        Color[] colors = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PURPLE};
        return colors[(rowIndex - 2) % colors.length];
    }
    private final AnimationTimer animationTimer = new AnimationTimer() {
        private long lastUpdate;
        @Override
        public void handle(long now) {
            double diff = (now - lastUpdate)/1_000_000_000.;
            lastUpdate = now;

            switch (checkCollision()){
                case HorizontalCrush -> ball.bounceHorizontally();
                case VerticalCrush -> ball.bounceVertically();
            }

            if (isBallJammed()) {
                if (shouldBallBounceHorizontally()) {
                    ball.bounceHorizontally();
                }
                if (shouldBallBounceVertically()) {
                    ball.bounceVertically();
                }
                if (shouldBallBounceFromPaddle() && temp > 10){
                    double hit = paddle.x + paddle.width/2;
                    ball.bounceFromPaddle(hit);
                }
            }


            ball.updatePosition(diff);

            ++temp;
            //
            draw();
        }

        @Override
        public void start() {
            super.start();
            lastUpdate = System.nanoTime();
        }
    };

    public GameCanvas() {
        super(640, 700);
        setFocusTraversable(true);

        this.setOnMouseMoved(mouseEvent -> {
            paddle.setPosition(mouseEvent.getX());
            if(!gameRunning)
                ball.setPosition(new Point2D(mouseEvent.getX(), paddle.getY() - ball.getWidth() / 2));
//            else
//                ball.updatePosition();
            draw();
        });

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE){
                System.out.println("Test");
                gameRunning = !gameRunning;
                if (gameRunning){
                    animationTimer.start();
                }else animationTimer.stop();
            }
        });


        this.setOnMouseClicked(mouseEvent -> {
            gameRunning = true;
            animationTimer.start();
        });
    }
    public void initialize() {
        graphicsContext = this.getGraphicsContext2D();
        GraphicsItem.setCanvasSize(getWidth(), getHeight());
        paddle = new Paddle();
        ball = new Ball();
        loadLevel();
    }
    public void draw() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, getWidth(), getHeight());
        paddle.draw(graphicsContext);
        ball.draw(graphicsContext);
        for (var brick : bricks){
            brick.draw(graphicsContext);
        }
    }
    private boolean shouldBallBounceHorizontally(){
        return ball.x <= 0 || ball.x + ball.getWidth() >= GraphicsItem.canvasWidth;//
    }
    private boolean  shouldBallBounceVertically(){
        return ball.y <= 0;// || ball.x + 100 <= GraphicsItem.canvasWidth
    }
    private boolean  shouldBallBounceFromPaddle(){
        return ball.y + ball.height >= paddle.y
                && (ball.x >= paddle.x && ball.x <= paddle.x + paddle.width);
    }
    private boolean isBallJammed(){
        if (ball.x < 0 && ball.moveVector.getX() < 0) return true; //horizon L
        if (ball.x + ball.getWidth() > GraphicsItem.canvasWidth && ball.moveVector.getX() > 0) return true;//horizon R
        if (ball.y < 0 && ball.moveVector.getY() < 0) return true;// Vert
        return ball.y + ball.height > paddle.y
                && (ball.x > paddle.x && ball.x < paddle.x + paddle.width) && ball.moveVector.getY() > 0;
    }
}