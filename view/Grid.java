package view;

import java.awt.*;
import java.awt.geom.*;


public class Grid {

    //the reference point from which drawing of Grid will be started
    //is imported to keep the illusion of non shifting viewport on canvas resizing
    //should always be 0 or negative
    static Point2D gridOriginReferencePoint = new Point2D.Double(.0,.0);

    private int width;
    private int height;
    public static int cellSize = 50;
    private Color strongColor = new Color(215, 215, 215);
    private Color weakColor = new Color(190, 190, 190);
    private GeneralPath backgroundGrid;
    private GeneralPath foregroundGrid;


    public Grid(int width, int height) {
        // setReferencePoint();
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


    public GeneralPath generateGrid(int numCells) {
        GeneralPath grid = new GeneralPath();

        float off_x = (float)Grid.gridOriginReferencePoint.getX();
        float off_y = (float)Grid.gridOriginReferencePoint.getY();

        for (float i = 0-(cellSize + (off_x%cellSize)); i <= width; i += cellSize / numCells) {
            grid.moveTo(i, 2);
            grid.lineTo(i, height);
        }

        for (float i = 0-(cellSize + (off_y%cellSize)); i <= height; i += cellSize / numCells) {
            grid.moveTo(2, i);
            grid.lineTo(width, i);
        }

        return grid;
    }


    public void drawGrid(Graphics2D g2) {
        if (backgroundGrid == null) {
            backgroundGrid = generateGrid(5);
        }
        g2.setPaint(strongColor);
        g2.draw(backgroundGrid);

        if (foregroundGrid == null) {
            foregroundGrid = generateGrid(1);
        }
        g2.setPaint(weakColor);
        g2.draw(foregroundGrid);
    }
}
