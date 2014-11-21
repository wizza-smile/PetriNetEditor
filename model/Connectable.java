package model;

import view.figures.*;

import java.awt.geom.Point2D;

public abstract class Connectable extends PetriNetElement {

    public String label;

    public void addArcId(String arc_id){}


    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Point2D getPosition() { return position; }

    public void setPosition(Point2D position) {
        this.position = position;
        //update figure position!
        ((Positionable)this.getFigure()).updatePosition();
    }

}

