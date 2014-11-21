package model;

import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.geom.Point2D;

public class Place extends Connectable {

    public String label;

    private int tokenCount;
    private ArrayList<String> arc_ids = new ArrayList<String>();


    public Place(String elementId, Point2D position) {
        this.setId(elementId);
        this.position = position;
        this.setLabel("LABEL");
        //cache a figure
        this.getFigure();
        // this.tokenFigure = new TokenSetFigure(this);
        // this.ellipse = generateEllipse();
    }

    public void addArcId(String arc_id) {
        arc_ids.add(arc_id);
    }

    public void removeArcId(String arc_id) {
        arc_ids.remove(arc_id);
    }


    public void delete() {
        System.out.println( "1" );
        SelectionController.clearSelection();
        System.out.println( "2"+arc_ids.size() );
        Arc[] all_arcs = new Arc[arc_ids.size()];
        int index = 0;
        for (String arc_id : arc_ids ) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            all_arcs[index++] = arc;
        }
        for (Arc arc  : all_arcs) {
            arc.delete();
        }
        System.out.println( "3" );
        CanvasController.removeFigure(this.getId());
        System.out.println( "4" );
        PetriNetController.removePlace(this.getId());
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public BaseFigure getFigure() {
        if (figureId == null) {
            PlaceFigure placeFigure = new PlaceFigure(this);
            figureId = this.getId();
            CanvasController.addPlaceFigure(figureId, placeFigure);
            return (BaseFigure)placeFigure;
        } else {
            return CanvasController.getFigureById(this.getId());
        }
    }


}