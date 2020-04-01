package JanusGraphTest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.shaded.kryo.io.Input;
import org.janusgraph.core.ConfiguredGraphFactory;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.RelationType;
import org.janusgraph.core.schema.JanusGraphManagement;

public class JanusGraphPractice {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		
//		JanusGraph graph = JanusGraphFactory.
//				open("/home/selva/Downloads/janusgraph/janusgraph-0.5.0/conf/janusgraph-cql.properties");
		
		JanusGraph graph = JanusGraphFactory.build().
				set("storage.backend", "cql").
				set("storage.hostname", "127.0.0.1").
				set("storage.cql.keyspace", "janusgraph_test").
				set("index.search.backend", "elasticsearch").
				set("index.search.hostname", "127.0.0.1").open();
		
		
		
		
		final JanusGraphManagement management = graph.openManagement();
		
		if(!management.getRelationTypes(RelationType.class).iterator().hasNext()) {
		
		//Create properties
		management.makePropertyKey("name").dataType(String.class).make();
        management.makePropertyKey("age").dataType(Integer.class).make();
		
        //create vertex label
        management.makeVertexLabel("student").make();
        management.makeVertexLabel("teacher").make();
        
        //create edge label
        management.makeEdgeLabel("friend").multiplicity(Multiplicity.MANY2ONE).make();
        management.makeEdgeLabel("staff").multiplicity(Multiplicity.MANY2ONE).make();
		}
		
//        System.out.println("*************************" + management.get("index.search.backend"));

		
		management.commit();
		
		JanusGraphTransaction tx = graph.newTransaction();
		
		//creating vertices
        Vertex selva = tx.addVertex(T.label, "student", "name", "selva", "age", 21);
        Vertex sundar = tx.addVertex(T.label, "student", "name", "sundar", "age", 21);
        Vertex matt = tx.addVertex(T.label, "teacher", "name", "matt", "age", 34);
        Vertex henry = tx.addVertex(T.label, "teacher", "name", "henry", "age", 40);

        //creating edges
        selva.addEdge("staff", matt);
        selva.addEdge("friend", sundar);
        
        tx.commit();
        
        GraphTraversalSource graphTraversalSource = graph.traversal();
        
        System.out.println(graphTraversalSource.V().hasLabel("student").out("friend").valueMap(true).next());
		
		graph.close();
		
	}

}
