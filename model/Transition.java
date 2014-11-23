package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Transition extends Connectable {
    protected final int elementType = PetriNetController.ELEMENT_TRANSITION;

    public String label;

    public Transition(Point2D position) {
        register();
        this.position = position;
        this.setLabel("T LABEL");
        this.getFigure();
    }


    public void register() {
        String transition_id = "t_"+PetriNetController.getPetriNet().getNextElementId();
        this.setId(transition_id);
        PetriNetController.addElement(this, PetriNetController.ELEMENT_TRANSITION);
    }


    public int getElementType() {
        return PetriNetController.ELEMENT_TRANSITION;
    }


    public void addArcId(String arc_id) {
        arc_ids.add(arc_id);
    }

    public void removeArcId(String arc_id) {
        arc_ids.remove(arc_id);
    }

    public void delete() {
        SelectionController.clearSelection();
        Arc[] all_arcs = new Arc[arc_ids.size()];
        int index = 0;
        for (String arc_id : arc_ids ) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            all_arcs[index++] = arc;
        }
        for (Arc arc  : all_arcs) {
            arc.delete();
        }
        CanvasController.removeFigure(this.getId());
        PetriNetController.removeTransition(this.getId());
    }



    public BaseFigure getFigure() {
        if (figureId == null) {
            TransitionFigure transitionFigure = new TransitionFigure(this);
            figureId = transitionFigure.getId();
            // CanvasController.addTransitionFigure(figureId, transitionFigure);
            return (BaseFigure)transitionFigure;
        } else {
            return CanvasController.getFigureById(this.getId());
        }
    }

    public boolean isActivated() {
        boolean isActivated = true;
        for (String arc_id : this.getArcIds()) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            if (arc.getTargetType() != Arc.TARGET_PLACE) {
                if (arc.getPlace() != null && arc.getPlace().getTokenCount() == 0){
                    isActivated = false;
                }
            }
        }

        return isActivated;
    }


    ///////////////
    //POPUP    ////
    public JPopupMenu getPopup(String transition_id) {
        JPopupMenu transitionPopupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(new DeletePetriNetElementAction(this));//new DeletePetriNetObjectAction(myObject)
        menuItem.setText("Delete Transition");
        transitionPopupMenu.add(menuItem);
        transitionPopupMenu.addSeparator();

        if (this.isActivated()) {
            JMenuItem menuItemActivate = new JMenuItem();
            menuItemActivate.setText("activate Transition");
            menuItemActivate.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    System.out.println( "activateTransition" );
                    CanvasController.repaintCanvas();
                }
            });
            transitionPopupMenu.add(menuItemActivate);
        }

        return transitionPopupMenu;
    }


}