package View;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;

public class MusicManager {


    public MusicManager(){
        File file = new File("src/View/Resources/videogame_music_demo.mp3");
        Media gameMusic = new Media(file.toURI().toString());
        MediaPlayer mp = new MediaPlayer(gameMusic);
        mp.play();
        mp.setOnEndOfMedia(() -> mp.seek(new Duration(0)));
    }
}
