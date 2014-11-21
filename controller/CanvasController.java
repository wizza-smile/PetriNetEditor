package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;
import java.lang.Math;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


import javax.swing.*;
import javax.swing.SwingUtilities;



public class CanvasController {

    public final static Double PETRINET_PADDING_BASE = 20.;
    public static Double PETRINET_PADDING = PETRINET_PADDING_BASE;

    static private view.Canvas canvas;

    static Point2D mousePressPoint;
    static Point2D currentMousePoint;

    public static String arc_no_target_id;





    public static ArrayList<String> getAllFigureIds() {
        ArrayList<String> positionablesIds = new ArrayList<String>();
        positionablesIds.addAll(canvas.label_figure_ids);
        positionablesIds.addAll(canvas.place_figure_ids);
        positionablesIds.addAll(canvas.transition_figure_ids);
        positionablesIds.addAll(canvas.arc_figure_ids);

        return positionablesIds;
    }

    public static ArrayList<String> getPlacesAndTransitionFiguresIds() {
        ArrayList<String> placesAndTransitionFiguresIds = new ArrayList<String>();
        placesAndTransitionFiguresIds.addAll(canvas.place_figure_ids);
        placesAndTransitionFiguresIds.addAll(canvas.transition_figure_ids);

        return placesAndTransitionFiguresIds;
    }

    public static ArrayList<String> getPositionablesIds() {
        ArrayList<String> positionablesIds = new ArrayList<String>();
        positionablesIds.addAll(canvas.label_figure_ids);
        positionablesIds.addAll(canvas.place_figure_ids);
        positionablesIds.addAll(canvas.transition_figure_ids);

        return positionablesIds;
    }

    public static BaseFigure getFigureById(String figureId) {
        return canvas.getFigureById(figureId);
    }

