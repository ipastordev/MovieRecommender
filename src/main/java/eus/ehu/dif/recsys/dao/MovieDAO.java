package eus.ehu.dif.recsys.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MovieDAO {
	private static MovieDAO mMovieDAO = new MovieDAO();
	private Map<Integer, MovieInfo> moviesInfo;
	private Set<String> tagCatalog;

	private MovieDAO() {

	}

	/**
	 * Método que devuelve la instancia unica de la clase (Singleton)
	 * @return la instancia de MovieDAO
	 */
	public static MovieDAO getMovieDAO() {
		return mMovieDAO;
	}

	/**
	 * Método que devuelve la lista de identificadores de películas
	 * @return la lista de películas
	 */
	public Set<Integer> getMovieIds() {
		ensureLoadedData();
		return moviesInfo.keySet();
	}

	/**
	 * Método que, dado el identificador de una película, devuelve su título
	 * @param pId el identificador de la película
	 * @return el título de la película
	 */
	public String getMovieTitle(int pId) {
		ensureLoadedData();
		if (moviesInfo.containsKey(pId)) {
			return moviesInfo.get(pId).getTitle();
		} else {
			return "UNKNOWN";
		}
	}

	/**
	 * Método que, dado el identificador de una película, devuelve la lista de etiquetas
	 * de la perícula
	 * @param pId el identificador de la película
	 * @return la lista de etiquetas de la película
	 */
	public List<String> getMovieTags(Integer pId) {
		ensureLoadedData();
		if (moviesInfo.containsKey(pId)) {
			return moviesInfo.get(pId).getTagList();
		} else {
			return new ArrayList<>();
		}
	}
	
	/**
	 * Método que devuelve el catálogo de etiquetas utilizadas para decribir las 
	 * películas
	 * @return el conjunto de etiquetas utilizadas
	 */
	public Set<String> getTagCatalog() {
		ensureLoadedData();
		return tagCatalog;
	}

	private void ensureLoadedData() {
		if (moviesInfo == null) {
			synchronized (this) {
				if (moviesInfo == null) {
					try (Stream<String> movieInfoStream = Files
							.lines(Paths.get(getClass().getResource("/movie-titles.csv").toURI()));
							Stream<String> movieTagStream = Files
									.lines(Paths.get(getClass().getResource("/movie-tags.csv").toURI()))) {
						moviesInfo = new HashMap<>();
						// Register movie info
						movieInfoStream.forEach(this::processMovieInfo);
						// Add tags
						tagCatalog = new HashSet<>();
						movieTagStream.forEach(this::processTag);

					} catch (IOException e) {
						System.err.println(e.getMessage());
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

	private void processMovieInfo(String pMovieInfo) {
		try (Scanner movieInfoScanner = new Scanner(pMovieInfo)) {
			movieInfoScanner.useDelimiter(Pattern.compile("[;|]"));
			int id = movieInfoScanner.nextInt();
			String title = movieInfoScanner.next();
			MovieInfo movieInfo = new MovieInfo(id, title);
			while (movieInfoScanner.hasNext()) {
				movieInfo.addGenre(movieInfoScanner.next());
			}
			moviesInfo.put(id, movieInfo);
		}
	}

	private void processTag(String pTagInfo) {
		try (Scanner tagInfoScanner = new Scanner(pTagInfo)) {
			tagInfoScanner.useDelimiter(";");
			int movieId = tagInfoScanner.nextInt();
			String tag = tagInfoScanner.next();
			addTag(movieId, tag);
		}
	}

	private void addTag(int movieId, String tag) {
		if (moviesInfo.containsKey(movieId)) {
			moviesInfo.get(movieId).addTag(tag);
			tagCatalog.add(tag);
		}

	}



}
