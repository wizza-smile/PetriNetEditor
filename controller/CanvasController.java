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


    public static boolean elements_in_viewport_x = true;
    public static boolean elements_outside_canvas_x = false;


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
        if (PetriNetController.getPetriNet().getElementCount() > 0) {
          checkIfElementsAreOutsideCanvas();
        }

        checkIfElementsAreInsideViewport();

        // computeAndSetCanvasSize();
        // PetriNetController.fixPetriNetElementPositions();
        // adjustViewportViewposition()
        // canvas.setCanvasSize(new Dimension(1000, 1000));




    }

    public static void checkIfElementsAreOutsideCanvas() {
        //X-AXIS
        Rectangle2D canvas_x_span = new Rectangle2D.Double(0, 0, getCanvasSize().getWidth(), 1);
        Rectangle2D petrinet_x_span = new Rectangle2D.Double(PetriNetController.getPetriNet().upper_left.getX(), 0, Math.max(PetriNetController.getPetriNet().netDimension.getWidth(), 1), 1);

        if (!canvas_x_span.contains(petrinet_x_span) && !elements_outside_canvas_x && GlobalController.MODE_DRAG_SELECTION != GlobalController.mode) {//

            elements_outside_canvas_x = true;

            Double width_off = (-1) * PetriNetController.getPetriNet().upper_left.getX();


            Dimension cd = canvas.getPreferredSize();
            System.out.println(cd.getWidth() + " ACTION: Elements outside Canvas X by: "+ width_off);
            cd.setSize(cd.getWidth()+width_off, cd.getHeight());
            canvas.setPreferredSize(cd);

            // canvas.setVisible(false);
            System.out.println(cd.toString());

            System.out.println(" revalidate ");


            Double scrollToWidth = MainWindowController.getViewport().getExtentSize().getWidth();
            Double scrollToHeight = MainWindowController.getViewport().getExtentSize().getHeight();
            Rectangle rect = new Rectangle(width_off.intValue(), 0, scrollToWidth.intValue(), scrollToHeight.intValue());
            System.out.println(" scrollRectToVisible ");

            canvas.revalidate();
            // canvas.setVisible(true);

            MainWindowController.getViewport().scrollRectToVisible(rect);
            PetriNetController.moveAllElements(width_off, .0);


            // MainWindowController.getViewport().setViewPosition(new Point(width_off.intValue(), 0));

        } else if (canvas_x_span.contains(petrinet_x_span) && elements_outside_canvas_x) {
            System.out.println("Elements NOT ANYMORE outside Canvas X");
            elements_outside_canvas_x = false;
        }

        //Y-AXIS
        Rectangle2D canvas_y_span = new Rectangle2D.Double(0, 0, 1, getCanvasSize().getHeight());
        Rectangle2D petrinet_y_span = new Rectangle2D.Double(0, PetriNetController.getPetriNet().upper_left.getY(), 1, PetriNetController.getPetriNet().netDimension.getHeight());

        if (!canvas_y_span.contains(petrinet_y_span)) {
            System.out.println("Elements outside Canvas Y " + (elements_outside_canvas_x==false));
        }

    }


    public static void movinFinished() {
        System.out.println("MOVIN AFTER FINISHED mNEXT");
        System.out.println(MainWindowController.getViewport().getViewPosition().toString());
    }




    public static void checkIfElementsAreInsideViewport() {
        //X-AXIS
        Rectangle2D viewport_x_span = new Rectangle2D.Double(MainWindowController.getViewport().getViewPosition().getX(), 0, MainWindowController.getViewport().getSize().getWidth(), 1);
        Rectangle2D petrinet_x_span = new Rectangle2D.Double(PetriNetController.getPetriNet().upper_left.getX(), 0, PetriNetController.getPetriNet().netDimension.getWidth(), 1);

        if (viewport_x_span.contains(petrinet_x_span) && !elements_in_viewport_x && GlobalController.MODE_DRAG_SELECTION != GlobalController.mode) {
            System.out.println("Elem back in viewport");
            System.out.println(MainWindowController.getViewport().getViewPosition().toString());

            elements_in_viewport_x = true;

            // PetriNetController.moveAllElements(-7.0, .0);


            // System.out.println(width_off);
            // // MainWindowController.getViewport().setViewPosition(new Point(150,0));
            Dimension cd = canvas.getPreferredSize();
            cd.setSize(MainWindowController.getViewport().getSize().getWidth(), cd.getHeight());
            canvas.setPreferredSize(cd);
            canvas.revalidate();
            // System.out.println(MainWindowController.getViewport().getViewPosition().toString());
            System.out.println("Elements contained in viewport X");
        } else if (!viewport_x_span.contains(petrinet_x_span) && elements_in_viewport_x) {
            elements_in_viewport_x = false;
        }

        //Y-AXIS
        Rectangle2D viewport_y_span = new Rectangle2D.Double(0, MainWindowController.getViewport().getViewPosition().getY(), 1, MainWindowController.getViewport().getSize().getHeight());
        Rectangle2D petrinet_y_span = new Rectangle2D.Double(0, PetriNetController.getPetriNet().upper_left.getY(), 1, PetriNetController.getPetriNet().netDimension.getHeight());

        if (viewport_y_span.contains(petrinet_y_span)) {
            //System.out.println("Elements contained Y");
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