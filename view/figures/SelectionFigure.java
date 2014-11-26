package view.figures;

import controller.*;

import java.awt.*;
import java.awt.geom.*;

/**
 * the colored rectangle that is used to select connectable PetriNetFigures
 */
public class SelectionFigure {
    static Rectangle2D rectangle;

    protected static Color strokeColor = new Color(0, 255, 0);
    protected static Color fillColor = new Color(255, 0, 255, 50);

    public static void draw(Graphics2D g) {
        rectangle = SelectionController.selectionRectangle.getBounds();
        drawFill(g);
        drawStroke(g);
    }

    public static void drawFill(Graphics2D g) {
        g.setPaint(fillColor);
        g.fill(rectangle);
    }

    public static void drawStroke(Graphics2D g) {
        g.setPaint(strokeColor);
        g.setStroke(new java.awt.BasicStroke(2f));
        g.draw(rectangle);

    }

}
