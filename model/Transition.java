package model;

import view.figures.*;

import java.awt.geom.Point2D;

public class Transition extends PetriNetElement {

    public String label;

    public Transition(String elementId, Point2D position) {
        this.setId(elementId);
        this.position = position;
        this.label = "T LABEL";
        //cache a figure
        this.getFigure();
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BaseFigure getFigure() {
        if (figure == null) {
            figure = new TransitionFigure(this);
        }

        return figure;
    };


}