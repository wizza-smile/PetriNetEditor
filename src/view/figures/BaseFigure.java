package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

import javax.swing.*;

/**
 * is superclass of all PetriNetFigures (Arc/Label/Transition/Place)
 */
public abstract class BaseFigure {
    /**  id of this figure. */
    protected String id;

    /** the element that this figure represents. */
    protected PetriNetElement element;

    /**
     * returns the type of this figure as defined in CanvasController.
     * @return the figure type.
     */
    public abstract int getFigureType();

    /** set the id for this BaseFigure and add it to the figures HashMap and the corrsponding id Collection of the Canvas. */
    public abstract void register();

    /** delete this Figure from the Canvas figures-HashMap and its id from the corresponding id List. */
    public abstract void delete();

    /**
     * checks wether a relevant part of the figures representaion has been clicked on
     * @param  position   position of mouse click
     * @return            boolean
     */
    public abstract boolean contains(Point2D position);

    /**
     * draw this figure to Canvas. Called in paintComponent in Canvas.
     * @param g the graphics object of Canvas.
     */
    public abstract void draw(Graphics2D g);

    /**
     * show the Popup for this figure.
     * @param position the position where the popUp will appear.
     */
    public abstract void showPopup(Point2D position);

    /**
     * return the element that this figure represents.
     * @return the PetriNetElement.
     */
    public PetriNetElement getElement() {
        return this.element;
    }

    public String getId() {return this.id;}
    public void setId(String id) {this.id = id;}


    ///////////////
    //POPUP    ////

    /**
     * show this PopUp, if multiple Elements are selected.
     * @param e the MouseEvent.
     */
    public void showMultiSelectionPopup(MouseEvent e) {
        JPopupMenu contextMenu = getMultiSelectionPopup();
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * the PopUp to show, if multiple Elements are selected.
     * @return the JPopupMenu.
     */
    public JPopupMenu getMultiSelectionPopup() {
        JPopupMenu multiSelectionMenu = new JPopupMenu();
        JMenuItem menuItemDelete = new JMenuItem();
        menuItemDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SelectionController.deleteSelection();
                CanvasController.cleanUpCanvas();
                CanvasController.repaintCanvas();
             }
        });
        menuItemDelete.setText("Delete Selected Elements");
        multiSelectionMenu.add(menuItemDelete);
        multiSelectionMenu.addSeparator();

        return multiSelectionMenu;
    }

    /** PopUp-Action for adding a Label to a Connectable (Place/Transition). */
    protected class AddLabelToConnectableAction extends AbstractAction {
        protected Connectable element;

        AddLabelToConnectableAction() {}

        AddLabelToConnectableAction(Connectable elem) {
            element = elem;
        }

        public void actionPerformed(ActionEvent e) {
            LabelFigure labelFigure = ((Positionable)element.getFigure()).getLabelFigure();
            labelFigure.showPopup(labelFigure.getPosition());
            CanvasController.repaintCanvas();
        }
    }

    /** PopUp-Action for deleting a PetriNetElement from the PetriNet. */
    protected class DeletePetriNetElementAction extends AbstractAction {
        protected PetriNetElement element;

        DeletePetriNetElementAction() {}

        DeletePetriNetElementAction(PetriNetElement elem) {
            element = elem;
        }

        public void actionPerformed(ActionEvent e) {
            element.delete();
            CanvasController.repaintCanvas();
        }
    }

}
