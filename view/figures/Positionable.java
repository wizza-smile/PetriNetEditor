package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.Font;
import java.awt.font.*;
import java.awt.geom.*;


public abstract class Positionable extends BaseFigure {

    protected boolean selected = false;
    protected boolean highlighted = false;
    protected Color selectedColor = new Color(183, 55, 55, 40);
    protected Color highlightedColor = new Color(115, 230, 0);
    //offset to mouse pointer (for dragging)
    protected Point2D offset;

    public abstract boolean intersects(Rectangle2D r);
    public abstract Point2D getPosition();
    public abstract void setPosition(Point2D position);
    public abstract void updatePosition();

    public Point2D getOffset() { return offset; }

    public void setOffset(Point2D referencePoint) {
        Double offset_x = this.getPosition().getX() - referencePoint.getX();
        Double offset_y = this.getPosition().getY() - referencePoint.getY();

        this.offset = new Point2D.Double(offset_x, offset_y);
    }




    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(0, 0, 0, 0);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

}
