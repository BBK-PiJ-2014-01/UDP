/**
 * Created by Pierre on 03/04/2015.
 */

public class AudioServerLauncher {
    public static void main(String[] args) {
        AudioServerLauncher asl = new AudioServerLauncher();
        asl.launch();
    }

    private void launch() {
        AudioService audioService = new AudioServer();

    }
}
