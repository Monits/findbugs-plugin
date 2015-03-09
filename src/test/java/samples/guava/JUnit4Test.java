/*
 *  (c) tolina GmbH, 2014
 */
package samples.guava;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Class used only for
 * {@link jp.co.worksap.oss.findbugs.guava.UnexpectedAccessDetector}-tests
 * 
 * @author tolina GmbH
 *
 */
@Ignore("Not a real test, but used as test sample")
public class JUnit4Test {

	@Before
	void setUp() {
		new MethodWithVisibleForTesting().method();
	}

	@BeforeClass
	void onlyOnce() {
		new MethodWithVisibleForTesting().method();
	}

	@After
	void tearDown() {
		new MethodWithVisibleForTesting().method();
	}

	@Test
	void test() {
		new MethodWithVisibleForTesting().method();
	}
}
