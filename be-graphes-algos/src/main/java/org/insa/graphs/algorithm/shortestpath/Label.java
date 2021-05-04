package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
    
    // sommet courant
    private Node currentSummit;

    private boolean mark;

    private float origin_Cost;

    private Arc father;

    public Label(Node node){
        this.currentSummit = node;
        this.mark = false;
        this.origin_Cost = 0;
        this.father = null;
    }

    public Node getCurrentSummit(){
        return(this.currentSummit);
    }

    public void setCurrentSummit(Node currentSummit){
        this.currentSummit = currentSummit;
    }

    public boolean getMark(){
        return(this.mark);
    }

    public void setMark(boolean mark){
        this.mark = mark;
    }

    public float getOriginCost(){
        return(this.origin_Cost);
    }

    public void setOriginCost(float origin_Cost){
        this.origin_Cost = origin_Cost;
    }

    public Arc getFather(){
        return(this.father);
    }

    public void setFather(Arc father){
        this.father = father;
    }

    public float getTotalCost(){
        return this.origin_Cost;
    }

    public int compareTo(Label other){
        return (Float.compare(this.getTotalCost(),other.getTotalCost()));
    }


}
