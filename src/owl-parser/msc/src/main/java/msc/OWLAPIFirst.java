package msc;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
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

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import org.semanticweb.owlapi.*;

public class OWLAPIFirst {
	public static final File pizza_file = new File("pizza.owl");
	public static final File test_file = new File("test2.owl");
	public static final IRI pizza_iri = IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl");
	public static final IRI shake_iri = IRI.create("http://workingontologist.org/Examples/Chapter3/shakespeare.owl");
	public static final File a_file = new File(
			"/home/andrew/Downloads/bla-ontologies.owx/bla-ontologies-owx-REVISION-HEAD/a.owx");

	public static final File example1 = new File("example1.owl");

	private static void p(Object o) {
		System.out.println(o);
	}

	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		OWLOntology[] onts = { man.loadOntologyFromOntologyDocument(pizza_file), man.loadOntology(shake_iri),
				man.loadOntologyFromOntologyDocument(a_file), man.loadOntologyFromOntologyDocument(test_file),
				man.loadOntologyFromOntologyDocument(example1) };

		for (OWLOntology ont : onts) {
			OWL2GraphicELProfile gel = new OWL2GraphicELProfile();
			OWLProfileReport report = gel.checkOntology(ont);

			p("Is OWL 2 Graphic EL: " + report.isInProfile());
			p(ont);
			p("");
		}

		GELGraph G = new GELGraph();
		p("Map IRIs");
		for (OWLClass c : onts[4].classesInSignature().toArray(OWLClass[]::new)) {
			p(c);
			G.addVertex(c.getIRI().toString());
		}

		p("Graph V: " + G.V());

		for (OWLClass c1 : onts[4].classesInSignature().toArray(OWLClass[]::new)) {
//			p(">>>>>");
			for (OWLAxiom a : onts[4].subClassAxiomsForSuperClass(c1).toArray(OWLAxiom[]::new)) {
//				p("(((((");
				OWLClassImpl c2 = (OWLClassImpl) a.components().toArray(Object[]::new)[0];

				p(c1 + " -> " + c2);
				G.addArrow(c1.getIRI().toString(), c2.getIRI().toString());

//				for (Object ob : a.components().toArray(Object[]::new)) {
//					if (ob instanceof OWLClassImpl) {
//						p(c + " -> " + ob);
//						G.addArrow(c.getIRI().toString(), ((OWLClassImpl) ob).getIRI().toString());
//					}
//				}
//				p(")))))");
			}
//			p("<<<<<");
		}

		p("Graph A: " + G.A());

//		for (OWLNamedIndividual i : onts[1].individualsInSignature().toArray(OWLNamedIndividual[]::new)) {
//			p(i);	
//		}
//		p("-----");
//		for (OWLAxiom a : onts[1].axioms().toArray(OWLAxiom[]::new)) {
//			p(a);
//		}
	}
}
