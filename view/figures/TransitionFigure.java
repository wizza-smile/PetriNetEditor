package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;


public class TransitionFigure extends Positionable {

    private Rectangle2D transitionRectangle;
    private String labelFigureId;

    final public static double DIMENSION_BASE = 45;
    public static double DIMENSION = DIMENSION_BASE;


    /**
     * the constructor will register the new TransitionFigure in the PetriNetElements-HashMap
     * @param  transition - the transition that will be figured
     */
    public TransitionFigure(Transition transition) {
        this.setId(transition.getId());
        this.element = (PetriNetElement)transition;
        register();

        LabelFigure labelFigure = new LabelFigure(this, this.getTransition().getPosition());
        this.labelFigureId = labelFigure.getId();
        CanvasController.addLabelFigure(labelFigureId, labelFigure);
    }

    public void register() {
        CanvasController.addFigure(this, CanvasController.FIGURE_TRANSITION);
    }

    public void delete() {
        getLabelFigure().delete();
        CanvasController.removeTransitionFigure(this.getId());
    }

    public void drawLabel(Graphics2D g) {
        this.getLabelFigure().draw(g);
    }

    public LabelFigure getLabelFigure() {
        return (LabelFigure)CanvasController.getFigureById(this.labelFigureId);
    }

    public Transition getTransition() {
        return (Transition)this.element;
    }

    public boolean contains(Point2D position) {
        return this.transitionRectangle.contains(position);
    }

    public boolean intersects(Rectangle2D rect) {
        return this.transitionRectangle.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public void draw(Graphics2D g) {
        setTransitionRectangle(generateTransitionRectangle());

        drawFill(g);
        drawBorder(g);
        drawLabel(g);
    }

    public void drawFill(Graphics2D g) {
        if (selected) {
            g.setPaint(GlobalController.opacity ? selectedColor : selectedColorAlpha);
        } else {
            g.setPaint(GlobalController.opacity ? fillColor : fillColorAlpha);
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

        g.setPaint(strokeColor);
        g.draw(transitionRectangle);

        if (!selected && this.isActivated()) {
            g.setPaint(Color.GREEN);
            transitionRectangle.setRect(
                transitionRectangle.getX()+1,
                transitionRectangle.getY()+1,
                transitionRectangle.getWidth()-2,
                transitionRectangle.getHeight()-2
            );
            g.draw(transitionRectangle);
        }
    }

    public boolean isActivated() {
        return this.getTransition().isActivated();
    }

    public Rectangle2D getBounds() {
        return this.transitionRectangle;
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
        getLabelFigure().updatePosition();
    }

    public void setTransitionRectangle(Rectangle2D r) {
        this.transitionRectangle = r;
    }

    public Rectangle2D getTransitionRectangle() {
        return generateTransitionRectangle();
    }


    ///////////////
    //POPUP    ////

    public void showPopup(Point2D position) {
        JPopupMenu contextMenu = this.getPopup();
        Double position_x = position.getX();
        Double position_y = position.getY();
        contextMenu.show(MainWindowController.main_window, position_x.intValue(), position_y.intValue());
    }

    public JPopupMenu getPopup() {
        JPopupMenu transitionPopupMenu = new JPopupMenu();

        final Transition transition = this.getTransition();
        //menu point "activate transition"
        if (this.isActivated()) {
            JMenuItem menuItemActivate = new JMenuItem();
            menuItemActivate.setText("activate Transition");
            menuItemActivate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    transition.activate();
                    CanvasController.repaintCanvas();
                }
            });
            transitionPopupMenu.add(menuItemActivate);
        }

        //menu point "add label"
        JMenuItem menuItemAddLabel = new JMenuItem(new AddLabelToConnectableAction(this.getElement()));
        if (!this.getElement().hasLabel()) {
            menuItemAddLabel.setText("Add Label");
            transitionPopupMenu.add(menuItemAddLabel);
        }

        transitionPopupMenu.addSeparator();

        //menu point "delete transition"
        JMenuItem menuItem = new JMenuItem(new DeletePetriNetElementAction(this.getElement()));//new DeletePetriNetObjectAction(myObject)
        menuItem.setText("Delete Transition");
        transitionPopupMenu.add(menuItem);

        return transitionPopupMenu;
    }


}
