package org.insa.graphs.algorithm.shortestpath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class AStarAlgorithmTest {
    
    // Graph used for tests
    private static Graph insa_map;
    private static Graph carre_dense_map;

    private static List<Node> nodes_insa = new ArrayList<Node>();
    private static List<Node> nodes_carre_dense = new ArrayList<Node>();

    private static GraphReader readerInsa;
    private static GraphReader readerCarreDense;

    @BeforeClass
    public static void initAll() throws IOException {

        String mapInsa = "C:/Users/bapti/OneDrive/Documents/COURS/S2/Graphes/BE_Graphes/maps_tf/insa.mapgr";
        String mapCarreDense = "C:/Users/bapti/OneDrive/Documents/COURS/S2/Graphes/BE_Graphes/maps_tf/carre-dense.mapgr";

        readerInsa = new BinaryGraphReader(new DataInputStream (new BufferedInputStream(new FileInputStream(mapInsa))));
        readerCarreDense = new BinaryGraphReader(new DataInputStream (new BufferedInputStream(new FileInputStream(mapCarreDense))));

        insa_map = readerInsa.read();
        carre_dense_map = readerCarreDense.read();
        
        nodes_insa = insa_map.getNodes();
        nodes_carre_dense = insa_map.getNodes();

    }

    @Test
    public void testDoRun() throws IOException {

        // Chemin de longueur nulle :

        // Carte routière
        
            List<ArcInspector> arc_inspectors = ArcInspectorFactory.getAllFilters();

            // Path Infeasible
            AStarAlgorithm A_Sh_all_roads_infeasible = new AStarAlgorithm(new ShortestPathData(insa_map,nodes_insa.get(1256),nodes_insa.get(1020),arc_inspectors.get(0)));
            assertEquals(A_Sh_all_roads_infeasible.run().isFeasible(),false);

            // Unique Summit
            AStarAlgorithm A_Sh_all_roads_oneSummit = new AStarAlgorithm(new ShortestPathData(insa_map,nodes_insa.get(0),nodes_insa.get(0),arc_inspectors.get(0)));
            assertEquals(A_Sh_all_roads_oneSummit.run().isFeasible(),true);


        readerInsa.close();
    }

    @Test
    public void testMapInsa() throws IOException {

        List<ArcInspector> arc_inspectors = ArcInspectorFactory.getAllFilters();
        int nb_tests_valid = 0;
        int index = 0;

        // On génère 50 paires Origine/Destination
        int id_origin;
        int id_dest;
        List<List<Node>> couples_nodes = new ArrayList<>(); 
        for (int i = 0; i <50;i++){
            List<Node> aux_list = new ArrayList<>();
            id_origin = (int)(Math.random()*nodes_insa.size());
            aux_list.add(nodes_insa.get(id_origin));
            id_dest = (int)(Math.random()*nodes_insa.size());
            aux_list.add(nodes_insa.get(id_dest));
            couples_nodes.add(aux_list);   
        }

        while(nb_tests_valid < 50){

            // Shortest Path, All Roads Allowed
            ArcInspector Short_all = arc_inspectors.get(0);

            AStarAlgorithm astar_short_all = new AStarAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_all));
            ShortestPathSolution astar_sol_short_all = astar_short_all.run();
            
            if (astar_sol_short_all.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_short_all = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_all));
                ShortestPathSolution bellman_sol_short_all = bellman_short_all.run();

                // Comparaison de la longueur du path
                assertEquals(astar_sol_short_all.getPath().getLength(),bellman_sol_short_all.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(astar_sol_short_all.getPath().getMinimumTravelTime(),bellman_sol_short_all.getPath().getMinimumTravelTime(),0.00001);

            }

            // Shortest Path, Only roads open for cars
            ArcInspector Short_only = arc_inspectors.get(1);

            AStarAlgorithm astar_short_only = new AStarAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_only));
            ShortestPathSolution astar_sol_short_only = astar_short_only.run();
            
            if (astar_sol_short_only.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_short_only = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_only));
                ShortestPathSolution bellman_sol_short_only = bellman_short_only.run();

                // Comparaison de la longueur du path
                assertEquals(astar_sol_short_only.getPath().getLength(),bellman_sol_short_only.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(astar_sol_short_only.getPath().getMinimumTravelTime(),bellman_sol_short_only.getPath().getMinimumTravelTime(),0.00001);

            }


            // Fastest Path, All Roads Allowed
            ArcInspector Fast_all = arc_inspectors.get(2);

            AStarAlgorithm astar_fast_all = new AStarAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_all));
            ShortestPathSolution astar_sol_fast_all = astar_fast_all.run();
            
            if (astar_sol_fast_all.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_fast_all = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_all));
                ShortestPathSolution bellman_sol_fast_all = bellman_fast_all.run();

                // Comparaison de la longueur du path
                assertEquals(astar_sol_fast_all.getPath().getLength(),bellman_sol_fast_all.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(astar_sol_fast_all.getPath().getMinimumTravelTime(),bellman_sol_fast_all.getPath().getMinimumTravelTime(),0.00001);

            }

            // Fastest Path, Only Roads open for cars
            ArcInspector Fast_only = arc_inspectors.get(3);

            AStarAlgorithm astar_fast_only = new AStarAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_only));
            ShortestPathSolution astar_sol_fast_only = astar_fast_only.run();
            
            if (astar_sol_fast_only.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_fast_only = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_only));
                ShortestPathSolution bellman_sol_fast_only = bellman_fast_only.run();

                // Comparaison de la longueur du path
                assertEquals(astar_sol_fast_only.getPath().getLength(),bellman_sol_fast_only.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(astar_sol_fast_only.getPath().getMinimumTravelTime(),bellman_sol_fast_only.getPath().getMinimumTravelTime(),0.00001);

            }

        index = (index + 1) %50;
    }
    readerInsa.close();
}

