package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        Graph graph = data.getGraph();
        List<Node> nodes = graph.getNodes();
        
        List<Label> labels = new ArrayList<Label>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>(); 
        

        // INITIALISATION
        // Boucle for qui va de 0 à N-1 : N = nombre de nodes
        for (int i = 0; i < nodes.size();i++) {
            Label currentLabel = new Label(nodes.get(i));
            currentLabel.mark = false;
            currentLabel.cost = Float.MAX_VALUE;
            currentLabel.father = null;
            labels.add(currentLabel);
        }

        labels.get(data.getOrigin().getId()).cost = 0;
        tas.insert(labels.get(data.getOrigin().getId()));
        Label label_x = null;
        Label label_y = null;
        boolean the_end = false;

        while ((!tas.isEmpty()) && !the_end){

            label_x = tas.deleteMin();
            label_x.mark = true;
            if (label_x == labels.get(data.getDestination().getId())){
                // On est arrivé au dernier noeud, on peut sortir de la boucle
                the_end = true;
            } else {
                Node node_x = nodes.get(label_x.currentSummit.getId());
                for (Arc arc : node_x.getSuccessors()){
                    if (!data.isAllowed(arc)) {
                        continue;
                    }
                    Node y = arc.getDestination();
                    label_y = labels.get(y.getId());
                    if (!(label_y.mark)){
                        float old_cost = label_y.getCost();
                        label_y.cost = Math.min(old_cost,(label_x.cost+arc.getLength()));
                        if (old_cost != label_y.cost){
                            // On fait remove puis insert pour mettre à jour et le mettre au bon endroit dans le tas
                            if (old_cost != Float.MAX_VALUE){
                                tas.remove(label_y);
                            }
                            tas.insert(label_y);
                            label_y.father = arc;
                        }
                    }
                }
            }
        }
        Path path_solution = null;
        List<Node> nodes_solution = new ArrayList<Node>();

        if (!the_end){
            Label current_label = label_x;
            nodes_solution.add(label_x.currentSummit);
            Node current_arc = current_label.father.getOrigin();
            nodes_solution.add(current_arc);
            while (labels.get(current_label.father.getOrigin().getId()).father != null){
                current_label = labels.get(current_label.father.getOrigin().getId());
                current_arc = current_label.father.getOrigin();
                nodes_solution.add(0,current_arc);
            }
            solution = new ShortestPathSolution(data,Status.INFEASIBLE,path_solution);
        } else {    
            path_solution = Path.createShortestPathFromNodes(graph, nodes_solution);
            solution = new ShortestPathSolution(data,Status.OPTIMAL,path_solution);
        }

        return solution;
    }

}
