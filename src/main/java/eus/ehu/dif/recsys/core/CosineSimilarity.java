package eus.ehu.dif.recsys.core;

/**
 * Esta clase permite calcular la similitud entre dos vectores mediante el coseno del ángulo entre los mismos
 */
public class CosineSimilarity {

	/**
	 * Calcula la similitud 
	 * @param pV1 el primer vector
	 * @param pV2 el segundo vector
	 * @return el coseno del ángulo entre los dos vectores
	 */
	public float similarity(SparseVector pV1, SparseVector pV2) {
		return pV1.dotProduct(pV2)/(pV1.norm()*pV2.norm());
	}
}
