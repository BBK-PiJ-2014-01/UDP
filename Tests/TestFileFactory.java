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
        byte[] fileChunks = FileFactory.toByteArray(audioFile);
        File newFile = new File("./newfile.wav");

        newFile = FileFactory.fromByteArray(fileChunks, newFile.getPath());
        ac.playAudio(newFile);
    }
}
