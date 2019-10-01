package msc;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
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
import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;

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

	private static String cleanIRI(Object IRI) {
		String sIRI = IRI.toString();
		return sIRI.toString().replaceFirst(".*#", "").replace(">", "");
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();

		OWLOntology[] onts = { man.loadOntologyFromOntologyDocument(pizza_file), man.loadOntology(shake_iri),
				man.loadOntologyFromOntologyDocument(a_file), man.loadOntologyFromOntologyDocument(test_file),
				man.loadOntologyFromOntologyDocument(example1) };

//		for (OWLOntology ont : onts) {
//			OWL2GraphicELProfile gel = new OWL2GraphicELProfile();
//			OWLProfileReport report = gel.checkOntology(ont);
//
//			p("Is OWL 2 Graphic EL: " + report.isInProfile());
//			p(ont);
//			p("");
//		}

		OWLOntology o = onts[4];
		GELGraph G = new GELGraph();
		p("Map IRIs");
		for (OWLClass c : o.classesInSignature().toArray(OWLClass[]::new)) {
			p("\t" + cleanIRI(c));
			G.addVertex(c.getIRI().toString());
		}

		p("\n");

		for (OWLClass c1 : o.classesInSignature().toArray(OWLClass[]::new)) {
			for (OWLAxiom a : o.subClassAxiomsForSuperClass(c1).toArray(OWLAxiom[]::new)) {

				OWLClassImpl c2 = (OWLClassImpl) a.components().toArray(Object[]::new)[0];

				p(cleanIRI(c1) + " -> " + cleanIRI(c2));
				G.addArrowISA(c1.getIRI().toString(), c2.getIRI().toString());
			}

			for (OWLEquivalentClassesAxiomImpl a : o.equivalentClassesAxioms(c1)
					.toArray(OWLEquivalentClassesAxiomImpl[]::new)) {
//				p(a);
				OWLClassExpression[] operands = a.operands().toArray(OWLClassExpression[]::new);
				
				String IRIA = operands[0].asOWLClass().getIRI().toString();

//				operands[1].getClassExpressionType().
				p("Equivalent:");
				String IRIB = "";
				String role = "";
				
				if (operands[1].getClassExpressionType().toString() == "ObjectSomeValuesFrom") {
					OWLClassExpression op2 = operands[1].nestedClassExpressions().toArray(OWLClassExpression[]::new)[1];
					IRIB = op2.asOWLClass().getIRI().toString();
					
					OWLObjectProperty prop = operands[1].objectPropertiesInSignature().toArray(OWLObjectProperty[]::new)[0];
					role = prop.getIRI().toString();
					
					p("\t" + cleanIRI(IRIA) + " -> " + "(" + cleanIRI(IRIB) + ", " + cleanIRI(role) + ")");
					G.addArrowRole(IRIA, IRIB, role);
				}
				else throw new UnsupportedOperationException();
				
			}
		}


		p("Individuals:");
		for (OWLNamedIndividual i : o.individualsInSignature().toArray(OWLNamedIndividual[]::new)) {
			p("\t" + cleanIRI(i));
			G.addVertex(i.toString());
		}


		p("Graph V: " + G.V());
		p("Graph A: " + G.A());
		//		p("-----");
//		for (OWLAxiom a : onts[1].axioms().toArray(OWLAxiom[]::new)) {
//			p(a);
//		}
	}
}
