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
    // static private view.Grid grid;
    static Point viewPortPosition;

    static Point2D mousePressPoint;
    static Point2D currentMousePoint;
    public static boolean shrink = false;


    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }


    public static void cleanUpCanvas() {
        // //save viewport position!
        // viewPortPosition = MainWindowController.getViewport().getViewPosition();

        PetriNetController.computePetriNetUpperLeftAndLowerRightCorner();

        //viewport relative to upper left petrinnetcorner
        // System.out.println(PetriNetController.getPetriNet().upper_left.getX());


        // System.out.println(MainWindowController.getViewport().setViewPosition.getX());

        PetriNetController.fixPetriNetElementPositions();

        computeAndSetCanvasSize();

        Double newViewportPosX = (-1)*Math.min(PetriNetController.getPetriNet().upper_left.getX(),0);
        Double newViewportPosY = (-1)*Math.min(PetriNetController.getPetriNet().upper_left.getY(),0);

        System.out.println("NEW VIEWPORT");
        System.out.println(newViewportPosX + ", " + newViewportPosY);


        // MainWindowController.getViewport().setViewPosition( new Point(newViewportPosX.intValue(), newViewportPosY.intValue()) );

    }


    public static void computeAndSetCanvasSize() {
        Double width, height, upper_left_x, upper_left_y, lower_right_x, lower_right_y;

        MainWindowController.computeViewportLowerRight();

        upper_left_x = Math.min(MainWindowController.viewport_upper_left.getX(), PetriNetController.getPetriNet().upper_left.getX());
        upper_left_y = Math.min(MainWindowController.viewport_upper_left.getY(), PetriNetController.getPetriNet().upper_left.getY());

        lower_right_x = Math.max(MainWindowController.viewport_lower_right.getX(), PetriNetController.getPetriNet().lower_right.getX());
        lower_right_y = Math.max(MainWindowController.viewport_lower_right.getY(), PetriNetController.getPetriNet().netDimension.getHeight());

        width = lower_right_x - upper_left_x;
        height = lower_right_y - upper_left_y;

        Dimension canvasDimension = new Dimension(width.intValue(), height.intValue());

        canvas.setCanvasSize(new Dimension(width.intValue(), height.intValue()));
        canvas.revalidate();


        System.out.println("\n viewport_upper_left.getX \n");
        System.out.println(MainWindowController.viewport_upper_left.getX());

        System.out.println("viewportDimension");
        System.out.println(MainWindowController.getViewport().getSize().toString());
        System.out.println("canvasDimension");
        System.out.println(canvasDimension.toString());
        System.out.println("netDimension");
        System.out.println(PetriNetController.getPetriNet().netDimension.toString());

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