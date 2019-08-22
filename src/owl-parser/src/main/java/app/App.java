package app;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Hello world!
 *
 */
public class App {


    public static void main(String[] args) throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File("pizza.owl");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        
        System.out.println("Hello World!");
    }
}
