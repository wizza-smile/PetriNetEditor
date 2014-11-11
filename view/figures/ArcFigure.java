
package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;



import java.awt.*;

// import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.*;


public class ArcFigure {

    protected Arc arc;

    public ArcFigure(Arc arc) {
        this.arc = arc;
    }

    public Arc getArc() {
        return this.arc;
    }

    // public boolean contains(Point2D position) {
    //     return this.transitionRectangle.contains(position);
    // }

    // public boolean intersects(Rectangle2D rect) {
    //     return this.transitionRectangle.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    // }

    public PetriNetElement getSourceElement() {
        return this.getArc().getSource();
    }

    public PetriNetElement getTargetElement() {
        return this.getArc().getTarget();
    }

    public void draw(Graphics2D g) {
        PetriNetElement target_element = getTargetElement();
        Point2D target_position;
        Point2D source_position = getSourceElement().getPosition();

        if (target_element == null) {
            target_position = CanvasController.getCurrentMousePoint();
        } else {
            target_position = target_element.getPosition();
        }

        Line2D.Double line = new Line2D.Double();
        line.setLine(source_position, target_position);

        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(new Color(0, 0, 0));
        g.draw(line);

        //intersectsLine(Line2D l)
        //System.out.println( "NO TARGET" );

        // System.out.println( sourceElement.getPosition().getX() );

    }

    public Point2D getIntersectionPoint(Line2D lineA, Line2D lineB) {

        double x1 = lineA.getX1();
        double y1 = lineA.getY1();
        double x2 = lineA.getX2();
        double y2 = lineA.getY2();

        double x3 = lineB.getX1();
        double y3 = lineB.getY1();
        double x4 = lineB.getX2();
        double y4 = lineB.getY2();

        Point2D p = null;

        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d != 0) {
            double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

            p = new Point2D.Double(xi, yi);

        }
        return p;
    }


    public static Point2D getIntersection(final Line2D.Double line1, final Line2D.Double line2) {

        final double x1,y1, x2,y2, x3,y3, x4,y4;
        x1 = line1.x1; y1 = line1.y1; x2 = line1.x2; y2 = line1.y2;
        x3 = line2.x1; y3 = line2.y1; x4 = line2.x2; y4 = line2.y2;
        final double x = (
                (x2 - x1)*(x3*y4 - x4*y3) - (x4 - x3)*(x1*y2 - x2*y1)
                ) /
                (
                (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
                );
        final double y = (
                (y3 - y4)*(x1*y2 - x2*y1) - (y1 - y2)*(x3*y4 - x4*y3)
                ) /
                (
                (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
                );

        return new Point2D.Double(x, y);

    }

}
