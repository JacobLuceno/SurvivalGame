package model;

import java.util.Random;

public class Base extends StationaryObject implements iRestable {

    //An enum to refer to the different types of bases in the game
    public enum BaseStatus {CAMP, SHACK, HOUSE, FORT;
        public BaseStatus upgrade(){
            try {
                return values()[ordinal() + 1];
            }
            catch(Exception e) {
                return values()[ordinal()];
            }
        }
    }

    //Final static value representing the costs of upgrades, and the progression of the decreasing encounterChance.
    private final int [] ENCOUNTER_CHANCES = new int[]{60, 50, 25, 10};
    private final int [] COST_TO_UPGRADE_BASE = new int[]{50, 100, 150};
    private final int [] COST_TO_UPGRADE_TOOL = new int[]{25, 50, 100};

    //This Base's current status
    private BaseStatus baseStatus;
    //Chance of Generating a combatEncounter when resting at base.
    private int curEncounterChance;
    //Cost to upgrade to the next Base status
    private int curBaseCost;
    //Cost to upgrade to the next tool.
    //Upgrading tools can only be done at base, to force the player to return to
    //base regularly.  Abstractly, it represents the player's need for time and a place to work when crafting new
    //tools.
    private int curToolCost;

    //A flag for the UI to change the base's sprite to the new upgrade.
    private boolean beenUpgraded;

    public Base(Vector2 pos, Game game){
        super(pos, game, false);
        beenUpgraded= false;
        //First Base status
        baseStatus = BaseStatus.CAMP;
        //set Costs and encounter chance.
        curEncounterChance = ENCOUNTER_CHANCES[0];
        curBaseCost = COST_TO_UPGRADE_BASE[0];
        curToolCost = COST_TO_UPGRADE_TOOL[0];
    }

    //Upgrade Functions:
    //buyNewBaseUpgrade - Checks to see if the Base is maximally upgraded, and if not calls the player's spendSticks
    //                    function, which returns a boolean value indicating if the player had enough sticks to make the
    //                    purchase.  If so,it calls upgradeBase, which iterates the BaseStatus, EncounterChance, and
    //                    Cost.
    //upgradeBase - subroutine to iterate the base's state related variables.
    //buyNewToolUpgrade - Checks to see if the tool is maximally upgraded, and if not calls the player's spendSticks
    //                    function, which returns a boolean value indicating if the player had enough sticks to make the
    //                    purchase.  If so, calls the player's upgradeTool function, and iterates cost to upgrade.
    public void buyNewBaseUpgrade(Player player) {
        if (baseStatus.ordinal() < 3) {
            if (player.spendSticks(curBaseCost)) {
                upgradeBase();
            }
        }
    }
    private void upgradeBase(){
        //Enum function which pushes current status to next status based on ordinal position.
        baseStatus = baseStatus.upgrade();
        curEncounterChance = ENCOUNTER_CHANCES[baseStatus.ordinal()];
        //iterate cost, if there is another upgrade
        if (baseStatus.ordinal() < COST_TO_UPGRADE_BASE.length) {
            curBaseCost = COST_TO_UPGRADE_BASE[baseStatus.ordinal()];
        }
        //flag the UI to change the sprite.
        beenUpgraded = true;
    }
    public void buyNewToolUpgrade(Player player){
        if (player.getTool().ordinal() < 3) {
            if (player.spendSticks(curToolCost)) {
                player.upgradeTool();
                //iterate cost, if there is another upgrade
                if (player.getTool().ordinal() < COST_TO_UPGRADE_TOOL.length) {
                    curToolCost = COST_TO_UPGRADE_TOOL[player.getTool().ordinal()];
                }
            }
        }
    }

    @Override
    public void interact(Player player) {
        // Empty function call.  This is implementation of the Interactable interface.  The reason it is here is that it
        //conveniently disables the player's ability to move into this space.  If an interactable object can be walked
        //into or over, this method will manually set the player's location.  Since the location is not set in this
        //function, it serves to allow bases to exist as impassable terrain to the player.
    }

    //Implements the iRestable interface.  Allows the player to rest at base, with base specific chance of combat
    //encounter.
    @Override
    public void restAt(Player player) {

        Game game = getGame();
        //Determine if encounter
        Random rand = new Random();
        int encounterRoll = rand.nextInt(100)+1;
        //if Encounter happens generate new enemy and combat encounter during the rest cycle
        if (encounterRoll <= curEncounterChance){
            CarnivoreFactory cf = new CarnivoreFactory(game);
            Carnivore enemy = null;
            //Gives 2/3 chance of Wolf, 1/3 chance of lion.
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
        }
        // if no encounter is generated, the player regains all their HP, and their cyclesSinceRest are reset.
        else {
            player.heal(player.getMAX_HIT_POINTS());
            player.setCyclesSinceRest(0);
        }
    }


    //Getters and setters:

    public int getCurBaseCost() {
        return curBaseCost;
    }

    public int getCurToolCost() {
        return curToolCost;
    }

    public boolean isBeenUpgraded() {
        return beenUpgraded;
    }

    public void setBeenUpgraded(boolean beenUpgraded) {
        this.beenUpgraded = beenUpgraded;
    }

    public BaseStatus getBaseStatus() {
        return baseStatus;
    }

}
