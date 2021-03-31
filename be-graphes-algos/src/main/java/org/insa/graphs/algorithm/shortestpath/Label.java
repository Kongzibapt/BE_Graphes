package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;

public class Label {
    
    // sommet courant
    private int currentSummit;

    private boolean mark;

    private float cost;

    private Arc father;

    public Label(int node){
        this.currentSummit = node;
        
    }


    public float getCost(){
        return(this.cost);
    }


}
