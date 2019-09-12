package msc;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.*;

public class OWLAPIFirst {
	public static final File pizza_file = new File("pizza.owl");
	public static final IRI pizza_iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");

	private static void p(Object o) {
		System.out.println(o);
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		OWLOntology o = man.loadOntologyFromOntologyDocument(pizza_file);
		
		for (OWLLogicalAxiom a : o.logicalAxioms().toArray(OWLLogicalAxiom[]::new)) {
			p(a.getAxiomType());
			for (Object c : a.componentsWithoutAnnotations().toArray(Object[]::new)) {
				p(c.getClass());
			}
			System.out.println();
		}
//		o.logicalAxioms().forEach(
//				(p) -> p.components().forEach( 
//						(c) -> System.out.println(c)
//						
//						)
//				
//				);
		
		System.out.println(o);
	}
}
