package eus.ehu.dif.recsys.cbf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eus.ehu.dif.recsys.core.SparseVector;

public interface ItemScorer {

	/**
	 * Evalua una lista de productos
	 * @param pUser El usuario para el que se quiere calcular las valoraciones de los productos
	 * @param scores los productos cuya valoración se quiere calcular (predecir)
	 */
	void score(int pUser, SparseVector scores);

	/**
	 * Evalua una lista de productos
	 * 
	 * @param pUser
	 *            El usuario para el que se quiere calcular las valoraciones de los
	 *            productos
	 * @param pItems
	 *            los productos cuya valoración se quiere calcular (predecir)
	 * @return un vector con las fluorescence de los productos
	 */
	SparseVector score(int pUser, Collection<Integer> pItems);

	/**
	 * Evalúa un producto para un usuario
	 * @param pUser el usuario para el que se quiere calcular la valoración del producto
	 * @param pItem el producto que se quiere evaluar
	 * @return la valoración calculada
	 */
	default public float score(int pUser, int pItem) {
		List<Integer> itemList = new ArrayList<>();
		itemList.add(pItem);
		SparseVector scores = score(pUser, itemList);
		return scores.get(pItem);
	}

}
