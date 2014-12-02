package view;


import model.*;
import view.figures.*;
import controller.*;


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


import javax.swing.*;



public class Canvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    public Grid grid;
    boolean enabledGrid = true;
    Dimension minSize = new Dimension(100, 100);

    private HashMap<String, BaseFigure> figures = new HashMap<String, BaseFigure>();
    public ArrayList<String> arc_figure_ids = new ArrayList<String>();
    public ArrayList<String> label_figure_ids = new ArrayList<String>();
    public ArrayList<String> place_and_transition_figure_ids = new ArrayList<String>();

    public Canvas() {
        this.setFocusable(false);
        this.setCanvasSize(MainWindowController.getViewport().getSize());
        this.setOpaque(false);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

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

        SelectionController.drawSelectionFigure(g2);

        CanvasController.finishedCanvasPaint();
    }


    public void addFigure(String figureId, BaseFigure figure) {
        figures.put(figureId, figure);
    }

    public void removeFigure(String figureId) {
        figures.remove(figureId);
    }

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

    //get the rectangle that encloses all labels
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

    //get the rectangle that encloses all transitions and places
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


    public void addToGridReferencePoint(Point2D p) {
        Grid.addToReferencePoint(p);
    }




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
        if (!component.hasFocus()) {
            component.requestFocusInWindow();
        }
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

    //the reference point from which drawing of Grid will be started
    //it's imported to keep the illusion of non shifting viewport on canvas resizing
    //should always be 0 or negative
    static Point2D gridOriginReferencePoint = new Point2D.Double(.0,.0);

    private int width;
    private int height;
    public static float cellSize = 50f;
    private Path2D subGrid;
    private Path2D mainGrid;
    private Color subGridColor = new Color(215, 215, 215);
    private Color mainGridColor = new Color(190, 190, 190);


    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static void setReferencePoint(Point2D p) {
        Grid.gridOriginReferencePoint = p;
    }

    /**
     * adjusts the reference point from which the grid will be painted.
     * @param p - the x/y amount by which the reference point will be adjusted
     */
    public static void addToReferencePoint(Point2D p) {
        Point2D new_point = new Point2D.Double(Grid.gridOriginReferencePoint.getX()-p.getX(), Grid.gridOriginReferencePoint.getY()-p.getY());
        Grid.gridOriginReferencePoint = new_point;
    }

    /**
     * draws the grid. It consists of to components a main grid with cellSize and
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
