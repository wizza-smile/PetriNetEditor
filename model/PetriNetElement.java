package model;

import view.figures.*;
import controller.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public abstract class PetriNetElement {

    protected Point2D position;
    protected String id;
    protected BaseFigure figure;

    ///////////////////
    //abstract methods
    ////////////////////

    public abstract void delete();
    public abstract BaseFigure createFigure();
    public abstract int getElementType();
    public abstract String generateId();
    public abstract void unregister();


    /////////////////////
    //implemented methods
    //////////////////////

    public void register(String element_id) {
        this.setId(element_id);
        PetriNetController.addElement(this, getElementType());
    }

    public void register() {
        String element_id = generateId();
        this.setId(element_id);
        PetriNetController.addElement(this, getElementType());
    }

    /**
     * when called for the first time:
     * creates a related figure and keeps refernce
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

