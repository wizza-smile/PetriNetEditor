package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.geom.Point2D;

public class Place extends PetriNetElement {

    public String label;

    private int tokenCount;
    private ArrayList<String> arc_ids = new ArrayList<String>();


    public Place(String elementId, Point2D position) {
        this.setId(elementId);
        this.position = position;
        this.label = "LABEL";
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

    public int getTokenCount() {
        return tokenCount;
    }

    public BaseFigure getFigure() {
        if (figureId == null) {
            PlaceFigure placeFigure = new PlaceFigure(this);
            figureId = this.getId();
            CanvasController.addPlaceFigure(figureId, placeFigure);
            return (BaseFigure)placeFigure;
        } else {
            return CanvasController.getFigureById(this.getId());
        }
    }


}