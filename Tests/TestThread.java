/**
 * Created by Pierre on 26/04/2015.
 */

import org.junit.BeforeClass;
import org.junit.Test;

public class TestThread {

    private static AudioServerLauncher asl;

    @BeforeClass
    public static void buildUp() {
        asl = new AudioServerLauncher();
        asl.launch();
    }

    @Test
    public void tests_threads() {
        for (int i=1;i<10;i++) {
            AudioClientLauncher name = new AudioClientLauncher();
            name.launch();
        }
    }
}
