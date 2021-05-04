package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    public LabelStar makeLabel(Node node, Node destination){
        return(new LabelStar(node,(float)(Point.distance(node.getPoint(),destination.getPoint()))));
    }

}
