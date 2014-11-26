package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Place extends Connectable {
    public String label;

    private int tokenCount;

    public Place(String place_id) {
        register(place_id);
        this.position = new Point2D.Double(0.,0.);
        this.setTokenCount(0);
        this.setLabel("P LABEL");
        this.getFigure();
    }

    public Place(Point2D position) {
        register();
        this.position = position;
        this.setTokenCount(0);
        this.setLabel("P LABEL");
        //create/'cache??' a figure
        this.getFigure();
    }

    public String generateId() {
        return "p_"+PetriNetController.getPetriNet().getNextElementId();
    }

    public int getElementType() {
        return PetriNetController.ELEMENT_PLACE;
    }

    public void unregister() {
        PetriNetController.removePlace(this.getId());
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public PlaceFigure createFigure() {
        return new PlaceFigure(this);
    }


}