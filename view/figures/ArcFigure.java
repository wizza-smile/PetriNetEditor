
package view.figures;

import model.*;
import view.*;
import controller.*;

import java.lang.Math;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;


public class ArcFigure extends BaseFigure {
    //radius of the circle surrounding arrow (size of arrow!)
    final public static Double ARROW_RADIUS_BASE = 6.1;
    public static Double ARROW_RADIUS = ARROW_RADIUS_BASE;

    /**
     * the line between the midpoints of transition and place
     */
    protected Line2D line;

    /**
     * filled with two possible arrow_heads.
     * [0] = target_transition [1] = target_place
     */
    protected ArrowHead[] arrow_heads = new ArrowHead[2];

    /**
     * the target_type of the most recently selected arrowHead of this arcFigure.
     */
    public int selectedArrowHeadsTargetType;

    //gradient triangle
    Double a, b, c, alpha, gradient;
    boolean is_negative_gradient;
    boolean place_is_left;
    boolean place_is_up;

    /**
     * the point of intersection of [line] with the place resp. transition figure.
     */
    protected Point2D intersection_place, intersection_transition;


    public ArcFigure(Arc arc) {
        this.setId(arc.getId());
        this.element = (PetriNetElement)arc;
        register();
    }

    public Arc getArc() {
        return (Arc)this.element;
    }

    public void register() {
        CanvasController.addFigure(this, CanvasController.FIGURE_ARC);
    }

    public void delete() {
        CanvasController.removeFigure(this.getId(), CanvasController.FIGURE_ARC);
    }

    public int getFigureType() {
        return CanvasController.FIGURE_ARC;
    }

    /**
     * check if an arrowHead has been clicked on,
     * if yes: store its target_type in property (selectedArrowHeadsTargetType)
     * @param  position - position of mouse click
     * @return boolean
     */
    public boolean contains(Point2D position) {
        for (ArrowHead ah : arrow_heads ) {
            if (ah != null && ah.contains(position)) {
                this.selectedArrowHeadsTargetType = ah.TARGET_TYPE;
                return true;
            }
        }

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

        // determine the orientation of gradient triangle by considering who is up or left (place/transition)
        this.place_is_left = getPlace().getPosition().getX() < getTransition().getPosition().getX();
        this.place_is_up = getPlace().getPosition().getY() < getTransition().getPosition().getY();
        this.is_negative_gradient = (place_is_left ^ place_is_up) ? false : true;

        Rectangle2D gradientRectangle = line.getBounds2D();

        //gradient triangle
        this.a = gradientRectangle.getWidth();
        this.b = gradientRectangle.getHeight();
        //distance between midPoints of place and transition
        this.c = Math.hypot(a, b);

        //the angle alpha // opposite to x length (a) in gradient triangle
        this.alpha = Math.abs(Math.asin(a/c)*180/Math.PI);
    }

    /**
     * exploits the similarity between the gradient rectangle and
     * the rectangular rectangle which's hypotenuse is the line
     * between place's midpoint and intersection point
     */
    protected void computeIntersectionPlace() {
        Double place_intersect_x = .0; Double place_intersect_y = .0;

        //determine in which direction from place's midpoint the intersection lies
        Point factor_x_y = getFactor_X_Y(place_is_left, place_is_up);

        //ratio between PlaceRadius and the distance between midPoints
        //the arc will be shortened by this amount on the Place side
        //to determin the intersection with place
        Double p = (PlaceFigure.DIAMETER/2) / c;

        //compute the position of intersection with Place
        place_intersect_x = getPlace().getPosition().getX() + p*a*factor_x_y.getX();
        place_intersect_y = getPlace().getPosition().getY() + p*b*factor_x_y.getY();

        intersection_place = new Point2D.Double(place_intersect_x, place_intersect_y);
    }


