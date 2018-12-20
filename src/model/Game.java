package model;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    //Used for movement and facing for the player
    public enum Direction {RIGHT, LEFT, UP, DOWN}

    //final variables that define the play parameters of the game
    private final int MAX_ANIMALS = 5;
    private final int HEIGHT = 100;
    private final int WIDTH = 100;
    private final int STEPS_PER_CYCLE = 100;
    private final int DAYS_TO_SURVIVE = 7;

    //Game States
    private boolean dayTime;
    private boolean inCombat;
    private boolean gameWon;
    private boolean gameOver;
    private boolean animalTaggedForRemoval;
    private int day;

    //Mobile Game Objects
    private Player player;
    private ArrayList<Animal> animals;
    //Stationary Game Objects
    private Base base;

    //Referenced when game is in combat mode
    private CombatEncounter currentEncounter;

    //Defines placement of TerrainTiles and StationaryObjects
    private Map map;

    //Factories for instantiation of new animals
    private HerbivoreFactory herbFact;
    private CarnivoreFactory carnFact;

    //no argument Constructor for the game. Sets up game state variables, creates a new map, places the player and
    // the base, and reveals the tiles the player can currently see on the miniMap
    public Game() {
        gameWon = false;
        gameOver = false;
        dayTime = true;
        inCombat = false;
        animalTaggedForRemoval = false;
        day = 1;

        animals = new ArrayList<>();

        map = new Map(this);
        herbFact = new HerbivoreFactory(this);
        carnFact = new CarnivoreFactory(this);
        placePlayerAndBase();
        initMapReveal();
    }


    //SET UP FUNCTIONS--
    //  placePlayerAndBase - randomly positions player on empty grassLand tile. positions base within 1 tile.
    //                       Calls clearBrush().
    //  clearBrush - clears all stationaryObjects from around base, so that when player rests at base, they are less
    //               likely to accidentally rest at a near-by tree.
    //  initMapReveal - Tags squares within player's visual range to be revealed on the miniMap
    private void placePlayerAndBase(){
        Random rand = new Random();
        player = new Player(new Vector2(0, 0), this, 10);

        //Randomly choose players starting location, guaranteeing player starts in grassLand tile with no stationaryObjects
        do {
            player.setPosition(new Vector2(rand.nextInt(HEIGHT), rand.nextInt(WIDTH)));
        } while(
                map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getTerrainType() != TerrainTile.TerrainType.GRASSLAND
                        || map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getHasStatObj()
        );

        //Randomly places the base 1 square from the player, as long as the tile isn't Water,
        // and clears the surrounding area of stationaryObjects with call to clearBrush().
        Vector2 displacementVector;
        boolean basePlaced = false;
        do {
            displacementVector = player.getPosition().getDisplacementVector(2, 2);
            try {
                if (map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].getTerrainType() != TerrainTile.TerrainType.WATER) {
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setHasStatObjNull();
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setHasStatObj(true);
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setStatObjType(TerrainTile.StatObjType.BASE);
                    base = new Base(displacementVector, this);
                    map.getTerrainMap()[(int) displacementVector.getv0()][(int) displacementVector.getv1()].setStatObj(base);
                    basePlaced = true;
                }
            } catch(IndexOutOfBoundsException e){
                //Do nothing. since base won't have been placed the loop will run again insuring the base is placed
            }
        } while(!basePlaced);

        clearBrush();
    }
    private void clearBrush(){
        int y =(int)base.getPosition().getv0();
        int x = (int)base.getPosition().getv1();
        for (int i = y - 2; i < y + 3; i++){
            for (int j = x - 2; j < x + 3; j++){
                if (i != y || j != x) {
                    try {
                        //set Tile's stationary Object to null.  Set Tile's hasStatObj property to false
                        map.getTerrainMap()[i][j].setHasStatObjNull();
                    } catch (IndexOutOfBoundsException e) {
                        //Ignore exception.  Signifies base is near edge of map
                    }
                }
            }
        }
    }
    private void initMapReveal() {
        for (int y = (int) player.getPosition().getv0() - 10; y <= player.getPosition().getv0() + 10; y++) {
            for (int x = (int) player.getPosition().getv1() - 10; x <= player.getPosition().getv1() + 10; x++) {
                try {
                    map.getTerrainMap()[y][x].setRevealedOnMiniMap(true);
                } catch (IndexOutOfBoundsException e) {
                    //do nothing, it just means there's no map to reveal there
                }
            }
        }
    }

    //Functions relating to Animal encounters:
    //checkForEncounter - called each time the player takes a step. An animal is spawned on average every 20 steps.
    //                     If an animal is generated, it has a 50& chance of being an Herbivore or a Carnivore.
    //                     Function limits number of animals spawned at one time, to insure top performance.
    //spawnNewHerbivore/SpawnNewCarnivore - subroutines to determine which animal is spawned, and where.
    //spawnNextAnimalLocation - subroutine to propose new animal location based on player facing
    //despawnDistantAnimals- Tags animals sufficiently far from the player for removal. Saves memory
    //                       and processing time, as calls to animal's pathfinding algorithm is expensive, and not
    //                       necessary if animal is not likely to interact with player.
    //clearAllAnimals - Tags all animals currently in play for removal. Called during rests to simulate time passing.
    public void checkForEncounter() {
        if (animals.size() < MAX_ANIMALS) {
            //This statement insures that the player did not attempt to move but fail to do so, such as in the case of
            //walking into the base, which blocks the player from moving, but still makes a call to player.attemptMove().
            if (!(player.getPriorLoc().getv0() == player.getPosition().getv0() && player.getPriorLoc().getv1() == player.getPosition().getv1())) {
                Random rand = new Random();
                int encounter = rand.nextInt(40);
                switch (encounter) {
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
    }
    private void spawnNewHerbivore(){
        try {
            //Subroutine to propose spot to spawn animal based on player's facing
            Vector2 newPos = spawnNewAnimalLocation();
            //if water - > fish; else 50/50 rabbit/deer
            if (map.getTerrainMap()[(int) newPos.getv0()][(int) newPos.getv1()].getTerrainType() != TerrainTile.TerrainType.WATER) {
                Random rand = new Random();
                boolean rabbit = rand.nextBoolean();
                //Since the try block will throw an exception at the first "if" statement above referencing TerrainMap,
                //bound checking is not needed before spawning animals.
                if (rabbit) {
                    animals.add(herbFact.produceHerbivore(newPos, Herbivore.HerbType.RABBIT));
                }
                else{
                    animals.add(herbFact.produceHerbivore(newPos, Herbivore.HerbType.DEER));
                }
            } else {
                animals.add(herbFact.produceHerbivore(newPos, Herbivore.HerbType.FISH));
            }
        } catch(IndexOutOfBoundsException e){
            //Don't spawn animal because the position you're attempting to spawn at is out of bounds
        }
    }
    private void spawnNewCarnivore(){
        try {
            //Subroutine to propose spot to spawn animal based on player's facing
            Vector2 newPos = spawnNewAnimalLocation();
            //if water - > crocodile; else 50/50 wolf/lion
            if (map.getTerrainMap()[(int) newPos.getv0()][(int) newPos.getv1()].getTerrainType() != TerrainTile.TerrainType.WATER) {
                Random rand = new Random();
                boolean wolf = rand.nextBoolean();
                //Since the try block will throw an exception at the first "if" statement above referencing TerrainMap,
                //bound checking is not needed before spawning animals.
                if (wolf){
                    animals.add(carnFact.produceCarnivore(newPos, Carnivore.CarnType.WOLF));
                }
                else{
                    animals.add(carnFact.produceCarnivore(newPos, Carnivore.CarnType.LION));
                }
            } else {
                    animals.add(carnFact.produceCarnivore(newPos, Carnivore.CarnType.CROC));
            }
        } catch (IndexOutOfBoundsException e){
            //Don't spawn anything because the location you're attempting to spawn at is out of bounds
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
    public void clearAllAnimals(){
        for (Animal animal : animals) {
            animal.setRemove(true);
        }
        animalTaggedForRemoval = true;
    }


    //Checks to see if player has walked a full day's steps.  If so, it resets players steps, increments the count of
    //cycles since they last rested, switches between day and night modes, auto applies 1 damage to simulate hunger, and
    // iterates the day count.  If the player hasn't rested in over a full day, they take increasing damage each cycle
    // until they rest.
    public void checkDayCycle(){
        if (player.getStepsToday() > STEPS_PER_CYCLE){
            dayTime = !dayTime;
            player.setStepsToday(0);
            player.setCyclesSinceRest(player.getCyclesSinceRest() + 1);
            if (player.getCyclesSinceRest() > 2){
                player.sufferHarm(player.getCyclesSinceRest());
            } else if (player.getCyclesSinceRest() > 1){
                player.sufferHarm(1);
            }
            if (dayTime){
                day++;
            }
        }
    }
    //reveals the entire block terrain surrounding player after the flee into the wilderness from combat.
    public void mapRevealAfterFlee(){
        initMapReveal();
        player.setFled(false);
    }


    //GETTERS AND SETTERS
    public int getHEIGHT() {
        return HEIGHT;
    }
    public int getWIDTH() {
        return WIDTH;
    }
    public int getSTEPS_PER_CYCLE() {
        return STEPS_PER_CYCLE;
    }
    public int getDaysToSurvive() {
        return DAYS_TO_SURVIVE;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public boolean isGameWon() {
        return gameWon;
    }
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public boolean isDayTime() {
        return dayTime;
    }
    public int getDay() {
        return day;
    }
    public boolean isInCombat() {
        return inCombat;
    }
    public void setInCombat(boolean isInCombat){ inCombat = isInCombat;}
    public boolean isAnimalTaggedForRemoval() {
        return animalTaggedForRemoval;
    }
    public void setAnimalTaggedForRemoval(boolean animalTaggedForRemoval) {
        this.animalTaggedForRemoval = animalTaggedForRemoval;
    }
    public CombatEncounter getCurrentEncounter() {
        return currentEncounter;
    }
    public void setCurrentEncounter(CombatEncounter currentEncounter) {
        this.currentEncounter = currentEncounter;
    }

    public Player getPlayer(){
        return player;
    }
    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    public Map getMap() {
        return map;
    }
    public Base getBase() {
        return base;
    }
}
