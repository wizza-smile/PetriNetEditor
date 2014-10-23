package model;

import view.figures.*;

import java.awt.geom.Point2D;

public class Place implements PetriNetElement {
    // public static String classname = "Place";
    public String label = "LABEL_TEXT";
    public Point2D position;
    private String placeId;

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

    public Object clone() {
        Place clonedPlace = new Place(placeId, position);

        return clonedPlace;
    }

    public String getLabel() {
        return label;
    }

    public BaseFigure getFigure(){
        PlaceFigure placeFigure = new PlaceFigure(this);

        return placeFigure;
    };


}