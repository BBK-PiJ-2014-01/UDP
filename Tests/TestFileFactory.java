/**
 * Created by Pierre on 11/04/2015.
 */

import org.junit.Test;
import static org.junit.Assert.assertEquals;

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

    @Test
    public void tests_concatenateBytesArray_ReturnsCorrectConcatenatedArray() {
        String message1 = "Pierre";
        byte[] byteArray1 = message1.getBytes();
        String message2 = "Meyer";
        byte[] byteArray2 = message2.getBytes();

        String expectedMessage = "PierreMeyer";

        byte[] actualByteArray = FileFactory.concatenateByteArrays(byteArray1, byteArray2);
        assertEquals("Incorrect byte array generated", expectedMessage, new String(actualByteArray));
    }

}
