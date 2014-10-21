package model;

import view.figures.*;

import java.awt.geom.Point2D;

public class Place extends AbstractPetriNetElement {
    public static String classname = "Place";
    private String placeId;
    protected Point2D position;

    public Place(String placeId, Point2D position) {
        this.placeId = placeId;
        this.position = position;
        this.label = "LABEL";
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }

    public Object clone() {
        Place clonedPlace = new Place(placeId, position);

        return clonedPlace;
    }

    public String getLabel() {
        return label;
    }


}