
package view.figures;

import model.*;
import view.Grid;


import java.awt.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;


public class PlaceFigure extends BaseFigure implements Selectable {

    private Ellipse2D ellipse;
    private Ellipse2D tokenPoint;

    final public static double DIAMETER = Grid.cellSize/1;
    //protected TokenSetFigure tokenFigure;

    public PlaceFigure(Place place) {
        this.element = place;
        this.ellipse = generateEllipse();

        // this.placeId = placeId;
        // this.position = position;
        // this.label = new TextFigure(this);
        // this.tokenFigure = new TokenSetFigure(this);
    }

    public Place getPlace() {
        return (Place)this.element;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // public boolean isSelected() {
    //     return selected;
    // }

    public boolean contains(Point2D position) {
        return this.ellipse.contains(position);
    }

    public boolean intersects(Rectangle2D rect) {
        return this.ellipse.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }


    // public RectangularShape getBounds() {
    //     return new Ellipse2D.Double(place.getPosition().getX() - DIAMETER / 2, place.getPosition().getY() - DIAMETER / 2, DIAMETER, DIAMETER);
    // }


    public void draw(Graphics2D g) {
        drawFill(g);
        drawBorder(g);

        if (1==1 || this.getPlace().getTokenCount() == 1) {
            this.tokenPoint = generateTokenPoint();
            drawToken(g);
        }

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


    public void drawBorder(Graphics2D g) {

        float dash1[] = {5f, 3f};
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash1, 0f));

        if (highlighted) {
            g.setPaint(highlightedColor);
        } else {
            g.setPaint(strokeColor);
        }
        g.draw(ellipse);
    }





    public void drawToken(Graphics2D g) {
        g.setPaint(new Color(0, 0, 0));
        g.fill(tokenPoint);

        // System.out.println("PAINT TOKEN!");
    }




    public Ellipse2D generateTokenPoint() {
        return new Ellipse2D.Double(
            getPlace().getPosition().getX() - DIAMETER / 12,
            getPlace().getPosition().getY() - DIAMETER / 12,
            DIAMETER/6,
            DIAMETER/6
        );
    }


    public Ellipse2D generateEllipse() {
        return new Ellipse2D.Double(
            getPlace().getPosition().getX() - DIAMETER / 2,
            getPlace().getPosition().getY() - DIAMETER / 2,
            DIAMETER,
            DIAMETER
        );
    }


    public void updatePosition() {
        this.ellipse = generateEllipse();
    }


    // public void setPosition(Point2D newPosition) {
    //     place.getPosition() = newPosition;
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
