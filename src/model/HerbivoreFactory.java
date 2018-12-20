package model;

import javafx.scene.image.Image;

public class HerbivoreFactory {

    private static final Image RABBIT_COMBAT_IMAGE = new Image("model/Resources/rabbit_pixelized.png");
    private static final Image DEER_COMBAT_IMAGE = new Image("model/Resources/deer_pixelized.png");
    private static final Image FISH_COMBAT_IMAGE = new Image("model/Resources/fish_pixelized.png");
    private static final Image RABBIT_GAME_IMAGE = new Image("model/Resources/rabbit.png");
    private static final Image DEER_GAME_IMAGE = new Image("model/Resources/deer.png");
    private static final Image FISH_GAME_IMAGE = new Image("model/Resources/fish.png");

    private Game game;

    public HerbivoreFactory(Game game){
        this.game = game;
    }

    //Here is where the specifics of each animal subType are defined.  Their values are given in their constructors,
    // and the factory returns a new carnivore/herbivore without the client having to worry about maintaining
    // consistency in the values passed to the constructor.  It is also very convenient for tweaking the values during
    // game play testing to have the source for all new carnivores/herbivores in the same spot.
    public Herbivore produceHerbivore(Vector2 pos, Herbivore.HerbType type){
        switch(type){
            case RABBIT:
                return new Herbivore(pos, game,1, 1, 0, new boolean[]{true, true, false}, RABBIT_COMBAT_IMAGE, RABBIT_GAME_IMAGE, type);
            case DEER:
                return new Herbivore(pos, game,1, 3, 0, new boolean[]{true, true, false}, DEER_COMBAT_IMAGE, DEER_GAME_IMAGE, type);
            case FISH:
                return new Herbivore(pos, game,1, 1, 0, new boolean[]{false, false, true}, FISH_COMBAT_IMAGE, FISH_GAME_IMAGE, type);
        }
        return null;
    }
}
