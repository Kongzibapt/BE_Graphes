package org.insa.graphs.algorithm.utils;

import java.io.IOException;
import java.util.*;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {
    
    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    // Some paths...
    private static Path emptyPath, singleNodePath, shortPath, longPath, loopPath, longLoopPath, invalidPath;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation motorway = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                non_motorway = new RoadInformation(RoadType.PEDESTRIAN, null, true, 36, ""),
                motorway_fast = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[5];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, motorway_fast, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 15, motorway, null);
        a2e = Node.linkNodes(nodes[0], nodes[4], 15, non_motorway, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 10, motorway, null);
        c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, motorway_fast, null);
        c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, motorway, null);
        c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, non_motorway, null);
        d2a = Node.linkNodes(nodes[3], nodes[0], 15, motorway, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, non_motorway, null);
        e2d = Node.linkNodes(nodes[4], nodes[0], 10, motorway_fast, null);

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

        // Créer des scénarios différents
        // - Cartes Routière / Non routière
        // - Coût en Temps / Distance
        // - Origine et Destination différentes

    }

    @Test

    public void testDoRun() {

        // Chemin de longueur nulle :
        

    }

}
