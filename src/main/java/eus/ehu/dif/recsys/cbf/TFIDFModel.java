package eus.ehu.dif.recsys.cbf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eus.ehu.dif.recsys.core.SparseVector;
import eus.ehu.dif.recsys.dao.MovieDAO;

public class TFIDFModel {
	
	private static TFIDFModel mTfidfModel = new TFIDFModel();
	private Map<String,Integer> conjuntoEtiquetas;
	private Map<Integer,SparseVector> modelosPeliculas;
	
	private TFIDFModel() {
		
	}

	public static TFIDFModel getTFIDFModel() {
		return mTfidfModel;
	}
	
	/**
	 * Metodo que devuelve el modelo de una pelicula
	 */
	
	public SparseVector getItemVector(int pId) {
		ensureTFIDFModel();
		if(modelosPeliculas.containsKey(pId)) {
			return modelosPeliculas.get(pId);
		} else {
			
		}
		
		return SparseVector.create(conjuntoEtiquetas.values());
	}
	
	
	/**
	 * Metodo que devuelve un modelo vacio (lo utilizaremos para crear el model del usuario)
	 */
	
	public SparseVector newTagVector() {
		ensureTFIDFModel();
		return SparseVector.create(conjuntoEtiquetas.values());
	}

	/**
	 * Devuelve los identificacioes de las etiquetas
	 */
	private void ensureTFIDFModel() {
		if(conjuntoEtiquetas == null) {
			synchronized (this) {
				if (conjuntoEtiquetas == null) {
					// Construir catalogo de etiquetas
					Set<String> etiquetas = MovieDAO.getMovieDAO().getTagCatalog();
					conjuntoEtiquetas = new HashMap<>();
					int id = 1;
					for (String etiqueta : etiquetas) {
						conjuntoEtiquetas.put(etiqueta, id++);						
					}
					
					//Obtener el model TFIDF de las peliculas
					SparseVector docFrequency = SparseVector.create(conjuntoEtiquetas.values());
					
					Set<Integer> idPeliculas = MovieDAO.getMovieDAO().getMovieIds();
					modelosPeliculas = new HashMap<>();
					for (Integer idPelicula : idPeliculas) {
						SparseVector modeloPelicula = SparseVector.empty();
						List<String> etiquetasPelicula = MovieDAO.getMovieDAO().getMovieTags(idPelicula);	
						for (String etiqueta : etiquetasPelicula) {
							int idEtiqueta = conjuntoEtiquetas.get(etiqueta);
							if (!modeloPelicula.containsKey(idEtiqueta)){
								docFrequency.add(idEtiqueta, 1);
								modeloPelicula.put(idEtiqueta, 0);
							}
							modeloPelicula.add(idEtiqueta, 1);
						}
						// Guardar el "modelo" de la pelicula
						modelosPeliculas.put(idPelicula, modeloPelicula);
					}
					// Aplicar IDF
					int numPeliculas = modelosPeliculas.size();
					for (Integer tagId : docFrequency.keySet()) {
						float idf = (float) Math.log10(numPeliculas/docFrequency.get(tagId));
						docFrequency.put(tagId, idf);
					}
					
					for (Integer idPelicula : modelosPeliculas.keySet()) {
						SparseVector modeloPelicula = modelosPeliculas.get(idPelicula);
						modeloPelicula.multiply(docFrequency);
						// Normalización
						modeloPelicula.multiply(1/modeloPelicula.norm());
						
					}
				}				
			}
		}
		
	}



}
