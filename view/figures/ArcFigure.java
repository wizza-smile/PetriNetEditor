
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
            target_position = (Point2D)target_element.getPosition().clone();
            Double x = .0; Double y = .0; Double alpha = .0; Double beta = .0;

            Double a = source_position.getX() - target_position.getX();
            Double b = source_position.getY() - target_position.getY();

            Double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            Double p = PlaceFigure.DIAMETER / (2*c);

            x = target_position.getX() + p*a;
            y = target_position.getY() + p*b;

            Line2D.Double line = new Line2D.Double(source_position, target_position);

            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(203, 0, 0, 23));
            g.draw(line);


            target_position.setLocation(x, y);
            line.setLine(source_position, target_position);
            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(23, 0, 0));
            g.draw(line);




            //dreick radius
            Double RADIUS = 6.;
            Double angle = 0.;

            Double move_x = (RADIUS/c) * a;
            Double move_y = (RADIUS/c) * b;

            int r = RADIUS.intValue();
            Polygon poly = new Polygon(
                       new int[]{r, 2*r, 0},
                       new int[]{0, 2*r, 2*r},
                       3);

  // Double a = source_position.getX() - target_position.getX();
  // Double b = source_position.getY() - target_position.getY();

  // Double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

            Double rad = Math.abs(Math.asin((a)/(c))*180/Math.PI);
            // Double rad = Math.asin(Math.sin(Math.PI*40/180))*180/Math.PI;
            System.out.println("RAD: " + rad );


            //pythagoras
            if (move_x < 0) {
                //0-180
                if (move_y > 0) {
                    // 0-90
                    angle = rad;
                } else {
                    // 90-180
                    angle = 180-rad;
                }
                // alpha = angle%90;
                // beta = 90-alpha;
                // factor = -1.;
                // factorx = factor;
            } else {
              //180-360
              if (move_y < 0) {
                    angle = rad+180;
                    // 180-270

              } else {
                  // 270-360
                    angle = 360-rad;

              }
            }


            // Double la = RADIUS*Math.sin(Math.PI*alpha/180);
            // Double lb = RADIUS*Math.sin(Math.PI*beta/180);



            // System.out.println( "Math.sin(alpha) a" + Math.sin(Math.PI*alpha/180) );
            // System.out.println( "Math.sin(beta) a" + Math.sin(Math.PI*beta/180) );
            // System.out.println( "alpha a" + alpha );

            // Polygon shape = new Polygon();
            // shape.addPoint(...);
            // ....

            // Double move_x = (-1)*(la+RADIUS);
            // Double move_y = (-1)*(lb+RADIUS+PlaceFigure.DIAMETER/2);

            // System.out.println( "LENGTH a: " + la );
            // System.out.println( "LENGTH b: " + lb );
            System.out.println( "move_x: " + move_x );
            System.out.println( "move_y: " + move_y );

            Rectangle bounds = poly.getBounds();
            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(angle), bounds.width / 2, bounds.height / 2);

            Path2D path = new GeneralPath(poly);
            Shape rotated = path.createTransformedShape( transform );


            g.translate(target_position.getX(), target_position.getY());
            g.translate(-RADIUS, -RADIUS);
            g.translate(move_x, move_y);
            // g.setColor(Color.LIGHT_GRAY);
            // g.fill(bounds);
            g.setColor(Color.BLACK);
            g.fill(rotated);
            g.translate(-target_position.getX(), -target_position.getY());
            g.translate(RADIUS, RADIUS);
            g.translate(-move_x, -move_y);
        }

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
