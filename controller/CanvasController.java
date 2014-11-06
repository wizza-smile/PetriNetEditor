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
    static Point2D mousePressPoint;
    static Point2D currentMousePoint;
    public static boolean shrink = false;


    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }


    public static void cleanUpCanvas() {
        PetriNetController.computePetriNetUpperLeftAndLowerRightCorner();
        computeAndSetCanvasSize();
        PetriNetController.fixPetriNetElementPositions();
    }


    public static void computeAndSetCanvasSize() {
        System.out.println("netDimension");
        System.out.println(PetriNetController.getPetriNet().netDimension.toString());
        System.out.println("getViewportSize");
        System.out.println(MainWindowController.getViewportSize().toString());
        Double width, height;

        width = Math.max(MainWindowController.getViewportSize().getWidth(), PetriNetController.getPetriNet().netDimension.getWidth());
        height = Math.max(MainWindowController.getViewportSize().getHeight(), PetriNetController.getPetriNet().netDimension.getHeight());

        canvas.setCanvasSize(new Dimension(width.intValue(), height.intValue()));
        canvas.revalidate();


        // Double width, height;
        // Dimension referenceSize = min_ref ? canvas.getMinSize() : canvas.getSize();

        // System.out.println(min_ref);
        // System.out.println(PetriNetController.getPetriNet().lowerRightCorner.getX() + " CS "+ referenceSize.getWidth());

        // width = Math.max( referenceSize.getWidth(), PetriNetController.getPetriNet().lowerRightCorner.getX()+10 );
        // height = Math.max( referenceSize.getHeight(), PetriNetController.getPetriNet().lowerRightCorner.getY()+10 );
        // Dimension canvasSize = new Dimension(width.intValue(), height.intValue());
        // canvas.setCanvasSize(canvasSize);
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