    /**
     * Check the sides of transition rectangle for an intersection with arrowLine.
     * if one is found, call getIntersectionPoint(side, line)
     */
    protected void computeIntersectionTransition() {
        //check the four lines of transition rectangle
        Point2D[] corners = new Point2D[4];
        Rectangle2D transition_rectangle = ((TransitionFigure)getTransition().getFigure()).getTransitionRectangle();

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
        //iterate all sides of transition and check for intersection, until one is found
        for (int i = 0; i < 4; i++) {
            Line2D.Double side = new Line2D.Double(corners[i], corners[(i+1)%4]);
            if (side.intersectsLine(line)) {
                intersection_transition = getIntersectionPoint(side, line);

                break;
            }
        }
    }


    public void draw(Graphics2D g) {

        if (!getArc().isTargetSet()) {
            //a traget is not yet set, follow the mouse with a red line!!
            Arc arc = getArc();
            Point2D source_position = arc.getSource().getPosition();
            Point2D target_position = CanvasController.getCurrentMousePoint();
            CanvasController.arc_no_target_id = this.getId();

            line = new Line2D.Double(source_position, target_position);
            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(23, 0, 0));
            g.setColor(Color.RED);
            g.draw(line);

        } else {
            //this is an arc, that connects two elements.
            //determine where to draw arrow head(s)
            Transition transition = getTransition();
            Place place = getPlace();

            //unlikely case that place and transition share the same position. do nothing.
            if (transition.getPosition().equals(place.getPosition())) return;

            line = new Line2D.Double(transition.getPosition(), place.getPosition());

            //first draw the full line between mid points
            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(123, 123, 123));
            g.draw(line);

            computeGradientTriangle();
            computeIntersectionPlace();
            computeIntersectionTransition();

            if (intersection_transition == null) return;

            //then draw the second line (shortened by intersections)
            Line2D shortened_line = new Line2D.Double(intersection_transition, intersection_place);

            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(new Color(23, 0, 0));
            g.setColor(Color.BLACK);
            g.draw(shortened_line);

            //DRAW ARROW_HEAD(S)
            Double BASE_POINT_DIAMETER = 5.7*ARROW_RADIUS;
            if (this.getTargetType() == Arc.TARGET_TRANSITION || this.getTargetType() == Arc.TARGET_BOTH) {
                // TRANSITION is target
                ArrowHead arrowHead = new ArrowHead(Arc.TARGET_TRANSITION);
                arrow_heads[0] = arrowHead;
                arrowHead.draw(g);
            } else if (intersection_transition != null) {

                Rectangle2D point = new Rectangle2D.Double(
                    intersection_transition.getX() - BASE_POINT_DIAMETER / 12,
                    intersection_transition.getY() - BASE_POINT_DIAMETER / 12,
                    BASE_POINT_DIAMETER/6,
                    BASE_POINT_DIAMETER/6
                );
                g.setColor(new Color(0,0,0,200));
                g.fill(point);
            }

            if (this.getTargetType() == Arc.TARGET_PLACE || this.getTargetType() == Arc.TARGET_BOTH) {
                //PLACE is target
                ArrowHead arrowHead = new ArrowHead(Arc.TARGET_PLACE);
                arrow_heads[1] = arrowHead;
                arrowHead.draw(g);
            } else {
                Rectangle2D point = new Rectangle2D.Double(
                    intersection_place.getX() - BASE_POINT_DIAMETER / 12,
                    intersection_place.getY() - BASE_POINT_DIAMETER / 12,
                    BASE_POINT_DIAMETER/6,
                    BASE_POINT_DIAMETER/6
                );
                g.setColor(new Color(0,0,0,200));
                g.fill(point);
            }
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

    //determines, if negative or positive for x/y direction
    public Point getFactor_X_Y(boolean target_is_left, boolean target_is_up) {
        Point factor_x_y;

        int factor_x = target_is_left ? 1 : -1;
        int factor_y = target_is_up ? 1 : -1;

        factor_x_y = new Point(factor_x, factor_y);

        return factor_x_y;
    }

    /**
     * the arrowHead Figure
     */
    public class ArrowHead {
        final int TARGET_TYPE;

