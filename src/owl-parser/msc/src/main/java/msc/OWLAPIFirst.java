package msc;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OWLAPIFirst {
	public static final IRI pizza_iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology o = man.loadOntologyFromOntologyDocument(pizza_iri);
		System.out.println(o);
	}
}

