package controller;

import model.*;
import view.*;

import javax.swing.*;


public class PetriNetController {

    // GlobalController global_controller;

    static PetriNet petriNet;

    // PetriNetController(GlobalController gctrl) {
    //     this.global_controller = gctrl;
    // }

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