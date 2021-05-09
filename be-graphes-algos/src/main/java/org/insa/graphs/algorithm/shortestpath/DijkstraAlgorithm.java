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

    public void setCost(Label label, float cost) {
        label.setOriginCost(cost);
    }

    public Label makeLabel(Node node,Node destination){
        return(new Label(node));
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        Graph graph = data.getGraph();
        List<Node> nodes = graph.getNodes();

        if (data.getDestination() == data.getOrigin()){
            return new ShortestPathSolution(data,Status.INFEASIBLE,new Path(graph));
        }
        
        List<Label> labels = new ArrayList<Label>();
        BinaryHeap<Label> tas = new BinaryHeap<Label>(); 
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // INITIALISATION
        // Boucle for qui va de 0 à N-1 : N = nombre de nodes
        for (int i = 0; i < nodes.size();i++) {
            Label currentLabel = makeLabel(nodes.get(i),data.getDestination());
            currentLabel.setMark(false);
            currentLabel.setOriginCost(Float.MAX_VALUE);
            currentLabel.setFather(null);
            labels.add(currentLabel);
        }

        labels.get(data.getOrigin().getId()).setOriginCost(0);
        tas.insert(labels.get(data.getOrigin().getId()));
        Label label_x = null;
        Label label_y = null;
        boolean the_end = false;

        while (!(tas.isEmpty()) && !the_end){

            label_x = tas.deleteMin();
            notifyNodeReached(label_x.getCurrentSummit());
            label_x.setMark(true);
            // System.out.println("Coût : "+label_x.getTotalCost());
            if (label_x == labels.get(data.getDestination().getId())){
                // On est arrivé au dernier noeud, on peut sortir de la boucle
                the_end = true;
            } else {
                Node node_x = nodes.get(label_x.getCurrentSummit().getId());
                // System.out.println("nb nodes : "+node_x.getNumberOfSuccessors());
                for (Arc arc : node_x.getSuccessors()){
                    // System.out.println("One successor");
                    if (!data.isAllowed(arc)) {
                        continue;
                    }
                    Node y = arc.getDestination();
                    label_y = labels.get(y.getId());
                    if (!(label_y.getMark())){
                        float old_origin_Cost = label_y.getOriginCost();
                        label_y.setOriginCost(Math.min(old_origin_Cost,(label_x.getOriginCost()+arc.getLength())));
                        if (old_origin_Cost != label_y.getOriginCost()){
                            // On fait remove puis insert pour mettre à jour et le mettre au bon endroit dans le tas
                            if (old_origin_Cost != Float.MAX_VALUE){
                                tas.remove(label_y);
                            }
                            tas.insert(label_y);
                            label_y.setFather(arc);
                        }
                    }
                }
            }

            // System.out.println("Le tas est valide : "+tas.isValid());

        }
        
        List<Node> nodes_solution = new ArrayList<Node>();

        if (!the_end){
            solution = new ShortestPathSolution(data,Status.INFEASIBLE,Path.createShortestPathFromNodes(graph,nodes_solution));
        } else {    
            Label current_label = label_x;
            nodes_solution.add(0,label_x.getCurrentSummit());
            Node current_node = current_label.getFather().getOrigin();
            nodes_solution.add(0,current_node);
            while (labels.get(current_label.getFather().getOrigin().getId()).getFather() != null){
                current_label = labels.get(current_label.getFather().getOrigin().getId());
                current_node = current_label.getFather().getOrigin();
                nodes_solution.add(0,current_node);
            }
            Path path_solution = Path.createShortestPathFromNodes(graph, nodes_solution);
            // System.out.println("Chemin valide ? "+path_solution.isValid());
            // System.out.println("Path : "+path_solution.getLength()+" Algo : "+label_x.origin_Cost);
            solution = new ShortestPathSolution(data,Status.OPTIMAL,path_solution);
            notifyDestinationReached(data.getDestination());
        }

        

        return solution;
    }

}
