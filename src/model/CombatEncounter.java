package model;

import java.util.Random;

public class CombatEncounter {

    //Objects specific to the CombatEncounter which define it
    private Player player;
    private Animal animal;
    private Game game;

    //Booleans used in turn logic and defining the state of the encounter
    private boolean encounterOver;
    private boolean playersTurn;
    private boolean animalsTurn;

    //Flag and string passed to the UI to construct the combat system's dialog boxes
    private boolean messageToDisplay;
    private String message;



    CombatEncounter(Player player, Animal animal){
        player.getGame().setAnimalTaggedForRemoval(true);
        animal.setRemove(true);
        this.player = player;
        this.animal = animal;
        playersTurn = false;
        animalsTurn = false;
        messageToDisplay = false;
        animal.setFled(false);
        player.setFled(false);
        encounterOver = false;
        determineCombatOrder();
    }
    //50/50 chance of who goes first
    protected void determineCombatOrder(){
        Random rand = new Random();
        int encounterOrder = rand.nextInt(2);
        switch(encounterOrder){
            case 0:
                playersTurn = true;
                message = "You seize the initiative!";
                break;
            default:
                animalsTurn = true;
                message = "The animal moves quickly!";
        }
        messageToDisplay = true;
    }

    //Checks to make sure the encounter is not over, if the animal's turn, it implements its combat logic and sets the
    //message depending on whether its a carnivore or an herbivore.  If the encounter is over, the function determines
    // the results.  if the player kills the animal it gets its meat.  if the player flees from a carinvore they flee
    //to a random map location.  if they flee from an herbivore they simply appear in the same spot, signifying they
    //let the animal go.  if the animal flees the player doesnt move and gets no meat.  if the player dies, there is a
    //message displayed before the game ends
    public void combatTurn(){
        if (!player.isAlive() || !animal.isAlive() || player.isFled() || animal.isFled()){
            encounterOver = true;
        }
        if(!encounterOver) {
            if (animalsTurn && animal.isAlive() && !animal.isFled()) {
                if(animal.combatLogic(player)) {
                    if (animal instanceof Carnivore) {
                        message = "The animal attacks viciously!\n It bites you!";
                    } else {
                        message = "The animal attempts to flee!";
                    }
                } else{
                    if (animal instanceof Carnivore) {
                        message = "The animal attacks viciously!\n You dodge its attack!";
                    } else {
                        message = "The animal attempts to flee!\n You stop it from escaping";
                    }
                }
                messageToDisplay = true;
                animalsTurn = false;
                playersTurn = true;
            }
        } else {
            if (!animal.isAlive()){
                message = "You've slayed the animal!";
                decodeMeats();
            } else if (animal.isFled()){
                message = "It gets away!";
            } else if (player.isFled()){
                if (animal instanceof Carnivore) {
                    Random rand = new Random();
                    Map map = game.getMap();
                    do {
                        player.setPosition(new Vector2(rand.nextInt(game.getHEIGHT()), rand.nextInt(game.getWIDTH())));
                    }
                    while (map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getTerrainType() != TerrainTile.TerrainType.GRASSLAND
                        || map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getHasStatObj());
                    message = "You flee into the wilderness!";
                } else {
                    if (animal instanceof Herbivore){
                        if (((Herbivore) animal).getType() != Herbivore.HerbType.FISH){
                            message = "You let the animal scamper off.";
                        } else{
                            message = "You let the fish swim off.";
                        }
                    }
                }
            }
            if (!player.isAlive()){
                message = "You've been mortally wounded.\nYou didn't survive.";
            }
            messageToDisplay = true;
        }
    }

    //Subroutine to determine which type of food to award the player
    private void decodeMeats(){
        if (animal instanceof Carnivore){
            switch(((Carnivore) animal).getType()){
                case LION:
                    player.getInventory().insertFood(Food.FoodType.Lion);
                    break;
                case WOLF:
                    player.getInventory().insertFood(Food.FoodType.Wolf);
                    break;
                case CROC:
                    player.getInventory().insertFood(Food.FoodType.Crocodile);
                    break;
            }
        } else if (animal instanceof Herbivore){
            switch(((Herbivore) animal).getType()){
                case RABBIT:
                    player.getInventory().insertFood(Food.FoodType.Rabbit);
                    break;
                case DEER:
                    player.getInventory().insertFood(Food.FoodType.Deer);
                    break;
                case FISH:
                    player.getInventory().insertFood(Food.FoodType.Fish);
                    break;
            }
        }
    }

    //Getters and Setters
    public void setGame (Game game){ this.game = game;}
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }

    public void setAnimalsTurn(boolean animalsTurn) {
        this.animalsTurn = animalsTurn;
    }

    public boolean isEncounterOver() {
        return encounterOver;
    }

    public boolean isMessageToDisplay() {
        return messageToDisplay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageToDisplay(boolean messageToDisplay) {
        this.messageToDisplay = messageToDisplay;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
