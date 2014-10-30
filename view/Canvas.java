package view;


import model.*;
import view.figures.*;
import controller.*;


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

import javax.swing.*;



public class Canvas extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private Grid grid;
    private boolean enabledGrid = true;


    public Canvas() {
        //this.setPreferredSize(new Dimension(800,800));
        this.setOpaque(false);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public void paintComponent(Graphics graphics) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        grid = new Grid(this.getWidth(), this.getHeight());
        if (this.enabledGrid) {
            grid.drawGrid(g2);
        }


        // Draw Net Objects Places, Transitions and Arcs
        Iterator it = PetriNetController.getPetriNet().getElements().values().iterator();
        while (it.hasNext()) {

            ((PetriNetElement)it.next()).getFigure().draw(g2);

      /*      if (it.next() instanceof Place) {
                System.out.println("ITS A PLACE");
            }*/

            // switch ( )

            // AbstractFigure element = (AbstractFigure) Class.forName(it.next().classname + "Figure").newInstance();
            // element.draw(g2);
        }

        // if (arcFigure != null) {
        //     // arcFigure.draw(g2);
        // }

        // selectionManager.updateBounds();

        //MainWindowController.setStatusBarText("PAINT");

        SelectionController.drawSelectionFigure(g2);
        CanvasController.computeAndSetCanvasSize();
    }



    public void setCanvasSize(Dimension dim) {
        this.setPreferredSize(dim);
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
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        CanvasController.mouseDragged(e);
    }

    public void mouseMoved(MouseEvent e) {
        //main_window.setStatusBarText("MOUSE MOVED");
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }


}
