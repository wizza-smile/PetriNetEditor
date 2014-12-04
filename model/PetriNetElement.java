package model;

import view.figures.*;
import controller.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;


/**
 * the base class for all petriNetElements (Arc/Transition/Place)
 */
public abstract class PetriNetElement {

    protected String id;
    protected BaseFigure figure;

    ///////////////////
    //abstract methods
    ////////////////////

    /**
     * delete this PetriNetElement from the PetriNet element-HashMap and its id from the corresponding id List
     */
    public abstract void delete();

    /**
     * create a figure of this PetriNetElements corresponding figure-type
     * @return the figure object
     */
    public abstract BaseFigure createFigure();

    /**
     * returns the int value of the corresponding element types constant, as specified in PetriNetController.
     * @return the int value (ELEMENT_PLACE|ELEMENT_TRANSITION|ELEMENT_ARC)
     */
    public abstract int getElementType();

    /**
     * generates a unique id for a new instance by using UUIDs.
     * @return   the unique id.
     */
    public abstract String generateId();

    /**
     * remove this element from the PetriNet element HashMap and the corresponding ids Collection.
     */
    public abstract void unregister();


    /////////////////////
    //implemented methods
    //////////////////////

    /**
     * set the id for this PetriNetElement and add it to the elements HashMap and the corrsponding id Collection of the current PetriNet.
     * @param element_id the id to be set for this element.
     */
    public void register(String element_id) {
        this.setId(element_id);
        PetriNetController.addElement(this, getElementType());
    }

    /**
     * generate and set the id for this PetriNetElement and add it to the elements HashMap elements and the corrsponding id Collection of the current PetriNet.
     */
    public void register() {
        String element_id = generateId();
        this.setId(element_id);
        PetriNetController.addElement(this, getElementType());
    }

    /**
     * when called for the first time:
     * creates a related figure and keeps a reference.
     * @return the related figure
     */
    public BaseFigure getFigure() {
        if (figure == null) {
            figure = createFigure();
        }

        return figure;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

