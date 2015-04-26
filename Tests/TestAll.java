/**
 * Created by Pierre on 26/04/2015.
 * Suite of all unit tests written for the UDP project
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestClientProtocol.class,
        TestServerProtocol.class,
        TestFileFactory.class,
        TestPlayAudio.class,

})
public class TestAll {
}
