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
        super(place_id);
    }

    public Place(Point2D position) {
        super(position);
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