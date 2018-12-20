package model;

import java.util.Random;

public class Tree extends StationaryObject implements iRestable {

    //Private final variable. encounter chance when sleeping under tree
    private final int ENCOUNTER_CHANCE = 65;
    //Number of sticks the tree contains
    private int numberSticks;


    //Sticks are generated to have 1-3 sticks
    public Tree(Vector2 pos, Game game) {
        super(pos, game, false);
        Random rand = new Random();
        this.numberSticks = rand.nextInt(3) + 1;
    }

    //Sets sticks to 0, and returns the number of sticks harvested
    private int harvestSticks(){
            int returnVal = numberSticks;
            numberSticks = 0;
            return returnVal;
    }
    //When the player interacts with the tree, they harvest it for its sticks, using its gatherSticks function
    //the tree is then set null by the player class.  Implements the interactable interface
    @Override
    public void interact(Player player){
        player.gatherSticks(harvestSticks());
    }
    //implements the iRestable interface
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
            //Generates the game's encounter, but doesn't yet set game.inCombat to true. It leaves that responsibility
            // to the GameViewManager, in order to give the rest animation time to complete first.
            game.setCurrentEncounter(new RestingCombatEncounter(player, enemy));
        } else {
            // if no encounter is generated, the player regains all their HP, and their cyclesSinceRest are reset.
            player.heal(player.getMAX_HIT_POINTS());
            player.setCyclesSinceRest(0);
        }
    }
}


