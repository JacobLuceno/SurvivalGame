package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Animal;

public class AnimalImageView extends ImageView {

    private Animal animal;


    private boolean animationTriggered;


    public AnimalImageView(Image image, Animal animal) {
        super(image);
        this.animal = animal;
        animationTriggered = false;
    }

    public Animal getAnimal() {
        return animal;
    }

    public boolean isAnimationTriggered() {
        return animationTriggered;
    }

    public void setAnimationTriggered(boolean animationTriggered) {
        this.animationTriggered = animationTriggered;
    }
}
