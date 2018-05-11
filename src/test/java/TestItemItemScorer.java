
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import eus.ehu.dif.recsys.cbf.ItemItemScorer;

public class TestItemItemScorer {
	@Test
	public void test() {
		ItemItemScorer iScorer = ItemItemScorer.getItemScorer();
		assertEquals(4.6853, iScorer.score(2048, 161), 0.0001);
		assertEquals(4.1253, iScorer.score(2048, 788), 0.0001);
		assertEquals(3.8545, iScorer.score(2048, 36955), 0.0001);
		assertEquals(4.5102, iScorer.score(2048, 77), 0.0001);
	}

}
