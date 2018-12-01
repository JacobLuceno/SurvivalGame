package model;

import java.util.Random;

public class Game {
    public enum Direction{RIGHT, LEFT, UP, DOWN}
    private final int HEIGHT;
    private final int WIDTH;

    Map map;
    Player player;

    public Game(int height, int width){
        this.WIDTH = width;
        this.HEIGHT = height;
        Random rand = new Random();
        map = new Map(WIDTH,HEIGHT);
        player = new Player(new Vector2(rand.nextInt(HEIGHT),rand.nextInt(WIDTH)), 10);
    }

    public Map getMap() {
        return map;
    }
    public Player getPlayer(){
        return player;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }
    public int getWIDTH() {
        return WIDTH;
    }
}
