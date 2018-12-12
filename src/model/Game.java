package model;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    public enum Direction {RIGHT, LEFT, UP, DOWN}

    private final static int HEIGHT = 100;
    private final static int WIDTH = 100;
    private final int STEPS_PER_CYCLE = 50;

    private static CombatEncounter currentEncounter;

    private Map map;
    private Player player;
    private ArrayList<Animal> animals;
    private boolean dayTime;
    private static boolean inCombat;
    private boolean gameWon;
    private boolean animalTaggedForRemoval;
    private int day;

    private HerbivoreFactory herbFact;

    public Game() {
        gameWon = false;
        dayTime = true;
        inCombat = false;
        animalTaggedForRemoval = false;
        animals = new ArrayList<>();
        day = 1;
        map = new Map(WIDTH, HEIGHT);
        herbFact = new HerbivoreFactory();
        placePlayerAndBase();
        initMapReveal();
    }

    private void initMapReveal() {
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
    private void placePlayerAndBase(){
        Random rand = new Random();
        player = new Player(new Vector2(rand.nextInt(HEIGHT), rand.nextInt(WIDTH)), 10);
        do {
            player.setPosition(new Vector2(rand.nextInt(HEIGHT), rand.nextInt(WIDTH)));
        } while(map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getTerrainType() != TerrainTile.TerrainType.GRASSLAND
                || map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getHasStatObj());
        Vector2 displacementVector;
        do {
            displacementVector = player.getPosition().getDisplacementVector(4, 4);
            try {
                map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setHasStatObjNull();
                map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setHasStatObj(true);
                map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setStatObjType(TerrainTile.StatObjType.BASE);
                TerrainTile.setBasePlaced(true);
                TerrainTile.setBaseLocation(displacementVector);
                map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setStatObj(new Base(displacementVector));

            } catch(IndexOutOfBoundsException e){
                displacementVector = player.getPosition().getDisplacementVector(4, 4);
            }
        } while(!TerrainTile.getBasePlaced() && map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].getTerrainType() == TerrainTile.TerrainType.WATER);
    }

    //TODO FINISH THIS LIL STUB OF A FUNCTION IT ONLY WORKS WITH RABBITS
    public void checkForEncounter() {
        if (!(player.getPriorLoc().getv0() == player.getPosition().getv0() && player.getPriorLoc().getv1() == player.getPosition().getv1())) {
            Random rand = new Random();
            int encounter = rand.nextInt(20);
            switch (encounter) {
                case 1:
                    Vector2 newPos = spawnNewAnimalLocation();
                    if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH || newPos.getv0() < 0 || newPos.getv0() > HEIGHT)) {
                        System.out.println("i make rabbit");
                        animals.add(herbFact.produceHerbiore(newPos, Herbivore.HerbType.RABBIT));
                    }
                    break;
            }
        }
    }
    private Vector2 spawnNewAnimalLocation(){
        switch(player.getFacing()){
            case UP:
                return new Vector2(player.getPosition().getv0()-11, player.getPosition().getv1());
            case DOWN:
                return new Vector2(player.getPosition().getv0()+11, player.getPosition().getv1());
            case RIGHT:
                return new Vector2(player.getPosition().getv0(), player.getPosition().getv1()+11);
            default:
                return new Vector2(player.getPosition().getv0(), player.getPosition().getv1()-11);
        }
    }
    public void despawnDistantAnimals(){
        for (Animal animal : animals){
            if (animal.getPosition().distance(player.getPosition()) > 20){
                animal.setRemove(true);
                animalTaggedForRemoval = true;
            }
        }
    }

    public void checkDayCycle(){
        if (player.getStepsToday() > STEPS_PER_CYCLE){
            dayTime = !dayTime;
            player.setStepsToday(0);
            if (dayTime){
                day++;
                System.out.println("Day: " + Integer.toString(day));
            }
        }
    }


    //GETTERS AND SETTERS

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
    public boolean isGameWon() {
        return gameWon;
    }
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }
    public ArrayList<Animal> getAnimals() {
        return animals;
    }
    public static CombatEncounter getCurrentEncounter() {
        return currentEncounter;
    }
    public static void setCurrentEncounter(CombatEncounter currentEncounter) {
        Game.currentEncounter = currentEncounter;
    }
    public static boolean isInCombat() {
        return inCombat;
    }
    public static void setInCombat(boolean isInCombat){ inCombat = isInCombat;}
    public boolean isAnimalTaggedForRemoval() {
        return animalTaggedForRemoval;
    }
    public void setAnimalTaggedForRemoval(boolean animalTaggedForRemoval) {
        this.animalTaggedForRemoval = animalTaggedForRemoval;
    }

}
