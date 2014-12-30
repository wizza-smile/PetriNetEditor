package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


/**
 * the controller of element selection related functionality
 */
public class SelectionController {

    /** holds the current dimensions and position of the SelectionFigure */
    protected static Rectangle2D selectionRectangle;

    /** a List of ids of all elements currently selected */
    protected static ArrayList<String> selectedElements_ids = new ArrayList<String>();


    public static Rectangle2D getSelectionRectangle() {
        return selectionRectangle;
    }

    /**
     * a handler method for left mouse clicks in ActionMode "SELECT"
     * @param e the mouseEvent of the left mouse click
     */
    public static void mousePressedInModeSelect(MouseEvent e) {
        Point2D mousePressPoint = CanvasController.mousePressPoint;
        BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);

        if (figureUnderMousePointer != null) {
            //check if a label is under mouse pointer
            //if yes: only this label will be selected (and dragged)
            if(figureUnderMousePointer instanceof LabelFigure && !CanvasController.selectionKeyActive) {
                SelectionController.clearSelection();
                SelectionController.addFigureToSelection((LabelFigure)figureUnderMousePointer);
                GlobalController.setActionMode(GlobalController.ACTION_DRAG_SELECTION);
            } else {
                if (figureUnderMousePointer instanceof Positionable) {
                    //now its clear its a transition or place!
                    //check if its already in selected elements!
                    if (selectedElements_ids.contains(figureUnderMousePointer.getId())) {
                        if (CanvasController.selectionKeyActive) {
                            SelectionController.removeFigureFromSelection((Positionable)figureUnderMousePointer);
                        } else {
                            //user wants to drag selected elements!
                            GlobalController.setActionMode(GlobalController.ACTION_DRAG_SELECTION);
                        }
                    } else {
                        //if the user is pressing the selectionKey (strg/ctrl) the element will be added to current selection
                        //otherwise the current selection gets cleared
                        if (!CanvasController.selectionKeyActive) {
                            SelectionController.clearSelection();
                        }
                        //select the element start dragging
                        SelectionController.addFigureToSelection((Positionable)figureUnderMousePointer);
                        GlobalController.setActionMode(GlobalController.ACTION_DRAG_SELECTION);
                    }
                    onUpdateSelectedElements();
                }
            }
            SelectionController.setOffsetToSelectedElements(mousePressPoint);
        } else {
            if (!CanvasController.selectionKeyActive) {
                SelectionController.clearSelection();
            }
        }
    }

    /**
     * will select all petriNetElements that are intersecting with the selection rectangle.
     */
    public static void updateSelection() {
        double currentX = CanvasController.currentMousePoint.getX() > 0 ? CanvasController.currentMousePoint.getX() : 0;
        double currentY = CanvasController.currentMousePoint.getY() > 0 ? CanvasController.currentMousePoint.getY() : 0;

        //compute the current selection rectangle
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
                if (!CanvasController.selectionKeyActive) {
                    SelectionController.removeFigureFromSelection(figure);
                }
            }
        }

        onUpdateSelectedElements();
    }

    /**
     * remove and add again all selected transition and place ids to their id Collection,
     * so that they will always be drawn ontop of unselected elements
     */
    public static void onUpdateSelectedElements() {
        CanvasController.getPlacesAndTransitionFiguresIds().removeAll(selectedElements_ids);
        CanvasController.getPlacesAndTransitionFiguresIds().addAll(selectedElements_ids);
    }

    /**
     * set the offset of all selected elements to the position of the mouse pointer, during the moment of the dragging starts.
     * @param point the position of the mouse pointer
     */
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

    /**
     * get the topmost figure, that is under pointer (TransitionFigure, PlaceFigure, LabelFigure, ArcFigure)
     * @param  pointer position of the mouse pointer
     * @return         the figure if one is found
     */
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

    /**
     * deletes all selected elements.
     */
    public static void deleteSelection() {
        for (String id : selectedElements_ids ) {
            ((Positionable)CanvasController.getFigureById(id)).getElement().delete();
        }
    }

}