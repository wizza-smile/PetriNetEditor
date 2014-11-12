
package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;



import java.awt.*;

// import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.*;


public class ArcFigure extends BaseFigure {

    public ArcFigure(Arc arc) {
        this.element = (PetriNetElement)arc;
    }

    public Arc getArc() {
        return (Arc)this.element;
    }

    public boolean contains(Point2D position) {
        return false;
    }

    public boolean intersects(Rectangle2D rect) {
        return false;
    }

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
            CanvasController.arc_no_target_id = this.getId();

            Line2D.Double line = new Line2D.Double(source_position, target_position);
            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(23, 0, 0));
            g.draw(line);

        } else {
            //first draw the line
            //then draw the arrowHead

            target_position = (Point2D)target_element.getPosition().clone();
            Double x = .0; Double y = .0;

            Line2D.Double line = new Line2D.Double(source_position, target_position);

            Rectangle2D gradientRectangle = line.getBounds2D();
            //gradient triangle
            Double a = gradientRectangle.getWidth();
            Double b = gradientRectangle.getHeight();



            // g.setColor(Color.BLUE);
            // g.draw(gradientRectangle);

            //compute distance between midPoints of source and target
            //Pythagoras
            Double c = Math.hypot(a, b);


            //the angle alpha // opposite to x length (a) in gradient triangle
            Double rad = Math.abs(Math.asin(a/c)*180/Math.PI);




            //draw the arrow

            //radius of the triangle of arrow (size of arrow!)
            Double RADIUS = 6.;
            //angle by which to rotate the arrow
            Double angle = 0.;





//PLACE is target

            //determine the orientation of gradient triangle by considering who is up or left (target/source)
            //to get the correct angle,
            Double mx = target_position.getX() - source_position.getX();
            Double my = target_position.getY() - source_position.getY();


            if (mx > 0) {
                    a *= -1;
                if (my < 0) {
                    // 0-90°
                    angle = rad;
                } else {
                    b *= -1;
                    // 90-180°
                    angle = 180-rad;
                }
            } else {
                if (my > 0) {
                    // 180-270°
                    angle = 180+rad;
                    b *= -1;

                } else {
                    // 270-360°
                    angle = 360-rad;
                }
            }

            //the rectangualar around the arrow needs repositioning, so that the arrow will always touch the intersection point
            //is this true for transition as target ????
            Double move_x = (RADIUS/c) * a;
            Double move_y = (RADIUS/c) * b;


            //relative factor of PlaceRadius to that distance
            //the arc will be shortened by this amount on the Place side
            //to determin the intersection with place
            Double p = PlaceFigure.DIAMETER / (2*c);

            //compute the position of intersection with Place
            x = target_position.getX() + p*a;
            y = target_position.getY() + p*b;

//END PLACE

            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(0, 0, 0));
            g.draw(line);

            // //draw the shortened line
            target_position.setLocation(x, y);
            // line.setLine(source_position, target_position);
            // g.setStroke(new java.awt.BasicStroke(1f));
            // g.setPaint(new Color(23, 0, 0));
            // g.draw(line);




            //compute the actual offset of arrow to (0,0)
            Double offset_x = target_position.getX()-RADIUS+move_x;
            Double offset_y = target_position.getY()-RADIUS+move_y;



            //create the shape
            int r = RADIUS.intValue();
            Polygon poly = new Polygon(
                new int[]{r, 2*r, 0},
                new int[]{0, 2*r, 2*r},
                3
            );

            Rectangle bounds = poly.getBounds();
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), bounds.width / 2, bounds.height / 2);

            Path2D path = new GeneralPath(poly);
            Shape rotated = path.createTransformedShape( transform );

            g.translate(offset_x, offset_y);
            // g.setColor(Color.LIGHT_GRAY);
            // g.fill(bounds);
            g.setColor(Color.BLACK);
            g.fill(rotated);
            g.translate(-offset_x, -offset_y);
        }


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
