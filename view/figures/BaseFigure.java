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
    protected String id;

    //the element the figure represents
    protected String elementId;
    protected PetriNetElement element;

    public abstract int getFigureType();
    public abstract void register();
    public abstract void delete();

    /**
     * checks wether an relevant part of the figures representaion has been clicked on
     * @param  position   position of mouse click
     * @return            boolean
     */
    public abstract boolean contains(Point2D position);
    public abstract void draw(Graphics2D g);
    public abstract void showPopup(Point2D position);


    public PetriNetElement getElement() {
        if (this.element == null) {
            this.element = PetriNetController.getElementById(this.elementId);
        }
        return this.element;
    }

    public String getId() {return this.id;}
    public void setId(String id) {this.id = id;}


    ///////////////
    //POPUP    ////

    public void showMultiSelectionPopup(MouseEvent e) {
        JPopupMenu contextMenu = getMultiSelectionPopup();
        contextMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    public JPopupMenu getMultiSelectionPopup() {
        JPopupMenu multiSelectionMenu = new JPopupMenu();
        JMenuItem menuItemDelete = new JMenuItem();
        menuItemDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SelectionController.deleteSelection();
                CanvasController.cleanUpCanvas();
             }
        });
        menuItemDelete.setText("Delete Selected Elements");
        multiSelectionMenu.add(menuItemDelete);
        multiSelectionMenu.addSeparator();

        return multiSelectionMenu;
    }

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
