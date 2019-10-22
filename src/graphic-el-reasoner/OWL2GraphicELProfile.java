package br.usp.ime.dcc;

import static org.semanticweb.owlapi.model.parameters.Imports.INCLUDED;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asUnorderedSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.profiles.OWL2DLProfile;
import org.semanticweb.owlapi.profiles.OWLOntologyProfileWalker;
import org.semanticweb.owlapi.profiles.OWLProfile;
import org.semanticweb.owlapi.profiles.OWLProfileReport;
import org.semanticweb.owlapi.profiles.OWLProfileViolation;
import org.semanticweb.owlapi.profiles.violations.*;
import org.semanticweb.owlapi.util.OWLObjectPropertyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author Andrew Ijano Lopes, University of Sao Paulo
 */
class OWL2GraphicELProfile implements OWLProfile {

	protected static final Set<IRI> ALLOWED_DATATYPES = asUnorderedSet(OWL2Datatype.EL_DATATYPES.stream().map(i -> i
	        .getIRI()));
	
	@Override
	public IRI getIRI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "OWL 2 Graphic EL";
	}

	public OWLProfileReport checkOntology(OWLOntology ontology) {
        OWL2DLProfile profile = new OWL2DLProfile();
        OWLProfileReport report = profile.checkOntology(ontology);
        Set<OWLProfileViolation> violations = new HashSet<>();
        violations.addAll(report.getViolations());
        OWLOntologyProfileWalker ontologyWalker = new OWLOntologyProfileWalker(ontology.importsClosure());
        OWL2GraphicELProfileObjectVisitor visitor = new OWL2GraphicELProfileObjectVisitor(ontologyWalker);
        ontologyWalker.walkStructure(visitor);
        violations.addAll(visitor.getProfileViolations());
        return new OWLProfileReport(this, violations);
    }

    protected class OWL2GraphicELProfileObjectVisitor extends OWLOntologyWalkerVisitor {

        private OWLObjectPropertyManager propertyManager;
        @Nonnull private final Set<OWLProfileViolation> profileViolations = new HashSet<>();

        public OWL2GraphicELProfileObjectVisitor(OWLOntologyWalker walker) {
            super(walker);
        }

        public Set<OWLProfileViolation> getProfileViolations() {
            return new HashSet<>(profileViolations);
        }

        private OWLObjectPropertyManager getPropertyManager() {
            if (propertyManager == null) {
                propertyManager = new OWLObjectPropertyManager(getCurrentOntology());
            }
            return propertyManager;
        }

        @Override
        public void visit(OWLDatatype node) {
            if (!ALLOWED_DATATYPES.contains(node.getIRI())) {
                profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
            }
        }

        @Override
        public void visit(OWLAnonymousIndividual individual) {
            profileViolations.add(new UseOfAnonymousIndividual(getCurrentOntology(), getCurrentAxiom(), individual));
        }

        @Override
        public void visit(OWLObjectInverseOf property) {
            profileViolations.add(new UseOfObjectPropertyInverse(getCurrentOntology(), getCurrentAxiom(), property));
        }

        @Override
        public void visit(OWLDataAllValuesFrom ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLDataExactCardinality ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLDataMaxCardinality ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLDataMinCardinality ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLObjectAllValuesFrom ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLObjectComplementOf ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLObjectExactCardinality ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLObjectMaxCardinality ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLObjectMinCardinality ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }

        @Override
        public void visit(OWLObjectOneOf ce) {
            if (ce.individuals().count() != 1) {
                profileViolations
                    .add(new UseOfObjectOneOfWithMultipleIndividuals(getCurrentOntology(), getCurrentAxiom(), ce));
            }
        }

        @Override
        public void visit(OWLObjectUnionOf ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }
        
        @Override
        public void visit(OWLObjectIntersectionOf ce) {
            profileViolations.add(new UseOfIllegalClassExpression(getCurrentOntology(), getCurrentAxiom(), ce));
        }
        
        @Override
        public void visit(OWLDataComplementOf node) {
            profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
        }

        @Override
        public void visit(OWLDataOneOf node) {
            if (node.values().count() != 1) {
                profileViolations
                    .add(new UseOfDataOneOfWithMultipleLiterals(getCurrentOntology(), getCurrentAxiom(), node));
            }
        }

        @Override
        public void visit(OWLDatatypeRestriction node) {
            profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
        }

        @Override
        public void visit(OWLDataUnionOf node) {
            profileViolations.add(new UseOfIllegalDataRange(getCurrentOntology(), getCurrentAxiom(), node));
        }

        @Override
        public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLClassAssertionAxiom axiom) {
            axiom.getClassExpression().accept(this);
        }
        
        @Override
        public void visit(OWLDisjointClassesAxiom axiom) {
        	profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }
        
        @Override
        public void visit(OWLDisjointDataPropertiesAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLDisjointUnionAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLInverseObjectPropertiesAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), axiom));
        }

        @Override
        public void visit(SWRLRule rule) {
            profileViolations.add(new UseOfIllegalAxiom(getCurrentOntology(), rule));
        }

        @Override
        public void visit(OWLSubPropertyChainOfAxiom axiom) {
            // Do we have a range restriction imposed on our super property?
            getCurrentOntology().axioms(AxiomType.OBJECT_PROPERTY_RANGE, INCLUDED).forEach(rngAx -> {
                if (getPropertyManager().isSubPropertyOf(axiom.getSuperProperty(), rngAx.getProperty())) {
                    // Imposed range restriction!
                    OWLClassExpression imposedRange = rngAx.getRange();
                    // There must be an axiom that imposes a
                    // range on the last
                    // prop in the chain
                    List<OWLObjectPropertyExpression> chain = axiom.getPropertyChain();
                    if (!chain.isEmpty()) {
                        OWLObjectPropertyExpression lastProperty = chain.get(chain.size() - 1);
                        boolean rngPresent = rangePresent(imposedRange, lastProperty);
                        if (!rngPresent) {
                            profileViolations
                                .add(new LastPropertyInChainNotInImposedRange(getCurrentOntology(), axiom, rngAx));
                        }
                    }
                }
            });
        }

        protected boolean rangePresent(OWLClassExpression imposedRange, OWLObjectPropertyExpression lastProperty) {
            return getCurrentOntology().importsClosure().flatMap(o -> o.objectPropertyRangeAxioms(lastProperty))
                .anyMatch(l -> l.getRange().equals(imposedRange));
        }

        @Override
        public void visit(OWLOntology ontology) {
            propertyManager = null;
        }
    }

}
