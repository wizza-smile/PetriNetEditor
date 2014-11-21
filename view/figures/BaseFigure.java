package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.Font;
import java.awt.font.*;
import java.awt.geom.*;

///SELECTABLE FIGURES ARC / PLACE / TRANSIITONS
public abstract class BaseFigure {
    protected String id;

    //the element the figure represents
    protected String elementId;
    protected PetriNetElement element;



    protected Color strokeColor = new Color(0, 0, 0);
    protected Color fillColor = new Color(255, 255, 255, 195);//195

    // protected boolean selected = false;
    // protected boolean highlighted = false;
    // protected Color selectedColor = new Color(183, 55, 55, 40);
    // protected Color highlightedColor = new Color(115, 230, 0);


    public abstract void delete();
    public abstract boolean contains(Point2D position);
    public abstract void draw(Graphics2D g);

    //do NOT call this function in Constructor!
    public PetriNetElement getElement() {
        if (this.element == null) {
            this.element = PetriNetController.getElementById(this.elementId);
        }
        return this.element;
    }



    public String getId() {return this.id;}
    public void setId(String id) {this.id = id;}


    // public abstract boolean intersects(Rectangle2D r);
    // public abstract Point2D getLowerRightCorner();
    //






    // public Rectangle2D getBounds() {
    //     return new Rectangle2D.Double(0, 0, 0, 0);
    // }



    // //getter / setter
    // public Point2D getPosition() { return getElement().getPosition(); }
    // public void setPosition(Point2D position) { getElement().setPosition(position); }









}
