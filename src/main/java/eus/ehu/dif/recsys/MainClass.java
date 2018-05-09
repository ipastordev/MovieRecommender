package eus.ehu.dif.recsys;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eus.ehu.dif.recsys.cbf.TopNItemRecommender;
import eus.ehu.dif.recsys.core.ScoredId;
import eus.ehu.dif.recsys.dao.MovieDAO;

public class MainClass {

	public static void main(String[] args) {
		List<Integer> usersToRecommend = parseArgs(args);

		TopNItemRecommender cbfRec = TopNItemRecommender.getTopNItemRecommender();
		MovieDAO movieDAO = MovieDAO.getMovieDAO();
		
		for (Integer user : usersToRecommend) {
			System.out.println(String.format("\n\nRecommendations for user: %1$d", user));
			System.out.println("======================================");
			
			List<ScoredId> recommendations = cbfRec.recommend(user);
			for (ScoredId scoredId : recommendations) {
				int item = scoredId.getId();
				String title = movieDAO.getMovieTitle(item);
				float score = scoredId.getScore();
				System.out.println(String.format(Locale.ROOT,
						"User:\t%1$d\tMovie Id:\t%2$d\tScore:\t%3$.4f\tItem:\t%4$s\t", user, item, score,
						title));
			}
		}
	}

	private static List<Integer> parseArgs(String[] args) {
			Pattern pat = Pattern.compile("(\\d+)");
			List<Integer> ids = new ArrayList<>();
			for (String arg : args) {
				Matcher m = pat.matcher(arg);
				if (m.matches()) {
					int uid = Integer.parseInt(m.group(1));
					ids.add(uid);
				} else {
					System.err.println("unparseable command line argument");
				}
			}
			return ids;
	}

}
