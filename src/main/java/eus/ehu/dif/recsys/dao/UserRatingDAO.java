package eus.ehu.dif.recsys.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import eus.ehu.dif.recsys.core.SparseVector;

public class UserRatingDAO {
	private static UserRatingDAO mUserRatingDAo = new UserRatingDAO();
	private Map<Integer, SparseVector> userRatings;

	private UserRatingDAO() {
	}

	/**
	 * Método que devuelve la instancia única de la clase (Singleton)
	 * @return la instancia única de UserRatinDAO
	 */
	public static UserRatingDAO getUserRatingDAO() {
		return mUserRatingDAo;
	}

	/**
	 * Método que devuelve el conjunto de identificadores de los usuarios
	 * @return el conjunto de identicadores de los usuarios
	 */
	public Set<Integer> getUserIds() {
		ensureUserRatings();
		return userRatings.keySet();
	}
	
	/**
	 * Método que, dado el identificador de un usuario, devuelve 
	 * un vector con las valoraciones realizadas por el usuario
	 * @param pUserId el identificador del usuario
	 * @return las valoraciones realizadas por el usuario
	 */
	public SparseVector getUserRatings(int pUserId) {
		ensureUserRatings();
		return userRatings.get(pUserId).copy();
	}
	
	private void ensureUserRatings() {
		if (userRatings == null) {
			synchronized (this) {
				if (userRatings == null) {
					loadUserRatings();
				}
			}
		}
	}

	private void loadUserRatings() {
		try (Stream<String> fileStream = Files.lines(Paths.get(getClass()
				.getResource("/movie-ratings.csv").toURI()))) {
			userRatings  = new HashMap<>();
			fileStream.forEach(this::processRatingLine);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	private void processRatingLine(String pRatingRecord) {
		try (Scanner recordScanner = new Scanner(pRatingRecord)) {
			recordScanner.useDelimiter(",");
			recordScanner.useLocale(Locale.ROOT);
			int userId = recordScanner.nextInt();
			int movieId = recordScanner.nextInt();
			float rating = recordScanner.nextFloat();
			addRating(userId, movieId, rating);
		}
	}
	
	private void addRating(int userId, int movieId, float rating) {
		if (!userRatings.containsKey(userId)) {
			userRatings.put(userId, new SparseVector());
		}
		userRatings.get(userId).put(movieId, rating);
	}
}
