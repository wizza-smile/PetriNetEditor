package view.figures;

import model.*;
import controller.*;

import java.awt.*;
import java.awt.geom.*;

///SELECTABLE FIGURES   PLACE / TRANSIITONS
public abstract class BaseFigure {
    //the element the figure represents
    protected String elementId;
    protected PetriNetElement element;

    protected Point2D offset;

    protected boolean selected = false;
    protected boolean highlighted = false;

    protected Color strokeColor = new Color(0, 0, 0);
    protected Color fillColor = new Color(255, 255, 255, 195);
    protected Color selectedColor = new Color(183, 55, 55, 40);
    protected Color highlightedColor = new Color(115, 230, 0);


    public abstract boolean intersects(Rectangle2D r);
    public abstract boolean contains(Point2D position);
    public abstract Point2D getLowerRightCorner();
    public abstract void updatePosition();


    public void draw(Graphics2D g) {
        // PetriNetController.checkLowerRightCorner(this.getLowerRightCorner());
    }

    //getter / setter
    public Point2D getPosition() { return getElement().getPosition(); }
    public void setPosition(Point2D position) { getElement().setPosition(position); }

    public Point2D getOffset() { return offset; }

    public void setOffsetToPoint(Point2D point) {
        Double offset_x = this.getPosition().getX() - point.getX();
        Double offset_y = this.getPosition().getY() - point.getY();

        this.offset = new Point2D.Double(offset_x, offset_y);
    }


    public PetriNetElement getElement() { return element; }
    public String getId() { return element.getId(); }



}
