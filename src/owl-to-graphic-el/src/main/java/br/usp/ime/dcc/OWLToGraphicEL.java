package br.usp.ime.dcc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

class OWLToGraphicEL {

	private static void p(Object o) {
		System.out.println(o);
	}

	private static void p0(Object o) {
		System.out.print(o);
	}

	private static String cleanIRI(Object IRI) {
		String sIRI = IRI.toString();
		return sIRI.toString().replaceFirst(".*#", "").replace(">", "");
	}

	static GraphicELGraph OWLToGraphicELGraph(String OWLFile) throws OWLOntologyCreationException {
		File ontologyFile = new File(OWLFile);
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLOntology o = man.loadOntologyFromOntologyDocument(ontologyFile);

		OWL2GraphicELProfile GELProfile = new OWL2GraphicELProfile();
		OWLProfileReport report = GELProfile.checkOntology(o);
		if (!report.isInProfile())
			throw new IllegalArgumentException();

		GraphicELGraph G = new GraphicELGraph();
		G.addVertex("INIT");
		G.addVertex("http://www.w3.org/2002/07/owl#Nothing");
		
		p("vertices:");
		o.classesInSignature().forEach(c -> {
			p("\t" + cleanIRI(c));
			G.addVertex(c.getIRI().toString());
		});

		o.individualsInSignature().forEach(i -> {
			p("\t" + cleanIRI(i));
			String individualIRI = i.getIRI().toString();
			G.addVertex(individualIRI);
			G.addArrowInit(individualIRI);
		});

		OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(o));
		OWLOntologyWalkerVisitor visitor = new OWLOntologyWalkerVisitor(walker) {
			@Nonnull
			private final Stack<String> operations = new Stack<String>();

			private void insertArrows() {
				insertArrows(false);
			}
			
			private void insertArrowsEquivalent() {
				insertArrows(true);
			}
			
			private void insertArrows(boolean isEquivalent) {
				String IRIA = operations.pop();
				String propertyIRI = "";
				String artificialNodeIRI = "";
				int type = 0;
				p0("\t" + cleanIRI(IRIA) + " ");
				if (operations.peek().equals("PROPERTY")) {
					operations.pop();
					propertyIRI = operations.pop();
					artificialNodeIRI = propertyIRI + "." + IRIA;
					G.addVertex(artificialNodeIRI);
					p0("(" + cleanIRI(propertyIRI) + ")");
					type = 1;
				}

				if (isEquivalent)
					p0("<->");
				else
					p0("->");

				String IRIB = operations.pop();
				if (!operations.isEmpty() && operations.peek().equals("PROPERTY")) {
					operations.pop();
					propertyIRI = operations.pop();
					p0("(" + cleanIRI(propertyIRI) + ")");
					artificialNodeIRI = propertyIRI + "." + IRIB;
					G.addVertex(artificialNodeIRI);
					type = 2;
				}
				p(" " + cleanIRI(IRIB));

				if (!operations.isEmpty())
					throw new IllegalArgumentException();

				if (type == 0)
					G.addArrowISA(IRIA, IRIB);
				if (type == 0 && isEquivalent)
					G.addArrowISA(IRIB, IRIA);

				if (type == 1)
					G.addArrowRole(artificialNodeIRI, IRIB, propertyIRI);			
				if (type == 1 && isEquivalent)
					G.addArrowRole(IRIB, IRIA, propertyIRI);
	
				if (type == 2)
					G.addArrowRole(IRIA, IRIB, propertyIRI);
				if (type == 2 && isEquivalent)
					G.addArrowRole(artificialNodeIRI, IRIA, propertyIRI);
	
			}

			@Override
			public void visit(OWLOntology o) {
				o.axioms().forEach(a -> a.accept(this));
			}

			@Override
			public void visit(OWLSubClassOfAxiom a) {
				a.getSuperClass().accept(this);
				a.getSubClass().accept(this);
				insertArrows();
			}

			@Override
			public void visit(OWLEquivalentClassesAxiom a) {
				a.operands().forEach(op -> op.accept(this));
				insertArrowsEquivalent();
			}

			@Override
			public void visit(OWLClass ce) {
				operations.push(ce.getIRI().toString());
			}
			
			public void visit(OWLNamedIndividual ce) {
				operations.push(ce.getIRI().toString());
			}

			@Override
			public void visit(OWLObjectSomeValuesFrom ce) {
				ce.objectPropertiesInSignature().forEach(op -> operations.push(op.getIRI().toString()));
				operations.push("PROPERTY");
				ce.classesInSignature().forEach(cl -> operations.push(cl.getIRI().toString()));
			}
			
			@Override
			public void visit(OWLClassAssertionAxiom ce) {
				ce.getClassExpression().accept(this);
				ce.getIndividual().accept(this);
				insertArrows();
			}
			
			@Override
			public void visit(OWLObjectPropertyAssertionAxiom ce) {
				ce.getProperty().accept(this);
				ce.getObject().accept(this);
				ce.getSubject().accept(this);
				insertArrows();
			}
			
			public void visit(OWLObjectProperty ce) {
				operations.push(ce.getIRI().toString());
				operations.push("PROPERTY");
			}

		};

		p("axioms:");
		o.accept(visitor);


		p("subproperties:");
		for (OWLAxiom a : o.getRBoxAxioms(Imports.INCLUDED)) {
			String IRIA = "";
			String IRIB = "";
			String IRIC = "";

			for (Object ob : a.componentsWithoutAnnotations().toArray(Object[]::new)) {
				try {
					OWLObjectPropertyImpl opi = (OWLObjectPropertyImpl) ob;
					if (IRIA == null || IRIA.isEmpty())
						IRIA = opi.getIRI().toString();
					else if (IRIB == null || IRIB.isEmpty())
						IRIB = opi.getIRI().toString();
					else
						IRIC = opi.getIRI().toString();
				} catch (Exception e) {
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
				p("\t" + cleanIRI(IRIA) + " -> " + cleanIRI(IRIB));
				G.addSubPropery(IRIA, IRIB);
			} else {
				p("\t" + cleanIRI(IRIA) + " o " + cleanIRI(IRIB) + " -> " + cleanIRI(IRIC));
				G.addChainedSubProperty(IRIA, IRIB, IRIC);
			}
		}
		
		return G;
	}

	static String OWLToGraphicELJSON(String OWLFile) throws OWLOntologyCreationException {
		return OWLToGraphicEL.OWLToGraphicELGraph(OWLFile).toString();
	}
}
