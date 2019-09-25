package msc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GELGraph {
	private class Vertex {
		private String IRI;

		public Vertex(String IRI) {
			this.IRI = IRI;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			
			if (this.getClass() != o.getClass()) return false;
			
			Vertex v = (Vertex) o;
			return v.IRI.equals(this.IRI);
		}
		
		@Override
		public int hashCode() {
			return this.IRI.hashCode();
		}
		
		@Override
		public String toString() {
			return this.IRI;
		}		
	}
	
	private int V;
	private int A;
	private Map<Vertex, List<Vertex>> adjVertices;
	
	public GELGraph() {
		this.V = 0;
		this.A = 0;
		this.adjVertices = new HashMap<Vertex, List<Vertex>>();
	}
	
	public void addVertex(String IRI) {
		if (IRI == null) throw new NullPointerException();
		
		this.adjVertices.put(new Vertex(IRI), new ArrayList<Vertex>());
		this.V++;
	}
	
	public void addArrow(String IRIA, String IRIB) {
		if (IRIA == null || IRIB == null) throw new NullPointerException();
		
		Vertex a = new Vertex(IRIA);
		Vertex b = new Vertex(IRIB);
		
		if (this.adjVertices.get(a).contains(b)) throw new IllegalArgumentException();
		
		this.adjVertices.get(a).add(b);
		A++;
	}
	
	public int V() {
		return this.V;
	}
	
	public int A() {
		return this.A;
	}
	
}
