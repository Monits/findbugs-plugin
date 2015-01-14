/*
 *  (c) tolina GmbH, 2014
 */
package samples.guava;

import jp.co.worksap.oss.findbugs.guava.UnexpectedAccessDetector;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Class used only for {@link UnexpectedAccessDetector}-tests
 * @author tolina GmbH
 *
 */
@Ignore("Not a real test, but used as test sample")
public class JUnit4Test {

	@Test
	void test() {
		new MethodWithVisibleForTesting().method();
	}
}