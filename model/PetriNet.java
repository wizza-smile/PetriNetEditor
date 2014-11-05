package model;

import controller.*;
import view.figures.*;

import java.util.*;

import java.awt.geom.Point2D;

public class PetriNet {
    //counts id's for PetriNetElements
    private static int id_counter = 0;

    private HashMap<String, PetriNetElement> elements = new HashMap<String, PetriNetElement>();



    public int getNextElementId() {
        return ++this.id_counter;
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


    public void addElement(String elementId, PetriNetElement element) {
        elements.put(elementId, element);
    }

    public PetriNetElement getElementById(String elementId) {
        return elements.get(elementId);
    }






}