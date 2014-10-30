package model;

import view.figures.*;

import java.awt.geom.Point2D;

public class Place extends PetriNetElement {

    public String label;

    private int tokenCount;

    Place(String placeId, Point2D position) {
        this.setId(placeId);
        this.position = position;
        this.label = "LABEL";
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }

    Place(Place source) {
        this.setId(source.getId());
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


    public BaseFigure getFigure() {
        if (figure == null) {
            figure = new PlaceFigure(this);
        }

        return figure;
    };







}