@Test
public void testMapCarreDense() throws IOException {

    List<ArcInspector> arc_inspectors = ArcInspectorFactory.getAllFilters();
    int nb_tests_valid = 0;
    int index = 0;

    // On génère 50 paires Origine/Destination
    int id_origin;
    int id_dest;
    List<List<Node>> couples_nodes = new ArrayList<>(); 
    for (int i = 0; i <50;i++){
        List<Node> aux_list = new ArrayList<>();
        id_origin = (int)(Math.random()*nodes_carre_dense.size());
        aux_list.add(nodes_carre_dense.get(id_origin));
        id_dest = (int)(Math.random()*nodes_carre_dense.size());
        aux_list.add(nodes_carre_dense.get(id_dest));
        couples_nodes.add(aux_list);   
    }

    while(nb_tests_valid < 50){

        ArcInspector Short_all = arc_inspectors.get(0);

        AStarAlgorithm astar_short_all = new AStarAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_all));
        ShortestPathSolution astar_sol_short_all = astar_short_all.run();

        ArcInspector Fast_all = arc_inspectors.get(2);

        AStarAlgorithm astar_fast_all = new AStarAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_all));
        ShortestPathSolution astar_sol_fast_all = astar_fast_all.run();
        
        if (astar_sol_short_all.isFeasible() && astar_sol_fast_all.isFeasible()){
            nb_tests_valid++;

            // Comparaison de la longueur du path en distance avec la longueur du path en temps
            assertTrue(astar_sol_short_all.getPath().getLength()<=astar_sol_fast_all.getPath().getLength());

            // Comparaison du temps du path en temps avec le temps du path en distance
            assertTrue(astar_sol_fast_all.getPath().getMinimumTravelTime()<=astar_sol_short_all.getPath().getMinimumTravelTime());
        }


        index = (index + 1) %50;
    }

    readerCarreDense.close();
}

}

// Tester pour une carte très grande : carre dense
//     - L'algo en temps doit être inférieur en temps par rapport à l'algo en distance
//     - L'algo en distance doit être inférieur en distance par rapport à l'algo en temps

