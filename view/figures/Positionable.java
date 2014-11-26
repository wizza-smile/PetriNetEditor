package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.Font;
import java.awt.font.*;
import java.awt.geom.*;

/**
 * is superclass of all positionable Figures (Label/Transition/Place)
 */
public abstract class Positionable extends BaseFigure {

    protected Color strokeColor = new Color(0, 0, 0);
    protected Color fillColor = new Color(255, 255, 255);
    protected Color fillColorAlpha = new Color(255, 255, 255, 195);
    protected boolean selected = false;
    protected Color selectedColorAlpha = new Color(183, 55, 55, 40);
    protected Color selectedColor = new Color(201, 157, 172);

    //offset to mouse pointer (for dragging)
    protected Point2D offset;

    public abstract boolean intersects(Rectangle2D r);
    public abstract void updatePosition();
    public abstract Rectangle2D getBounds();


    public Point2D getPosition() {
        return getElement().getPosition();
    }

    public void setPosition(Point2D position) {
        getElement().setPosition(position);
    }

    public void markSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Point2D getOffset() {
        return offset;
    }

    public void setOffset(Point2D referencePoint) {
        Double offset_x = this.getPosition().getX() - referencePoint.getX();
        Double offset_y = this.getPosition().getY() - referencePoint.getY();

        this.offset = new Point2D.Double(offset_x, offset_y);
    }

    public Connectable getElement() {
        return (Connectable)super.getElement();
    }

}
