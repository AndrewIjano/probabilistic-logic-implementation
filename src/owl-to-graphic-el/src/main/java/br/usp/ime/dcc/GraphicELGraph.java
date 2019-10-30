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
			if (o == null)
				return false;

			if (this.getClass() != o.getClass())
				return false;

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
			if (o == null)
				return false;

			if (this.getClass() != o.getClass())
				return false;

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

	private class ChainedProperties {
		private String first;
		private String second;

		public ChainedProperties(String first, String second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (this.getClass() != o.getClass())
				return false;

			ChainedProperties cp = (ChainedProperties) o;
			return cp.first.equals(this.first) && cp.second.equals(this.second);
		}

		@Override
		public int hashCode() {
			return this.first.hashCode() * this.second.hashCode();
		}

		@Override
		public String toString() {
			return "(" + this.first + ", " + this.second + ")";
		}
	}

	private int V;
	private int A;
	private Map<Vertex, List<Arrow>> adjVertices;
	private Map<String, String> subProperties;
	private Map<ChainedProperties, String> chainedSubProperties;

	private static final String ISA = "ISA";

	public GraphicELGraph() {
		this.V = 0;
		this.A = 0;
		this.adjVertices = new HashMap<Vertex, List<Arrow>>();
		this.subProperties = new HashMap<String, String>();
		this.chainedSubProperties = new HashMap<ChainedProperties, String>();
	}

	public void addVertex(String IRI) {
		if (IRI == null)
			throw new NullPointerException();

		this.adjVertices.put(new Vertex(IRI), new ArrayList<Arrow>());
		this.V++;
	}

	public void addArrowRole(String IRIA, String IRIB, String role) {
		if (IRIA == null || IRIB == null || role == null)
			throw new NullPointerException();

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

	public void addSubPropery(String IRIA, String IRIB) {
		if (IRIA == null || IRIB == null)
			throw new NullPointerException();

		if (this.subProperties.get(IRIA) == null || !this.subProperties.get(IRIA).contains(IRIB)) {
			this.subProperties.put(IRIA, IRIB);
		}
	}

	public void addChainedSubProperty(String IRIA, String IRIB, String IRIC) {
		if (IRIA == null || IRIB == null || IRIC == null)
			throw new NullPointerException();

		ChainedProperties cp = new ChainedProperties(IRIA, IRIB);
		if (this.chainedSubProperties.get(cp) == null || !this.chainedSubProperties.get(cp).contains(IRIC)) {
			this.chainedSubProperties.put(cp, IRIC);
		}
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
			if (vertexIndex != 0)
				s += ",\n";
			indexes.put(v.IRI, vertexIndex++);
			s += "    \"" + v.toString() + "\"";
		}
		s += "  ],\n";
		s += "  \"arrows\" : [\n";
		int roleIndex = 1;
		Map<String, Integer> roleIndexes = new HashMap<String, Integer>();
		roleIndexes.put(ISA, 0);
		boolean isFirstVertex = true;
		for (Vertex v : this.adjVertices.keySet()) {
			if (isFirstVertex)
				isFirstVertex = false;
			else
				s += ",\n";

			s += "    [\n";
			boolean isFirstArrow = true;
			for (Arrow a : this.adjVertices.get(v)) {
				if (isFirstArrow)
					isFirstArrow = false;
				else
					s += ",\n";

				if (roleIndexes.get(a.role()) == null)
					roleIndexes.put(a.role(), roleIndex++);
				s += "      { \"role\" : " + roleIndexes.get(a.role()) + ",";
				s += " \"vertex\" : " + indexes.get(a.vertex().IRI) + "}";
			}
			s += "\n    ]";
		}
		s += "\n  ],\n";

		s += " \"roles\" : [\n";
		boolean isFirstRole = true;
		for (String role : roleIndexes.keySet()) {
			if (isFirstRole)
				isFirstRole = false;
			else
				s += ",\n";

			s += "    \"" + role  + ", " + roleIndexes.get(role) + "\"";
		}
		s += "],\n";
		s += "\"V\" : " + this.V + ",\n";
		s += "\"A\" : " + this.A + ",\n";
		s += "\"R\" : " + roleIndex + "\n";
		s += "}";
		return s;
	}

}
