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
    public final static int FIGURE_PLACE = 0;
    public final static int FIGURE_TRANSITION = 1;
    public final static int FIGURE_ARC = 2;
    public final static int FIGURE_LABEL = 3;

    public final static Double PETRINET_PADDING_BASE = 20.;
    public static Double PETRINET_PADDING = PETRINET_PADDING_BASE;

    static private view.Canvas canvas;

    public static boolean cleanUpCanvasAfterRepaint = false;
    public static boolean selectionKeyActive = false;

    static Point2D mousePressPoint;
    static Point2D currentMousePoint = new Point2D.Double(.0,.0);

    public static String arc_no_target_id;


    public static ArrayList<String> getAllFigureIds() {
        ArrayList<String> allFigureIds = new ArrayList<String>();
        allFigureIds.addAll(canvas.label_figure_ids);
        allFigureIds.addAll(canvas.place_and_transition_figure_ids);
        allFigureIds.addAll(canvas.arc_figure_ids);

        return allFigureIds;
    }

    public static java.util.List<String> getPlacesAndTransitionFiguresIds() {
        return canvas.place_and_transition_figure_ids;
    }

    public static ArrayList<String> getArcFiguresIds() {
        return canvas.arc_figure_ids;
    }

    public static ArrayList<String> getPositionablesIds() {
        ArrayList<String> positionablesIds = new ArrayList<String>();
        positionablesIds.addAll(canvas.label_figure_ids);
        positionablesIds.addAll(canvas.place_and_transition_figure_ids);

        return positionablesIds;
    }

    public static BaseFigure getFigureById(String figureId) {
        return canvas.getFigureById(figureId);
    }

    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }


    /* Resize the Canvas and move Elements and Viewport to create illusion of endless canvas */
    public static void cleanUpCanvas() {
        if (PetriNetController.getPetriNetElementCount() == 0) {
            canvas.setToMinSize();
            return;
        }

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
        if (figure != null && !(figure instanceof LabelFigure)) {
            if (figure.getElement() instanceof Connectable) {
                ((Connectable)figure.getElement()).addNewArc();
            }
            GlobalController.setActionMode(GlobalController.ACTION_ARC_SELECT_TARGET);
        }
    }


    /**
     * adjusts the reference point from which the grid will be painted.
     * @param p - the x/y amount by which the reference point will be adjusted
     */
    public static void addToGridReferencePoint(Point2D p) {
        canvas.addToGridReferencePoint(p);
    }


    public static void addFigure(BaseFigure figure, int type) {
        addFigureId(figure.getId(), type);
        canvas.addFigure(figure.getId(), figure);
    }

    public static void addFigureId(String figureId, int type) {
        switch (type) {
            case CanvasController.FIGURE_PLACE:
                // canvas.place_figure_ids.add(figureId);
                canvas.place_and_transition_figure_ids.add(figureId);
                break;
            case CanvasController.FIGURE_TRANSITION:
                // canvas.transition_figure_ids.add(figureId);
                canvas.place_and_transition_figure_ids.add(figureId);
                break;
            case CanvasController.FIGURE_ARC:
                canvas.arc_figure_ids.add(figureId);
                break;
            case CanvasController.FIGURE_LABEL:
                canvas.label_figure_ids.add(figureId);
                break;
            default:
                return;
        }
    }

    public static void removeFigure(String figureId, int type) {
        removeFigureId(figureId, type);
        canvas.removeFigure(figureId);
    }

    public static void removeFigureId(String figureId, int type) {
        switch (type) {
            case CanvasController.FIGURE_PLACE:
                canvas.place_and_transition_figure_ids.remove(figureId);
                break;
            case CanvasController.FIGURE_TRANSITION:
                canvas.place_and_transition_figure_ids.remove(figureId);
                break;
            case CanvasController.FIGURE_ARC:
                canvas.arc_figure_ids.remove(figureId);
                break;
            case CanvasController.FIGURE_LABEL:
                canvas.label_figure_ids.remove(figureId);
                break;
            default:
                return;
        }
    }



    public static Dimension getCanvasSize() {
        return canvas.getPreferredSize();
    }


    public static void mouseClicked(MouseEvent e) {
        switch (GlobalController.getActionMode()) {
            case GlobalController.ACTION_SELECT:
                //on dbl-click: clear selection and select figure under pointer, if any
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    SelectionController.clearSelection();
                    BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);
                    if (figureUnderMousePointer instanceof Positionable){
                        SelectionController.addFigureToSelection((Positionable)figureUnderMousePointer);
                        GlobalController.setActionMode(GlobalController.ACTION_SELECT);
                    }
                }
                break;
            default:
                break;
        }
    }


    public static void mousePressed(MouseEvent e) {
        mousePressPoint = new Point2D.Double(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch (GlobalController.getActionMode()) {
                case GlobalController.ACTION_SELECT:
                    SelectionController.mouseClickedInModeSelect(e);
                    break;
                case GlobalController.ACTION_PLACE:
                    PetriNetController.newConnectableElementAtPosition(mousePressPoint, PetriNetController.ELEMENT_PLACE);
                    break;
                case GlobalController.ACTION_TRANSITION:
                    PetriNetController.newConnectableElementAtPosition(mousePressPoint, PetriNetController.ELEMENT_TRANSITION);
                    break;
                case GlobalController.ACTION_ARC:
                    handleMousePressedModeArc();
                    break;
                case GlobalController.ACTION_ARC_SELECT_TARGET:
                    //check if a Place/Transition is under mousepointer
                    BaseFigure figure = SelectionController.getFigureUnderMousePointer(mousePressPoint);
                    //if not: do nothing
                    //if yes: add arc to Place/Transition
                    if (figure != null && (figure instanceof Positionable) && !(figure instanceof LabelFigure)) {
                        Arc arc = (Arc)PetriNetController.getElementById(arc_no_target_id);
                        if (arc.selectTarget(figure.getId())) {
                            GlobalController.setActionMode(GlobalController.ACTION_ARC);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        //RECHTER MOUSE PRESS
        if (SwingUtilities.isRightMouseButton(e)) {
            if (GlobalController.getActionMode() != GlobalController.ACTION_ARC_SELECT_TARGET) {
                //if a figure is under mouse, show its popup
                BaseFigure figureUnderMousePointer = SelectionController.getFigureUnderMousePointer(mousePressPoint);
                if (figureUnderMousePointer != null) {
                    if (SelectionController.getSelectedElementsIds().size() < 2) {
                        SelectionController.clearSelection();
                        figureUnderMousePointer.showPopup(new Point(e.getX(),e.getY()));
                    } else {
                        figureUnderMousePointer.showMultiSelectionPopup(e);
                    }
                }
            } else {
                //delete unfinished/red Arc
                GlobalController.setActionMode(GlobalController.ACTION_ARC);
            }
        }

        CanvasController.repaintCanvas();
    }


    public static void mouseDragged(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch (GlobalController.getActionMode()) {
                case  GlobalController.ACTION_SELECT:
                    SelectionController.updateSelection();
                    break;
                case GlobalController.ACTION_DRAG_SELECTION:
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
        switch (GlobalController.getActionMode()) {
            case GlobalController.ACTION_SELECT:
                SelectionController.removeSelectionRectangle();
                break;
            case GlobalController.ACTION_DRAG_SELECTION:
                GlobalController.setActionMode(GlobalController.ACTION_SELECT);
                break;
            default:
                break;
        }
        cleanUpCanvas();
        CanvasController.repaintCanvas();
    }


    public static void mouseMoved(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());
        CanvasController.repaintCanvas();
    }


    public static Point2D getCurrentMousePoint() {
        return currentMousePoint;
    }

    public static void repaintCanvas() {
        canvas.repaint();
    }





    /**
     * compute a surrounding Rectangle of all Figures with added padding
     * @return the surrounding Rectangle
     */
    public static Rectangle2D getFiguresBounds() {
        Rectangle2D petriNetBounds = canvas.getPetriNetFiguresBounds();
        Rectangle2D labelsBounds = canvas.getLabelsBounds();
        if (labelsBounds != null) {
            petriNetBounds.add(labelsBounds);
        }

        //add padding
        Rectangle2D figuresBounds = new Rectangle2D.Double(
            petriNetBounds.getX() - PETRINET_PADDING,
            petriNetBounds.getY() - PETRINET_PADDING,
            petriNetBounds.getWidth() + 2*PETRINET_PADDING,
            petriNetBounds.getHeight() + 2*PETRINET_PADDING
        );

        return figuresBounds;
    }


    public static void finishedCanvasPaint() {
        if (cleanUpCanvasAfterRepaint) {
            cleanUpCanvas();
            cleanUpCanvasAfterRepaint = false;
        }
    }


    public static void keyPressed( KeyEvent e) {
        int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        if (e.getModifiers() == shortcut) {
            selectionKeyActive = true;
        }
    }

    public static void keyReleased(KeyEvent e) {
        selectionKeyActive = false;
    }

}