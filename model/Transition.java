package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.geom.Point2D;

public class Transition extends PetriNetElement {

    public String label;
    private ArrayList<String> arc_ids = new ArrayList<String>();

    public Transition(String elementId, Point2D position) {
        this.setId(elementId);
        this.position = position;
        this.label = "T LABEL";
        //cache a figure
        this.getFigure();
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }

    public void addArcId(String arc_id) {
        arc_ids.add(arc_id);
    }

    public void removeArcId(String arc_id) {
        arc_ids.remove(arc_id);
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public BaseFigure getFigure() {
        if (figureId == null) {
            TransitionFigure transitionFigure = new TransitionFigure(this);
            figureId = this.getId();
            CanvasController.addTransitionFigure(figureId, transitionFigure);
            return (BaseFigure)transitionFigure;
        } else {
            return CanvasController.getFigureById(this.getId());
        }
    }

}