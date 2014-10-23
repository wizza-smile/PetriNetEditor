package model;

import view.figures.*;


public interface PetriNetElement {

    public PetriNetElement cloneElement();
    public BaseFigure getFigure();

}

