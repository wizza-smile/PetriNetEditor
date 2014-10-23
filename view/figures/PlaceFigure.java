/* Copyright viewllem Catala. www.viewllemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package view.figures;

import model.*;
import view.Grid;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

/**
 *
 * @author viewllem
 */
public class PlaceFigure extends BaseFigure {

    Place place;

    protected Point2D position;
    protected boolean selected = false;
    protected boolean highlighted = false;
    protected Color fillColor = new Color(255, 255, 255);
    protected Color strokeColor = new Color(0, 0, 0);
    protected Color selectedColor = new Color(153, 153, 255);
    protected Color highlightedColor = new Color(115, 230, 0);

    private String placeId;
    private Ellipse2D ellipse;
    final public static int DIAMETER = Grid.cellSize;
    //protected TokenSetFigure tokenFigure;

    public PlaceFigure(Place place) {
        this.place = place;

        // this.placeId = placeId;
        // this.position = position;
        // this.label = new TextFigure(this);
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }

    public Point2D getPosition() {

        return position;
    }

    // public boolean contains(Point2D position) {
    //     return this.ellipse.contains(position);
    // }


    // public RectangularShape getBounds() {
    //     return new Ellipse2D.Double(place.position.getX() - DIAMETER / 2, place.position.getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    // }


    public void draw(Graphics2D g) {
        this.ellipse = generateEllipse();
        drawFill(g);
        drawStroke(g);
        // tokenFigure.draw(g);
    }


    public void drawFill(Graphics2D g) {
        if (selected) {
            g.setPaint(selectedColor);
        } else {
            g.setPaint(fillColor);
        }
        g.fill(ellipse);
    }


    public void drawStroke(Graphics2D g) {
        g.setStroke(new java.awt.BasicStroke(2f));
        if (highlighted) {
            g.setPaint(highlightedColor);
        } else {
            g.setPaint(strokeColor);
        }
        g.draw(ellipse);
    }


    public Ellipse2D generateEllipse() {
        return new Ellipse2D.Double(place.position.getX() - DIAMETER / 2, place.position.getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    }


    // public void setPosition(Point2D newPosition) {
    //     place.position = newPosition;
    //     label.setRelativePosition(newPosition);
    //     tokenFigure.setRelativePosition(newPosition);
    // }


    // public String getElementId() {
    //     return this.placeId;
    // }


    // public void setElementId(String id) {
    //     throw new UnsupportedOperationException("Not supported yet.");
    // }
}
