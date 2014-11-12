
package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;
import java.util.*;



import java.awt.*;

// import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.swing.*;


public class ArcFigure extends BaseFigure {


    //radius of the circle surrounding arrow (size of arrow!)
    static Double ARROW_RADIUS = 6.;

    Line2D line;

    //gradient triangle
    Double a, b, c, alpha, gradient;
    boolean is_negative_gradient;
    boolean place_is_left;
    boolean place_is_up;


    Point2D intersetion_place, intersection_transition;


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

    public Transition getTransition() {
        return this.getArc().getTransition();
    }

    public Place getPlace() {
        return this.getArc().getPlace();
    }

    public int getTargetType() {
        return this.getArc().getTargetType();
    }


    protected void computeGradientTriangle() {
        Rectangle2D gradientRectangle = line.getBounds2D();

        //gradient triangle
        this.a = gradientRectangle.getWidth();
        this.b = gradientRectangle.getHeight();
        //distance between midPoints of place and transition
        this.c = Math.hypot(a, b);

        //the angle alpha // opposite to x length (a) in gradient triangle
        this.alpha = Math.abs(Math.asin(a/c)*180/Math.PI);

    // determine the orientation of gradient triangle by considering who is up or left (target/source)
    // to get the correct angle,

        this.place_is_left = getPlace().getPosition().getX() < getTransition().getPosition().getX();
        this.place_is_up = getPlace().getPosition().getY() < getTransition().getPosition().getY();
        this.is_negative_gradient = (place_is_left ^ place_is_up) ? false : true;

        this.gradient = is_negative_gradient ? -b/a : b/a;

        // System.out.println( gradient );
    }

    public void draw(Graphics2D g) {


        if (!getArc().isTargetSet()) {
            Point2D source_position = getArc().getSource().getPosition();
            Point2D target_position = CanvasController.getCurrentMousePoint();
            CanvasController.arc_no_target_id = this.getId();

            line = new Line2D.Double(source_position, target_position);
            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(23, 0, 0));
            g.draw(line);

        } else {


            Transition transition = getTransition();
            Place place = getPlace();

            if (transition.getPosition().equals(place.getPosition())) return;

            //first draw the line
            line = new Line2D.Double(transition.getPosition(), place.getPosition());

            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(0, 0, 0));
            g.draw(line);

            //then draw the arrowHead

            computeGradientTriangle();






            // TRANSITION is target
            if (1==1 || this.getTargetType() == Arc.TARGET_TRANSITION) {
                Point2D p = new Point2D.Double(0,0);

                    Double transition_intersect_x = .0; Double transition_intersect_y = .0;
                    Point2D target_position = (Point2D)transition.getPosition().clone();
                    Point2D source_position = place.getPosition();

                    //draw the arrow on transition side
                    Point factor_x_y = getFactor_X_Y(!place_is_left, !place_is_up);
                    //angle by which to rotate the arrow
                    Double angle = getRotationAngle(!place_is_left, alpha);


                    //the rectangualar around the arrow needs repositioning, so that the arrow will always touch the intersection point
                    //is this true for transition as target ????
                    Double transition_arrow_move_x = (ARROW_RADIUS/c) * a * factor_x_y.getX();
                    Double transition_arrow_move_y = (ARROW_RADIUS/c) * b * factor_x_y.getY();





                    //GET the four lines of transition rectangle
                    Point2D[] corners = new Point2D[4];
                    Rectangle2D transition_rectangle = ((TransitionFigure)transition.getFigure()).getTransitionRectangle();

                    //upper_left_corner
                    corners[0] = new Point2D.Double(
                        transition_rectangle.getX(),
                        transition_rectangle.getY()
                    );
                    //upper_right_corner
                    corners[1] = new Point2D.Double(
                        transition_rectangle.getX() + transition_rectangle.getWidth(),
                        transition_rectangle.getY()
                    );
                    //lower_right_corner
                    corners[2]  = new Point2D.Double(
                        transition_rectangle.getX() + transition_rectangle.getWidth(),
                        transition_rectangle.getY() + transition_rectangle.getHeight()
                    );
                    //lower_left_corner
                    corners[3]  = new Point2D.Double(
                        transition_rectangle.getX(),
                        transition_rectangle.getY() + transition_rectangle.getHeight()
                    );

                    intersection_transition = null;
                    for (int i = 0; i < 4; i++) {
                        Line2D.Double side = new Line2D.Double(corners[i], corners[(i+1)%4]);
                        if (side.intersectsLine(line)) {
                            intersection_transition = getIntersectionPoint(side, line);

                            break;
                        }
                    }



                    if (intersection_transition != null) {


                        //DRAW ARROW

                        target_position.setLocation(intersection_transition);

                        //compute the actual offset of transition_arrow to (0,0)
                        Double offset_x = target_position.getX()-ARROW_RADIUS+transition_arrow_move_x;
                        Double offset_y = target_position.getY()-ARROW_RADIUS+transition_arrow_move_y;

                        //create the shape
                        int r = ARROW_RADIUS.intValue();
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
                        g.setColor(Color.BLACK);
                        g.fill(rotated);
                        g.translate(-offset_x, -offset_y);

                    }


            }













