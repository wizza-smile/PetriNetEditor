package view.figures;

import model.*;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class BaseFigure {
    //the element the figure represents
    PetriNetElement element;

    protected Point2D offset;

    protected boolean selected = false;
    protected boolean highlighted = false;

    protected Color strokeColor = new Color(0, 0, 0);
    protected Color fillColor = new Color(255, 255, 255);
    protected Color selectedColor = new Color(153, 153, 255);
    protected Color highlightedColor = new Color(115, 230, 0);


    public abstract void draw(Graphics2D g);


    //getter / setter
    public Point2D getPosition() { return getElement().getPosition(); }
    public void setPosition(Point2D position) { getElement().setPosition(position); }

    public Point2D getOffset() { return offset; }
    public void setOffset(Point2D offset) { this.offset = offset; }


    public PetriNetElement getElement() { return element; }



}

