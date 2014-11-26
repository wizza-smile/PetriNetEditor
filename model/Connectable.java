package model;

import view.figures.*;

import java.util.*;

import java.awt.geom.Point2D;

public abstract class Connectable extends PetriNetElement {

    public String label;
    protected ArrayList<String> arc_ids = new ArrayList<String>();


    public void addNewArc() {
        Arc arc = new Arc(this.getId(), this.getElementType());
        addArcId(arc.getId());
    }

    public ArrayList<String> getArcIds() {
        return arc_ids;
    }

    public void addArcId(String arc_id) {
        arc_ids.add(arc_id);
    }

    public void removeArcId(String arc_id) {
        arc_ids.remove(arc_id);
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Point2D getPosition() { return position; }

    /**
     * whenever the position of a connectable is set the related Figure's updatePosition() method gets called,
     * so that the Label's positon will be updated too.
     * @param position [description]
     */
    public void setPosition(Point2D position) {
        this.position = position;
        //update figure position!
        ((Positionable)this.getFigure()).updatePosition();
    }

}

