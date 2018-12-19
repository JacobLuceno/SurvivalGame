package model;

import java.util.Random;

public class RestingCombatEncounter extends CombatEncounter {

    public RestingCombatEncounter(Player player, Animal animal) {
        super(player, animal);
    }

    @Override
    protected void determineCombatOrder(){
        super.determineCombatOrder();
        setMessage("Your rest is interrupted by a hungry carnivore!");
    }

    @Override
    public void combatTurn() {
        Player player = getPlayer();
        Game game = player.getGame();
        super.combatTurn();
        if (isEncounterOver()) {
            if (player.isAlive() && !player.isFled()) {
                player.heal(player.getMAX_HIT_POINTS());
                player.setCyclesSinceRest(0);
                player.setStepsToday(game.getSTEPS_PER_CYCLE() + 1);
                game.checkDayCycle();
            }
            if (!getAnimal().isAlive()){
                setMessage("You slay the animal\nand get some much needed rest.");
                setMessageToDisplay(true);
            }
            if (player.isFled()) {
                Random rand = new Random();
                Map map = game.getMap();
                do {
                    player.setPosition(new Vector2(rand.nextInt(game.getHEIGHT()), rand.nextInt(game.getWIDTH())));
                }
                while (map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getTerrainType() != TerrainTile.TerrainType.GRASSLAND
                        || map.getTerrainMap()[(int) player.getPosition().getv0()][(int) player.getPosition().getv1()].getHasStatObj());
                setMessage("You flee into the wilderness, \nhoping to find shelter elsewhere!");
                setMessageToDisplay(true);
            }
            game.setCurrentEncounter(null);
        }
    }
}
