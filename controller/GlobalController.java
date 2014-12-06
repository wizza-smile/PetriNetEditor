package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GlobalController {

    public static String applicationTitle = "PetriNetEditor - Jonas Karsten q9272968";
    /** the reference size of displayed elements. */
    public static Double size = 1.;

    /** whether opacity mode is active. */
    public static boolean opacity = false;

    /** the current actionMode of the application changes its behaviour on user-interaction. */
    public static int actionMode = 0;

    //the possible actionModes of the application
    /** the user can select and start to drag elements in this ActionMode. */
    public static final int ACTION_SELECT = 0;
    /** When this ActionMode is active, the user is currently dragging selected elements. */
    public static final int ACTION_DRAG_SELECTION = 1;
    /** the user can add a new Place in this ActionMode. */
    public static final int ACTION_PLACE = 2;
    /** the user can add a new Transition in this ActionMode. */
    public static final int ACTION_TRANSITION = 3;
    /** the user can add a new Arc in this ActionMode. */
    public static final int ACTION_ARC = 4;
    /** the user can select a target for a new/unfinished Arc in this ActionMode. */
    public static final int ACTION_ARC_SELECT_TARGET = 5;

    /**
     * starts the Application and injects the Canvas that is private property of CanvasController into MainWindow.
     */
    public static void startApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PetriNetController.createPetriNet();
                MainWindowController.startMainWindow();
                view.Canvas canvas = CanvasController.createCanvas();
                canvas.setSize(new Dimension(200, 200));
                MainWindowController.injectCanvas(canvas);
                //set initial size
                GlobalController.setSize(0.86);
            }
        });
    }

    /**
     * create a UUID.
     * see https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html
     * @return  the UUID.
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * set the current actionMode.
     * performs cleanup operations depending on which ActionMode
     * the Application is switching from/to
     * @param actionMode - the actionMode to switch to
     */
    public static void setActionMode(int actionMode) {
        //cleanup after actionMode
        switch (GlobalController.actionMode) {
            case ACTION_ARC_SELECT_TARGET:
                if (CanvasController.arc_no_target_id != null) {
                    Arc arc = (Arc)PetriNetController.getElementById(CanvasController.arc_no_target_id);
                    arc.delete();
                    CanvasController.arc_no_target_id = null;
                    CanvasController.repaintCanvas();
                }
                break;
            default:
                break;
        }
        //cleanup before actionMode
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

    /**
     * reset all relevant reference sizes and repaint canvas.
     * @param size the new global refernce size.
     */
    public static void setSize(Double size) {
        GlobalController.size = size;

        ArcFigure.ARROW_HEAD_RADIUS = ArcFigure.ARROW_HEAD_RADIUS_BASE * size;
        PlaceFigure.DIAMETER = PlaceFigure.DIAMETER_BASE * size;
        TransitionFigure.SIZE = TransitionFigure.SIZE_BASE * size;
        CanvasController.PETRINET_PADDING = CanvasController.PETRINET_PADDING_BASE * size;

        CanvasController.repaintCanvas();
    }

    /** toggle opacity mode. */
    public static void toggleOpacity() {
        GlobalController.opacity = !GlobalController.opacity;
        CanvasController.repaintCanvas();
    }

    /** reset the Editor to clean initial State. Current PetriNet will be deleted. */
    public static void clearPetriNetEditor() {
        SelectionController.clearSelection();
        PetriNetController.createPetriNet();
        MainWindowController.injectCanvas(CanvasController.createCanvas());
    }

}