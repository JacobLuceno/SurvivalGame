package model;

public class HerbivoreFactory {

    public Herbivore produceHerbiore(Vector2 pos,Herbivore.HerbType type){
        switch(type){
            case RABBIT:
                return new Herbivore(pos, 1, 1, 3, new boolean[]{true, true, false}, type);
            case DEER:
                return new Herbivore(pos, 1, 3, 5, new boolean[]{true, false, false}, type);
            case FISH:
                return new Herbivore(pos, 1, 1, 2, new boolean[]{false, false, true}, type);
        }
        return null;
    }
}
