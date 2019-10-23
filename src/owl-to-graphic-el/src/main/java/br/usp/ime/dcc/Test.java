package br.usp.ime.dcc;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Test {	
	public static String exampleToGraphicEL() throws OWLOntologyCreationException {
		return OWLToGraphicEL.OWLToGraphicELGraph("example1.owl").toString();
	}
	public static void main(String[] args) throws OWLOntologyCreationException {
		System.out.println(OWLToGraphicEL.OWLToGraphicELGraph("example1.owl"));
	}
}
