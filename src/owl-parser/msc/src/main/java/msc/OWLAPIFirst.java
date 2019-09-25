package msc;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWL2ELProfile;
import org.semanticweb.owlapi.profiles.OWL2Profile;
import org.semanticweb.owlapi.profiles.OWL2QLProfile;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.util.DLExpressivityChecker;
import org.semanticweb.owlapi.*;

public class OWLAPIFirst {
	public static final File pizza_file = new File("pizza.owl");
	public static final File test_file = new File("test2.owl");
	public static final IRI pizza_iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
	public static final IRI shake_iri = IRI.create("http://workingontologist.org/Examples/Chapter3/shakespeare.owl");
	public static final File a_file = new File(
			"/home/andrew/Downloads/bla-ontologies.owx/bla-ontologies-owx-REVISION-HEAD/a.owx");

	private static void p(Object o) {
		System.out.println(o);
	}

	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		OWLOntology[] onts = { man.loadOntologyFromOntologyDocument(pizza_file), man.loadOntology(shake_iri),
				man.loadOntologyFromOntologyDocument(a_file), man.loadOntologyFromOntologyDocument(test_file)};

		for (OWLOntology ont : onts) {
			OWL2GraphicELProfile gel = new OWL2GraphicELProfile();
			OWLProfileReport report = gel.checkOntology(ont);

			p("Is OWL 2 Graphic EL: " + report.isInProfile());
			p(ont);
			p("");
		}

		for (OWLClass c : onts[1].classesInSignature().toArray(OWLClass[]::new)) {
			p(c);
		}
		
		
		for (OWLNamedIndividual i : onts[1].individualsInSignature().toArray(OWLNamedIndividual[]::new)) {
			p(i);
			
		}
		p("-----");
		for (OWLAxiom a : onts[1].axioms().toArray(OWLAxiom[]::new)) {
			p(a);
		}

		
//		
//		for (OWLLogicalAxiom a : o.logicalAxioms().toArray(OWLLogicalAxiom[]::new)) {
//			p(a.getAxiomType());
//			
////			p(a.dataPropertiesInSignature().toArray(OWLDataProperty[]::new).length != 0);
////			p(a.annotations().toArray(OWLAnnotation[]::new).length != 0);
////			p(a.classesInSignature().toArray(OWLClass[]::new).length != 0);
////			p(a.individualsInSignature().toArray(OWLNamedIndividual[]::new).length != 0);
//			for (OWLClass x : a.classesInSignature().toArray(OWLClass[]::new)) {
////				p("oi");
//			
//				p(x.asOWLClass());
//			}
//			
////			for (OWLDataProperty c : a.dataPropertiesInSignature().toArray(OWLDataProperty[]::new)) {
////				p(c.asOWLClass());
////				p("oi");
////				
////			}
//			System.out.println();
//		}
//		o.logicalAxioms().forEach(
//				(p) -> p.components().forEach( 
//						(c) -> System.out.println(c)
//						
//						)
//				
//				);

//		System.out.println(o);
	}
}
