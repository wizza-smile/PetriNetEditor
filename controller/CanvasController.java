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


        // computeAndSetCanvasSize();
        // PetriNetController.fixPetriNetElementPositions();
        // adjustViewportViewposition()
        // canvas.setCanvasSize(new Dimension(1000, 1000));




    }

    public static void checkIfElementsAreOutsideCanvas() {
        Double width_off = .0;
        Double height_off = .0;

        boolean move_elements_direction_x = true;//true == direction right / elements are too far left
        boolean move_elements_direction_y = true;//true == direction down / elements are too far up

        //X-AXIS
        Rectangle2D canvas_x_span = new Rectangle2D.Double(0, 0, getCanvasSize().getWidth(), 1);
        Rectangle2D petrinet_x_span = new Rectangle2D.Double(PetriNetController.getPetriNet().upper_left.getX(), 0, Math.max(PetriNetController.getPetriNet().netDimension.getWidth(), 1), 1);

        boolean canvas_contains_petrinet_x = canvas_x_span.contains(petrinet_x_span);

        if (!canvas_contains_petrinet_x) {
            if (PetriNetController.getPetriNet().upper_left.getX() < 0) {
                //elements are too left
                width_off = (-1) * PetriNetController.getPetriNet().upper_left.getX();
            } else {
                //elements are too right
                move_elements_direction_x = false;
                width_off = PetriNetController.getPetriNet().lower_right.getX() - getCanvasSize().getWidth();
            }

        }

        //Y-AXIS
        Rectangle2D canvas_y_span = new Rectangle2D.Double(0, 0, 1, getCanvasSize().getHeight());
        Rectangle2D petrinet_y_span = new Rectangle2D.Double(0, PetriNetController.getPetriNet().upper_left.getY(), 1, PetriNetController.getPetriNet().netDimension.getHeight());

        boolean canvas_contains_petrinet_y = canvas_y_span.contains(petrinet_y_span);

        if (!canvas_contains_petrinet_y) {
            if (PetriNetController.getPetriNet().upper_left.getY() < 0) {
                //elements are too high
                height_off = (-1) * PetriNetController.getPetriNet().upper_left.getY();
            } else {
                //elements are too low
                move_elements_direction_y = false;
                height_off = PetriNetController.getPetriNet().lower_right.getY() - getCanvasSize().getHeight();
            }
        }


        if (width_off != 0 || height_off != 0) {
            //ENLARGE CANVAS SIZE
            Dimension cd = canvas.getPreferredSize();
            cd.setSize(cd.getWidth()+width_off, cd.getHeight()+height_off);
            canvas.setPreferredSize(cd);

            // width_off = move_elements_direction_x ? -width_off : width_off;
            // height_off = move_elements_direction_y ? -height_off : height_off;

            if (move_elements_direction_x || move_elements_direction_y) {
                //move all elements to relative position
                System.out.println(width_off + " " + height_off);
                PetriNetController.moveAllElements(move_elements_direction_x ? width_off : 0, move_elements_direction_y ? height_off : 0);
            }

            //compute new scroll position
            // Double scrollToX = MainWindowController.getViewport().getViewPosition().getX();
            // Double scrollToY = MainWindowController.getViewport().getViewPosition().getY();
            Double scrollToX = move_elements_direction_x ? width_off : MainWindowController.getViewport().getViewPosition().getX();
            Double scrollToY = move_elements_direction_y ? height_off : MainWindowController.getViewport().getViewPosition().getY();
            Double scrollToWidth = MainWindowController.getViewport().getExtentSize().getWidth();
            Double scrollToHeight = MainWindowController.getViewport().getExtentSize().getHeight();


            Rectangle rect = new Rectangle(scrollToX.intValue(), scrollToY.intValue(), scrollToWidth.intValue(), scrollToHeight.intValue());
            System.out.println(rect);

            //REVALIDATE new canvas size
            canvas.revalidate();

            //scroll to new position
            MainWindowController.getViewport().scrollRectToVisible(rect);
        }


    }


    public static void movinFinished() {
        System.out.println("MOVIN AFTER FINISHED mNEXT");
        System.out.println(MainWindowController.getViewport().getViewPosition().toString());
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


        //SET CANVAS SIZE TO VIEWPORT SIZE
        System.out.println("SET CANVAS SIZE TO VIEPORT \n\n");

        if (viewport_contains_petrinet_x || viewport_contains_petrinet_y) {
            Dimension cd = canvas.getPreferredSize();
            width = viewport_contains_petrinet_x ? MainWindowController.getViewport().getSize().getWidth() : cd.getWidth();
            height = viewport_contains_petrinet_y ? MainWindowController.getViewport().getSize().getHeight() : cd.getHeight();
            cd.setSize(width, height);
            canvas.setPreferredSize(cd);


            // System.out.println("ADD " + PetriNetController.getPetriNet().lower_right.getX() + " " + MainWindowController.getViewport().getSize().getWidth());
            Double move_x = viewport_contains_petrinet_x ? Math.max(PetriNetController.getPetriNet().lower_right.getX() - MainWindowController.getViewport().getSize().getWidth(), 0) : 0;
            Double move_y = viewport_contains_petrinet_y ? Math.max(PetriNetController.getPetriNet().lower_right.getY() - MainWindowController.getViewport().getSize().getHeight(), 0) : 0;
            if (move_x != 0 || move_y != 0) {
                System.out.println("ACC " + move_x + " " + move_y);
                PetriNetController.moveAllElements(-move_x, -move_y);
            }

            canvas.revalidate();
        }
    }

    //minimum: viewport size
    public static void computeAndSetCanvasSize() {
        Double width, height, upper_left_x, upper_left_y, lower_right_x, lower_right_y;

        Double petrinet_offset_x = PetriNetController.getPetriNet().upper_left.getX();


        width = Math.max(MainWindowController.getViewport().getSize().getWidth(), PetriNetController.getPetriNet().netDimension.getWidth() + PetriNetController.PETRINET_PADDING);
        height = Math.max(MainWindowController.getViewport().getSize().getHeight(), PetriNetController.getPetriNet().netDimension.getHeight() + PetriNetController.PETRINET_PADDING);

        Dimension canvasDimension = new Dimension(width.intValue(), height.intValue());


        canvas.setCanvasSize(canvasDimension);
        canvas.revalidate();

        System.out.println("\n viewportDimension");
        System.out.println(MainWindowController.getViewport().getSize().toString());
        System.out.println("canvasDimension");
        System.out.println(canvasDimension.toString());
        System.out.println("netDimension");
        System.out.println(PetriNetController.getPetriNet().netDimension.toString());


        // MainWindowController.computeViewportUpperLeftLowerRight();

        // upper_left_x = Math.min(MainWindowController.viewport_upper_left.getX(), PetriNetController.getPetriNet().upper_left.getX());
        // upper_left_y = Math.min(MainWindowController.viewport_upper_left.getY(), PetriNetController.getPetriNet().upper_left.getY());

        // lower_right_x = Math.max(MainWindowController.viewport_lower_right.getX(), PetriNetController.getPetriNet().lower_right.getX());
        // lower_right_y = Math.max(MainWindowController.viewport_lower_right.getY(), PetriNetController.getPetriNet().netDimension.getHeight());




        // width = lower_right_x - upper_left_x;
        // height = lower_right_y - upper_left_y;

        // Dimension canvasDimension = new Dimension(width.intValue(), height.intValue());

        // canvas.setCanvasSize(canvasDimension);
        // canvas.revalidate();


        // System.out.println("\n lower_right_x \n");
        // System.out.println(upper_left_x);

        // System.out.println("viewportDimension");
        // System.out.println(MainWindowController.getViewport().getSize().toString());
        // System.out.println("canvasDimension");
        // System.out.println(canvasDimension.toString());

        // System.out.println("getViewportSize");
        //
        // System.out.println(MainWindowController.getViewport().getSize().toString());
        // System.out.println("getViewportPosition");
        // System.out.println(MainWindowController.getViewport().getViewPosition().getX());

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
                System.out.println("MOUSE mouseReleased");
                break;
        }

        canvas.repaint();
    }


}