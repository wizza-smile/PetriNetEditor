package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;


import java.awt.*;
import java.awt.geom.*;


public class SelectionController {

    public static Rectangle2D selectionRectangle;



    public static void updateSelection() {
        double width = Math.abs(CanvasController.mousePressPoint.getX() - CanvasController.mouseReleasePoint.getX());
        double height = Math.abs(CanvasController.mousePressPoint.getY() - CanvasController.mouseReleasePoint.getY());
        double x = Math.min(CanvasController.mousePressPoint.getX(), CanvasController.mouseReleasePoint.getX());
        double y = Math.min(CanvasController.mousePressPoint.getY(), CanvasController.mouseReleasePoint.getY());

        selectionRectangle = new Rectangle2D.Double(x, y, width, height);


        System.out.printf("%f, %f, %f, %f \n", x, y, width, height );

        Iterator it = PetriNetController.getPetriNet().getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement element = (PetriNetElement)it.next();
            // MainWindowController.setStatusBarText("INTERSECTION");
            boolean selected = element.getFigure().intersects(selectionRectangle);
            if (selected) {
                System.out.println("MOUSE mouseReleased");
                MainWindowController.setStatusBarText("INTERSECTION");
            }
        }

    }


    public static void drawSelectionFigure(Graphics2D g) {
        if (selectionRectangle != null) {
            SelectionFigure.draw(g);
        }
    }

}