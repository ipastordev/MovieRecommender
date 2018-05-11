package eus.ehu.dif.recsys.cbf;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;

import eus.ehu.dif.recsys.core.ScoredId;
import eus.ehu.dif.recsys.core.SparseVector;
import eus.ehu.dif.recsys.dao.MovieDAO;
import eus.ehu.dif.recsys.dao.UserRatingDAO;

/**
 * Esta clase permite realizar las recomendaciones al usuario utilizando la
 * técnica Content Based Filtering
 */
public class TopNItemRecommender {
	private static TopNItemRecommender mTopNItemRecommender = new TopNItemRecommender();

	private ItemScorer scorer = ItemItemScorer.getItemScorer();
	
	private TopNItemRecommender() {
	}

	/**
	 * Método que devuelve la instancia única de la clase (Singleton)
	 * 
	 * @return la instancia de recomendador
	 */
	public static TopNItemRecommender getTopNItemRecommender() {
		return mTopNItemRecommender;
	}

	/**
	 * Devuelve la lista de 5 mejores recomendaciones
	 * 
	 * @param pUser
	 *            el usuario para el que se quieren obtener las recomendaciones
	 * @return las 5 mejores recomendaciones
	 */
	public List<ScoredId> recommend(int pUser) {
		return recommend(pUser, 5);
	}

	/**
	 * Devuelve la lista de mejores recomendaciones
	 * 
	 * @param pUser
	 *            el usuario para el que se quieren obtener las recomendaciones
	 * @param pNumRecs
	 *            el numero de recomendaciones deseado
	 * @return las pNumRecs mejores recomendaciones
	 */
	public List<ScoredId> recommend(int pUser, int pNumRecs) {
		// 1.- Obtener los elementos a evaluar
		Set<Integer> peliculasPosibles = getCandidates(pUser);
		// 2.- Obtener las "valoraciones" de las peliculas
		SparseVector valoraciones = scorer.score(pUser, peliculasPosibles);
		// 3.- Ordenar las peliculas por valoracion
		// List<ScoredId> listaValoraciones = new ArrayList();
		// for (Integer id : valoraciones.keySet()) {
		// float valoracion = valoraciones.get(id);
		// ScoredId valoracionPelicula = ScoredId.create(id, valoracion);
		// listaValoraciones.add(valoracionPelicula);
		// }
		// //los ordenad de mnemor a mayor, y le damos la vuelta
		// listaValoraciones.sort(comparing(ScoredId::getScore).reversed());
		// // 4.- Devolver la lista con las pNumRecs primeras peliculas
		// return listaValoraciones.subList(0, pNumRecs);
		
		//java 8
		List<ScoredId> listaValoraciones = valoraciones.keySet().stream()
				.map(id -> ScoredId.create(id, valoraciones.get(id)))
				.sorted(comparing(ScoredId::getScore).reversed())
				.limit(pNumRecs)
				.collect(toList());	
		
		System.out.println("Recommendations for user: "+pUser);
		System.out.println("======================================");
		System.out.println("");
		for (ScoredId scoredId : listaValoraciones) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("User: "+pUser+" ");
			stringBuilder.append("Item id: "+scoredId.getId()+" ");
			stringBuilder.append("Score: "+scoredId.getScore()+" ");
			stringBuilder.append("Item: "+MovieDAO.getMovieDAO().getMovieTitle(scoredId.getId())+" ");
			System.out.println( stringBuilder.toString());
		}

		return listaValoraciones;

	}

	private Set<Integer> getCandidates(int pUser) {
		Set<Integer> conjPeliculas = MovieDAO.getMovieDAO().getMovieIds();
		Set<Integer> conPeliculasUsuario = UserRatingDAO.getUserRatingDAO().getUserRatings(pUser).keySet();
		conjPeliculas.removeAll(conPeliculasUsuario);
		return conjPeliculas;

	}

}
