package controller;

import model.*;
import view.*;

import javax.swing.*;


public class PetriNetController {

    static PetriNet petriNet;


    public static void createPetriNet() {
        petriNet = new PetriNet();
    }

    public static PetriNet getPetriNet() {
        return petriNet;
    }

    public static void addPetriNetElement() {
        petriNet.addElement();
    }

}