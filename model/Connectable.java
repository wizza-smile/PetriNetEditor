package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.geom.Point2D;

/**
 * Connectable PetriNet Elements (Transition/Place)
 */
public abstract class Connectable extends PetriNetElement {

    /** the position of this Connectable's midpoint. */
    protected Point2D position;

    /**
     * if the label of a Connectable is identical to this String, no label will be shown or saved.
     */
    public static final String NO_LABEL_IDENTIFIER = "no_label";

    /** the label text of this Connectable. */
    public String label;

    /** Collection of arc_ids of all arcs that have this Connectable as source or target. */
    protected ArrayList<String> arc_ids = new ArrayList<String>();


    /**
     * Constructor to be called by PNML Parser
     * @param  connectable_id  id to be used for the new Connectable
     */
    Connectable (String connectable_id) {
        register(connectable_id);
        this.position = new Point2D.Double(0.,0.);
        this.setLabel(NO_LABEL_IDENTIFIER);
        this.getFigure();
    }

    /**
     * Constructor to be called when user clicks into canvas
     * @param  position where to place the ne Connectable
     */
    Connectable(Point2D position) {
        register();
        this.position = position;
        this.setLabel(NO_LABEL_IDENTIFIER);
        this.getFigure();
    }

    /**
     * delete a Connectable Element (Place/Transition).
     */
    public void delete() {
        SelectionController.clearSelection();
        //delete all Arc that are connected to this Element.
        Arc[] all_arcs = new Arc[arc_ids.size()];
        int index = 0;
        for (String arc_id : arc_ids ) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            all_arcs[index++] = arc;
        }
        for (Arc arc  : all_arcs) {
            arc.delete();
        }
        this.getFigure().delete();
        this.unregister();
    }

    /**
     * add a new arc with this Connectable as source.
     * (Target to be choosen)
     */
    public void addNewArc() {
        Arc arc = new Arc(this.getId(), this.getElementType());
        addArcId(arc.getId());
    }

    public ArrayList<String> getArcIds() {
        return arc_ids;
    }

    /** add an arc_id to the arc_ids property. */
    public void addArcId(String arc_id) {
        arc_ids.add(arc_id);
    }

    /** remove an arc_id from the arc_ids property. */
    public void removeArcId(String arc_id) {
        arc_ids.remove(arc_id);
    }

    /** checks whether the label is set to the NO_LABEL_IDENTIFIER constant. */
    public boolean hasLabel() {
        return !this.label.equals(Connectable.NO_LABEL_IDENTIFIER);
    }

    /** returns the label String of this Connectable if it is not set to the NO_LABEL_IDENTIFIER constant. */
    public String getLabel() {
        return hasLabel() ? this.label : "";
    }

    /** set the label String of this Connectable */
    public void setLabel(String label) {
        this.label = label;
    }

    /** return the position of this Connectable's midpoint */
    public Point2D getPosition() { return position; }

    /**
     * whenever the position of a connectable is set the related Figure's updatePosition() method gets called,
     * so that eventually the Label's positon will be updated too.
     * @param position [description]
     */
    public void setPosition(Point2D position) {
        this.position = position;
        //update figure position!
        ((Positionable)this.getFigure()).updatePosition();
    }

}

