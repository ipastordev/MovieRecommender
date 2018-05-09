package eus.ehu.dif.recsys.cbf;

import java.util.Collection;

import eus.ehu.dif.recsys.core.CosineSimilarity;
import eus.ehu.dif.recsys.core.SparseVector;
import eus.ehu.dif.recsys.dao.UserRatingDAO;

/**
 * Esta clase representa el componente encargado de predecir las valoraciones de
 * los productos utilizando la técnica Content Based Filtering
 */
public class TFIDFItemScorer implements ItemScorer {
	private static TFIDFItemScorer mItemScorer = new TFIDFItemScorer();
	private TFIDFModel tfidf = TFIDFModel.getTFIDFModel();

	private TFIDFItemScorer() {
	}

	/**
	 * Devuelve la instancia única de la clase (Singleton)
	 * 
	 * @return
	 */
	public static TFIDFItemScorer getItemScorer() {
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
		// 1.- Obtener una instancia de CosineSimilarity
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		// 2.- Obtener el Modelo del usuario
		SparseVector modeloUsuario = getModeloUsuario(pUser);
		// 3.- Para cada pelicula, obtener su modelo y calcular la semejanza
		for (Integer id : scores.keySet()) {
			// Obtener el modelo de la pelicula id
			SparseVector modeloPelicula = tfidf.getItemVector(id);
			float semejanza = cosineSimilarity.similarity(modeloUsuario, modeloPelicula);
			scores.put(id, semejanza);

		}
	}

	private SparseVector getModeloUsuario(int pUser) {
		// Obtener el modelo del usuario a partir de sus valoraciones
		SparseVector valoracionesUsuario = UserRatingDAO.getUserRatingDAO().getUserRatings(pUser);
		if (valoracionesUsuario == null) {
			return SparseVector.empty();
		} else {
			// Calcular el modelo del usuario e base a las peliculas que ha valorado con
			// puntuacion superior o igual a 3.5
			SparseVector modeloUsuario = tfidf.newTagVector();
			for (Integer id : valoracionesUsuario.keySet()) {
				if (valoracionesUsuario.get(id) >= 3.5f) {
					SparseVector modeloPelicula = tfidf.getItemVector(id);
					modeloUsuario.add(modeloPelicula);
				}
			}
			return modeloUsuario;
		}
	}

	/**
	 * Evalua una lista de productos
	 * 
	 * @param pUser
	 *            El usuario para el que se quiere calcular las valoraciones de los
	 *            productos
	 * @param pItems
	 *            os productos cuya valoración se quiere calcular (predecir)
	 * @return un vector con las valoraciones de los productos
	 */
	public SparseVector score(int pUser, Collection<Integer> pItems) {
		SparseVector itemScores = SparseVector.create(pItems);
		score(pUser, itemScores);
		return itemScores;
	}

}
