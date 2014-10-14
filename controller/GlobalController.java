package controller;

import view.*;

import javax.swing.*;


public class GlobalController {

    MainWindowController mainWindow_controller;
    CanvasController canvas_controller;


    /** The current PetriNet model */
    // public static PetriNet petriNet = new PetriNet();
    /** Application mode*/
    public static int mode = 0;
    /** To enable figure selection*/
    public static final int SELECTMODE = 0;
    /** To add places */
    public static final int PLACEMODE = 1;
    /** To add transitions */
    public static final int TRANSITIONMODE = 2;
    /** To add arcs*/
    public static final int NORMALARCMODE = 3;
    /** When simulation occurs */
    public static final int SIMULATIONMODE = 4;




    public void startApplication() {
        mainWindow_controller = new MainWindowController(this);
        mainWindow_controller.startMainWindow();
    }

    public void getCanvasController() {
        if (!canvas_controller) canvas_controller = new CanvasController();

        return canvas_controller;
    }



}