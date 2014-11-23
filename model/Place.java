package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Place extends Connectable {
    public String label;

    private int tokenCount;

    public Place(Point2D position) {
        register();
        this.position = position;
        this.setLabel("LABEL");
        //create/'cache??' a figure
        this.getFigure();
    }

    public void register() {
        String place_id = "p_"+PetriNetController.getPetriNet().getNextElementId();
        this.setId(place_id);
        PetriNetController.addElement(this, PetriNetController.ELEMENT_PLACE);
    }

    public int getElementType() {
        return PetriNetController.ELEMENT_PLACE;
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
        PetriNetController.removePlace(this.getId());
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    public BaseFigure getFigure() {
        if (figureId == null) {
            PlaceFigure placeFigure = new PlaceFigure(this);
            figureId = placeFigure.getId();
            // CanvasController.addPlaceFigure(figureId, placeFigure);
            return (BaseFigure)placeFigure;
        } else {
            return CanvasController.getFigureById(this.getId());
        }
    }



    ///////////////
    //POPUP    ////
    public JPopupMenu getPopup(String place_id) {
        JPopupMenu placePopupMenu = new JPopupMenu();
        JMenuItem menuItemDelete = new JMenuItem(new DeletePetriNetElementAction(this));
        menuItemDelete.setText("Delete Place");
        placePopupMenu.add(menuItemDelete);
        placePopupMenu.addSeparator();

        JMenuItem menuItemToken = new JMenuItem();
        menuItemToken.setText("set TokenCount");
        menuItemToken.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String input = JOptionPane.showInputDialog("Enter tokenCount:");
                if (input != null) {
                    Place place = (Place)PetriNetController.getElementById(place_id);
                    place.setTokenCount(Integer.parseInt(input));
                }
                CanvasController.repaintCanvas();
            }
        });
        placePopupMenu.add(menuItemToken);

        return placePopupMenu;
    }



}