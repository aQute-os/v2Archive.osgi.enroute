package osgi.enroute.logger.simple.provider;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 *
 *
 *
 */

public class LogForwardTest {

	@Test
	public void testVerySimple() {
		Logger logger = LoggerFactory.getLogger("Test");
		logger.debug("Hello");
	}
}
