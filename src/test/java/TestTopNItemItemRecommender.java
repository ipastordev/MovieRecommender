
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import eus.ehu.dif.recsys.cbf.TopNItemRecommender;
import eus.ehu.dif.recsys.core.ScoredId;

public class TestTopNItemItemRecommender {
	@Test
	public void test() {

		TopNItemRecommender rec = TopNItemRecommender.getTopNItemRecommender();
		List<ScoredId> results = rec.recommend(2048);
		assertEquals(5, results.size());
		assertEquals(161, results.get(0).getId());
		assertEquals(640, results.get(1).getId());
		assertEquals(1422, results.get(2).getId());
		assertEquals(550, results.get(3).getId());
		assertEquals(807, results.get(4).getId());

	}
}
