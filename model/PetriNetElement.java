package model;

import view.figures.*;

import java.awt.geom.Point2D;

public abstract class PetriNetElement {

    protected Point2D position;
    protected String id;
    protected String figureId;


    ///////////////////
    //abstract methods
    ////////////////////

    // public abstract PetriNetElement cloneElement();
    //return associated figure Object
    public abstract BaseFigure getFigure();


    /////////////////////
    //implemented methods
    //////////////////////

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }




}

