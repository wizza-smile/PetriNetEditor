package view;

import java.awt.*;
import java.awt.geom.*;


/**
 * the grid that is laid out over the canvas
 */
public class Grid {

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
