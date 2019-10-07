package br.usp.ime.dcc;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

public class Test {
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLToGraphicEL.OWLToGraphicELGraph("example1.owl");
	}
}
