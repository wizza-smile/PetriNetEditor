package model;

import view.figures.*;

import java.awt.geom.Point2D;

public abstract class PetriNetElement {

    protected Point2D position;

    //abstract methods
    public abstract PetriNetElement cloneElement();
    public abstract BaseFigure getFigure();

    //implemented methods
    public Point2D getPosition() { return position; }
    public void setPosition(Point2D position) { this.position = position; }


}

