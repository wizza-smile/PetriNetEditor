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

///SELECTABLE FIGURES ARC / PLACE / TRANSIITONS
public abstract class BaseFigure {
    protected String id;

    //the element the figure represents
    protected String elementId;
    protected PetriNetElement element;

    public abstract void delete();
    public abstract boolean contains(Point2D position);
    public abstract void draw(Graphics2D g);
    public abstract void showPopup(MouseEvent e);

    //do NOT call this function in Constructor!
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
                CanvasController.repaintCanvas();
            }
        });
        menuItemDelete.setText("Delete Selected Elements");
        multiSelectionMenu.add(menuItemDelete);
        multiSelectionMenu.addSeparator();

        return multiSelectionMenu;
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
