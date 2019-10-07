package br.usp.ime.dcc;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.profiles.OWLProfileReport;

import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;

class OWLToGraphicEL {

	private static void p(Object o) {
		System.out.println(o);
	}

	private static String cleanIRI(Object IRI) {
		String sIRI = IRI.toString();
		return sIRI.toString().replaceFirst(".*#", "").replace(">", "");
	}

	static GraphicELGraph OWLToGraphicELGraph(String OWLFile) throws OWLOntologyCreationException {
		File ontologyFile = new File("example1.owl");
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology o = man.loadOntologyFromOntologyDocument(ontologyFile);

		OWL2GraphicELProfile GELProfile = new OWL2GraphicELProfile();
		OWLProfileReport report = GELProfile.checkOntology(o);

		GraphicELGraph G = new GraphicELGraph();

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
//				p(a);
				String classType = operands[1].getClassExpressionType().toString();
//				p(classType);
				if (classType == "ObjectSomeValuesFrom") {
					for (OWLClassExpression opp : operands[1].nestedClassExpressions()
							.toArray(OWLClassExpression[]::new)) {
						if (opp.isOWLClass()) {
							IRIB = opp.asOWLClass().getIRI().toString();

						}
//						p(opp);
					}

					OWLObjectProperty prop = operands[1].objectPropertiesInSignature()
							.toArray(OWLObjectProperty[]::new)[0];
					role = prop.getIRI().toString();

					p("\t" + cleanIRI(IRIA) + " (->, " + cleanIRI(role) + ") " + cleanIRI(IRIB));
					G.addArrowRole(IRIA, IRIB, role);

					String artificialNode = role + "." + IRIB;
					p("\t" + cleanIRI(role) + "." + cleanIRI(IRIB) + " -> " + cleanIRI(IRIA));
					G.addVertex(artificialNode);
					G.addArrowISA(artificialNode, IRIA);

				} else if (classType == "Class") {
					for (OWLClassExpression opp : operands[1].nestedClassExpressions()
							.toArray(OWLClassExpression[]::new))
						if (opp.isOWLClass())
							IRIB = opp.asOWLClass().getIRI().toString();

					p("\t " + cleanIRI(IRIA) + " -> " + cleanIRI(IRIB));
					G.addArrowISA(IRIA, IRIB);

					p("\t " + cleanIRI(IRIB) + " -> " + cleanIRI(IRIA));
					G.addArrowISA(IRIB, IRIA);

				} else
					throw new UnsupportedOperationException();

			}
		}

		p("Individuals:");

		for (OWLNamedIndividual i : o.individualsInSignature().toArray(OWLNamedIndividual[]::new)) {
			p(cleanIRI(i));
			String individualIRI = i.getIRI().toString();
			G.addVertex(individualIRI);

		}
		for (OWLNamedIndividual i : o.individualsInSignature().toArray(OWLNamedIndividual[]::new)) {
			String individualIRI = i.getIRI().toString();
			for (OWLClassAssertionAxiom ca : o.classAssertionAxioms(i).toArray(OWLClassAssertionAxiom[]::new)) {
//				p(ca);
//				p(ca.getIndividual());
				if (ca.getClassExpression().isOWLClass()) {

					String classIRI = ((OWLClass) ca.getClassExpression()).getIRI().toString();
					p("\t " + cleanIRI(individualIRI) + " -> " + cleanIRI(classIRI));
					G.addArrowISA(individualIRI, classIRI);
				} else
					throw new UnsupportedOperationException();

			}

			for (OWLObjectPropertyAssertionAxiom pa : o.objectPropertyAssertionAxioms(i)
					.toArray(OWLObjectPropertyAssertionAxiom[]::new)) {
//				p(pa);
//				p(pa.getSubject());
//				p(pa.getProperty());
//				p(pa.getObject());

				String subjectIRI = pa.getSubject().asOWLNamedIndividual().getIRI().toString();
				String propertyIRI = pa.getProperty().getNamedProperty().getIRI().toString();
				String objectIRI = pa.getObject().asOWLNamedIndividual().getIRI().toString();

				p("\t" + cleanIRI(subjectIRI) + " ( ->, " + cleanIRI(propertyIRI) + ") " + cleanIRI(objectIRI));
				G.addArrowRole(subjectIRI, objectIRI, propertyIRI);
			}

		}

		p("Graph V: " + G.V());
		p("Graph A: " + G.A());

		return G;
	}
}
