package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    
    // sommet courant
    protected Node currentSummit;

    protected boolean mark;

    protected float cost;

    protected Arc father;

    public Label(Node node){
        this.currentSummit = node;
        this.mark = false;
        this.cost = 0;
        this.father = null;
    }


    public float getCost(){
        return(this.cost);
    }

    public int compareTo(Label other){
        return (int)(this.getCost()-other.getCost());
    }


}
