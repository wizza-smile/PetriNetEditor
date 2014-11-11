package model;

import view.figures.*;

import java.awt.geom.Point2D;

public class Place extends PetriNetElement {

    public String label;

    private int tokenCount;

    public Place(String elementId, Point2D position) {
        this.setId(elementId);
        this.position = position;
        this.label = "LABEL";
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

    public int getTokenCount() {
        return tokenCount;
    }

    public BaseFigure getFigure() {
        if (figure == null) {
            figure = new PlaceFigure(this);
        }

        return figure;
    };


}