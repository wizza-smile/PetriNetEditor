package model;

import view.figures.*;
import controller.*;

import java.awt.geom.Point2D;

public class Arc extends PetriNetElement {

    public static final int TARGET_PLACE = 0;
    public static final int TARGET_TRANSITION = 1;
    public static final int TARGET_BOTH = 2;

    protected String elementId;
    protected ArcFigure arcfigure;

    protected int target_type;

    protected String transition_id, place_id, source_id, target_id;

    public Arc(String arcId, String source_id, int type) {
        this.elementId = arcId;
        this.source_id = source_id;
        if (type == PetriNetController.ELEMENT_TRANSITION) {
            this.transition_id = this.source_id;
            this.target_type = TARGET_PLACE;
        }
        if (type == PetriNetController.ELEMENT_PLACE) {
            this.place_id = this.source_id;
            this.target_type = TARGET_TRANSITION;
        }
        //cache a figure
        this.getFigure();
    }

    public String getId() { return elementId; }

    public ArcFigure getFigure() {
        if (arcfigure == null) {
            arcfigure = new ArcFigure(this);
        }

        return arcfigure;
    };

    public boolean connectsSameElements(String elem_id_1, String elem_id_2) {
        if (elem_id_1 == this.target_id && elem_id_2 == this.source_id
            || elem_id_2 == this.target_id && elem_id_1 == this.source_id) {
            return true;
        }
        return false;
    }

    //the input arc will be removed!
    protected void merge(Arc arc) {
        if (arc.source_id != this.source_id || arc.target_type == TARGET_BOTH) {
            //now both directions
            this.target_type = TARGET_BOTH;
        }

        return;
    }


    public boolean selectTarget(String target_id) {

        PetriNetElement target_elem = PetriNetController.getElementById(target_id);
        if (TARGET_TRANSITION == this.target_type && target_elem instanceof Transition) {
            this.target_id = target_id;
            this.transition_id = this.target_id;
        }
        if (TARGET_PLACE == this.target_type && target_elem instanceof Place) {
            this.target_id = target_id;
            this.place_id = this.target_id;
        }


        //Check if arc already exists with the same place and transition
        Arc arc = null;
        boolean doublette_found = false;
        for (String arc_id : PetriNetController.getPetriNet().getArcIds() ) {
            arc = (Arc)PetriNetController.getElementById(arc_id);
            if (this.getId() != arc.getId() && arc.connectsSameElements(target_id, this.source_id)) {
                doublette_found = true;
                break;
            }
        }
        //if yes merge!
        if (doublette_found) {
            this.merge(arc);
            //remove after iteration (concurrency)
            PetriNetController.removeArc(arc.getId());
        }

        return false;
    }

    public Transition getTransition() {
        return (Transition)PetriNetController.getElementById(this.transition_id);
    }

    public Place getPlace() {
        return (Place)PetriNetController.getElementById(this.place_id);
    }


    public PetriNetElement getSource() {
        return PetriNetController.getElementById(this.source_id);
    }

    public int getTargetType() {
        return this.target_type;
    }

    public boolean isTargetSet() {
        return target_id == null ? false : true;
    }
}