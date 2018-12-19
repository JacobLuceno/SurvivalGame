package model;

import java.util.Random;

public class Base extends StationaryObject implements iRestable {

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

    private BaseStatus baseStatus;
    private int curEncounterChance;
    private int curBaseCost;
    private int curToolCost;

    private final int [] ENCOUNTER_CHANCES = new int[]{60, 50, 25, 10};
    private final int [] COST_TO_UPGRADE_BASE = new int[]{100, 150, 200};
    private final int [] COST_TO_UPGRADE_TOOL = new int[]{25, 50, 100};

    private int baseIterator;
    private int toolIterator;

    public Base(Vector2 pos, Game game){
        super(pos, game, false);
        baseStatus = BaseStatus.CAMP;
        curEncounterChance = ENCOUNTER_CHANCES[0];
        baseIterator = 0;
        toolIterator = 0;
        curBaseCost = COST_TO_UPGRADE_BASE[baseIterator];
        curToolCost = COST_TO_UPGRADE_TOOL[toolIterator];
    }

    public void buyNewBaseUpgrade(Player player){
        if (player.spendSticks(curBaseCost)){
            upgradeBase();
        }

    }

    public void buyNewToolUpgrade(Player player){
        if (player.spendSticks(curToolCost)){
            player.upgradeTool();
            //iterate cost
            curToolCost=COST_TO_UPGRADE_TOOL[++toolIterator];
        }
    }

    @Override
    public void interact(Player player) {
        player.setInteractingWithBase(true);
    }

    @Override
    public void restAt(Player player) {

        Game game = player.getGame();
        //Determine if encounter
        Random rand = new Random();
        int encounterRoll = rand.nextInt(100)+1;
        //if Encounter happens generate new enemy and combat encounter during the rest cycle
        if (encounterRoll <= curEncounterChance){
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


    private void upgradeBase(){
        baseStatus = baseStatus.upgrade();
        curEncounterChance = ENCOUNTER_CHANCES[baseStatus.ordinal()];
        //Increment cost
        curBaseCost = COST_TO_UPGRADE_BASE[++baseIterator];
    }

    public int getCurBaseCost() {
        return curBaseCost;
    }

    public void setCurBaseCost(int curBaseCost) {
        this.curBaseCost = curBaseCost;
    }

    public int getCurToolCost() {
        return curToolCost;
    }

    public void setCurToolCost(int curToolCost) {
        this.curToolCost = curToolCost;
    }

}
