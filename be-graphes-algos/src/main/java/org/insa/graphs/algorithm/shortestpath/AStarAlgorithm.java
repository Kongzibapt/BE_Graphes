package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    public LabelStar makeLabel(Node node, ShortestPathData data){
        if (data.getMode() == Mode.LENGTH){
            return(new LabelStar(node,(float)(Point.distance(node.getPoint(),data.getDestination().getPoint()))));
        } else {
            return(new LabelStar(node,(float)( Point.distance(node.getPoint(),data.getDestination().getPoint()) / (data.getGraph().getGraphInformation().getMaximumSpeed()/3.6) )));
        }
    }

}
