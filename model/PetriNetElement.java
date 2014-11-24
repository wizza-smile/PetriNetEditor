package model;

import view.figures.*;
import controller.*;

import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.*;

public abstract class PetriNetElement {

    protected Point2D position;
    protected String id;
    protected String figureId;
    // protected final int elementType = -1;

    ///////////////////
    //abstract methods
    ////////////////////

    // public abstract PetriNetElement cloneElement();
    //return associated figure Object
    public abstract BaseFigure getFigure();
    public abstract void delete();
    public abstract int getElementType();


    /////////////////////
    //implemented methods
    //////////////////////

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

