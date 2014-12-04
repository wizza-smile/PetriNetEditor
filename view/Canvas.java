package view;

import model.*;
import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;


/**
 * the canvas that the PetriNet is drawn upon.
 */
public class Canvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    /** the grid that is drawn on the canvas */
    public Grid grid;

    /** the minimal size of the Canvas */
    Dimension minSize = new Dimension(100, 100);

    /**
     * the HashMap that stores all the figures currently drawn on the canvas.
     * The key is the figure's id.
     */
    private HashMap<String, BaseFigure> figures = new HashMap<String, BaseFigure>();

    /** stores the ids of all ArcFigures currently drawn on Canvas. */
    public ArrayList<String> arc_figure_ids = new ArrayList<String>();
    /** stores the ids of all LabelFigures currently drawn on Canvas. */
    public ArrayList<String> label_figure_ids = new ArrayList<String>();
    /** stores the ids of all Place- and TransitionFigures currently drawn on Canvas. */
    public ArrayList<String> place_and_transition_figure_ids = new ArrayList<String>();


    public Canvas() {
        this.setFocusable(false);
        this.setCanvasSize(MainWindowController.getViewport().getSize());
        this.setOpaque(false);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }


    /**
     * draw the grid, the current configuration of the PetriNet and the selctionRectangle (if one is set).
     * @param graphics [description]
     */
    public void paintComponent(Graphics graphics) {

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        grid = new Grid(this.getWidth(), this.getHeight());
        grid.drawGrid(g2);

        //Draw all arcs first
        for (String arc_figure_id : CanvasController.getArcFiguresIds()) {
            ArcFigure arcFigure = (ArcFigure)CanvasController.getFigureById(arc_figure_id);
            arcFigure.draw(g2);
        }

        //draw places and transitions
        for (String figure_id : CanvasController.getPlacesAndTransitionFiguresIds()) {
            BaseFigure figure = CanvasController.getFigureById(figure_id);
            figure.draw(g2);
        }

        //draw labels
        for (String label_figure_id : label_figure_ids) {
            LabelFigure labelFigure = (LabelFigure)CanvasController.getFigureById(label_figure_id);
            labelFigure.draw(g2);
        }

        //draw the selection rectangle.
        SelectionController.drawSelectionFigure(g2);

        //callback function.
        CanvasController.finishedCanvasPaint();
    }

    /**
     * add a figure to the figures HashMap.
     * @param figureId the key to be used.
     * @param figure   the figure to be stored.
     */
    public void addFigure(String figureId, BaseFigure figure) {
        figures.put(figureId, figure);
    }

    /**
     * remove a figure from the figure HashMap.
     * @param figureId the key of the figure to be removed.
     */
    public void removeFigure(String figureId) {
        figures.remove(figureId);
    }

    /**
     * get a figure from the figures HashMap by the key/id.
     * @param  figureId the key of the figure to be retrieved.
     * @return  the figure.
     */
    public BaseFigure getFigureById(String figureId) {
        return figures.get(figureId);
    }

    public void setCanvasSize(Dimension dim) {
        this.setPreferredSize(dim);
    }

    public void setToMinSize() {
        this.setPreferredSize(minSize);
        revalidate();
    }

    /**
     * compute a rectangle that encloses all labels.
     * @return the rectangle.
     */
    public Rectangle2D getLabelsBounds() {
        Rectangle2D labelsBounds = null;
        boolean initialized = false;

        for (String label_figure_id : label_figure_ids) {
            LabelFigure labelFigure = (LabelFigure)getFigureById(label_figure_id);
            if (labelFigure.hasLabel()) {
                if (initialized) {
                    labelsBounds.add(labelFigure.getBounds());
                } else {
                    labelsBounds = labelFigure.getBounds();
                    initialized = true;
                }
            }
        }

        return labelsBounds;
    }

    /**
     * compute a rectangle that encloses all transitions and places.
     * @return the rectangle.
     */
    public Rectangle2D getPetriNetFiguresBounds() {
        Rectangle2D petriNetBounds = new Rectangle2D.Double(0, 0, 0, 0);
        boolean initialized = false;

        for (String figure_id : CanvasController.getPlacesAndTransitionFiguresIds()) {
            Positionable figure = (Positionable)getFigureById(figure_id);
            if (initialized) {
                petriNetBounds.add(figure.getBounds());
            } else {
                petriNetBounds = figure.getBounds();
                initialized = true;
            }
        }

        return petriNetBounds;
    }

    /**
     * shift the refernce Point from whicgh the grid will be drawn
     * by the x / y values of p
     * @param p a Point that carries the x / y values.
     */
    public void addToGridReferencePoint(Point2D p) {
        Grid.addToReferencePoint(p);
    }


    ///////////
    // override LISTENER methods (MOUSE/ MOUSE MOVE).
    public void mouseClicked(MouseEvent e) {
        CanvasController.mouseClicked(e);
    }

    public void mousePressed(MouseEvent e) {
        CanvasController.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        CanvasController.mouseReleased(e);
    }

    public void mouseEntered(MouseEvent e) {
        Component component = e.getComponent();
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        CanvasController.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        CanvasController.mouseMoved(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

}




/**
 * the grid that is laid out over the canvas
 */
class Grid {

    /**
     * the reference point from which drawing of Grid will be started.
     * it's imported to keep the illusion of non shifting viewport on canvas resizing.
     * should always be 0 or negative.
     */
    static Point2D gridOriginReferencePoint = new Point2D.Double(.0,.0);

    private int width;
    private int height;
    public static float cellSize = 50f;
    private Path2D subGrid;
    private Path2D mainGrid;
    private Color subGridColor = new Color(215, 215, 215);
    private Color mainGridColor = new Color(190, 190, 190);

    /**
     * @param  width  the width of the grid. Is equal to the width of canvas.
     * @param  height the height of the grid. Is equal to the height of canvas.
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static void setReferencePoint(Point2D p) {
        Grid.gridOriginReferencePoint = p;
    }

    /**
     * adjusts the reference point from which the grid will be painted.
     * @param p - a point that carries the x/y amount by which the reference point will be adjusted
     */
    public static void addToReferencePoint(Point2D p) {
        Point2D new_point = new Point2D.Double(Grid.gridOriginReferencePoint.getX()-p.getX(), Grid.gridOriginReferencePoint.getY()-p.getY());
        Grid.gridOriginReferencePoint = new_point;
    }

    /**
     * draws the grid. It consists of two components. A main grid with size = cellSize and
     * a finer subgrid.
     * @param g2 - the Graphics2D of Canvas
     */
    public void drawGrid(Graphics2D g2) {
        subGrid = generateGrid(5);
        g2.setPaint(subGridColor);
        g2.draw(subGrid);

        mainGrid = generateGrid(1);
        g2.setPaint(mainGridColor);
        g2.draw(mainGrid);
    }

    /**
     * generate a grid that subdivides the cellSize
     * @param  numCells - the number of subdivision in x/y of cellSize.
     * @return the grid
     */
    public Path2D generateGrid(int numCells) {
        Path2D grid = new Path2D.Float();

        float off_x = (float)Grid.gridOriginReferencePoint.getX();
        float off_y = (float)Grid.gridOriginReferencePoint.getY();

        for (float i = 0-(cellSize + (off_x%cellSize)); i <= width; i += cellSize / numCells) {
            grid.moveTo(i, 0f);
            grid.lineTo(i, (float)height);
        }

        for (float i = 0-(cellSize + (off_y%cellSize)); i <= height; i += cellSize / numCells) {
            grid.moveTo(0f, i);
            grid.lineTo((float)width, i);
        }

        return grid;
    }

}
