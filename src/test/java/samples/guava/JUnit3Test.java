/*
 *  (c) tolina GmbH, 2014
 */
package samples.guava;

import jp.co.worksap.oss.findbugs.guava.UnexpectedAccessDetector;
import junit.framework.TestCase;

import org.junit.Ignore;

/**
 * Class used only for {@link UnexpectedAccessDetector}-tests
 * @author Juan Martin Sotuyo Dodero
 *
 */
@Ignore("Not a real test, but used as test sample")
public class JUnit3Test extends TestCase {

	void testCallVisibleForTestingMethod() {
		new MethodWithVisibleForTesting().method();
	}
}