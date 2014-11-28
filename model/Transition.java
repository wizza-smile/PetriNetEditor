package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Transition extends Connectable {
    protected final int elementType = PetriNetController.ELEMENT_TRANSITION;

    public String label;

    public Transition(String transition_id) {
        super(transition_id);
    }

    public Transition(Point2D position) {
        super(position);
    }

    public String generateId() {
        return "t_"+GlobalController.getUUID();
    }


    public void unregister() {
        PetriNetController.removeTransition(this.getId());
    }

    public int getElementType() {
        return PetriNetController.ELEMENT_TRANSITION;
    }

    public TransitionFigure createFigure() {
        return new TransitionFigure(this);
    }

    /**
     * check whether this Transition can(!) be activated
     */
    public boolean isActivated() {
        boolean isActivated = true;
        for (String arc_id : this.getArcIds()) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            if (arc.getTargetType() != Arc.TARGET_PLACE) {
                if (arc.getPlace() != null && arc.getPlace().getTokenCount() == 0){
                    isActivated = false;
                }
            }
        }

        return isActivated;
    }

    /** activate this Transition */
    public void activate() {
        for (String arc_id : this.getArcIds()) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            if (arc.getTargetType() != Arc.TARGET_TRANSITION) {
                arc.getPlace().setTokenCount(arc.getPlace().getTokenCount()+1);
            }
            if (arc.getTargetType() != Arc.TARGET_PLACE) {
                arc.getPlace().setTokenCount(arc.getPlace().getTokenCount()-1);
            }
        }
    }

}