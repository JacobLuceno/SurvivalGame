package model;

import java.util.Random;

public class CombatEncounter {


    private Player player;
    private Animal animal;
    private Game game;
    private boolean encounterOver;
    private boolean playersTurn;

    private boolean messageToDisplay;
    private boolean playerReady;


    private String message;


    private boolean animalsTurn;


    CombatEncounter(Player player, Animal animal){

        player.getGame().setAnimalTaggedForRemoval(true);
        animal.setRemove(true);
        this.player = player;
        this.animal = animal;
        playersTurn = false;
        animalsTurn = false;
        playerReady = false;
        messageToDisplay = false;
        animal.setFled(false);
        player.setFled(false);
        encounterOver = false;
        determineCombatOrder();
    }

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
                Vector2 playerInitialPos = player.getPosition();
                do {
                    player.setPosition(playerInitialPos.getDisplacementVector(game.getHEIGHT(), game.getWIDTH()));
                    System.out.println(player.getPosition().toString());
                } while(player.getPosition().getv1() < 0 || player.getPosition().getv1() >= player.getGame().getWIDTH()
                    || player.getPosition().getv0() < 0 || player.getPosition().getv0() >= player.getGame().getHEIGHT());
                message = "You flee into the wilderness!";
            }
            if (!player.isAlive()){
                message = "You've been mortally wounded.\nYou didn't survive.";
            }
            messageToDisplay = true;
        }
    }

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

    public boolean isAnimalsTurn() {
        return animalsTurn;
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

    public boolean isPlayerReady() {
        return playerReady;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageToDisplay(boolean messageToDisplay) {
        this.messageToDisplay = messageToDisplay;
    }

    public void setPlayerReady(boolean playerReady) {
        this.playerReady = playerReady;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
