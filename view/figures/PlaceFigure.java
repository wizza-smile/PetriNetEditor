
package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;



import java.awt.*;

// import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.*;


public class PlaceFigure extends BaseFigure implements Selectable {


    private Ellipse2D ellipse;
    private Ellipse2D tokenPoint;



    final public static double DIAMETER = Grid.cellSize/1;
    //protected TokenSetFigure tokenFigure;

    public PlaceFigure(Place place) {
        this.element = (PetriNetElement)place;
        setEllipse(generateEllipse());
        if (1==1 || this.getPlace().getTokenCount() == 1) {
            this.tokenPoint = generateTokenPoint();
        }
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

    // public Point2D getLowerRightCorner() {
    //     Point2D position = getPosition();
    //     Point2D lrc = new Point2D.Double(position.getX() + DIAMETER/2, position.getY() + DIAMETER/2);

    //     return lrc;
    // }

    public void draw(Graphics2D g) {
        // regenrate Ellipse token
        setEllipse(generateEllipse());
        if (1==1 || this.getPlace().getTokenCount() == 1) {
            this.tokenPoint = generateTokenPoint();
        }

        drawLabel(g);

        drawFill(g);
        drawBorder(g);
        drawToken(g);

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
            getPlace().getPosition().getX() - DIAMETER / 12 + DIAMETER/16,
            getPlace().getPosition().getY() - DIAMETER / 12,
            DIAMETER/6,
            DIAMETER/6
        );
    }


    public Ellipse2D generateEllipse() {
        // //check if position is inside canvas/viewport
        // //if not move the net diagonally, so that this Element is fully inside
        // Double minX = getPlace().getPosition().getX() - DIAMETER / 2;
        // Double minY = getPlace().getPosition().getY() - DIAMETER / 2;


        // if (minX < 0 || minY < 0) {
        //     CanvasController.shrink = true;

        //     System.out.println("SHRINK TRUE");

        //     Double x_off = minX < 0 ? Math.abs(minX) : 0;
        //     Double y_off = minY < 0 ? Math.abs(minY) : 0;

        //     PetriNetController.moveAllElementDownDiagonally(x_off, y_off);
        // }

        return new Ellipse2D.Double(
            getPlace().getPosition().getX() - DIAMETER / 2,
            getPlace().getPosition().getY() - DIAMETER / 2,
            DIAMETER+DIAMETER/8,
            DIAMETER
        );
    }


    public void updatePosition() {
        // setEllipse(generateEllipse());
    }



    public void setEllipse(Ellipse2D e) {
        this.ellipse = e;
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
