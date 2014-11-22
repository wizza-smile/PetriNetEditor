package model;

import controller.*;
import view.figures.*;

import java.util.*;
import java.util.Collections.*;

import java.awt.*;
import java.awt.geom.*;

public class PetriNet {
    //counts id's for PetriNetElements
    private static int id_counter = 0;
    private static int element_counter = 0;

    private HashMap<String, PetriNetElement> elements = new HashMap<String, PetriNetElement>();
    public ArrayList<String> place_ids = new ArrayList<String>();
    public ArrayList<String> transition_ids = new ArrayList<String>();
    public ArrayList<String> arc_ids = new ArrayList<String>();



    public int getNextElementId() {
        return ++this.id_counter;
    }

    public int getElementCount() {
        return element_counter;
    }

    //returnes a deep copy of contained elements
    public HashMap<String, PetriNetElement> getElements() {
        return elements;
        // HashMap<String, PetriNetElement> outputElements = new HashMap<String, PetriNetElement>();

        // Iterator it = elements.entrySet().iterator();
        // while (it.hasNext()) {
        //     Map.Entry pairs = (Map.Entry)it.next();

        //     outputElements.put((String)pairs.getKey(), ((PetriNetElement)pairs.getValue()).cloneElement());
        // }

        // return outputElements;
    }


    public java.util.List<String> getArcIds() {
        return arc_ids;
    }
    public java.util.List<String> getTransitionIds() {
        // return Collections.synchronizedList(transition_ids);
        return transition_ids;
    }
    public java.util.List<String> getPlaceIds() {
        // return Collections.synchronizedList(transition_ids);
        return place_ids;
    }



    public void addArc(String arcId, PetriNetElement arc) {
        addElement(arcId, arc);
        getArcIds().add(arcId);
    }



    public void removeArcId(String arcId) {
        System.out.println( "REMOVE ARC ID" );
        getArcIds().remove(arcId);
        System.out.println( getArcIds() );
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
        System.out.println( elementId + "  Delete id" );
        getElements().remove(elementId);
        element_counter--;
    }

    public PetriNetElement getElementById(String elementId) {
        return elements.get(elementId);
    }






}