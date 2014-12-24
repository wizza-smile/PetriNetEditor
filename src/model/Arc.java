package model;

import view.figures.*;
import controller.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 * the Arc.
 * an Arc can have one, two or no target (yet to be choosen).
 */
public class Arc extends PetriNetElement {

    /**
     * defines the type of target(s) of this arc (place/transition/both).
     */
    protected int target_type;

    /**
     * target type
     */
    public static final int TARGET_PLACE = 0, TARGET_TRANSITION = 1, TARGET_BOTH = 2;

    /**
     * the id of a connectable (transition/place) related to this Arc (if already choosen).
     */
    protected String transition_id, place_id;

    /**
     * Constructor to be called by PNML Parser
     * @param  arc_id    id to be used for new arc
     * @param  source_id id of source element of this arc
     * @param  target_id id of target element of this arc
     */
    public Arc(String arc_id, String source_id, String target_id) {
        register(arc_id);

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

    /**
     * Constructor to be called when user clicks into canvas
     * @param  source_id id of source element of this arc
     * @param  type      target type of this arc (transition/place)
     */
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


    public String generateId() {
        return "a_"+GlobalController.getUUID();
    }

    public void unregister() {
        PetriNetController.removeArc(this.getId());
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
        CanvasController.removeFigure(this.getId(), CanvasController.FIGURE_ARC);
        unregister();
    }

    public int getTargetType() {
        return this.target_type;
    }

    /**
     * remove the target of type target_type from this Arc. If no target remains, delete this Arc.
     * @param target_type the target_type to be removed.
     */
    public void removeTarget(int target_type) {
        if (this.target_type == TARGET_BOTH) {
            this.target_type -= target_type+1;
        }
        if (this.target_type == target_type ) {
            this.delete();
        }
    }

    /**
     * check if a target has already been choosen for this Arc.
     * @return boolean
     */
    public boolean isTargetSet() {
        return (transition_id == null || place_id == null) ? false : true;
    }

    /**
     * set the target for an unfinished Arc to the element with given target_id.
     * Checks if an Arc with the same set of Place and Transition elements already exists,
     * and if so, merges the two Arcs.
     * @param  target_id the element_id of the target to set.
     * @return true, if the target has been successfully selected, false otherwise.
     */
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

        checkAndMergeDoublette();

        //unset the red arc with no target
        CanvasController.arc_no_target_id = null;

        return true;
    }

    /**
     * if an Arc with the same Place and Transition Elements exists, merge the two Arcs.
     */
    public void checkAndMergeDoublette() {
        Arc arc = null;
        boolean doublette_found = false;
        for (String arc_id : this.getSource().getArcIds() ) {
            arc = (Arc)PetriNetController.getElementById(arc_id);
            if (this.getId() != arc.getId() && arc.connectsSameElements(this.transition_id, this.place_id)) {
                doublette_found = true;
                break;
            }
        }
        if (doublette_found) {
            //merge/remove after iteration (concurrency!)
            this.merge(arc);
        }
    }

    /**
     * check if the two input element_ids are identical to the transition_id and place_id of this Arc.
     * @param  elem_id_1 an element_id
     * @param  elem_id_2 an element_id
     * @return boolean
     */
    public boolean connectsSameElements(String elem_id_1, String elem_id_2) {
        if (elem_id_1.equals(this.place_id) && elem_id_2.equals(this.transition_id)
            || elem_id_2.equals(this.place_id) && elem_id_1.equals(this.transition_id)) {
            return true;
        }
        return false;
    }

    /**
     * merge the target types, save it to this Arc and delete the input arc.
     * @param arc the Arc to be merged with this Arc
     */
    protected void merge(Arc arc) {
        if (arc.target_type == TARGET_BOTH || arc.target_type != this.target_type ) {
            this.target_type = TARGET_BOTH;
        }
        arc.delete();

        return;
    }

    /**
     * returns the Transition related to this Arc (if already choosen).
     * @return the Place.
     */
    public Transition getTransition() {
        return (Transition)PetriNetController.getElementById(this.transition_id);
    }

    /**
     * returns the Place related to this Arc (if already choosen).
     * @return the Place.
     */
    public Place getPlace() {
        return (Place)PetriNetController.getElementById(this.place_id);
    }

    /**
     * if this Arc has a definite source element (it's target_type is not TARGET_BOTH), return the source element.
     * @return [description]
     */
    public Connectable getSource() {
        return (Connectable)PetriNetController.getElementById(getSourceId());
    }

    /**
     * if this Arc has a definite source element (it's target_type is not TARGET_BOTH), return the source's element_id.
     * @return the source's id, if defined, null otherwise.
     */
    public String getSourceId() {
        if (this.target_type == TARGET_PLACE) {
            return this.transition_id;
        }
        if (this.target_type == TARGET_TRANSITION) {
            return this.place_id;
        }
        return null;
    }

    /**
     * if this Arc has a definite target element (it's target_type is not TARGET_BOTH), return the target's element_id.
     * @return the target's id, if defined, null otherwise.
     */
    public String getTargetId() {
        if (this.target_type == TARGET_PLACE) {
            return this.place_id;
        }
        if (this.target_type == TARGET_TRANSITION) {
            return this.transition_id;
        }
        return null;
    }


    public ArcFigure createFigure() {
        return new ArcFigure(this);
    }

}