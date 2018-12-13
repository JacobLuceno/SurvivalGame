package model;

public class Base extends StationaryObject {

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

    public Base(Vector2 pos){
        super(pos, false);
        baseStatus = BaseStatus.CAMP;
    }

    @Override
    public void interact(Player player) {
        player.setInteractingWithBase(true);
    }

    public void upgradeBase(){
        baseStatus = baseStatus.upgrade();
    }
}
