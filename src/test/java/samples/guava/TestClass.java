/*
 *  (c) tolina GmbH, 2014
 */
package samples.guava;

import jp.co.worksap.oss.findbugs.guava.UnexpectedAccessDetector;

import com.google.common.annotations.VisibleForTesting;

/**
 * Class used only for {@link UnexpectedAccessDetector}-tests
 * @author tolina GmbH
 *
 */
public class TestClass {

	@VisibleForTesting
	void test() {
		return;
	}

}