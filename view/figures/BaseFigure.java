package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

///SELECTABLE FIGURES ARC / PLACE / TRANSIITONS
public abstract class BaseFigure {
    protected String id;

    //the element the figure represents
    protected String elementId;
    protected PetriNetElement element;

    public abstract void delete();
    public abstract boolean contains(Point2D position);
    public abstract void draw(Graphics2D g);
    public abstract void showPopup(MouseEvent e);

    //do NOT call this function in Constructor!
    public PetriNetElement getElement() {
        if (this.element == null) {
            this.element = PetriNetController.getElementById(this.elementId);
        }
        return this.element;
    }



    public String getId() {return this.id;}
    public void setId(String id) {this.id = id;}






}
