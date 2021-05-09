package org.insa.graphs.algorithm.utils;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {
    
    // Graph used for tests
    private static Graph g1;

    private static List<Node> nodes = new ArrayList<Node>();

    @BeforeClass
    public static void initAll() throws IOException {

        String mapCarreDense = "C:/Users/bapti/OneDrive/Documents/COURS/S2/Graphes/BE_Graphes/maps_tf/carre-dense.mapgr";

        GraphReader readerCarreDense = new BinaryGraphReader(new DataInputStream(new FileInputStream(mapCarreDense)));
        
        g1 = readerCarreDense.read();
        
        nodes = g1.getNodes();


        // Créer des scénarios différents
        // - Cartes Routière / Non routière
        // - Coût en Temps / Distance
        // - Origine et Destination différentes

    }

    @Test
    public void testDoRun() {

        // Chemin de longueur nulle :
        
        // Carte non-routière
        // Mode Distance
        // On génère 50 paires Origine/Destination
        int id_origin;
        int id_dest;
        List<List<Node>> couples_nodes = new ArrayList<>(); 
        List<Node> aux_list = new ArrayList<>();
        for (int i = 0; i <50;i++){
            id_origin = (int)Math.random()*nodes.size();
            aux_list.add(nodes.get(id_origin));
            id_dest = (int)Math.random()*nodes.size();
            aux_list.add(nodes.get(id_dest));
            couples_nodes.add(aux_list);
            aux_list.clear();
        }

        DijkstraAlgorithm dijkstra_dist_no_road = new DijkstraAlgorithm(new ShortestPathData(g1,couples_nodes.get(0).get(0),couples_nodes.get(0).get(1),null));
        ShortestPathSolution dijkstra_solution_dist_no_road = dijkstra_dist_no_road.run();
        
        DijkstraAlgorithm bellman_dist_no_road = new DijkstraAlgorithm(new ShortestPathData(g1,couples_nodes.get(0).get(0),couples_nodes.get(0).get(1),null));
        ShortestPathSolution bellman_solution_dist_no_road = bellman_dist_no_road.run();

        assertEquals(dijkstra_solution_dist_no_road,bellman_solution_dist_no_road);
    }

}
