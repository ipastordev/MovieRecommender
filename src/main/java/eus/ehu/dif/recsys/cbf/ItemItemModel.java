package eus.ehu.dif.recsys.cbf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eus.ehu.dif.recsys.core.CosineSimilarity;
import eus.ehu.dif.recsys.core.ScoredId;
import eus.ehu.dif.recsys.core.SparseVector;
import eus.ehu.dif.recsys.dao.UserRatingDAO;

public class ItemItemModel {

	private static ItemItemModel mItemItemModel = new ItemItemModel();

	private Map<Integer, SparseVector> userRatings;
	private Map<Integer, List<ScoredId>> movieNeighborhood;

	private ItemItemModel() {

	}

	// Método que devuelve la única instancia
	public static ItemItemModel getItemItemModel() {
		return mItemItemModel;
	}

	public List<ScoredId> getNeighbors(int pItem) {
		ensureMovieNeighborhood();
		return movieNeighborhood.get(pItem);

	}
	
	public SparseVector getUserRatings(int pUser) {
		ensureMovieNeighborhood();
		return userRatings.get(pUser);
	}

	private void ensureMovieNeighborhood() {
		if (movieNeighborhood == null) {
			synchronized (this) {
				if (movieNeighborhood == null) {
					loadMovieNeighborhood();
				}
			}
		}
	}

	private void loadMovieNeighborhood() {
		movieNeighborhood = new HashMap<>();
		userRatings = new HashMap<>();
		UserRatingDAO userRatingDao = UserRatingDAO.getUserRatingDAO();
		Set<Integer> userIds = userRatingDao.getUserIds();
		Map<Integer, SparseVector> movieRatings = new HashMap<>();
		for (Integer pUserId : userIds) {
			SparseVector pUserRatings = userRatingDao.getUserRatings(pUserId).copy();
			userRatings.put(pUserId, pUserRatings);
			float userRatingAverage = pUserRatings.average();
			for (Integer movieId : pUserRatings.keySet()) {
				if (!movieRatings.containsKey(movieId)) {
					movieRatings.put(movieId, SparseVector.empty());
				}
				movieRatings.get(movieId).put(pUserId, pUserRatings.get(movieId) - userRatingAverage);
			}
		}
		
		CosineSimilarity cosineSimilarity = new CosineSimilarity();
		for (Entry<Integer, SparseVector> entry1 : movieRatings.entrySet()) {
			Integer movie1Id = entry1.getKey();
			SparseVector movie1Ratings = entry1.getValue();
			List<ScoredId> scores = new ArrayList<>();
			for (Entry<Integer, SparseVector> entry2 : movieRatings.entrySet()) {
				Integer movie2Id = entry2.getKey();
				if(!movie1Id.equals(movie2Id)) {
					SparseVector movie2Ratings = entry2.getValue();
					float cosine = cosineSimilarity.similarity(movie1Ratings, movie2Ratings);
					scores.add(new ScoredId(movie2Id, Math.max(0, cosine)));
				}
			}
			Collections.sort(scores, ScoredId.compareByScoreDesc());
			movieNeighborhood.put(movie1Id, scores);
		}
		

	}

	

}
