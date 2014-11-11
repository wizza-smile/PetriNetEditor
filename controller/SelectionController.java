package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;


import java.awt.*;
import java.awt.geom.*;


public class SelectionController {

    public static Rectangle2D selectionRectangle;
    private static ArrayList<String> selectedElements_ids = new ArrayList<String>();


    //will select all petriNetElements that are intersecting with the selection rectangle.
    public static void updateSelection() {
        double currentX = CanvasController.currentMousePoint.getX() > 0 ? CanvasController.currentMousePoint.getX() : 0;
        double currentY = CanvasController.currentMousePoint.getY() > 0 ? CanvasController.currentMousePoint.getY() : 0;

        double width = Math.abs(CanvasController.mousePressPoint.getX() - currentX);
        double height = Math.abs(CanvasController.mousePressPoint.getY() - currentY);
        double x = Math.min(CanvasController.mousePressPoint.getX(), currentX);
        double y = Math.min(CanvasController.mousePressPoint.getY(), currentY);

        selectionRectangle = new Rectangle2D.Double(x, y, width, height);

        // System.out.printf("%f, %f, %f, %f \n", x, y, width, height );

        Iterator it = PetriNetController.getPetriNet().getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement element = (PetriNetElement)it.next();
            BaseFigure figure = element.getFigure();
            boolean inSelectionRectangle = figure.intersects(selectionRectangle);
            if (figure instanceof Selectable) {
                if (inSelectionRectangle) {
                    SelectionController.addFigureToSelection(figure);
                } else {
                    SelectionController.removeFigureFromSelection(figure);
                }
            }
        }
    }

    public static void setOffsetToSelectedElements(Point2D point) {
        for (String id : selectedElements_ids ) {
            BaseFigure figure = PetriNetController.getElementById(id).getFigure();
            figure.setOffsetToPoint(point);
        }
    }




    public static void drawSelectionFigure(Graphics2D g) {
        if (selectionRectangle != null) {
            SelectionFigure.draw(g);
        }
    }


    //select figure, if one is under pointer
    public static BaseFigure selectFigureUnderMousePointer(Point2D pointer) {
        Iterator it = PetriNetController.getPetriNet().getElements().values().iterator();
        while (it.hasNext()) {
            BaseFigure figure = ((PetriNetElement)it.next()).getFigure();
            if (figure.contains(pointer) && figure instanceof Selectable) {

                return figure;
            }
        }
        return null;
    }



    public static void addFigureToSelection(BaseFigure figure) {
        if (figure instanceof Selectable && !selectedElements_ids.contains(figure.getId())) {
            selectedElements_ids.add(figure.getId());
            ((Selectable)figure).setSelected(true);
        }
    }



    public static void removeSelectionRectangle() {
        selectionRectangle = null;
    }


    public static void removeFigureFromSelection(BaseFigure figure) {
        selectedElements_ids.remove(figure.getId());
        ((Selectable)figure).setSelected(false);
    }


    public static ArrayList<String> getSelectedElementsIds() {
        return selectedElements_ids;
    }

    public static void clearSelection() {
        for (String id : selectedElements_ids ) {
            if (PetriNetController.getElementById(id).getFigure() instanceof Selectable)
                ((Selectable)PetriNetController.getElementById(id).getFigure()).setSelected(false);
        }
        selectedElements_ids = new ArrayList<String>();

    }

}