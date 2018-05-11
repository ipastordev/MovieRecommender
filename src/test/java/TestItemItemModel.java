import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import eus.ehu.dif.recsys.cbf.ItemItemModel;
import eus.ehu.dif.recsys.core.ScoredId;

public class TestItemItemModel {

	@Test
	public void test() {
		ItemItemModel model = ItemItemModel.getItemItemModel();
		List<ScoredId> neighbors = model.getNeighbors(77);
		assertEquals(550, neighbors.get(0).getId());
		assertEquals(0.319184, neighbors.get(0).getScore(), 0.0001);
		assertEquals(629, neighbors.get(1).getId());
		assertEquals(0.307768, neighbors.get(1).getScore(), 0.0001);
		assertEquals(38, neighbors.get(2).getId());
		assertEquals(0.257384, neighbors.get(2).getScore(), 0.0001);
	}

}
