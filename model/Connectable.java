package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.geom.Point2D;

/**
 * Connectable PetriNet Elements (Transition/Place)
 */
public abstract class Connectable extends PetriNetElement {

    public static final String NO_LABEL_IDENTIFIER = "no_label";
    public String label;
    protected ArrayList<String> arc_ids = new ArrayList<String>();

    /**
     * Constructor to be called by PNML Parser
     */
    Connectable (String connectable_id) {
        register(connectable_id);
        this.position = new Point2D.Double(0.,0.);
        this.setLabel(NO_LABEL_IDENTIFIER);
        this.getFigure();
    }

    /**
     * Constructor to be called when user clicks into canvas
     */
    Connectable(Point2D position) {
        register();
        this.position = position;
        this.setLabel(NO_LABEL_IDENTIFIER);
        this.getFigure();
    }

    public void delete() {
        SelectionController.clearSelection();
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
        unregister();
    }

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
     * so that eventually the Label's positon will be updated too.
     * @param position [description]
     */
    public void setPosition(Point2D position) {
        this.position = position;
        //update figure position!
        ((Positionable)this.getFigure()).updatePosition();
    }

}