                //PLACE is target
                if (this.getTargetType() == Arc.TARGET_PLACE) {
                        //draw the arrow on place side
                        Double place_intersect_x = .0; Double place_intersect_y = .0;
                        Point2D target_position = (Point2D)getPlace().getPosition().clone();
                        Point2D source_position = getTransition().getPosition();


                        Point factor_x_y = getFactor_X_Y(place_is_left, place_is_up);
                        //angle by which to rotate the arrow
                        Double angle = getRotationAngle(place_is_left, alpha);


                        //the rectangualar around the arrow needs repositioning, so that the arrow will always touch the intersection point
                        //is this true for transition as target ????
                        Double place_arrow_move_x = (ARROW_RADIUS/c) * a * factor_x_y.getX();
                        Double place_arrow_move_y = (ARROW_RADIUS/c) * b * factor_x_y.getY();


                        //relative factor of PlaceRadius to that distance
                        //the arc will be shortened by this amount on the Place side
                        //to determin the intersection with place
                        Double p = (PlaceFigure.DIAMETER/2) / c;

                        //compute the position of intersection with Place
                        place_intersect_x = target_position.getX() + p*a*factor_x_y.getX();
                        place_intersect_y = target_position.getY() + p*b*factor_x_y.getY();


                        intersetion_place = new Point2D.Double(place_intersect_x, place_intersect_y);

            //END PLACE COMPUTATION


                        //DRAW ARROW
                        // //draw the shortened line
                        target_position.setLocation(intersetion_place);
                        // line.setLine(source_position, target_position);
                        // g.setStroke(new java.awt.BasicStroke(1f));
                        // g.setPaint(new Color(23, 0, 0));
                        // g.draw(line);




                        //compute the actual offset of place_arrow to (0,0)
                        Double offset_x = target_position.getX()-ARROW_RADIUS+place_arrow_move_x;
                        Double offset_y = target_position.getY()-ARROW_RADIUS+place_arrow_move_y;



                        //create the shape
                        int r = ARROW_RADIUS.intValue();
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


    }


    public Point getFactor_X_Y(boolean target_is_left, boolean target_is_up) {
        Point factor_x_y;

        int factor_x = 1;
        int factor_y = 1;

        factor_x = target_is_left ? 1 : -1;
        factor_y = target_is_up ? 1 : -1;

        factor_x_y = new Point(factor_x, factor_y);

        return factor_x_y;
    }


    public Double getRotationAngle(boolean target_is_left, Double alpha) {
        Double rotation_angle;

        if (!target_is_left) {
           if (!is_negative_gradient) {
                // 0-90°
                rotation_angle = alpha;
            } else {
                // 90-180°
                rotation_angle = 180-alpha;
            }
        } else {
           if (!is_negative_gradient) {
                // 180-270°
                rotation_angle = 180+alpha;
            } else {
                // 270-360°
                rotation_angle = 360-alpha;
            }
        }

        return rotation_angle;
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


    // public static Point2D getIntersection(final Line2D.Double line1, final Line2D.Double line2) {

    //     final double x1,y1, x2,y2, x3,y3, x4,y4;
    //     x1 = line1.x1; y1 = line1.y1; x2 = line1.x2; y2 = line1.y2;
    //     x3 = line2.x1; y3 = line2.y1; x4 = line2.x2; y4 = line2.y2;
    //     final double x = (
    //             (x2 - x1)*(x3*y4 - x4*y3) - (x4 - x3)*(x1*y2 - x2*y1)
    //             ) /
    //             (
    //             (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
    //             );
    //     final double y = (
    //             (y3 - y4)*(x1*y2 - x2*y1) - (y1 - y2)*(x3*y4 - x4*y3)
    //             ) /
    //             (
    //             (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
    //             );

    //     return new Point2D.Double(x, y);

    // }

}
