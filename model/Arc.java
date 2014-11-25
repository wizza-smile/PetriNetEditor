package model;

import view.figures.*;
import controller.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public class Arc extends PetriNetElement {

    public static final int TARGET_PLACE = 0;
    public static final int TARGET_TRANSITION = 1;
    public static final int TARGET_BOTH = 2;

    protected ArcFigure arcfigure;

    protected int target_type;

    protected String transition_id, place_id;

    public Arc(String source_id, String target_id) {
        register();

        Connectable source_element = (Connectable)PetriNetController.getElementById(source_id);

        if (source_element instanceof Transition) {
            this.transition_id = source_id;
            this.target_type = TARGET_PLACE;
        }
        if (source_element instanceof Place) {
            this.place_id = source_id;
            this.target_type = TARGET_TRANSITION;
        }
        //cache a figure
        this.getFigure();
        this.getSource().addArcId(this.getId());
        selectTarget(target_id);
    }

    public Arc(String source_id, int type) {
        register();

        if (type == PetriNetController.ELEMENT_TRANSITION) {
            this.transition_id = source_id;
            this.target_type = TARGET_PLACE;
        }
        if (type == PetriNetController.ELEMENT_PLACE) {
            this.place_id = source_id;
            this.target_type = TARGET_TRANSITION;
        }
        //cache a figure
        this.getFigure();
    }

    public void register() {
        String arc_id = "a_"+PetriNetController.getPetriNet().getNextElementId();
        this.setId(arc_id);
        PetriNetController.addElement(this, PetriNetController.ELEMENT_ARC);
    }

    public int getElementType() {
        return PetriNetController.ELEMENT_ARC;
    }

    public void delete() {
        if (place_id != null) {
            Place place = (Place)PetriNetController.getElementById(place_id);
            place.removeArcId(this.getId());
        }
        if (transition_id != null){
            Transition transition = (Transition)PetriNetController.getElementById(transition_id);
            transition.removeArcId(this.getId());
        }
        CanvasController.removeFigure(this.getId());
        PetriNetController.removeArc(this.getId());
    }

    public ArcFigure getFigure() {
        if (arcfigure == null) {
            arcfigure = new ArcFigure(this);
        }

        return arcfigure;
    };

    public boolean connectsSameElements(String elem_id_1, String elem_id_2) {
        if (elem_id_1 == this.place_id && elem_id_2 == this.transition_id
            || elem_id_2 == this.place_id && elem_id_1 == this.transition_id) {
            return true;
        }
        return false;
    }

    //the input arc will be removed!
    protected void merge(Arc arc) {
        //if the two arcs differ in place/transiiton
        if (arc.target_type == TARGET_BOTH || arc.target_type != this.target_type ) {
            //now both directions
            this.target_type = TARGET_BOTH;
        }
        arc.delete();

        return;
    }


    public boolean selectTarget(String target_id) {
        String source_id = this.getSourceId();
        boolean validTarget = false;


        Connectable target_elem = (Connectable)PetriNetController.getElementById(target_id);
        if (TARGET_TRANSITION == this.target_type && target_elem instanceof Transition) {
            // this.target_id = target_id;
            this.transition_id = target_id;
            target_elem.addArcId(this.getId());
            validTarget = true;
        }
        if (TARGET_PLACE == this.target_type && target_elem instanceof Place) {
            // this.target_id = target_id;
            this.place_id = target_id;
            target_elem.addArcId(this.getId());
            validTarget = true;
        }


        if (!validTarget) return false;

        //Check if an arc already exists with the same place and transition
        Arc arc = null;
        boolean doublette_found = false;
        for (String arc_id : PetriNetController.getPetriNet().getArcIds() ) {
            arc = (Arc)PetriNetController.getElementById(arc_id);
            if (this.getId() != arc.getId() && arc.connectsSameElements(target_id, source_id)) {
                doublette_found = true;
                break;
            }
        }

        //if yes merge!
        if (doublette_found) {
            //merge/remove after iteration (concurrency!)
            this.merge(arc);
        }

        //unset the red arc with no target
        CanvasController.arc_no_target_id = null;

        return true;
    }

    public Transition getTransition() {
        return (Transition)PetriNetController.getElementById(this.transition_id);
    }

    public Place getPlace() {
        return (Place)PetriNetController.getElementById(this.place_id);
    }

    //if Arc has definite source element, return this elements id
    public String getSourceId() {
        if (this.target_type == TARGET_PLACE) {
            return this.transition_id;
        }
        if (this.target_type == TARGET_TRANSITION) {
            return this.place_id;
        }
        return null;
    }

    //if Arc has definite source element, return this elements id
    public String getTargetId() {
        if (this.target_type == TARGET_PLACE) {
            return this.place_id;
        }
        if (this.target_type == TARGET_TRANSITION) {
            return this.transition_id;
        }
        return null;
    }

    public Connectable getSource() {
        return (Connectable)PetriNetController.getElementById(getSourceId());
    }

    public int getTargetType() {
        return this.target_type;
    }

    public void removeTarget(int target_type) {
        if (this.target_type == TARGET_BOTH) {
            this.target_type -= target_type+1;
        }
        if (this.target_type == target_type ) {
            this.delete();
        }
    }

    public boolean isTargetSet() {
        return transition_id == null || place_id == null ? false : true;
    }


}