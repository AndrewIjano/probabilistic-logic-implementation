package msc;

import java.io.File;
import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class OWLAPIFirst {
	public static final File pizza_file = new File("pizza.owl");
	public static final IRI pizza_iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");

	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		OWLOntology o = man.loadOntologyFromOntologyDocument(pizza_file);

		// Create the walker
		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(o));
		// Now ask our walker to walk over the ontology
		OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(walker) {
			public void visit(OWLObjectSomeValuesFrom desc) {
				System.out.println(desc);
				System.out.println(" " + getCurrentAxiom());
				return;
			}
		};
		// Have the walker walk...
		walker.walkStructure(visitor);

		System.out.println(o);
	}
}
