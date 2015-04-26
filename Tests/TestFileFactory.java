/**
 * Created by Pierre on 11/04/2015.
 */

import org.junit.Test;

import java.io.File;

public class TestFileFactory {

    @Test
    public void tests_audioFileIsConvertedToByteArray_convertedBackToFileThatCanBePlayed() {
        AudioClient ac = new AudioClientImpl();
        File audioFile = new File("./Tests/firetrucks.wav");
        // Converting an audioFile into a byte array
        byte[] fileChunks = FileFactory.toByteArray(audioFile);
        // Converting the byte array back to a .wav file
        File newFile = new File("./Tests/newfiretrucks.wav");
        newFile = FileFactory.fromByteArray(fileChunks, newFile.getPath());
        // Playing the newly created file
        ac.playAudio(newFile);
    }
}
