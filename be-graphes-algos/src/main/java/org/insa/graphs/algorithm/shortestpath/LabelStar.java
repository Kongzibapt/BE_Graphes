package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class LabelStar extends Label {
    
    private float estimated_Cost;

    public LabelStar(Node node, float estimated){
        super(node);
        this.estimated_Cost = estimated;
    }

    public float getEstimatedCost(){
        return(this.estimated_Cost);
    }

    public void setEstimatedCost(float estimated_Cost){
        this.estimated_Cost = estimated_Cost;
    }

    public float getTotalCost(){
        return(this.estimated_Cost+this.getOriginCost());
    }

}
