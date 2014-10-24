package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;


public class Grid {

    /** Grid width.*/
    private int width;
    /** Grid height.*/
    private int height;
    /** Size of the foreground cell.*/
    public static int cellSize = 50;
    /** Color of foreground cell.*/
    private Color strongColor = new Color(215, 215, 215);
    /** Color of the background cell.*/
    private Color weakColor = new Color(190, 190, 190);
    /** Background grid.*/
    private GeneralPath backgroundGrid;
    /** Foreground grid.*/
    private GeneralPath foregroundGrid;


    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public GeneralPath generateGrid(int numCells) {
        GeneralPath grid = new GeneralPath();
        for (float i = 0; i <= width; i += cellSize / numCells) {
            grid.moveTo(i, 2);
            grid.lineTo(i, height);
        }

        for (float i = 0; i <= height; i += cellSize / numCells) {
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
