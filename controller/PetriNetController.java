package controller;

import model.*;
import view.*;

import java.awt.geom.Point2D;
import javax.swing.*;


public class PetriNetController {

    public final static int ELEMENT_PLACE = 0;
    public final static int ELEMENT_TRANSITION = 1;
    public final static int ELEMENT_ARC = 2;

    static PetriNet petriNet;


    public static void createPetriNet() {
        petriNet = new PetriNet();
    }

    public static PetriNet getPetriNet() {
        return petriNet;
    }

    public static void addPetriNetElement(Point2D position, int type) {
        petriNet.addElement(position, type);
    }

    public static void addSelectedElementId(String elementId) {
        petriNet.addSelectedElementId(elementId);
    }



}