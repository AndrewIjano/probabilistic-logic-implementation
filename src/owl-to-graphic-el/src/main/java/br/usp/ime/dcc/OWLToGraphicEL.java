package br.usp.ime.dcc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
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
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.util.OWLAPIStreamUtils.Pair;


import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLEquivalentClassesAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

class OWLToGraphicEL {

	private static void p(Object o) {
		System.out.println(o);
	}

	private static String cleanIRI(Object IRI) {
		String sIRI = IRI.toString();
		return sIRI.toString().replaceFirst(".*#", "").replace(">", "");
	}

	private static class ChainedRoles {
		public Integer A;
		public Integer B;
		public Integer C;
		
		public ChainedRoles(Integer A, Integer B, Integer C) {
			this.A = A;
			this.B = B;
			this.C = C;
		}
	}
	
	static GraphicELGraph OWLToGraphicELGraph(String OWLFile) throws OWLOntologyCreationException {
		File ontologyFile = new File(OWLFile);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology o = man.loadOntologyFromOntologyDocument(ontologyFile);

		OWL2GraphicELProfile GELProfile = new OWL2GraphicELProfile();
		OWLProfileReport report = GELProfile.checkOntology(o);
		if (!report.isInProfile()) throw new IllegalArgumentException();
		
		GraphicELGraph G = new GraphicELGraph();

		p("Map IRIs");
//		Get Classes

		p("vertices:");
		o.classesInSignature().forEach((c) -> {
			p("\t" + cleanIRI(c));
			G.addVertex(c.getIRI().toString());
		});
		
		o.individualsInSignature().forEach((i) -> {
			p("\t" + cleanIRI(i));
			String individualIRI = i.getIRI().toString();
			G.addVertex(individualIRI);
		});
				
		
		p("\n");

		
		p("----");
		
		for (OWLClass c1 : o.classesInSignature().toArray(OWLClass[]::new)) {
			for (OWLAxiom a : o.subClassAxiomsForSuperClass(c1).toArray(OWLAxiom[]::new)) {
				OWLClassImpl c2 = (OWLClassImpl) a.components().toArray(Object[]::new)[0];

				p(cleanIRI(c1) + " -> " + cleanIRI(c2));
				
				G.addArrowISA(c1.getIRI().toString(), c2.getIRI().toString());
			}

			for (OWLEquivalentClassesAxiomImpl a : o.equivalentClassesAxioms(c1)
					.toArray(OWLEquivalentClassesAxiomImpl[]::new)) {

				OWLClassExpression[] operands = a.operands().toArray(OWLClassExpression[]::new);

				String IRIA = operands[0].asOWLClass().getIRI().toString();


				p("Equivalent:");
				String IRIB = "";
				String role = "";
				String classType = operands[1].getClassExpressionType().toString();
				
				if (classType == "ObjectSomeValuesFrom") {
					for (OWLClassExpression opp : operands[1].nestedClassExpressions()
							.toArray(OWLClassExpression[]::new)) {
						if (opp.isOWLClass()) {
							IRIB = opp.asOWLClass().getIRI().toString();
						}
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
			String individualIRI = i.getIRI().toString();
			for (OWLClassAssertionAxiom ca : o.classAssertionAxioms(i).toArray(OWLClassAssertionAxiom[]::new)) {
				if (ca.getClassExpression().isOWLClass()) {
					String classIRI = ((OWLClass) ca.getClassExpression()).getIRI().toString();
					p("\t " + cleanIRI(individualIRI) + " -> " + cleanIRI(classIRI));
					
					G.addArrowISA(individualIRI, classIRI);
				} else
					throw new UnsupportedOperationException();

			}

			for (OWLObjectPropertyAssertionAxiom pa : o.objectPropertyAssertionAxioms(i)
					.toArray(OWLObjectPropertyAssertionAxiom[]::new)) {
				String subjectIRI = pa.getSubject().asOWLNamedIndividual().getIRI().toString();
				String propertyIRI = pa.getProperty().getNamedProperty().getIRI().toString();
				String objectIRI = pa.getObject().asOWLNamedIndividual().getIRI().toString();
				
				p("\t" + cleanIRI(subjectIRI) + " ( ->, " + cleanIRI(propertyIRI) + ") " + cleanIRI(objectIRI));
				G.addArrowRole(subjectIRI, objectIRI, propertyIRI);
			}

		}

		List<Pair<Integer>> subRoles = new ArrayList<Pair<Integer>>();
		List<ChainedRoles> subChainRoles = new ArrayList<ChainedRoles>();
		
		for (OWLAxiom a : o.getRBoxAxioms(Imports.INCLUDED)) {
			String IRIA = "";
			String IRIB = "";
			String IRIC = "";
			
			for (Object ob : a.componentsWithoutAnnotations().toArray(Object[]::new)) {
				try {
					OWLObjectPropertyImpl opi = (OWLObjectPropertyImpl) ob;
					if (IRIA == null || IRIA.isEmpty()) IRIA = opi.getIRI().toString();
					else if (IRIB == null || IRIB.isEmpty()) IRIB = opi.getIRI().toString();
					else IRIC = opi.getIRI().toString();					
				} 
				catch (Exception e) {
					try {
						ArrayList al = (ArrayList) ob;
						OWLObjectPropertyImpl op1 = (OWLObjectPropertyImpl) al.get(0);
						OWLObjectPropertyImpl op2 = (OWLObjectPropertyImpl) al.get(1);
						IRIA = op1.getIRI().toString();
						IRIB = op2.getIRI().toString();
					} catch (Exception e2) {
					}
				}
				
			}
			
			if (IRIC == null || IRIC.isEmpty()) {
				p(IRIA + " -> " + IRIB);
				G.addSubPropery(IRIA, IRIB);
			}
			else {
				p(IRIA + " o " + IRIB + " -> " + IRIC);
				G.addChainedSubProperty(IRIA, IRIB, IRIC);	
			}
		}
		
		p("Graph V: " + G.V());
		p("Graph A: " + G.A());
		p(G.toString());
		return G;
	}
	
	static String OWLToGraphicELJSON(String OWLFile) throws OWLOntologyCreationException {
		return OWLToGraphicEL.OWLToGraphicELGraph(OWLFile).toString();
	}
}
