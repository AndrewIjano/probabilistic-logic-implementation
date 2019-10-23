package br.usp.ime.dcc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicELGraph {
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
	
	private class Arrow {
		private Vertex vertex;
		private String role;
		
		public Arrow(Vertex vertex, String roleIRI) {
			this.vertex = vertex;
			this.role = roleIRI;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			
			if (this.getClass() != o.getClass()) return false;
			
			Arrow a = (Arrow) o;
			return a.vertex.equals(this.vertex) && a.role.equals(this.role);
		}
		
		@Override
		public int hashCode() {
			return this.vertex.hashCode() * this.role.hashCode();
		}
		
		@Override
		public String toString() {
			return "(" + this.vertex + ", " + this.role + ")";
		}
		
		public Vertex vertex() {
			return this.vertex;
		}
		
		public String role() {
			return this.role;
		}
		
	}
	private int V;
	private int A;
	private Map<Vertex, List<Arrow>> adjVertices;
	private static final String ISA = "ISA";
	
	public GraphicELGraph() {
		this.V = 0;
		this.A = 0;
		this.adjVertices = new HashMap<Vertex, List<Arrow>>();
	}
	
	public void addVertex(String IRI) {
		if (IRI == null) throw new NullPointerException();
		
		this.adjVertices.put(new Vertex(IRI), new ArrayList<Arrow>());
		this.V++;
	}
	
	public void addArrowRole(String IRIA, String IRIB, String role) {
		if (IRIA == null || IRIB == null || role == null) throw new NullPointerException();
		

		Vertex a = new Vertex(IRIA);
		Vertex b = new Vertex(IRIB);
		Arrow ab = new Arrow(b, role);
		
		if (!this.adjVertices.get(a).contains(ab)) {
			this.adjVertices.get(a).add(ab);
			A++;			
		}
	}
	
	public void addArrowISA(String IRIA, String IRIB) {
		this.addArrowRole(IRIA, IRIB, ISA);
	}
	
	public int V() {
		return this.V;
	}
	
	public int A() {
		return this.A;
	}
	
	@Override
	public String toString() {
		Map<String, Integer> indexes = new HashMap<String, Integer>();		
		String s = "";
		s += "{\n";
		s += "  \"vertices\" : [\n";
		int vertexIndex = 0;
		for (Vertex v : this.adjVertices.keySet()) {
			indexes.put(v.IRI, vertexIndex);
			s += "    {" + (vertexIndex++) + " : \"" + v.toString() + "\"},\n"; 
		}
		s += "  ],\n";
		s += "  \"arrows\" : [\n";
		int roleIndex = 1;
		Map<String, Integer> roleIndexes = new HashMap<String, Integer>();		
		roleIndexes.put(ISA, 0);
		for (Vertex v : this.adjVertices.keySet()) {
			for (Arrow a : this.adjVertices.get(v)) {
				if (roleIndexes.get(a.role()) == null)
					roleIndexes.put(a.role(), roleIndex++);
				s += "    {" + indexes.get(v.IRI) + " :";
				s += " { \"role\" : " + roleIndexes.get(a.role()) + ",";
				s += " \"vertex\" : " + indexes.get(a.vertex().IRI) +  "}";
				s += "}\n";				
			}
		}
		s += "  ],\n";
		s += "\"V\" : " + this.V + ",\n";
		s += "\"A\" : " + this.A + ",\n";
		s += "\"R\" : " + roleIndex + "\n";
		s += "}";
		return s;
	}
	
}
