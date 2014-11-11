package model;

import view.figures.*;
import controller.*;

import java.awt.geom.Point2D;

public class Arc {
    protected String elementId;
    protected ArcFigure arcfigure;

    protected String source_id, target_id;

    public Arc(String elementId, String source_id) {
        this.elementId = elementId;

        this.source_id = source_id;
        //cache a figure
        this.getArcFigure();
    }


    public ArcFigure getArcFigure() {
        if (arcfigure == null) {
            arcfigure = new ArcFigure(this);
        }

        return arcfigure;
    };

    public void selectTarget(String target_id) {
        this.target_id = target_id;
    }

    public PetriNetElement getSource() {
        return PetriNetController.getElementById(this.source_id);
    }

    public PetriNetElement getTarget() {
        return PetriNetController.getElementById(this.target_id);
    }

}