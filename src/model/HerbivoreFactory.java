package model;

import javafx.scene.image.Image;

public class HerbivoreFactory {

    private static final Image RABBIT_COMBAT_IMAGE = new Image("model/Resources/rabbit_pixelized.png");
    private static final Image DEER_COMBAT_IMAGE = new Image("model/Resources/deer_pixelized.png");
    private static final Image FISH_COMBAT_IMAGE = new Image("model/Resources/fish_pixelized.png");
    private static final Image RABBIT_GAME_IMAGE = new Image("model/Resources/rabbit.png");
    private static final Image DEER_GAME_IMAGE = new Image("model/Resources/deer.png");
    private static final Image FISH_GAME_IMAGE = new Image("model/Resources/fish.png");




    public Herbivore produceHerbiore(Vector2 pos,Herbivore.HerbType type){
        switch(type){
            case RABBIT:
                return new Herbivore(pos, 1, 1, 3, new boolean[]{true, true, false}, RABBIT_COMBAT_IMAGE, RABBIT_GAME_IMAGE, type);
            case DEER:
                return new Herbivore(pos, 1, 3, 5, new boolean[]{true, false, false}, DEER_COMBAT_IMAGE, DEER_GAME_IMAGE, type);
            case FISH:
                return new Herbivore(pos, 1, 1, 2, new boolean[]{false, false, true}, FISH_COMBAT_IMAGE, FISH_GAME_IMAGE, type);
        }
        return null;
    }
}