        Shape rotatedArrowShape;
        Point2D target_position, source_position;
        Point2D intersection_target;
        //rotation_angle by which to rotate the arrow
        Double rotation_angle;
        Double arrow_move_x, arrow_move_y;
        Double offset_x, offset_y;
        Point factor_x_y;
        boolean target_is_left, target_is_up;

        ArrowHead(int type) {

            this.TARGET_TYPE = type;

            if (TARGET_TYPE == Arc.TARGET_TRANSITION) {
                intersection_target = intersection_transition;

                target_position = (Point2D)getTransition().getPosition().clone();
                source_position = getPlace().getPosition();
                target_is_left = !place_is_left;
                target_is_up = !place_is_up;
            }

            if (TARGET_TYPE == Arc.TARGET_PLACE) {
                intersection_target = intersection_place;

                target_position = (Point2D)getTransition().getPosition().clone();
                source_position = getPlace().getPosition();
                target_is_left = place_is_left;
                target_is_up = place_is_up;
            }

            rotation_angle = getRotationAngle(target_is_left, alpha);
            factor_x_y = getFactor_X_Y(target_is_left, target_is_up);

            //due to rotation ? the rectangle around the arrow needs repositioning, so that the arrow will always touch the intersection point
            arrow_move_x = (ARROW_RADIUS/c) * a * factor_x_y.getX();
            arrow_move_y = (ARROW_RADIUS/c) * b * factor_x_y.getY();

            //compute the actual offset of arrow to (0,0)
            offset_x = intersection_target.getX()-ARROW_RADIUS+arrow_move_x;
            offset_y = intersection_target.getY()-ARROW_RADIUS+arrow_move_y;
        }

        public boolean contains(Point2D position) {
            Point2D modified_position = new Point2D.Double(position.getX() - offset_x, position.getY() - offset_y);
            if (rotatedArrowShape.contains(modified_position)) return true;
            return false;
        }

        //draw the arrowHead
        public void draw(Graphics2D g) {


            Path2D path = new Path2D.Double();
            path.moveTo(ARROW_RADIUS, 0);
            path.lineTo(2*ARROW_RADIUS, 2*ARROW_RADIUS);
            path.lineTo(0, 2*ARROW_RADIUS);
            path.closePath();

            Rectangle2D bounds = path.getBounds2D();

            AffineTransform transform = new AffineTransform();
            transform.rotate(Math.toRadians(rotation_angle), bounds.getX() + bounds.getWidth() / 2, bounds.getY() + bounds.getHeight() / 2);

            rotatedArrowShape = path.createTransformedShape( transform );
            g.setColor(Color.BLACK);
            g.translate(offset_x, offset_y);
            g.fill(rotatedArrowShape);

            // bounds = rotated.getBounds2D();
            // g.draw(bounds);

            g.translate(-offset_x, -offset_y);

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

    }




    ///////////////
    //POPUP    ////

    public void showPopup(Point2D position) {
        JPopupMenu contextMenu = this.getPopup(this.getId(), this.selectedArrowHeadsTargetType);
        Double position_x = position.getX();
        Double position_y = position.getY();
        contextMenu.show(MainWindowController.main_window, position_x.intValue(), position_y.intValue());
    }


    public JPopupMenu getPopup(String arc_id, int target_type) {
        JPopupMenu arcPopupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(new DeleteArcMenuAction(this.getElement(), target_type));
        menuItem.setText("Delete Arrow Head");
        arcPopupMenu.add(menuItem);
        arcPopupMenu.addSeparator();

        return arcPopupMenu;
    }

    /**
     * Popup menu action that deletes an arc target (and eventually an arc, if it was the last target).
     */
    protected class DeleteArcMenuAction extends DeletePetriNetElementAction {
        int target_type;

        public DeleteArcMenuAction(PetriNetElement elem, int target_type) {
            this.element = elem;
            this.target_type = target_type;
        }

        public void actionPerformed(ActionEvent e) {
            ((Arc)element).removeTarget(target_type);
            CanvasController.repaintCanvas();
        }

    }



}
