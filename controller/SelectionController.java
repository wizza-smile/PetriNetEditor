package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


public class SelectionController {

    public static Rectangle2D selectionRectangle;
    private static ArrayList<String> selectedElements_ids = new ArrayList<String>();

    public static void mouseClickedInModeSelect(MouseEvent e) {
        Point2D mousePressPoint = CanvasController.mousePressPoint;
        BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);

        if (figureUnderMousePointer != null) {
            //check if a label is under mouse pointer
            //if yes: only this label will be selected (and dragged)
            if(figureUnderMousePointer instanceof LabelFigure) {
                SelectionController.clearSelection();
                SelectionController.addFigureToSelection((LabelFigure)figureUnderMousePointer);
                GlobalController.setActionMode(GlobalController.ACTION_DRAG_SELECTION);
            } else {
                if (figureUnderMousePointer instanceof Positionable) {
                    //now its clear its a transition or place!
                    //check if its already in selected elements!
                    if (selectedElements_ids.contains(figureUnderMousePointer.getId())) {
                        //user wants to drag selected elements!
                        GlobalController.setActionMode(GlobalController.ACTION_DRAG_SELECTION);
                    } else {
                        //select the element start dragging
                        SelectionController.clearSelection();
                        SelectionController.addFigureToSelection((Positionable)figureUnderMousePointer);
                        GlobalController.setActionMode(GlobalController.ACTION_DRAG_SELECTION);
                    }
                    onUpdateSelectedElements();
                }
            }
            SelectionController.setOffsetToSelectedElements(mousePressPoint);
        } else {
            SelectionController.clearSelection();
        }
    }

    //will select all petriNetElements that are intersecting with the selection rectangle.
    public static void updateSelection() {
        double currentX = CanvasController.currentMousePoint.getX() > 0 ? CanvasController.currentMousePoint.getX() : 0;
        double currentY = CanvasController.currentMousePoint.getY() > 0 ? CanvasController.currentMousePoint.getY() : 0;

        double width = Math.abs(CanvasController.mousePressPoint.getX() - currentX);
        double height = Math.abs(CanvasController.mousePressPoint.getY() - currentY);
        double x = Math.min(CanvasController.mousePressPoint.getX(), currentX);
        double y = Math.min(CanvasController.mousePressPoint.getY(), currentY);

        selectionRectangle = new Rectangle2D.Double(x, y, width, height);

        for (String id : CanvasController.getPlacesAndTransitionFiguresIds() ) {
            Positionable figure = (Positionable)CanvasController.getFigureById(id);
            boolean inSelectionRectangle = figure.intersects(selectionRectangle);
            if (inSelectionRectangle) {
                SelectionController.addFigureToSelection(figure);
            } else {
                SelectionController.removeFigureFromSelection(figure);
            }
        }

        onUpdateSelectedElements();
    }

    /**
     * remove and add again all selected transition and place ids to their id Collection,
     * so that they willalways be drawn ontop of unselected elements
     */
    public static void onUpdateSelectedElements() {
        CanvasController.getPlacesAndTransitionFiguresIds().removeAll(selectedElements_ids);
        CanvasController.getPlacesAndTransitionFiguresIds().addAll(selectedElements_ids);
    }

    public static void setOffsetToSelectedElements(Point2D point) {
        for (String id : selectedElements_ids ) {
            Positionable figure = (Positionable)CanvasController.getFigureById(id);
            figure.setOffset(point);
        }
    }

    public static void drawSelectionFigure(Graphics2D g) {
        if (selectionRectangle != null) {
            SelectionFigure.draw(g);
        }
    }

    //get figure, if one is under pointer (TransitionFigure, PlaceFigure, LabelFigure, ArcFigure)
    public static BaseFigure getFigureUnderMousePointer(Point2D pointer) {
        for (String id : CanvasController.getAllFigureIds() ) {
            BaseFigure figure = CanvasController.getFigureById(id);

            if (figure.contains(pointer)) {
                return figure;
            }
        }

        return null;
    }

    public static void addFigureToSelection(Positionable figure) {
        if (!selectedElements_ids.contains(figure.getId())) {
            selectedElements_ids.add(figure.getId());
            figure.markSelected(true);
        }
    }

    public static void removeSelectionRectangle() {
        selectionRectangle = null;
    }

    public static void removeFigureFromSelection(Positionable figure) {
        selectedElements_ids.remove(figure.getId());
        figure.markSelected(false);
    }

    public static ArrayList<String> getSelectedElementsIds() {
        return selectedElements_ids;
    }

    public static void clearSelection() {
        for (String id : selectedElements_ids ) {
            ((Positionable)CanvasController.getFigureById(id)).markSelected(false);
        }
        selectedElements_ids = new ArrayList<String>();
    }

    public static void deleteSelection() {
        for (String id : selectedElements_ids ) {
            ((Positionable)CanvasController.getFigureById(id)).getElement().delete();
        }
    }

}