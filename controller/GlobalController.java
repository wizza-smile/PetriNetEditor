package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GlobalController {

    //the reference size of displayed elements
    public static Double size = 1.;
    public static boolean opacity = false;

    //the current actionMode of the application changes its behaviour on user-interaction.
    public static int actionMode = 0;
    //the possible actionModes of the application
    public static final int ACTION_SELECT = 0;
    public static final int ACTION_DRAG_SELECTION = 1;
    public static final int ACTION_PLACE = 2;
    public static final int ACTION_TRANSITION = 3;
    public static final int ACTION_ARC = 4;
    public static final int ACTION_ARC_SELECT_TARGET = 5;


    // public static boolean STOP_PAINT = false;

    // EVENTS
    // public static final int CANVAS_MOUSE_PRESSED = 0;


    public static void startApplication() {
        // Toolkit.getDefaultToolkit().setDynamicLayout(false);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PetriNetController.createPetriNet();

                MainWindowController.startMainWindow();

                view.Canvas canvas = CanvasController.createCanvas();
                canvas.setSize(new Dimension(200, 200));

                MainWindowController.injectCanvas(canvas);
                //set initialSize
                GlobalController.setSize(0.86);
            }
        });
    }

    /**
     * set the current actionMode.
     * performs cleanup operations depending on which ActionMode
     * the Application is switching from/to
     * @param actionMode - the actionMode to switch to
     */
    public static void setActionMode(int actionMode) {
        //cleanup after
        switch (GlobalController.actionMode) {
            case ACTION_ARC_SELECT_TARGET:
                if (CanvasController.arc_no_target_id != null) {
                    Arc arc = (Arc)PetriNetController.getElementById(CanvasController.arc_no_target_id);
                    arc.delete();
                    CanvasController.repaintCanvas();
                }
                break;
            default:
                break;
        }
        //cleanup before
        switch (actionMode) {
            case ACTION_PLACE:
            case ACTION_TRANSITION:
            case ACTION_ARC:
            case ACTION_ARC_SELECT_TARGET:
                SelectionController.clearSelection();
                CanvasController.repaintCanvas();
                break;
            default:
                break;
        }

        GlobalController.actionMode = actionMode;
    }

    public static int getActionMode() {
        return GlobalController.actionMode;
    }

    public static void setSize(Double size) {
        GlobalController.size = size;

        ArcFigure.ARROW_RADIUS = ArcFigure.ARROW_RADIUS_BASE * size;
        PlaceFigure.DIAMETER = PlaceFigure.DIAMETER_BASE * size;
        TransitionFigure.SIZE = TransitionFigure.SIZE_BASE * size;
        CanvasController.PETRINET_PADDING = CanvasController.PETRINET_PADDING_BASE * size;

        CanvasController.repaintCanvas();
    }

    public static void toggleOpacity() {
        GlobalController.opacity = !GlobalController.opacity;
        CanvasController.repaintCanvas();
    }


    public static ArrayList<String> combineIdArrayLists(ArrayList<String>[] list_arr) {
        ArrayList<String> newList = new ArrayList<String>();
        for (ArrayList<String> list : list_arr) {
            newList.addAll(list);
        }

        return newList;
    }


}