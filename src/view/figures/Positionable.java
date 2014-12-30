package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.Font;
import java.awt.font.*;
import java.awt.geom.*;

/** superclass of all positionable Figures (Label/Transition/Place) */
public abstract class Positionable extends BaseFigure {

    /** whether this figure should visually marked as "selected" */
    protected boolean selected = false;

    /** color used to paint the border of the figure */
    protected Color strokeColor = new Color(0, 0, 0);

    /** color used to paint the fill of the figure */
    protected Color fillColor = new Color(255, 255, 255);

    /** the fill color when in transparent mode */
    protected Color fillColorAlpha = new Color(255, 255, 255, 195);

    /** color used to paint the fill of a selected figure */
    protected Color selectedColor = new Color(201, 157, 172);

    /** the fill color of a selected figure when in transparent mode */
    protected Color selectedColorAlpha = new Color(183, 55, 55, 40);

    /** offset of the figure to the mouse pointer (needed for dragging) */
    protected Point2D offset;


    /**
     * checks whether this figure intersects with the selection rectangle (or any given rectangle)
     * @param  r the rectangle to check
     * @return   boolean
     */
    public abstract boolean intersects(Rectangle2D r);

    /** callback function when the position of the figure changes */
    public abstract void updatePosition();

    /**
     * returns a rectangle that tightly encloses the figure
     * @return the enclosing rectangle
     */
    public abstract Rectangle2D getBounds();

    /**
     * draws this figure. called by the canvas paintComponent method. Will call drawFill and drawBorder methods.
     * @param g the graphics object of the canvas
     */
    public abstract void draw(Graphics2D g);

    /**
     * draws the background of this figure
     * @param g the graphics object of the canvas
     */
    public abstract void drawFill(Graphics2D g);

    /**
     * draws the border of this figure
     * @param g the graphics object of the canvas
     */
    public abstract void drawBorder(Graphics2D g);

    /**
     * returns the LabelFigure that is related to this figure.
     * This will be the figure itself, if it is a LabelFigure.
     * @return the LabelFigure
     */
    public abstract LabelFigure getLabelFigure();

    /**
     * returns the text presented in the LabelFigure
     * @return the label text
     */
    public String getLabel() {
        return getElement().getLabel();
    }

    /**
     * returns the position of the Positionable figure
     * @return the position
     */
    public Point2D getPosition() {
        return getElement().getPosition();
    }

    /**
     * set the position of the element that this figure represents
     * @param position the position to set
     */
    public void setPosition(Point2D position) {
        getElement().setPosition(position);
    }

    /**
     * mark this figure as selected or remove such a marking
     * @param selected boolean
     */
    public void markSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * whether this figure is visually marked as selected
     * @return boolean
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * returns the stored offset of the figure to the mouse pointer (needed for dragging)
     * @return the x/y offset encoded as Point2D
     */
    public Point2D getOffset() {
        return offset;
    }

    /**
     * set the label text of the related LabelFigure
     * @param label the label text to set
     */
    public void setLabel(String label) {
        getElement().setLabel(label);
    }

    /**
     * store the x/y offset of the position's position to a given point
     * @param referencePoint the given point
     */
    public void setOffset(Point2D referencePoint) {
        Double offset_x = this.getPosition().getX() - referencePoint.getX();
        Double offset_y = this.getPosition().getY() - referencePoint.getY();

        this.offset = new Point2D.Double(offset_x, offset_y);
    }

    public Connectable getElement() {
        return (Connectable)super.getElement();
    }

}
