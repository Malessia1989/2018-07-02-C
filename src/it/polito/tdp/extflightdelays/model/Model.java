package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	Map<Integer, Airport> idMap;
	ExtFlightDelaysDAO dao;

	public Model() {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<>();
		dao=new ExtFlightDelaysDAO();
		dao.loadAllAirports(idMap);
		
		
		
	}
	public boolean isValid(String input) {
		
		return input.matches("\\d+");
	}

	public void creaGrafo(int numCompagnie) {
		
		for(Rotta r: dao.getRotte(idMap, numCompagnie)) {
			grafo.addVertex(r.getPartenza());
			grafo.addVertex(r.getDest());
			
			DefaultWeightedEdge arco=grafo.getEdge(r.getPartenza(), r.getDest());
			if(arco==null) {
				Graphs.addEdge(grafo, r.getPartenza(), r.getDest(), r.getPeso());
			}else {
				double peso=grafo.getEdgeWeight(arco);
				double newPeso= (peso+ r.getPeso())/2;
				
				grafo.setEdgeWeight(arco, newPeso);
			}
		}
		System.out.println("Grafo creato!");
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
		
	}
	public SimpleWeightedGraph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public String getElencoVicini(Airport a) {
		
		
		List<Airport> vicini= Graphs.neighborListOf(grafo, a);
		Collections.sort(vicini, new Comparator<Airport>() {

			@Override
			public int compare(Airport a1, Airport a2) {
				DefaultWeightedEdge arco1=grafo.getEdge(a1, a);
				double peso1=grafo.getEdgeWeight(arco1);
				
				DefaultWeightedEdge arco2=grafo.getEdge(a2, a);
				double peso2=grafo.getEdgeWeight(arco2);
				
				return (int) (peso2-peso1);
			}
		});
		
		String result=" ";
		for(Airport atemp:vicini) {
			DefaultWeightedEdge edge=grafo.getEdge(atemp, a);
			double peso= grafo.getEdgeWeight(edge);
				
			result+= atemp.getAirportName()+ " peso: "+peso+"\n";
		}
		return result;
	}

}
