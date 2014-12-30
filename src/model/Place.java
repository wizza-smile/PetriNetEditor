package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Place extends Connectable {

    private int tokenCount;

    /**
     * Constructor to be called by PNML Parser
     * @param  place_id id to be used for the new Place
     */
    public Place(String place_id) {
        super(place_id);
    }

    /**
     * Constructor to be called when user clicks into canvas
     * @param  position where to place the new Connectable
     */
    public Place(Point2D position) {
        super(position);
    }

    public String generateId() {
        return "p_"+GlobalController.getUUID();
    }

    public int getElementType() {
        return PetriNetController.ELEMENT_PLACE;
    }

    public void unregister() {
        PetriNetController.removePlace(this.getId());
    }

    /**
     * returns the number of tokens of this Place
     * @return int
     */
    public int getTokenCount() {
        return tokenCount;
    }

    /**
     * sets the number of tokens of this Place
     * @param tokenCount int
     */
    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public PlaceFigure createFigure() {
        return new PlaceFigure(this);
    }


}