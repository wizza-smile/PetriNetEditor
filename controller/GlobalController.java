package controller;

import model.*;
import view.*;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;


public class GlobalController {


    /** Application mode*/
    public static int mode = 0;
    /** To enable figure selection*/
    public static final int MODE_ARROW = 0;
    /** To add places */
    public static final int MODE_PLACE = 1;
    /** To add transitions */
    public static final int TRANSITIONMODE = 2;
    /** To add arcs*/
    public static final int NORMALARCMODE = 3;
    /** When simulation occurs */
    public static final int SIMULATIONMODE = 4;


    // EVENTS
    public static final int CANVAS_MOUSE_PRESSED = 0;




    public static void startApplication() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PetriNetController.createPetriNet();

                MainWindowController.startMainWindow();

                Canvas canvas = CanvasController.createCanvas();
                MainWindowController.injectCanvas(canvas);
            }
        });
    }



}