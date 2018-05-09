package eus.ehu.dif.recsys.core;

import static java.lang.Math.sqrt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Esta clase representa los vectores "poco densos". Incluye las operaciones
 * básicas a realizar sobre los vectores
 */
public class SparseVector {

	private Map<Integer, Float> vectorValues;

	/**
	 * Constructor. Crea un vector vacío
	 */
	public SparseVector() {
		vectorValues = new HashMap<>();
	}

	/**
	 * Crea un vector para los productos indicados, con valor 0
	 * 
	 * @param pKeySet
	 *            los identificadores de los productos recogidos en el vector
	 */
	public SparseVector(Collection<Integer> pKeySet) {
		this();
		pKeySet.stream().forEach(p -> put(p, 0f));
	}

	/**
	 * Establece el valor de un elemento del vector
	 * 
	 * @param pId
	 *            el identificador del elemento del vector
	 * @param pValue
	 *            el nuevo valor que se quiere asignar al elemento
	 */
	public void put(int pId, float pValue) {
		vectorValues.put(pId, pValue);
	}

	/**
	 * Crea un nuevo vector vacío (factory method)
	 * 
	 * @return el vector vacío
	 */
	public static SparseVector empty() {
		return new SparseVector();
	}

	/**
	 * Crea un vector para los productos indicados, con valor 0
	 * 
	 * @param pKeySet
	 *            los identificadores de los productos recogidos en el vector
	 * @return el vector generado
	 */
	public static SparseVector create(Collection<Integer> pKeySet) {
		return new SparseVector(pKeySet);
	}

	/**
	 * Comprueba si un elemento está recogido en el vector
	 * 
	 * @param pId
	 *            el elemento
	 * @return true si el elemento pId está en el vector y false en caso contrario
	 */
	public boolean containsKey(int pId) {
		return vectorValues.containsKey(pId);
	}

	/**
	 * Incremente el valor del elemento especificado en cierta cantidad
	 * 
	 * @param pKey
	 *            el identificador el elemento
	 * @param pValue
	 *            la cantidad a incrementar
	 */
	public void add(int pKey, float pValue) {
		if (vectorValues.containsKey(pKey)) {
			vectorValues.put(pKey, vectorValues.get(pKey) + pValue);
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Devuelve el valor de un elemento
	 * 
	 * @param pKey
	 *            el identificador el elemento
	 * @return el valor del elemento
	 */
	public Float get(Integer pKey) {
		if (vectorValues.containsKey(pKey)) {
			return vectorValues.get(pKey);
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Devuelve el valor del elemento especificado o un valor por defecto si el
	 * elemento no está en el vector
	 * 
	 * @param pKey
	 *            el elemento cuyo valor se quiere obtener
	 * @param pDefaultValue
	 *            el valor por defecto
	 * @return devuelve el valor del elemento especificado si está en el vector o
	 *         pDefaultValue en caso contrario
	 */
	public Float getOrDefault(Integer pKey, Float pDefaultValue) {
		return vectorValues.getOrDefault(pKey, pDefaultValue);
	}

	/**
	 * Devuelve los identificadores de los elementos recogidos en el vector
	 * 
	 * @return los identificadores de los elementos del vector
	 */
	public Set<Integer> keySet() {
		return vectorValues.keySet();
	}

	/**
	 * Devuelve el módulo del vector
	 * 
	 * @return el módulo
	 */
	public float norm() {
		double result = vectorValues.values().stream().mapToDouble(p -> p * p).sum();
		return (float) sqrt(result);
	}

	/**
	 * Multiplica el vector por otro, elemento a elemento
	 * 
	 * @param pVector
	 *            el vector por el que se multiplica
	 */
	public void multiply(SparseVector pVector) {
		for (Integer id : vectorValues.keySet()) {
			if (pVector.containsKey(id)) {
				put(id, get(id) * pVector.get(id));
			}
		}
	}

	/**
	 * Multiplica los valores de todos los elementos del vector por una cantidad
	 * 
	 * @param pValue
	 *            la cantidad
	 */
	public void multiply(float pValue) {
		for (Integer id : vectorValues.keySet()) {
			vectorValues.put(id, vectorValues.get(id) * pValue);
		}
	}

	/**
	 * Devuelve el producto de los dos vectores
	 * 
	 * @param pV
	 *            el vector por el que se quiere multiplicar
	 * @return el producto
	 */
	public float dotProduct(SparseVector pV) {
		double result = vectorValues.keySet().stream().filter(pV::containsKey).mapToDouble(p -> get(p) * pV.get(p)).sum();
		return (float) result;
	}

	/**
	 * Suma los valores de otro vector. Solo se suman los valores de los elementos
	 * del vector que están incluidos en el vector sobre el que se realiza la suma
	 * 
	 * @param pVector
	 *            el vector cuyos valores se quieren sumar
	 */
	public void add(SparseVector pVector) {
		for (Integer tagId : pVector.keySet()) {
			if (containsKey(tagId)) {
				put(tagId, get(tagId) + pVector.get(tagId));
			}
		}

	}

	/**
	 * Devuelve el valor medio del vector
	 * 
	 * @return el valor medio del vector
	 */
	public float average() {
		return (float) vectorValues.values().stream().mapToDouble(Float::floatValue).average().orElse(0);
	}

	@Override
	public String toString() {
		return String.format("[%s]", vectorValues);
	}

	public SparseVector copy() {
		SparseVector copia = SparseVector.create(keySet());
		for (Integer id : keySet()) {
			copia.put(id, get(id));
		}
		return copia;
	}
}
