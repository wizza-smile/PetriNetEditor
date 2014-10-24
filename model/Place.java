package model;

import view.figures.*;

import java.awt.geom.Point2D;

public class Place extends PetriNetElement {

    public String label;

    private String placeId;

    private int tokenCount;

    Place(String placeId, Point2D position) {
        this.placeId = placeId;
        this.position = position;
        this.label = "LABEL";
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }

    Place(Place source) {
        this.placeId  = source.placeId;
        this.position = source.position;
        this.label    = source.label;
    }

    //clone source place
    public PetriNetElement cloneElement() {
        Place clonedPlace = new Place(this);

        return clonedPlace;
    }


    public void setLabel(String label) {
        if (this.label == "") {
            this.label = label;
            this.paintLabelFigure();
        }
    }


    public int getTokenCount() {
        return tokenCount;
    }

    public String getLabel() {
        return label;
    }

    public void paintLabelFigure() {
        return;
    }


    public BaseFigure getFigure(){
        PlaceFigure placeFigure = new PlaceFigure(this);

        return placeFigure;
    };







}