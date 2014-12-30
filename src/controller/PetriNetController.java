package controller;


import model.*;
import view.*;
import view.figures.*;

import java.util.*;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

/** the controller class in charge of the current PetriNet object */
public class PetriNetController {

    /** identifier for place elements */
    public final static int ELEMENT_PLACE = 0;
    /** identifier for transition elements */
    public final static int ELEMENT_TRANSITION = 1;
    /** identifier for arc elements */
    public final static int ELEMENT_ARC = 2;

    /** the instance of the petriNet currently being edited */
    static private PetriNet petriNet;


    /**
     * create a new instance of PetriNet and store it in the petriNet property
     */
    public static void createPetriNet() {
        petriNet = new PetriNet();
    }

    public static PetriNet getPetriNet() {
        return petriNet;
    }

    /**
     * add an PetriNet-element to the elements HashMap and the corresponding ids List.
     * @param element the element to add
     * @param type    the type of the element (defined as constants)
     */
    public static void addElement(PetriNetElement element, int type) {
        petriNet.addElement(element.getId(), element);

        switch (type) {
            case PetriNetController.ELEMENT_PLACE:
                petriNet.getPlaceIds().add(element.getId());
                break;
            case PetriNetController.ELEMENT_TRANSITION:
                petriNet.getTransitionIds().add(element.getId());
                break;
            case PetriNetController.ELEMENT_ARC:
                petriNet.getArcIds().add(element.getId());
                break;
            default:
                return;
        }
    }

    /**
     * retrieve an element by id from the elements HashMap in the petriNet object
     * @param  elementId the id of the element
     * @return           the element
     */
    public static PetriNetElement getElementById(String elementId) {
        return petriNet.getElementById(elementId);
    }


    /**
     * returns a List of ids of all elements currently in the petriNet
     * @return the List
     */
    public static ArrayList<String> getAllElementIds() {
        ArrayList<String> allElementIds = new ArrayList<String>();
        allElementIds.addAll(petriNet.getPlaceIds());
        allElementIds.addAll(petriNet.getTransitionIds());
        allElementIds.addAll(petriNet.getArcIds());

        return allElementIds;
    }

    /**
     * returns a List of the ids of all elements of type Connectable (ie Places and Transitions)
     * @return the List
     */
    public static ArrayList<String> getConnectablesIds() {
        ArrayList<String> allElementIds = new ArrayList<String>();
        allElementIds.addAll(petriNet.getPlaceIds());
        allElementIds.addAll(petriNet.getTransitionIds());

        return allElementIds;
    }

    /**
     * returns the List of Place ids from petriNet object
     * @return the List
     */
    public static java.util.List<String> getPlaceIds() {
        return petriNet.getPlaceIds();
    }

    /**
     * returns the List of Transition ids from petriNet object
     * @return the List
     */
    public static java.util.List<String> getTransitionIds() {
        return petriNet.getTransitionIds();
    }

    /**
     * returns the List of Arc ids from petriNet object
     * @return the List
     */
    public static java.util.List<String> getArcIds() {
        return petriNet.getArcIds();
    }

    /**
     * add a new Connectable to the PetriNet. The new element will register itself in its Constructor.
     * @param position the position in the canvas at which to add the element
     * @param type     the of the element to add
     */
    public static void newConnectableElementAtPosition(Point2D position, int type) {
        switch (type) {
            case PetriNetController.ELEMENT_PLACE:
                new Place(position);
                break;
            case PetriNetController.ELEMENT_TRANSITION:
                new Transition(position);
                break;
            default:
                return;
        }
    }

    /**
     * remove an Arc from elements HashMap and the arc ids List
     * @param arc_id the id of the Arc to remove
     */
    public static void removeArc(String arc_id) {
        petriNet.removeElement(arc_id);
        petriNet.removeArcId(arc_id);
    }

    /**
     * remove an Transition from elements HashMap and the Transition ids List
     * @param transition_id the id of the Transition to remove
     */
    public static void removeTransition(String transition_id) {
        petriNet.removeElement(transition_id);
        petriNet.removeTransitionId(transition_id);
    }

    /**
     * remove an Place from elements HashMap and the Place ids List
     * @param place_id the id of the Place to remove
     */
    public static void removePlace(String place_id) {
        petriNet.removeElement(place_id);
        petriNet.removePlaceId(place_id);
    }

    /**
     * alter the position of all Connectables by a fixed x/y value
     * @param x the x value
     * @param y the y value
     */
    public static void moveAllElements(Double x, Double y) {
        for (String elem_id : PetriNetController.getConnectablesIds()) {
            Connectable elem = (Connectable)PetriNetController.getElementById(elem_id);
            Point2D position = elem.getPosition();
            Point2D new_position = new Point2D.Double(position.getX()+x, position.getY()+y);
            elem.setPosition(new_position);
        }
    }

    /**
     * returns the number of PetriNetElements (Arc, Places, Transitions) currently in the PetriNet.
     * @return [description]
     */
    public static Integer getPetriNetElementCount() {
        return petriNet.getElementCount();
    }


}