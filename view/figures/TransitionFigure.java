
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
    private LabelFigure labelFigure;


    final public static double DIMENSION_BASE = 45;
    public static double DIMENSION = DIMENSION_BASE;


    public void drawLabel(Graphics2D g) {
        this.labelFigure.draw(g);
    }

    public TransitionFigure(Transition transition) {
        this.element = (PetriNetElement)transition;

        this.labelFigure = new LabelFigure(this.getId(), this.getTransition().getPosition());

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

        drawFill(g);
        drawBorder(g);
        drawLabel(g);
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

        if (selected) {
            float dash1[] = {5f, 3f};
            g.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash1, 0f));
        } else {
            g.setStroke(new BasicStroke(2f));
        }

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
        labelFigure.updatePosition();
    }



    public void setTransitionRectangle(Rectangle2D r) {
        this.transitionRectangle = r;
    }

    public Rectangle2D getTransitionRectangle() {
        return generateTransitionRectangle();
    }

}
