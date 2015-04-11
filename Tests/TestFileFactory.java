/**
 * Created by Pierre on 11/04/2015.
 */

import org.junit.Test;

import java.io.File;

public class TestFileFactory {

    @Test
    public void tests_fileIsSplitInChunks() {
        AudioClient ac = new AudioClientImpl();
        File audioFile = new File("./firetrucks.wav");
        File[] fileArray = FileFactory.split(audioFile,32768);

    }
}
