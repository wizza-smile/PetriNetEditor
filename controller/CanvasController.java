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

    static private view.Canvas canvas;


    public static boolean viewport_contains_petrinet_x = true;
    public static boolean viewport_contains_petrinet_y = true;


    static Point2D mousePressPoint;
    static Point2D currentMousePoint;
    public static boolean shrink = false;


    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }


    public static void cleanUpCanvas() {

        PetriNetController.computePetriNetUpperLeftAndLowerRightCorner();
        // MainWindowController.computeViewportUpperLeftLowerRight();
        if (GlobalController.MODE_DRAG_SELECTION != GlobalController.mode && PetriNetController.getPetriNet().getElementCount() > 0) {
            checkIfElementsAreOutsideCanvas();
            checkIfElementsAreInsideViewport();
        }
        // if (mode) //mode changed from moving/dragging
        // remove superfluous areas

        MainWindowController.setStatusBarText(MainWindowController.getViewport().getViewPosition().getX() + " " + MainWindowController.getViewport().getViewPosition().getY());
    }


    public static void checkIfElementsAreOutsideCanvas() {
        /* how far is the is the petrinet outside of canvas */
        Double width_off = .0;
        Double height_off = .0;

        /* if elements are too far left resp. up, move them into canvas */
        boolean move_elements_x = false;//if elements are too far left
        boolean move_elements_y = false;//if elements are too far up

        /////////////
        //X-AXIS

        /* 1px flat rectangles on x-axis over the span of canvas resp. petrinet */
        Rectangle2D canvas_x_span = new Rectangle2D.Double(0, 0, getCanvasSize().getWidth(), 1);
        Rectangle2D petrinet_x_span = new Rectangle2D.Double(PetriNetController.getPetriNet().upper_left.getX(), 0, Math.max(PetriNetController.getPetriNet().netDimension.getWidth(), 1), 1);

        /* check if petrinet lies within canvas on x-axis */
        boolean canvas_contains_petrinet_x = canvas_x_span.contains(petrinet_x_span);

        if (!canvas_contains_petrinet_x) {
            if (PetriNetController.getPetriNet().upper_left.getX() < 0) {
                //elements are too left
                width_off = (-1) * PetriNetController.getPetriNet().upper_left.getX();
                move_elements_x = true;
            } else {
                //elements are too right
                width_off = PetriNetController.getPetriNet().lower_right.getX() - getCanvasSize().getWidth();
            }

        }

        ////////////
        //Y-AXIS
        /* do the same as we did for x-axis */
        Rectangle2D canvas_y_span = new Rectangle2D.Double(0, 0, 1, getCanvasSize().getHeight());
        Rectangle2D petrinet_y_span = new Rectangle2D.Double(0, PetriNetController.getPetriNet().upper_left.getY(), 1, PetriNetController.getPetriNet().netDimension.getHeight());

        boolean canvas_contains_petrinet_y = canvas_y_span.contains(petrinet_y_span);

        if (!canvas_contains_petrinet_y) {
            if (PetriNetController.getPetriNet().upper_left.getY() < 0) {
                //elements are too high
                move_elements_y = true;
                height_off = (-1) * PetriNetController.getPetriNet().upper_left.getY();
            } else {
                //elements are too low
                height_off = PetriNetController.getPetriNet().lower_right.getY() - getCanvasSize().getHeight();
            }
        }


        if (width_off != 0 || height_off != 0) {
            //ENLARGE CANVAS SIZE
            Dimension cd = canvas.getPreferredSize();
            cd.setSize(cd.getWidth()+width_off, cd.getHeight()+height_off);
            canvas.setPreferredSize(cd);

            //IF ELEMENTS ARE TOO HIGH OR LEFT:
            //MOVE ALL ELEMENTS INTO CANVAS
            if (move_elements_x || move_elements_y) {
                PetriNetController.moveAllElements(move_elements_x ? width_off : 0, move_elements_y ? height_off : 0);
            }

            //compute new viewport scroll position (viewposition)
            Double scrollToX = move_elements_x ? width_off : MainWindowController.getViewport().getViewPosition().getX();
            Double scrollToY = move_elements_y ? height_off : MainWindowController.getViewport().getViewPosition().getY();
            Double scrollToWidth = MainWindowController.getViewport().getExtentSize().getWidth();
            Double scrollToHeight = MainWindowController.getViewport().getExtentSize().getHeight();
            Rectangle rect = new Rectangle(scrollToX.intValue(), scrollToY.intValue(), scrollToWidth.intValue(), scrollToHeight.intValue());

            //REVALIDATE new canvas size
            canvas.revalidate();

            //scroll to new position
            MainWindowController.getViewport().scrollRectToVisible(rect);
        }


    }




    public static void checkIfElementsAreInsideViewport() {
        Double width, height;

        //X-AXIS
        Rectangle2D viewport_x_span = new Rectangle2D.Double(MainWindowController.getViewport().getViewPosition().getX(), 0, MainWindowController.getViewport().getSize().getWidth(), 1);
        Rectangle2D petrinet_x_span = new Rectangle2D.Double(PetriNetController.getPetriNet().upper_left.getX(), 0, PetriNetController.getPetriNet().netDimension.getWidth(), 1);

        boolean viewport_contains_petrinet_x = viewport_x_span.contains(petrinet_x_span);


        //Y-AXIS
        Rectangle2D viewport_y_span = new Rectangle2D.Double(0, MainWindowController.getViewport().getViewPosition().getY(), 1, MainWindowController.getViewport().getSize().getHeight());
        Rectangle2D petrinet_y_span = new Rectangle2D.Double(0, PetriNetController.getPetriNet().upper_left.getY(), 1, PetriNetController.getPetriNet().netDimension.getHeight());

        boolean viewport_contains_petrinet_y = viewport_y_span.contains(petrinet_y_span);


        //SET CANVAS SIZE TO VIEWPORT SIZE ON MATCHING AXIS'
        if (viewport_contains_petrinet_x || viewport_contains_petrinet_y) {
            System.out.println("SET CANVAS SIZE TO VIEPORT \n\n");

            Dimension cd = canvas.getPreferredSize();
            width = viewport_contains_petrinet_x ? MainWindowController.getViewport().getSize().getWidth() : cd.getWidth();
            height = viewport_contains_petrinet_y ? MainWindowController.getViewport().getSize().getHeight() : cd.getHeight();

            Double width_diff = cd.getWidth() - width;
            Double height_diff = cd.getHeight() - height;

            cd.setSize(width, height);
            canvas.setPreferredSize(cd);


            //IF ELEMENTS FALL OUT OF CANVAS DUE TO CANVAS RESIZING (RIGHT OR DOWN):
            //MOVE THEM INTO VIEWPORT!
            boolean move_x = viewport_contains_petrinet_x ? MainWindowController.getViewport().getViewPosition().getX() > 0 : false;
            boolean move_y = viewport_contains_petrinet_y ? MainWindowController.getViewport().getViewPosition().getY() > 0 : false;

            //move left or up
            if (move_x || move_y) {
                System.out.println(" MOVE X LEFT" +move_x);
                PetriNetController.moveAllElements(-width_diff, -height_diff);
            }

            canvas.revalidate();
        }
    }


    public static void setGridReferencePoint(Point2D p) {
        Grid.setReferencePoint(p);
    }

    public static void addToGridReferencePoint(Point2D p) {
        Grid.addToReferencePoint(p);
    }

    public static Dimension getCanvasSize() {
        return canvas.getPreferredSize();
    }


    public static void mouseClicked(MouseEvent e) {
        //on dbl-click: clear selection and select figure under pointer, if any
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            SelectionController.clearSelection();
            SelectionController.selectFigureUnderMousePointer(mousePressPoint);
            GlobalController.mode = GlobalController.MODE_SELECT;
        }
    }


    public static void mousePressed(MouseEvent e) {
        mousePressPoint = new Point2D.Double(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            switch (GlobalController.mode) {
                case GlobalController.MODE_SELECT:
                    //check if selected elementFigure is under mouse pointer
                    //if yes: start dragging selection
                    //if not: remove current selection
                    ArrayList<String> selectedElements_ids = SelectionController.getSelectedElementsIds();
                    boolean clearSelection = true;
                    for (String id : selectedElements_ids ) {
                        if (PetriNetController.getElementById(id).getFigure().contains(mousePressPoint)) {
                            //user wants to drag selected elements!
                            clearSelection = false;
                            GlobalController.mode = GlobalController.MODE_DRAG_SELECTION;
                            break;
                        }
                    }
                    if(clearSelection) {
                        SelectionController.clearSelection();
                        SelectionController.selectFigureUnderMousePointer(mousePressPoint);
                    }
                    SelectionController.setOffsetToSelectedElements(mousePressPoint);
                    break;
                case GlobalController.MODE_PLACE:
                    PetriNetController.addPetriNetElement(mousePressPoint, PetriNetController.ELEMENT_PLACE);
                    break;
                default:
                    System.out.println("MOUSE PRESSSSSS");
                    break;
            }
        }

        canvas.repaint();
    }


    public static void mouseDragged(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());
        // MainWindowController.setStatusBarText("F " + currentMousePoint.getX());
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
                        BaseFigure figure = PetriNetController.getElementById(id).getFigure();
                        Point2D offset = figure.getOffset();
                        figure.setPosition( new Point2D.Double(currentMousePoint.getX()+offset.getX(), currentMousePoint.getY()+offset.getY()) );
                    }
                    break;
                default:
                    MainWindowController.setStatusBarText("MOUSE DRAGGED");
                    break;
            }
        }

        canvas.repaint();
    }


    public static void mouseReleased(MouseEvent e) {
        currentMousePoint = new Point2D.Double(e.getX(), e.getY());

        switch (GlobalController.mode) {
            case GlobalController.MODE_SELECT:
                SelectionController.removeSelectionRectangle();
                break;
            case GlobalController.MODE_DRAG_SELECTION:
                GlobalController.mode = GlobalController.MODE_SELECT;
                break;
            case GlobalController.MODE_PLACE:
                //
                break;
            default:
                break;
        }


        canvas.repaint();
    }


}