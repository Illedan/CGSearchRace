import com.codingame.gameengine.runner.MultiplayerGameRunner;
import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args) {

        // Uncomment this section and comment the other one to create a Solo Game
        /* Solo Game */

         SoloGameRunner gameRunner = new SoloGameRunner();

        // Sets the player
         gameRunner.setAgent(Agent1.class);

        // Sets a test case
         gameRunner.setTestCase("test701.json");

        // Another way to add a player
        // gameRunner.addAgent("python3 /home/user/player.py");
        

        gameRunner.start();
    }
}
