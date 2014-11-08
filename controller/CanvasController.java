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

    static Point2D mousePressPoint;
    static Point2D currentMousePoint;


    public static view.Canvas createCanvas() {
        canvas = new view.Canvas();

        return canvas;
    }


    public static void cleanUpCanvas() {
        Rectangle petrinet_rectangle = PetriNetController.getPetriNetRectangle();
        Rectangle viewport_rectangle = MainWindowController.getViewportRectangle();

        Rectangle combined_viewport_and_petrinet_rectangle = petrinet_rectangle.union(viewport_rectangle);

        Double cleanedCanvasWidth = combined_viewport_and_petrinet_rectangle.getWidth();
        Double cleanedCanvasHeight = combined_viewport_and_petrinet_rectangle.getHeight();
        canvas.setPreferredSize(new Dimension(cleanedCanvasWidth.intValue(), cleanedCanvasHeight.intValue()));

        Double move_x = (-1) * combined_viewport_and_petrinet_rectangle.getX();
        Double move_y = (-1) * combined_viewport_and_petrinet_rectangle.getY();

        //move left and/or up
        if (move_x != 0 || move_y != 0) {
            System.out.println(" MOVE after canvas resize | left or up");
            PetriNetController.moveAllElements(move_x, move_y);
        }

        //REVALIDATE new canvas size
        canvas.revalidate();

        //compute new viewport scroll position (viewposition)
        Double scrollToWidth = MainWindowController.getViewport().getExtentSize().getWidth();
        Double scrollToHeight = MainWindowController.getViewport().getExtentSize().getHeight();
        Rectangle scroll_rect = new Rectangle(move_x.intValue(), move_y.intValue(), scrollToWidth.intValue(), scrollToHeight.intValue());

        MainWindowController.getViewport().scrollRectToVisible(scroll_rect);
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
        // GlobalController.STOP_PAINT = false;
        switch (GlobalController.mode) {
            case GlobalController.MODE_SELECT:
                SelectionController.removeSelectionRectangle();
                break;
            case GlobalController.MODE_DRAG_SELECTION:
                CanvasController.cleanUpCanvas();
                // GlobalController.STOP_PAINT = true;
                // System.out.println("STOPPANT true");
                GlobalController.mode = GlobalController.MODE_SELECT;
                break;
            case GlobalController.MODE_PLACE:
                //
                break;
            default:
                break;
        }

        System.out.println("release true");
        // if (!GlobalController.STOP_PAINT)
        canvas.repaint();
    }


}