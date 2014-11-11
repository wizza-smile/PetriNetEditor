
package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;



import java.awt.*;

// import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.*;


public class TransitionFigure extends BaseFigure implements Selectable {


    private Rectangle2D transitionRectangle;

    final public static double DIMENSION = Grid.cellSize/0.9;



    public TransitionFigure(Transition transition) {
        this.element = (PetriNetElement)transition;
    }

    public Transition getTransition() {
        return (Transition)this.element;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean contains(Point2D position) {
        return this.transitionRectangle.contains(position);
    }

    public boolean intersects(Rectangle2D rect) {
        return this.transitionRectangle.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }


    public void draw(Graphics2D g) {
        // regenrate transitionRectangle
        setTransitionRectangle(generateTransitionRectangle());

        // drawLabel(g);

        drawFill(g);
        drawBorder(g);


    }


    public void drawFill(Graphics2D g) {
        if (selected) {
            g.setPaint(selectedColor);
        } else {
            g.setPaint(fillColor);
        }
        g.fill(transitionRectangle);
    }


    public void drawBorder(Graphics2D g) {

        float dash1[] = {5f, 3f};
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash1, 0f));

        if (highlighted) {
            g.setPaint(highlightedColor);
        } else {
            g.setPaint(strokeColor);
        }
        g.draw(transitionRectangle);
    }






    public Rectangle2D generateTransitionRectangle() {
        return new Rectangle2D.Double(
            getTransition().getPosition().getX() - DIMENSION / 4,
            getTransition().getPosition().getY() - DIMENSION / 2,
            DIMENSION/2,
            DIMENSION
        );
    }


    public void updatePosition() {
        // setEllipse(generateEllipse());
    }



    public void setTransitionRectangle(Rectangle2D r) {
        this.transitionRectangle = r;
    }

}
