package model;

import java.util.Random;

public class Tree extends StationaryObject implements iRestable {
    private boolean hasSticks;
    private int numberSticks;
    private final int ENCOUNTER_CHANCE = 65;



    public Tree(Vector2 pos, Game game) {
        super(pos, game, false);
        Random rand = new Random();
        this.numberSticks = rand.nextInt(3) + 1;
        if (this.numberSticks != 0) {
            hasSticks = true;
        }
    }

    public int harvestSticks(){
            hasSticks = false;
            int returnVal = numberSticks;
            numberSticks = 0;
            return returnVal;
    }
    @Override
    public void interact(Player player){
        player.gatherSticks(harvestSticks());
        System.out.println(player.getSticks());
    }

    //Getters
    public boolean getHasSticks() {
        return hasSticks;
    }
    public int getNumberSticks() {
        return numberSticks;
    }

    @Override
    public void restAt(Player player) {
        Game game = player.getGame();
        //Determine if encounter
        Random rand = new Random();
        int encounterRoll = rand.nextInt(100)+1;
        //if Encounter happens generate new enemy and combat encounter during the rest cycle
        if (encounterRoll <= ENCOUNTER_CHANCE){
            CarnivoreFactory cf = new CarnivoreFactory(game);
            Carnivore enemy = null;
            int enemyType = rand.nextInt(3);
            switch (enemyType){
                case 0:
                case 1:
                    enemy = cf.produceCarnivore(new Vector2(0,0), Carnivore.CarnType.WOLF);
                    break;
                case 2:
                    enemy = cf.produceCarnivore(new Vector2(0,0), Carnivore.CarnType.LION);
                    break;
            }
            game.setCurrentEncounter(new RestingCombatEncounter(player, enemy));
        } else {
            player.heal(player.getMAX_HIT_POINTS());
            player.setCyclesSinceRest(0);
        }
    }
}


