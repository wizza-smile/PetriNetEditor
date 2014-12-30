package model;

import controller.*;
import view.figures.*;

import java.util.*;
import java.util.Collections.*;

import java.awt.*;
import java.awt.geom.*;


/** a class modeling a petrinet  */
public class PetriNet {

    /** stores the total number of places, transitions and arcs currently in the petrinet */
    protected static int element_counter = 0;

    /** the HashMap that stores all place, transition and arc objects */
    protected HashMap<String, PetriNetElement> elements = new HashMap<String, PetriNetElement>();

    /** a list of all place ids */
    protected ArrayList<String> place_ids = new ArrayList<String>();

    /** a list of all transition ids */
    protected ArrayList<String> transition_ids = new ArrayList<String>();

    /** a list of all arc ids */
    protected ArrayList<String> arc_ids = new ArrayList<String>();


    ////////////////////////////////////////////////
    // The following methods are self-explanatory //
    ////////////////////////////////////////////////

    public int getElementCount() {
        return element_counter;
    }

    public HashMap<String, PetriNetElement> getElements() {
        return elements;
    }

    public java.util.List<String> getArcIds() {
        return arc_ids;
    }

    public java.util.List<String> getTransitionIds() {
        return transition_ids;
    }

    public java.util.List<String> getPlaceIds() {
        return place_ids;
    }

    public void addArc(String arcId, PetriNetElement arc) {
        addElement(arcId, arc);
        getArcIds().add(arcId);
    }

    public void removeArcId(String arcId) {
        getArcIds().remove(arcId);
    }

    public void removeTransitionId(String transition_id) {
        getTransitionIds().remove(transition_id);
    }

    public void removePlaceId(String place_id) {
        getPlaceIds().remove(place_id);
    }

    public void addElement(String elementId, PetriNetElement element) {
        getElements().put(elementId, element);
        element_counter++;
    }

    public void removeElement(String elementId) {
        getElements().remove(elementId);
        element_counter--;
    }

    public PetriNetElement getElementById(String elementId) {
        return elements.get(elementId);
    }


}