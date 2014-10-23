package view.figures;

import model.*;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class BaseFigure {
    //the element the figure represents
    BaseElement element;

    protected boolean selected = false;
    protected boolean highlighted = false;

    protected Color strokeColor = new Color(0, 0, 0);
    protected Color fillColor = new Color(255, 255, 255);
    protected Color selectedColor = new Color(153, 153, 255);
    protected Color highlightedColor = new Color(115, 230, 0);



    public BaseElement getElement() {
        return this.element;
    }

    public void draw(Graphics2D g) {}


    public Point2D getPosition() {

        return element.getPosition();
    }

}

