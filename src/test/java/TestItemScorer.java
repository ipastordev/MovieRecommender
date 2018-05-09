
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eus.ehu.dif.recsys.cbf.TFIDFItemScorer;

public class TestItemScorer {
	@Test
	public void test() {
		TFIDFItemScorer iScorer = TFIDFItemScorer.getItemScorer();

		assertEquals(0.1899, iScorer.score(4045, 2164), 0.0001);
		assertEquals(0.2612, iScorer.score(4045, 63), 0.0001);
		assertEquals(0.2363, iScorer.score(4045, 807), 0.0001);
		assertEquals(0.2059, iScorer.score(4045, 187), 0.0001);
		assertEquals(0.3596, iScorer.score(4045, 11), 0.0001);
	}

}
