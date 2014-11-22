package model;

import view.figures.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public abstract class PetriNetElement {

    protected Point2D position;
    protected String id;
    protected String figureId;
    // protected final int elementType = -1;

    ///////////////////
    //abstract methods
    ////////////////////

    // public abstract PetriNetElement cloneElement();
    //return associated figure Object
    public abstract BaseFigure getFigure();
    public abstract void delete();
    public abstract int getElementType();


    /////////////////////
    //implemented methods
    //////////////////////

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }


    protected class DeletePetriNetObjectAction extends AbstractAction {
        protected PetriNetElement element;

        DeletePetriNetObjectAction() {}

        DeletePetriNetObjectAction(PetriNetElement elem) {
            element = elem;
        }

        public void actionPerformed(ActionEvent e) {
            element.delete();
        }

    }

}

