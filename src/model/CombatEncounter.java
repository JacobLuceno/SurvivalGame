package model;

import javafx.scene.image.Image;

import java.util.Random;

public class CombatEncounter {


    private Player player;
    private Animal animal;
    private Game game;
    private boolean encounterOver;
    private boolean playersTurn;


    private boolean animalsTurn;

    CombatEncounter(Player player, Animal animal){
        this.player = player;
        this.animal = animal;
        playersTurn = false;
        animalsTurn = false;
        animal.setFled(false);
        player.setFled(false);
        encounterOver = false;
        determineCombatOrder();
        System.out.println("Combat encounter created.");
    }

    private void determineCombatOrder(){
        Random rand = new Random();
        int encounterOrder = rand.nextInt(2);
        switch(encounterOrder){
            case 0:
                playersTurn = true;
                break;
            default:
                animalsTurn = true;
        }
    }

    public void combatTurn(){
        if (!player.isAlive() || !animal.isAlive() || player.isFled() || animal.isFled()){
            encounterOver = true;
            animal.setRemove(true);
        }
        if(!encounterOver) {
            if (animalsTurn && animal.isAlive()) {
                animal.combatLogic();
                animalsTurn = false;
                playersTurn = true;
            }
        } else {
            if (!animal.isAlive()){
                System.out.println("Player slayed animal");
            } else if (player.isFled()){
                player.setPosition(TerrainTile.getBaseLocation().getDisplacementVector(5,5));
                System.out.println("Player has fled");
            }
            Game.setInCombat(false);
            Game.setCurrentEncounter(null);

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



}
