package controller;

import model.*;
import view.*;
import view.figures.*;

import java.util.*;


import java.awt.*;
import java.awt.geom.*;


public class SelectionController {

    public static Rectangle2D selectionRectangle;


    //will select all petriNetElements that are intersecting with the selection rectangle.
    public static void updateSelection() {
        double currentX = CanvasController.currentMousePoint.getX() > 0 ? CanvasController.currentMousePoint.getX() : 0;
        double currentY = CanvasController.currentMousePoint.getY() > 0 ? CanvasController.currentMousePoint.getY() : 0;

        double width = Math.abs(CanvasController.mousePressPoint.getX() - currentX);
        double height = Math.abs(CanvasController.mousePressPoint.getY() - currentY);
        double x = Math.min(CanvasController.mousePressPoint.getX(), currentX);
        double y = Math.min(CanvasController.mousePressPoint.getY(), currentY);

        selectionRectangle = new Rectangle2D.Double(x, y, width, height);

        // System.out.printf("%f, %f, %f, %f \n", x, y, width, height );

        Iterator it = PetriNetController.getPetriNet().getElements().values().iterator();
        while (it.hasNext()) {
            PetriNetElement element = (PetriNetElement)it.next();
            boolean selected = element.getFigure().intersects(selectionRectangle);
            if (selected) {
                PetriNetController.addSelectedElementId(element.getId());
            }
        }

    }


    public static void drawSelectionFigure(Graphics2D g) {
        if (selectionRectangle != null) {
            SelectionFigure.draw(g);
        }
    }

}