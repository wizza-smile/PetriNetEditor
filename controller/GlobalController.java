package controller;

import model.*;
import view.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GlobalController {


    public static int mode = 0;
    public static final int MODE_SELECT = 0;
    public static final int MODE_DRAG_SELECTION = 1;
    public static final int MODE_PLACE = 2;
    public static final int MODE_TRANSITION = 3;
    public static final int MODE_ARC = 4;


    public static boolean STOP_PAINT = false;

    // EVENTS
    public static final int CANVAS_MOUSE_PRESSED = 0;




    public static void startApplication() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PetriNetController.createPetriNet();

                MainWindowController.startMainWindow();

                view.Canvas canvas = CanvasController.createCanvas();
                canvas.setSize(new Dimension(200, 200));
                // canvas.setSize(new Dimension(200, 200));
                // // canvas.setWidth(200);
                MainWindowController.injectCanvas(canvas);
            }
        });
    }



}