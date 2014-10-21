package controller;

import model.*;
import view.*;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;


public class GlobalController {

    PetriNet petriNet;

    public MainWindowController mainWindow_controller;
    CanvasController canvas_controller;


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


    // EVENTS
    public static final int CANVAS_MOUSE_PRESSED = 0;




    public void startApplication() {
        petriNet = new PetriNet();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GlobalController.this.mainWindow_controller = new MainWindowController(GlobalController.this);
                GlobalController.this.mainWindow_controller.startMainWindow();

                GlobalController.this.canvas_controller = new CanvasController(GlobalController.this);
                GlobalController.this.mainWindow_controller.injectCanvas(canvas_controller.createCanvas());
            }
        });
    }


    public PetriNet getPetriNet() {
        return petriNet;
    }

    public void dispatchEvent(int type, EventObject e) {
        switch (type) {
            case CANVAS_MOUSE_PRESSED:
                canvas_controller.mousePressed((MouseEvent)e);
            default:
                break;
        }

    }

    public void addPetriNetElement() {
        petriNet.addElement();
    }

    public void setStatusBarText(String s) {
        mainWindow_controller.setStatusBarText(s);
    }



}