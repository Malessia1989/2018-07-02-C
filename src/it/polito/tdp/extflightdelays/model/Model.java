package it.polito.tdp.extflightdelays.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	ExtFlightDelaysDAO dao;
	Graph<Airport, DefaultWeightedEdge> grafo;
	Map<Integer, Airport> idMap;
	
	public Model() {
		idMap= new HashMap<>();
		dao= new ExtFlightDelaysDAO();
		dao.loadAllAirports(idMap); 
		
	}	

	public void creaGrafo(String numCompagnie) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Rotta> rotte = dao.getRotte(numCompagnie, idMap);

		for (Rotta r : rotte) {
			grafo.addVertex(r.getPartenza());
			grafo.addVertex(r.getArrivo());

			Graphs.addEdgeWithVertices(grafo, r.getPartenza(), r.getArrivo());
			DefaultWeightedEdge edge = grafo.getEdge(r.getPartenza(), r.getArrivo());
			if (edge == null) {
				Graphs.addEdge(grafo, r.getPartenza(), r.getArrivo(), r.getPeso());
			} else {
								
				grafo.setEdgeWeight(edge, r.getPeso());
			}
		}
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
	}

	public boolean isValid(String numCompagnie) {
		return numCompagnie.matches("\\d+");
	}




	public Graph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}




	public String getConnessi(Airport input, String numCompagnie) {
		
		if(grafo.vertexSet().size()== 0) {
			this.creaGrafo(numCompagnie);
		}

		List<Airport> vicini=Graphs.neighborListOf(grafo, input);
		Collections.sort(vicini, new Comparator<Airport>() {

			@Override
			public int compare(Airport a1, Airport a2) {
				//1) recupero l'arco 
				// 2) recupero il peso
				DefaultWeightedEdge arco1= grafo.getEdge(a1, input);
				double peso1=grafo.getEdgeWeight(arco1);
				
				DefaultWeightedEdge arco2= grafo.addEdge(a2, input);
				double peso2=grafo.getEdgeWeight(arco2);
				
				return (int) (peso2-peso1);
			}
		});
		
		String result="";
		for(Airport a: vicini) {
			DefaultWeightedEdge edge= grafo.getEdge( a,input);
			double peso= grafo.getEdgeWeight(edge);
			result+= a.getAirportName() + " "+ peso+ "\n";
		}
		return result;
	}
	
	

}
