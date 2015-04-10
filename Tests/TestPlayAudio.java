/**
 * Created by Pierre on 10/04/2015.
 */

import org.junit.Test;

import java.io.File;

public class TestPlayAudio {

    @Test
    public void tests_audioFileIsPlayed() {
        AudioClient ac = new AudioClientImpl();
        File audioFile = new File("./firetrucks.wav");
        ac.playAudio(audioFile);
    }
}
