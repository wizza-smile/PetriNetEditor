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
            System.out.println( "INIT w transition " +this.target_type);
        }
        if (type == PetriNetController.ELEMENT_PLACE) {
            this.place_id = this.source_id;
            this.target_type = TARGET_TRANSITION;
        }
        System.out.println("CREATE TARGET TYPE "+ this.target_type );
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

    public void selectTarget(String target_id) {
        //Check if arc already exists with the same place and transition
        //if yes merge!
        this.target_id = target_id;
        if (TARGET_TRANSITION == this.target_type) {
            this.transition_id = this.target_id;
        }
        if (TARGET_PLACE == this.target_type) {
            this.place_id = this.target_id;
        }
        System.out.println("TARGET TYPE "+ this.target_type );
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