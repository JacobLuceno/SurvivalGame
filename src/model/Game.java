package model;

import java.util.Random;

public class Game {
    public enum Direction{RIGHT, LEFT, UP, DOWN}
    private final static int HEIGHT = 100;
    private final static int WIDTH = 100;
    private final int STEPS_PER_CYCLE = 25;

    private Map map;
    private Player player;
    private boolean dayTime;

    public Game(){
        dayTime = true;
        Random rand = new Random();
        map = new Map(WIDTH,HEIGHT);
        player = new Player(new Vector2(rand.nextInt(HEIGHT),rand.nextInt(WIDTH)), 10);
        initMapReveal();
    }

    private void initMapReveal(){
        for (int y = (int) player.getPosition().getv0() - 10; y <= player.getPosition().getv0() + 10; y++) {
            for (int x = (int) player.getPosition().getv1() - 10; x <= player.getPosition().getv1() + 10; x++) {
                try {
                    map.getTerrainMap()[y][x].setRevealedOnMiniMap(true);
                } catch (IndexOutOfBoundsException e) {
                    //do nothing, it just means theres no map to reveal there
                }
            }
        }
    }


    public void checkDayCycle(){
        if (player.getStepsToday() > STEPS_PER_CYCLE){
            dayTime = !dayTime;
            player.setStepsToday(0);
        }
    }

    public boolean isDayTime() {
        return dayTime;
    }

    public Map getMap() {
        return map;
    }
    public Player getPlayer(){
        return player;
    }
    public static int getHEIGHT() {
        return HEIGHT;
    }
    public static int getWIDTH() {
        return WIDTH;
    }

    public int getSTEPS_PER_CYCLE() {
        return STEPS_PER_CYCLE;
    }
}
