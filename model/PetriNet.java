package model;

import controller.*;
import view.figures.*;

import java.util.*;

import java.awt.*;
import java.awt.geom.*;

public class PetriNet {
    //counts id's for PetriNetElements
    private static int id_counter = 0;
    private static int element_counter = 0;

    private HashMap<String, PetriNetElement> elements = new HashMap<String, PetriNetElement>();
    public ArrayList<String> place_transition_ids = new ArrayList<String>();
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


    public ArrayList<String> getArcIds() {
        return arc_ids;
    }





    public void addArc(String arcId, PetriNetElement arc) {
        addElement(arcId, arc);
        arc_ids.add(arcId);
    }



    public void addElement(String elementId, PetriNetElement element) {
        elements.put(elementId, element);
        element_counter++;
    }

    public PetriNetElement getElementById(String elementId) {
        return elements.get(elementId);
    }






}