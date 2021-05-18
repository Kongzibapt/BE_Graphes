package org.insa.graphs.algorithm.shortestpath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraAlgorithmTest {
    
    // Graph used for tests
    private static Graph insa_map;
    // private static Graph carre_dense_map;

    private static List<Node> nodes_insa = new ArrayList<Node>();
    // private static List<Node> nodes_carre_dense = new ArrayList<Node>();

    private static GraphReader readerInsa;
    // private static GraphReader readerCarreDense;

    @BeforeClass
    public static void initAll() throws IOException {

        String mapInsa = "C:/Users/bapti/OneDrive/Documents/COURS/S2/Graphes/BE_Graphes/maps_tf/insa.mapgr";
        // String mapCarreDense = "C:/Users/bapti/OneDrive/Documents/COURS/S2/Graphes/BE_Graphes/maps_tf/carre-dense.mapgr";

        readerInsa = new BinaryGraphReader(new DataInputStream (new BufferedInputStream(new FileInputStream(mapInsa))));
        // readerCarreDense = new BinaryGraphReader(new DataInputStream (new BufferedInputStream(new FileInputStream(mapCarreDense))));

        insa_map = readerInsa.read();
        // carre_dense_map = readerCarreDense.read();
        
        nodes_insa = insa_map.getNodes();
        // nodes_carre_dense = insa_map.getNodes();

        // Créer des scénarios différents
        // - Cartes Routière / Non routière
        // - Coût en Temps / Distance
        // - Origine et Destination différentes

    }

    @Test
    public void testDoRun() throws IOException {

        // Chemin de longueur nulle :

        // Carte routière
        
            List<ArcInspector> arc_inspectors = ArcInspectorFactory.getAllFilters();

            // Path Infeasible
            DijkstraAlgorithm D_Sh_all_roads_infeasible = new DijkstraAlgorithm(new ShortestPathData(insa_map,nodes_insa.get(1256),nodes_insa.get(1020),arc_inspectors.get(0)));
            assertEquals(D_Sh_all_roads_infeasible.run().isFeasible(),false);

            // Unique Summit
            DijkstraAlgorithm D_Sh_all_roads_oneSummit = new DijkstraAlgorithm(new ShortestPathData(insa_map,nodes_insa.get(0),nodes_insa.get(0),arc_inspectors.get(0)));
            assertEquals(D_Sh_all_roads_oneSummit.run().isFeasible(),true);

            // Path not valid
            // DijkstraAlgorithm D_Sh_all_roads_invalid = new DijkstraAlgorithm(new ShortestPathData(g1,nodes.get(1256),null,arc_inspectors.get(0)));
            // assertEquals(D_Sh_all_roads_invalid.run().getPath().isValid(),false);


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

            DijkstraAlgorithm dijkstra_short_all = new DijkstraAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_all));
            ShortestPathSolution dijkstra_sol_short_all = dijkstra_short_all.run();
            
            if (dijkstra_sol_short_all.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_short_all = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_all));
                ShortestPathSolution bellman_sol_short_all = bellman_short_all.run();

                System.out.println(couples_nodes.get(index).get(0)+" et "+couples_nodes.get(index).get(1));

                // Comparaison de la longueur du path
                assertEquals(dijkstra_sol_short_all.getPath().getLength(),
                bellman_sol_short_all.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(dijkstra_sol_short_all.getPath().getMinimumTravelTime(),bellman_sol_short_all.getPath().getMinimumTravelTime(),0.00001);

            }

            // Shortest Path, Only roads open for cars
            ArcInspector Short_only = arc_inspectors.get(1);

            DijkstraAlgorithm dijkstra_short_only = new DijkstraAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_only));
            ShortestPathSolution dijkstra_sol_short_only = dijkstra_short_only.run();
            
            if (dijkstra_sol_short_only.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_short_only = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Short_only));
                ShortestPathSolution bellman_sol_short_only = bellman_short_only.run();

                // Comparaison de la longueur du path
                assertEquals(dijkstra_sol_short_only.getPath().getLength(),bellman_sol_short_only.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(dijkstra_sol_short_only.getPath().getMinimumTravelTime(),bellman_sol_short_only.getPath().getMinimumTravelTime(),0.00001);

            }


            // Fastest Path, All Roads Allowed
            ArcInspector Fast_all = arc_inspectors.get(2);

            DijkstraAlgorithm dijkstra_fast_all = new DijkstraAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_all));
            ShortestPathSolution dijkstra_sol_fast_all = dijkstra_fast_all.run();
            
            if (dijkstra_sol_fast_all.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_fast_all = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_all));
                ShortestPathSolution bellman_sol_fast_all = bellman_fast_all.run();

                // Comparaison de la longueur du path
                assertEquals(dijkstra_sol_fast_all.getPath().getLength(),bellman_sol_fast_all.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(dijkstra_sol_fast_all.getPath().getMinimumTravelTime(),bellman_sol_fast_all.getPath().getMinimumTravelTime(),0.00001);

            }

            // Fastest Path, Only Roads open for cars
            ArcInspector Fast_only = arc_inspectors.get(3);

            DijkstraAlgorithm dijkstra_fast_only = new DijkstraAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_only));
            ShortestPathSolution dijkstra_sol_fast_only = dijkstra_fast_only.run();
            
            if (dijkstra_sol_fast_only.isFeasible()){
                nb_tests_valid++;

                BellmanFordAlgorithm bellman_fast_only = new BellmanFordAlgorithm(new ShortestPathData(insa_map,couples_nodes.get(index).get(0),couples_nodes.get(index).get(1),Fast_only));
                ShortestPathSolution bellman_sol_fast_only = bellman_fast_only.run();

                // Comparaison de la longueur du path
                assertEquals(dijkstra_sol_fast_only.getPath().getLength(),bellman_sol_fast_only.getPath().getLength(),0.00001);

                // Comparaison du temps min du path
                assertEquals(dijkstra_sol_fast_only.getPath().getMinimumTravelTime(),bellman_sol_fast_only.getPath().getMinimumTravelTime(),0.00001);

            }

        index = (index + 1) %50;
    }
    readerInsa.close();
}
}
