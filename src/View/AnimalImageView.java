package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Animal;

public class AnimalImageView extends ImageView {

    private Animal animal;

    public AnimalImageView(Animal animal) {
        this.animal = animal;
    }

    public AnimalImageView(String url, Animal animal) {
        super(url);
        this.animal = animal;
    }

    public AnimalImageView(Image image, Animal animal) {
        super(image);
        this.animal = animal;
    }

    public Animal getAnimal() {
        return animal;
    }
}
