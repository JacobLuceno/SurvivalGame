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
    private boolean gameOver;
    private boolean animalTaggedForRemoval;
    private int day;

    private HerbivoreFactory herbFact;
    private CarnivoreFactory carnFact;

    public Game() {
        gameWon = false;
        gameOver = false;
        dayTime = true;
        inCombat = false;
        animalTaggedForRemoval = false;
        animals = new ArrayList<>();
        day = 1;
        map = new Map(WIDTH, HEIGHT, this);
        herbFact = new HerbivoreFactory(this);
        carnFact = new CarnivoreFactory(this);
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
        player = new Player(new Vector2(0, 0), this, 10);
        do {
            player.setPosition(new Vector2(rand.nextInt(HEIGHT), rand.nextInt(WIDTH)));
        } while(map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getTerrainType() != TerrainTile.TerrainType.GRASSLAND
                || map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getHasStatObj());
        Vector2 displacementVector = player.getPosition().getDisplacementVector(4, 4);
        do {
            try {
                if (map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].getTerrainType() != TerrainTile.TerrainType.WATER) {
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setHasStatObjNull();
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setHasStatObj(true);
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setStatObjType(TerrainTile.StatObjType.BASE);
                    TerrainTile.setBasePlaced(true);
                    TerrainTile.setBaseLocation(displacementVector);
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setStatObj(new Base(displacementVector, this));
                }
            } catch(IndexOutOfBoundsException e){
                displacementVector = player.getPosition().getDisplacementVector(4, 4);
            }
        } while(!TerrainTile.getBasePlaced());
    }

    public void checkForEncounter() {
        if (!(player.getPriorLoc().getv0() == player.getPosition().getv0() && player.getPriorLoc().getv1() == player.getPosition().getv1())) {
            Random rand = new Random();
            int encounter = rand.nextInt(40);
            switch(encounter){
                case 0:
                    spawnNewHerbivore();
                    break;
                case 1:
                    spawnNewCarnivore();
                    break;
                default:
                    //nothing spawned
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
    private void spawnNewHerbivore(){
        try {
            Vector2 newPos = spawnNewAnimalLocation();
            if (map.getTerrainMap()[(int) newPos.getv0()][(int) newPos.getv1()].getTerrainType() != TerrainTile.TerrainType.WATER) {
                Random rand = new Random();
                int encounter = rand.nextInt(2);
                switch (encounter) {
                    case 0:
                        if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH - 1 || newPos.getv0() < 0 || newPos.getv0() > HEIGHT - 1)) {
                            System.out.println("i make rabbit");
                            animals.add(herbFact.produceHerbiore(newPos, Herbivore.HerbType.RABBIT));
                        }
                        break;
                    case 1:
                        if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH - 1 || newPos.getv0() < 0 || newPos.getv0() > HEIGHT - 1)) {
                            System.out.println("i make deer");
                            animals.add(herbFact.produceHerbiore(newPos, Herbivore.HerbType.DEER));
                        }
                        break;
                }
            } else {
                if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH - 1 || newPos.getv0() < 0 || newPos.getv0() > HEIGHT - 1)) {
                    System.out.println("i make fish");
                    animals.add(herbFact.produceHerbiore(newPos, Herbivore.HerbType.FISH));
                }
            }
        } catch(IndexOutOfBoundsException e){
            //Don't spawn animal because the position you're attempting to spawn at is out of bounds
        }
    }
    private void spawnNewCarnivore(){
        try {
            Vector2 newPos = spawnNewAnimalLocation();
            if (map.getTerrainMap()[(int) newPos.getv0()][(int) newPos.getv1()].getTerrainType() != TerrainTile.TerrainType.WATER) {
                Random rand = new Random();
                int encounter = rand.nextInt(3);
                switch (encounter) {
                    case 0:
                        if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH - 1 || newPos.getv0() < 0 || newPos.getv0() > HEIGHT - 1)) {
                            System.out.println("i make wolf");
                            animals.add(carnFact.produceCarnivore(newPos, Carnivore.CarnType.WOLF));
                        }
                        break;
                    case 1:
                        if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH - 1 || newPos.getv0() < 0 || newPos.getv0() > HEIGHT - 1)) {
                            System.out.println("i make lion");
                            animals.add(carnFact.produceCarnivore(newPos, Carnivore.CarnType.LION));
                        }
                        break;
                }
            } else {
                if (!(newPos.getv1() < 0 || newPos.getv1() > WIDTH - 1 || newPos.getv0() < 0 || newPos.getv0() > HEIGHT - 1)) {
                    System.out.println("i make croc");
                    animals.add(carnFact.produceCarnivore(newPos, Carnivore.CarnType.CROC));
                }
            }
        } catch (IndexOutOfBoundsException e){
            //Don't spawn anything because the location you're attempting to spawn at is out of bounds
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
    public void mapRevealAfterFlee(){
        initMapReveal();
        player.setFled(false);
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
