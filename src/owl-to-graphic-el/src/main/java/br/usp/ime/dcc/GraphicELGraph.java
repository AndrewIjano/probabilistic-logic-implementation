package br.usp.ime.dcc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicELGraph {
	private class Vertex {
		private Integer index;
		
		public Vertex(Integer index) {
			this.index = index;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (this.getClass() != o.getClass())
				return false;

			Vertex v = (Vertex) o;
			return v.index == this.index;
		}

		@Override
		public int hashCode() {
			return this.index;
		}

		@Override
		public String toString() {
			return this.index.toString();
		}
	}

	private class Arrow {
		private Vertex vertex;
//		private String role;
		private Integer role;
		
		public Arrow(Vertex vertex, Integer roleIndex) {
			this.vertex = vertex;
			this.role = roleIndex;
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

		public Integer role() {
			return this.role;
		}

	}

	private class SubProperty {
		private Integer first;
		private Integer second;

		public SubProperty(Integer first, Integer second) {
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (this.getClass() != o.getClass())
				return false;

			SubProperty cp = (SubProperty) o;
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
	
	private class ChainedSubProperty {
		private Integer first;
		private Integer second;
		private Integer third;

		public ChainedSubProperty(Integer first, Integer second, Integer third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (this.getClass() != o.getClass())
				return false;

			ChainedSubProperty cp = (ChainedSubProperty) o;
			return cp.first.equals(this.first) && cp.second.equals(this.second) && cp.third.equals(this.third);
		}

		@Override
		public int hashCode() {
			return this.first.hashCode() * this.second.hashCode() * this.third.hashCode();
		}

		@Override
		public String toString() {
			return "(" + this.first + ", " + this.second + ", " +  this.third + ")";
		}
	}

	private int V;
	private int A;
	private Map<Vertex, List<Arrow>> adjVertices;
	private List<SubProperty> subProperties;
	private List<ChainedSubProperty> chainedSubProperties;

	private List<String> iriList;
	private List<String> roleList;
	
	private static final String ISA = "ISA";

	public GraphicELGraph() {
		this.V = 0;
		this.A = 0;
		this.adjVertices = new HashMap<Vertex, List<Arrow>>();
		this.subProperties = new ArrayList<SubProperty>();
		this.chainedSubProperties = new ArrayList<ChainedSubProperty>();
		this.iriList = new ArrayList<String>();
		this.roleList = new ArrayList<String>();
		this.roleList.add(ISA);
	}

	public void addVertex(String IRI) {
		if (IRI == null)
			throw new NullPointerException();

		if (!iriList.contains(IRI)) {
			iriList.add(IRI);
			Integer index = iriList.indexOf(IRI);
			this.adjVertices.put(new Vertex(index), new ArrayList<Arrow>());
			this.V++;
		}
	}

	public void addArrowRole(String IRIA, String IRIB, String role) {
		if (IRIA == null || IRIB == null || role == null)
			throw new NullPointerException();

		Integer indexA = this.iriList.indexOf(IRIA);
		Integer indexB = this.iriList.indexOf(IRIB);
		if (!this.roleList.contains(role)) {
			this.roleList.add(role);
		}
		Integer indexR = this.roleList.indexOf(role);
		Vertex a = new Vertex(indexA);
		Vertex b = new Vertex(indexB);
		Arrow ab = new Arrow(b, indexR);

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
		
		if (!this.roleList.contains(IRIA)) 
			this.roleList.add(IRIA);
		if (!this.roleList.contains(IRIB))
			this.roleList.add(IRIB);
		
		Integer indexA = this.roleList.indexOf(IRIA);
		Integer indexB = this.roleList.indexOf(IRIB);
		SubProperty sb = new SubProperty(indexA, indexB);
		if (!this.subProperties.contains(sb)) {
			this.subProperties.add(sb);
		}
	}

	public void addChainedSubProperty(String IRIA, String IRIB, String IRIC) {
		if (IRIA == null || IRIB == null || IRIC == null)
			throw new NullPointerException();


		if (!this.roleList.contains(IRIA)) 
			this.roleList.add(IRIA);
		if (!this.roleList.contains(IRIB))
			this.roleList.add(IRIB);
		if (!this.roleList.contains(IRIC))
			this.roleList.add(IRIC);

		Integer indexA = this.roleList.indexOf(IRIA);
		Integer indexB = this.roleList.indexOf(IRIB);
		Integer indexC = this.roleList.indexOf(IRIC);
		ChainedSubProperty cp = new ChainedSubProperty(indexA, indexB, indexC);
		if (!this.chainedSubProperties.contains(cp)) {
			this.chainedSubProperties.add(cp);
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
		String s = "";
		s += "{\n";
		s += "  \"vertices\" : [\n";
		boolean isFirstVertex = true;
		for (String vertex : this.iriList) {
			if (isFirstVertex) isFirstVertex = false;
			else			   s += ",\n";
			
			s += "    \"" + vertex + "\"";
		}
		s += "  ],\n";
		s += "  \"roles\" : [\n";
		boolean isFirstRole = true;
		for (String role : this.roleList) {
			if (isFirstRole) isFirstRole = false;
			else			 s += ",\n";
			
			s += "    \"" + role + "\"";
		}
		s += "  ],\n";
		s += "  \"arrows\" : [\n";
		boolean isFirstArrows = true;
		for (List<Arrow> arrows : this.adjVertices.values()) {
			if (isFirstArrows) isFirstArrows = false;
			else			   s += ",\n";
			
			s += "    [\n";
			boolean isFirstArrow = true;
			for (Arrow arrow : arrows) {
				if (isFirstArrow) isFirstArrow = false;
				else			  s += ",\n";
				
				s += "     { \"role\": " + arrow.role +  ", \"vertex\": " + arrow.vertex +   "}";
			}
			s += "\n    ]";

		}
		s += "\n  ],\n";

		s += "  \"roleInclusions\" : [\n";
		boolean isFirstRoleInc = true;
		for (SubProperty sb : this.subProperties) {
			if (isFirstRoleInc) isFirstRoleInc = false;
			else			    s += ",\n";
			
			s += "    { \"first\": " + sb.first +  ", \"second\": " + sb.second +   "}";			
		}
		s += "\n  ],\n";
		
		s += "  \"chainedRoleInclusions\" : [\n";
		boolean isFirstChainRoleInc = true;
		for (ChainedSubProperty csb : this.chainedSubProperties) {
			if (isFirstChainRoleInc) isFirstChainRoleInc = false;
			else			         s += ",\n";
			
			s += "    { \"first\": " + csb.first +  ", \"second\": " + csb.second +  ", \"third\": " + csb.third + "}";			
		}
		s += "\n  ]\n";
		s += "}";
		return s;
	}

}
