package com.example.breakout_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class BallTest {
    @Test
    public void testGameCanvasConstructor() {
        GameCanvas gameCanvas = new GameCanvas();
        double canvasWidth = gameCanvas.getWidth();
        assertEquals(640, canvasWidth, 0);
    }
  
}