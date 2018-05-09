import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import eus.ehu.dif.recsys.cbf.ItemItemModel;
import eus.ehu.dif.recsys.core.ScoredId;

public class TestItemItemModel {

	@Test
	public void test() {
		
		ItemItemModel model = ItemItemModel.getItemItemModel();
		List<ScoredId> neighbors = model.getNeighbors(77);
		for (int i = 0; i < 5; i++) {
			System.out.println("ID: "+ neighbors.get(i).getId() + " - Score: "+ neighbors.get(i).getScore());
		}
		fail("Not yet implemented");
	}

}
