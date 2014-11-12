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

    public Grid grid;
    boolean enabledGrid = true;
    Dimension minSize = new Dimension(300, 250);


    public Canvas() {
        this.setCanvasSize(MainWindowController.getViewport().getSize());
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

        //Draw all arcs first
        for (String arc_id : PetriNetController.getPetriNet().arc_ids) {
            Arc arc = (Arc)PetriNetController.getElementById(arc_id);
            arc.getFigure().draw(g2);
        }

        for (String elem_id : PetriNetController.getPetriNet().place_transition_ids) {
            PetriNetElement elem = PetriNetController.getElementById(elem_id);
            elem.getFigure().draw(g2);
        }


        SelectionController.drawSelectionFigure(g2);


        MainWindowController.setStatusBarText(MainWindowController.getViewport().getViewPosition().getX() + " " + MainWindowController.getViewport().getViewPosition().getY());

    }



    public Dimension getMinSize() {
        return minSize;
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
        CanvasController.mouseMoved(e);
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
