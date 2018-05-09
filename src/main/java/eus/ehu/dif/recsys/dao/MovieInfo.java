package eus.ehu.dif.recsys.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieInfo {
	private int movieId;
	private String title;
	private List<String> tagList;
	private Set<String> genres;
	
	public MovieInfo(int pId, String pTitle) {
		movieId = pId;
		title = pTitle;
		tagList = new ArrayList<>();
		genres = new HashSet<>();
	}

	public int getMovieId() {
		return movieId;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public Set<String> getGenres() {
		return genres;
	}
	
	public void addGenre(String pGenre) {
		genres.add(pGenre);
	}
	
	public void addTag(String pTag) {
		tagList.add(pTag);
	}
	
	@Override
	public String toString() {
		String movie =String.format("Id:\t %d\tTitle:\t%s\n", getMovieId(), getTitle());
		movie += "Genres:\t" + getGenres().toString() + "\n";
		movie += "Tags:\t" + getTagList().toString() + "\n";
		return movie;
	}
	
}
