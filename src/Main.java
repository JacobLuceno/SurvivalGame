
//AUTHORS : JAKE LUCENO, MARISSA ALMOSLINO, JOSE VARGAS
//FOR : CSC 221 - Software Design Lab - Final Project - City College of New York
//Final Project Description:
/*Design the following project:
There is an artificial environment.
The agent moves freely on the field.
The field has some objects:
Trees: Object that are not movable. And the agent can rest under them or collect stick from them.
Rocks: Objects that are not movable and the agent can hide behind them.
Bushes: Object that are not movable. They may have berries that agent can eat from them.
Herbivores: Those animals that move on the grass land. Depending on the size they can be hunted by the user using different tools (e.g. Rabbit can be hunted by hand). Animals that move in the water.
Carnivore: Those that move in the grassland and desert. They can be hunted by the agent if the agent has spear. The big carnivores may attack the agent and kill it. Carnivore like Lion, Crocodiles, ... .
The agent can move freely, if it encounters a carnivore, it should change its direction or hide behind a rock. If the agent is on the sight of the carnivore then the carnivore may attack. The agent can scape and or resist. Depending on the tool the agent may manage to scape or get injured or get killed by the carnivore.
The tools are hand, steak, stone and spear.
The goal of the agent is to survive for a period of time until the rescue team arrives.
The agent should return to his base every day to have some rest.
The agent may get injured, depending on the injury, it should get rest under a tree and eat some food.
Agent has limited capacity and may collect limited amount of food per day.
Agent has limited sight and after that it does not now what is there; However, if he has visited the area, it has the map of the environment.
The agent should build a base around itself using sticks.
The sticks can be collected from the trees or from the land (if they are dropped there).
The base should get stronger every night so if a carnivore attacks it cannot get into it.
To move on night the agent should have a torch. Moving on night is not recommended because the sight of the agent is limited.
The program should be designed using javafx or swing library depending on which one you are more expert on (In class we talk about the javafx).
*/

import View.MusicManager;
import View.ViewManager;
import controller.ControlManager;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Game;


public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        ViewManager vm = new ViewManager(game);
        ControlManager cm = new ControlManager(vm.getScene(), game);
        primaryStage = vm.getStage();
        MusicManager mm = new MusicManager();
        primaryStage.show();
    }
}
