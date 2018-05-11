package eus.ehu.dif.recsys.cbf;

import java.util.Collection;
import java.util.Iterator;

import eus.ehu.dif.recsys.core.ScoredId;
import eus.ehu.dif.recsys.core.SparseVector;

public class ItemItemScorer implements ItemScorer {

	private static ItemItemScorer mItemScorer = new ItemItemScorer();
	private ItemItemModel item = ItemItemModel.getItemItemModel();

	private ItemItemScorer() {
	}

	/**
	 * Devuelve la instancia única de la clase (Singleton)
	 * 
	 * @return
	 */
	public static ItemItemScorer getItemScorer() {
		return mItemScorer;
	}

	/**
	 * Evalua una lista de productos
	 * 
	 * @param pUser
	 *            El usuario para el que se quiere calcular las valoraciones de los
	 *            productos
	 * @param scores
	 *            los productos cuya valoración se quiere calcular (predecir)
	 */
	public void score(int pUser, SparseVector scores) {
		SparseVector userRatings = item.getUserRatings(pUser);
		for (Integer itemId : scores.keySet()) {
			Iterator<ScoredId> neighbors = item.getNeighbors(itemId).iterator();
			int count = 0;
			float sumprod = 0;
			float sum = 0;
			while (neighbors.hasNext()) {
				ScoredId score = neighbors.next();
				if (userRatings.containsKey(score.getId())) {
					sum += score.getScore();
					sumprod += score.getScore() * userRatings.get(score.getId());
					count++;
					if (count == 20) {
						break;
					}
				}
			}
			if (sum > 0) {
				scores.put(itemId, sumprod / sum);
			}
		}
	}

	/**
	 * Evalua una lista de productos
	 * 
	 * @param pUser
	 *            El usuario para el que se quiere calcular las valoraciones de los
	 *            productos
	 * @param pItems
	 *            los productos cuya valoración se quiere calcular (predecir)
	 * @return un vector con las valoraciones de los productos
	 */
	public SparseVector score(int pUser, Collection<Integer> pItems) {
		SparseVector itemScores = SparseVector.create(pItems);
		score(pUser, itemScores);
		return itemScores;
	}

}
