package eus.ehu.dif.recsys.core;

/**
 * Esta clase representa los productos evaluados, indicando el identificados y la valoración predicha
 */
public class ScoredId {
	private int id;
	private float rating;
	
	/**
	 * Constructor de la clase
	 * @param id el identificador del producto evaluado
	 * @param rating la valoración calculada
	 */
	public ScoredId(int id, float rating) {
		super();
		this.id = id;
		this.rating = rating;
	}
	
	/**
	 * Método que construye una instancia de la clase
	 * @param id el identificador del producto evaluado
	 * @param rating la valoración calculada
	 * @return una instancia de ScoredId
	 */
	public static ScoredId create(int id, float rating) {
		return new ScoredId(id, rating);
	}
	
	/**
	 * Devuelve el identificador del elemento evaluado
	 * @return el identificador
	 */
	public int getId() {
		return id;
	}

	/**
	 * Devuelve la valoración del producto evaluado
	 * @return la valoración calculada
	 */
	public float getScore() {
		return rating;
	}
	
}