    public static void removeFigure(String figureId) {
        BaseFigure baseFigure = canvas.getFigureById(figureId);
        baseFigure.delete();
    }

    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }
    public static view.Canvas getCanvas() {
        return canvas;
    }

    /* Resize the Canvas and move Elements and Viewport to create illusion of endless canvas */
    public static void cleanUpCanvas() {
        if (PetriNetController.getPetriNetElementCount() == 0) return;

        Rectangle2D figures_rectangle = CanvasController.getFiguresBounds();
        Rectangle2D viewport_rectangle = MainWindowController.getViewportRectangle();

        figures_rectangle.add(viewport_rectangle);

        Double cleanedCanvasWidth = figures_rectangle.getWidth();
        Double cleanedCanvasHeight = figures_rectangle.getHeight();
        //set Canvas Size to combinedViewportAndPetrinetRectangle Size
        canvas.setPreferredSize(new Dimension(cleanedCanvasWidth.intValue(), cleanedCanvasHeight.intValue()));

        /* move all elements by the offset of
        a) point(0,0) and
        b) combinedViewportAndPetrinetRectangle upper_left point
        so that all elements will be within new canvas size */
        Double move_x = (-1) * figures_rectangle.getX();
        Double move_y = (-1) * figures_rectangle.getY();

        if (move_x != 0 || move_y != 0) {
            System.out.println(" MOVE after canvas resize | left or up");
            PetriNetController.moveAllElements(move_x, move_y);
        }

        //REVALIDATE new canvas size
        canvas.revalidate();

        //if the viewport stayed inside canvas through the resizing (ie. it's position has not been adjusted automatically by revalidating):
        //adjust viewport position by the movement made
        //so that the illusion of a static viewport is created
        boolean scroll_y = cleanedCanvasHeight > MainWindowController.getViewport().getViewPosition().getY() + MainWindowController.getViewport().getHeight();
        boolean scroll_x = cleanedCanvasWidth > MainWindowController.getViewport().getViewPosition().getX() + MainWindowController.getViewport().getWidth();
        Double scrollToWidth = MainWindowController.getViewport().getExtentSize().getWidth();
        Double scrollToHeight = MainWindowController.getViewport().getExtentSize().getHeight();

        //if the elements moved to the left or up, it is always necessary to adjust the viewport
        scroll_x = move_x.intValue() < 0 ? true : scroll_x;
        scroll_y = move_y.intValue() < 0 ? true : scroll_y;

        Rectangle scroll_rect = new Rectangle(scroll_x ? move_x.intValue() : 0, scroll_y ? move_y.intValue() : 0, scrollToWidth.intValue(), scrollToHeight.intValue());

        MainWindowController.getViewport().scrollRectToVisible(scroll_rect);
    }


    public static void handleMousePressedModeArc() {
        //check if a transition/place is under mousepointer
        BaseFigure figure = SelectionController.getFigureUnderMousePointer(mousePressPoint);
        //if not: do nothing
        //if yes: create new arc with source = transition
        if (figure != null) {
            if (figure.getElement() instanceof Transition) {
                PetriNetController.addArc(figure.getId(), PetriNetController.ELEMENT_TRANSITION);
            }
            if (figure.getElement() instanceof Place) {
                PetriNetController.addArc(figure.getId(), PetriNetController.ELEMENT_PLACE);
            }
            GlobalController.setMode(GlobalController.MODE_ARC_SELECT_TARGET);
        }
    }


    //adjust the reference point from which the grid will be painted
    //should always be 0 or negative
    public static void addToGridReferencePoint(Point2D p) {
        Grid.addToReferencePoint(p);
    }

    public static void addPlaceFigure(String figureId, PlaceFigure figure) {
        canvas.addFigure(figureId, figure);
        canvas.place_figure_ids.add(figureId);
    }

    public static void addLabelFigure(String figureId, LabelFigure figure) {
        canvas.addFigure(figureId, figure);
        canvas.label_figure_ids.add(figureId);
    }

    public static void addTransitionFigure(String figureId, TransitionFigure figure) {
        canvas.addFigure(figureId, figure);
        canvas.transition_figure_ids.add(figureId);
    }

    public static void removeTransitionFigure(String figureId) {
        canvas.removeFigure(figureId);
        canvas.transition_figure_ids.remove(figureId);
    }

    public static void removePlaceFigure(String figureId) {
        canvas.removeFigure(figureId);
        canvas.place_figure_ids.remove(figureId);
    }

    public static void removeLabelFigure(String figureId) {
        canvas.removeFigure(figureId);
        canvas.label_figure_ids.remove(figureId);
    }

    public static Dimension getCanvasSize() {
        return canvas.getPreferredSize();
    }


    public static void mouseClicked(MouseEvent e) {
        //on dbl-click: clear selection and select figure under pointer, if any
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            SelectionController.clearSelection();
            BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);
            if (figureUnderMousePointer instanceof Positionable){
                SelectionController.addFigureToSelection((Positionable)figureUnderMousePointer);
                GlobalController.setMode(GlobalController.MODE_SELECT);
            }
        }
    }


    public static void mousePressed(MouseEvent e) {
        mousePressPoint = new Point2D.Double(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch (GlobalController.mode) {
                case GlobalController.MODE_SELECT:
                    //check if a label is under mouse pointer
                    //if yes: only this label will be selected (and dragged)
                    BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);

                    if (figureUnderMousePointer != null && figureUnderMousePointer instanceof LabelFigure) {
                        SelectionController.clearSelection();
                        SelectionController.addFigureToSelection((Positionable)figureUnderMousePointer);
                        GlobalController.setMode(GlobalController.MODE_DRAG_SELECTION);
                    }

                    //check if a Positionable is under mouse pointer
                    if (figureUnderMousePointer instanceof Positionable) {
                        //now its clear its a transition or place!
                        //check if its already selected!
                        if (((Positionable)figureUnderMousePointer).isSelected()) {
                            //user wants to drag selected elements!
                            GlobalController.setMode(GlobalController.MODE_DRAG_SELECTION);
                        } else {
                            //select the element start dragging
                            SelectionController.clearSelection();
                            SelectionController.addFigureToSelection((Positionable)figureUnderMousePointer);
                            GlobalController.setMode(GlobalController.MODE_DRAG_SELECTION);
                        }
                    }
                    SelectionController.setOffsetToSelectedElements(mousePressPoint);
                    break;
                case GlobalController.MODE_PLACE:
                    PetriNetController.addPetriNetElement(mousePressPoint, PetriNetController.ELEMENT_PLACE);
                    break;
                case GlobalController.MODE_TRANSITION:
                    PetriNetController.addPetriNetElement(mousePressPoint, PetriNetController.ELEMENT_TRANSITION);
                    break;
                case GlobalController.MODE_ARC:
                    handleMousePressedModeArc();
                    break;
                case GlobalController.MODE_ARC_SELECT_TARGET:
                    //check if a Place is under mousepointer
                    BaseFigure figure = SelectionController.getFigureUnderMousePointer(mousePressPoint);
                    //if not: do nothing
                    //if yes: add arc to place
                    if (figure != null && !(figure instanceof LabelFigure)) {
                        Arc arc = (Arc)PetriNetController.getElementById(arc_no_target_id);
                        if (arc.selectTarget(figure.getId())) {
                            GlobalController.setMode(GlobalController.MODE_ARC);
                            // figure.getElement().addArcId(arc_no_target_id);
                        }
                    }
                    break;
                default:
                    System.out.println("LEFT MOUSE PRESSSSSS");
                    break;
            }
        }
        //RECHTER MOUSE PRESS
        if (SwingUtilities.isRightMouseButton(e)) {
            switch (GlobalController.mode) {
                case GlobalController.MODE_SELECT:

                    //check if a label is under mouse
                    BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);
                    if (figureUnderMousePointer != null) {

                        if (figureUnderMousePointer instanceof LabelFigure) {
                            MainWindowController.showLabelInput(e, figureUnderMousePointer.getId());
                        }

                        if (figureUnderMousePointer instanceof TransitionFigure) {
                            MainWindowController.showTransitionPopupMenu(e, figureUnderMousePointer.getId());
                        }


                        if (figureUnderMousePointer instanceof PlaceFigure) {
                            MainWindowController.showPlacePopupMenu(e, figureUnderMousePointer.getId());
                        }



                    }

                    //if click on arrowHead: SHOW the menu to delete Arc(target)
                    java.util.List<String> arc_ids = PetriNetController.getPetriNet().getArcIds();
                    for (String arc_id : arc_ids ) {
                        ArcFigure arcFigure = (ArcFigure)PetriNetController.getElementById(arc_id).getFigure();
                        Integer target_type = arcFigure.arrowHeadsContain(new Point2D.Double(e.getX(), e.getY()));
                        if (target_type != null) {
                            //open context menu to
                            //remove this target type /and evtl arrow??
                            MainWindowController.showArcPopupMenu(e, arc_id, target_type);
                        }
                    }

                    break;
                default:
                    System.out.println("RIGHT MOUSE");
                    break;
            }
        }

        CanvasController.repaintCanvas();
    }


    public static void mouseDragged(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch (GlobalController.mode) {
                case  GlobalController.MODE_SELECT:
                    SelectionController.updateSelection();
                    break;
                case GlobalController.MODE_DRAG_SELECTION:
                    //move all selected Elements to mouse position/ consider offset
                    ArrayList<String> selectedElements_ids = SelectionController.getSelectedElementsIds();
                    boolean clearSelection = true;
                    for (String id : selectedElements_ids ) {
                        Positionable figure = (Positionable)CanvasController.getFigureById(id);
                        Point2D offset = figure.getOffset();
                        figure.setPosition( new Point2D.Double(currentMousePoint.getX()+offset.getX(), currentMousePoint.getY()+offset.getY()) );
                    }
                    break;
                default:
                    MainWindowController.setStatusBarText("MOUSE DRAGGED");
                    break;
            }
        }

        CanvasController.repaintCanvas();
    }


    public static void mouseReleased(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());
        switch (GlobalController.mode) {
            case GlobalController.MODE_SELECT:
                SelectionController.removeSelectionRectangle();
                break;
            case GlobalController.MODE_DRAG_SELECTION:
                GlobalController.setMode(GlobalController.MODE_SELECT);
                break;
            case GlobalController.MODE_PLACE:
                //
                break;
            default:
                break;
        }
        cleanUpCanvas();
        CanvasController.repaintCanvas();
    }


    public static void mouseMoved(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());
        switch (GlobalController.mode) {
            case GlobalController.MODE_ARC_SELECT_TARGET:

                break;
            default:
                break;
        }

        CanvasController.repaintCanvas();
    }


    public static Point2D getCurrentMousePoint() {
        return currentMousePoint;
    }

    public static void repaintCanvas() {
        canvas.repaint();
    }

    //get the rectangle that encloses all labels
    public static Rectangle2D getLabelsBounds() {
        Rectangle2D labelsBounds = new Rectangle2D.Double(0, 0, 0, 0);
        boolean initialized = false;

        for (String label_figure_id : CanvasController.getCanvas().label_figure_ids) {
            LabelFigure labelFigure = (LabelFigure)CanvasController.getFigureById(label_figure_id);
            if (initialized) {
                labelsBounds.add(labelFigure.getBounds());
            } else {
                labelsBounds = labelFigure.getBounds();
                initialized = true;
            }
        }

        return labelsBounds;
    }

    //get the rectangle that encloses all transitions and places
    public static Rectangle2D getPetriNetFiguresBounds() {
        Rectangle2D petriNetBounds = new Rectangle2D.Double(0, 0, 0, 0);
        boolean initialized = false;

        for (String figure_id : CanvasController.getPlacesAndTransitionFiguresIds()) {
            Positionable figure = (Positionable)CanvasController.getFigureById(figure_id);
            if (initialized) {
                petriNetBounds.add(figure.getBounds());
            } else {
                petriNetBounds = figure.getBounds();
                initialized = true;
            }
        }

        return petriNetBounds;
    }

    public static Rectangle2D getFiguresBounds() {
        Rectangle2D petriNetBounds = getPetriNetFiguresBounds();
        petriNetBounds.add(getLabelsBounds());

        //add padding
        Rectangle2D figuresBounds = new Rectangle2D.Double(
            petriNetBounds.getX() - PETRINET_PADDING,
            petriNetBounds.getY() - PETRINET_PADDING,
            petriNetBounds.getWidth() + 2*PETRINET_PADDING,
            petriNetBounds.getHeight() + 2*PETRINET_PADDING
        );


        return figuresBounds;
    }



}