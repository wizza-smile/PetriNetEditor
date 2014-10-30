package model;

import view.figures.*;

import java.awt.geom.Point2D;

public abstract class PetriNetElement {

    protected Point2D position;
    protected String elementId;
    protected BaseFigure figure;

    ///////////////////
    //abstract methods
    ////////////////////

    public abstract PetriNetElement cloneElement();
    //return associated figure Object
    public abstract BaseFigure getFigure();

    //implemented methods
    public String getId() { return elementId; }
    public void setId(String elementId) { this.elementId = elementId; }

    public Point2D getPosition() { return position; }
    public void setPosition(Point2D position) { this.position = position; }


}

