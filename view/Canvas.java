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
        this.setOpaque(false);
        this.addMouseListener(this);
    }

    public void paintComponent(Graphics graphics) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        grid = new Grid(this.getWidth(), this.getHeight());
        if (this.enabledGrid) {
            grid.drawGrid(g2);
        }


        // Draw Net Objects Places, Transitions and Arcs
        Iterator it = controller.PetriNetController.getPetriNet().getElements().values().iterator();
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

        controller.MainWindowController.setStatusBarText("PAINT");

    }



    public void mouseClicked(MouseEvent e) {
        // main_window.setStatusBarText("MOUSE CLICKED");
    }

    public void mousePressed(MouseEvent e) {
        CanvasController.mousePressed(e);//, snapPointToGrid(e.getPoint())

        MainWindowController.setStatusBarText("MOUSE Pressed " + e.getY());
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
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



    // /** Creates new form Canvas */
    // public Canvas(view window) {
    //     initComponents();
    // }

    // /** This method is called from within the constructor to
    //  * initialize the form.
    //  * WARNING: Do NOT modify this code. The content of this method is
    //  * always regenerated by the Form Editor.
    //  */
    // @SuppressWarnings("unchecked")
    // // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    // private void initComponents() {

    //     setBackground(new java.awt.Color(255, 255, 255));

    //     javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    //     this.setLayout(layout);
    //     layout.setHorizontalGroup(
    //         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGap(0, 400, Short.MAX_VALUE)
    //     );
    //     layout.setVerticalGroup(
    //         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
    //         .addGap(0, 300, Short.MAX_VALUE)
    //     );
    // }// </editor-fold>//GEN-END:initComponents



    // /** Used for exporting image*/
    // public BufferedImage drawCanvas() {
    //     BufferedImage bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
    //     Graphics2D g2 = bufferedImage.createGraphics();
    //     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    //             RenderingHints.VALUE_ANTIALIAS_ON);
    //     g2.setColor(Color.white); // or the background color u want
    //     g2.fillRect(0, 0, this.getWidth(), this.getHeight());

    //     g2.setBackground(Color.white);
    //     // Draw Net Objects Places, Transitions and Arcs
    //     Iterator it = figures.values().iterator();
    //     while (it.hasNext()) {
    //         AbstractFigure element = (AbstractFigure) it.next();
    //         element.draw(g2);
    //     }
    //     g2.dispose();
    //     return bufferedImage;
    // }

    // /** Calls the window form to edit a PetriNet figure and object*/
    // public void showForm(NetObject netObject) throws HeadlessException {
    //     if (netObject != null) {
    //         FrmNetObject frmPlace = new FrmNetObject(JOptionPane.getFrameForComponent(this), true, netObject);
    //         repaint();
    //     }
    // }


    // /** Remove a figure from both the canvas and the PetriNet*/
    // public void removeFigure(AbstractFigure figure) {

    //     if (figure instanceof PlaceFigure) {
    //         Place place = (Place) Global.petriNet.getNetElement(figure.getElementId());
    //         Global.petriNet.getNetElement(place.getId());
    //         figures.remove(place.getId());
    //         figures.remove(place.getId() + "label");
    //         removeArcFigures(place.getId());
    //         Global.petriNet.removePlace(place);
    //     } else if (figure instanceof TransitionFigure) {
    //         Transition transition = (Transition) Global.petriNet.getNetElement(figure.getElementId());
    //         Global.petriNet.getNetElement(transition.getId());
    //         figures.remove(transition.getId());
    //         figures.remove(transition.getId() + "label");
    //         removeArcFigures(transition.getId());
    //         Global.petriNet.removeTransition(transition);
    //     } else if (figure instanceof NormalArcFigure) {
    //         Arc arc = (Arc) Global.petriNet.getNetElement(figure.getElementId());
    //         if (arc instanceof InputArc) {
    //             InputArc inputArc = (InputArc) arc;
    //             figures.remove(arc.getId());
    //             Global.petriNet.removeInputArc(inputArc);
    //         } else if (arc instanceof OutputArc) {
    //             OutputArc outputArc = (OutputArc) arc;
    //             figures.remove(outputArc.getId());
    //             Global.petriNet.removeOutputArc(outputArc);
    //         }
    //         AbstractArcFigure arcFigure = (AbstractArcFigure) figure;
    //         removePathPoints(arcFigure);
    //     } else if (figure instanceof PathPoint) {
    //         //arcId_pathpoint_x
    //         PathPoint pathPoint = (PathPoint) figure;
    //         StringTokenizer parts = new StringTokenizer(pathPoint.getElementId(), "_");
    //         String arcId = parts.nextToken();
    //         NormalArcFigure normalArc = (NormalArcFigure) figures.get(arcId);
    //         if (normalArc != null) {
    //             normalArc.removePoint(pathPoint);
    //         }
    //         figures.remove(figure.getElementId());
    //     }
    // }

    // /** Removes an arc Figure given its id*/
    // public void removeArcFigures(String id) {
    //     // conversion to array to prevent concurrent errors while removing arcFigures and PathPoints
    //     Object[] f = figures.values().toArray();
    //     for (int i = 0; i < f.length; i++) {
    //         AbstractFigure figure = (AbstractFigure) f[i];
    //         if (figure instanceof AbstractArcFigure) {
    //             AbstractArcFigure arcFigure = (AbstractArcFigure) figure;
    //             if (arcFigure.getStartConnector().getElementId().equals(id) || arcFigure.getEndConnector().getElementId().equals(id)) {
    //                 removePathPoints(arcFigure);
    //                 figures.remove(figure.getElementId());
    //             }
    //         }
    //     }

    // }

    // /** Remove the pathPoints of an arc*/
    // public void removePathPoints(AbstractArcFigure arcFigure) {
    //     Iterator it = arcFigure.getPoints().iterator();
    //     while (it.hasNext()) {
    //         PathPoint pathPoint = (PathPoint) it.next();
    //         figures.remove(pathPoint.getElementId());
    //         it.remove();
    //     }
    // }

    // /**
    //  * Search the figure located at the specified position
    //  * If one figure is over another it picks the latest added
    //  * @return an abstract Figure
    //  */
    // public AbstractFigure selectFigure(Point2D position) {
    //     Iterator it = figures.values().iterator();
    //     AbstractFigure figure = null;
    //     while (it.hasNext()) {
    //         AbstractFigure tmpFigure = (AbstractFigure) it.next();
    //         if (tmpFigure.contains(position)) {
    //             figure = tmpFigure;
    //         }
    //     }

    //     // If figure is an arc check if clicked over an arc point
    //     if (figure instanceof AbstractArcFigure) {
    //         AbstractArcFigure tmp = (AbstractArcFigure) figure;
    //         Point2D point = tmp.containsPoint(position);
    //         tmp.setSelectedPoint(point);
    //     }

    //     return figure;
    // }

    // /** Given a figure return its id*/
    // public String getFigureKey(AbstractFigure figure) {
    //     String id = "";
    //     for (Object o : figures.keySet()) {
    //         if (figures.get(o).equals(figure)) {
    //             id = (String) o;
    //         }
    //     }
    //     return id;
    // }

    // /** Returns true if there is another arc connected to same transition
    //  * and place int the same order. False otherwhise.*/
    // private boolean findDuplicatedArc(AbstractArcFigure arcFigure) {
    //     boolean found = false;
    //     Iterator it = figures.values().iterator();
    //     while (!found && it.hasNext()) {
    //         AbstractFigure figure = (AbstractFigure) it.next();
    //         if (figure instanceof AbstractArcFigure) {
    //             AbstractArcFigure tmp = (AbstractArcFigure) figure;
    //             if (tmp.getStartConnector().equals(arcFigure.getStartConnector()) &&
    //                     tmp.getEndConnector().equals(arcFigure.getEndConnector())) {
    //                 found = true;
    //             }

    //         }
    //     }
    //     return found;
    // }

    // /** Adds a new arc to the model and it's arc Figure */
    // public void addArc(Point2D position) {
    //     AbstractFigure figure = selectFigure(position);
    //     if (figure != null) {
    //         if (figure instanceof PlaceFigure || figure instanceof TransitionFigure) {
    //             if (arcFigure == null) {
    //                 //start Point
    //                 arcFigure = new NormalArcFigure();
    //                 arcFigure.addPoint(position);
    //                 arcFigure.setConnectionStart(figure);
    //             } else {
    //                 if (arcFigure.getStartConnector() instanceof PlaceFigure && figure instanceof TransitionFigure || arcFigure.getStartConnector() instanceof TransitionFigure &&
    //                         figure instanceof PlaceFigure) {
    //                     //end point
    //                     arcFigure.setConnectionEnd(figure);
    //                     arcFigure.addPoint(position);
    //                     if (!findDuplicatedArc(arcFigure)) {
    //                         addFigure(Global.NORMALARCMODE, position);
    //                     }
    //                 }
    //                 arcFigure = null;
    //             }
    //         }
    //     } else {
    //         if (arcFigure != null) {
    //             //Intermediate Point
    //             arcFigure.addPoint(position);
    //         }
    //     }
    //     repaint();
    // }

    // /** Hightlight the places linked to a transition*/
    // public void highlightPlaces(ArrayList list, String transitionId, boolean highlighted, boolean delay) {
    //     if (Simulation.COMPONENTDELAY > 0) {
    //         boolean any = false;
    //         Iterator it = list.iterator();
    //         while (it.hasNext()) {
    //             Arc arc = (Arc) it.next();
    //             if (arc.getTransition().getId().equals(transitionId)) {
    //                 any = true;
    //                 AbstractFigure abstractFigure = (AbstractFigure) figures.get(arc.getPlace().getId());
    //                 abstractFigure.setHighlighted(highlighted);
    //             }
    //         }
    //         if (any && delay) {
    //             sleepRepaint();
    //         }
    //     }
    // }

    // /** Hightlight a list of arcs */
    // public void highlightArcs(ArrayList list, String transitionId, boolean highlighted, boolean delay) {
    //     if (Simulation.COMPONENTDELAY > 0) {
    //         boolean any = false;
    //         Iterator it = list.iterator();
    //         while (it.hasNext()) {
    //             Arc arc = (Arc) it.next();
    //             if (arc.getTransition().getId().equals(transitionId)) {
    //                 any = true;
    //                 AbstractFigure abstractFigure = (AbstractFigure) figures.get(arc.getId());
    //                 abstractFigure.setHighlighted(highlighted);
    //             }
    //         }
    //         if (any && delay) {// delay only when highlighted is ON
    //             sleepRepaint();
    //         }
    //     }
    // }

    // /** Hightlight a transition given its id*/
    // public void highlightTransition(String id, boolean highlighted, boolean delay) {
    //     if (Simulation.COMPONENTDELAY > 0) {
    //         TransitionFigure transitionFigure = (TransitionFigure) figures.get(id);
    //         transitionFigure.setHighlighted(highlighted);
    //         if (delay) {// delay only when highlighted is ON
    //             sleepRepaint();
    //         }
    //     }
    // }

    // /** Adds a delay and repaint the canvas*/
    // public void sleepRepaint() {
    //     this.repaint();
    //     try {
    //         Thread.sleep(Simulation.COMPONENTDELAY);
    //     } catch (InterruptedException ex) {
    //         Logger.getLogger(Transition.class.getName()).log(Level.SEVERE, null, ex);
    //     }
    // }

    // /** Snaps a point to grid cells */
    // public Point2D snapPointToGrid(Point2D e) {
    //     int val = Grid.cellSize / 5;// number of parts per cell
    //     int x = (int) (((int) (e.getX()) / val) * val + val / 2);
    //     int y = (int) (((int) (e.getY()) / val) * val + val / 2);
    //     return new Point2D.Double(x, y);
    // }

    // public HashMap getFigures() {
    //     return figures;
    // }

    // public void setFigures(HashMap figures) {
    //     this.figures = figures;
    // }

    // /**
    //  * @return the enabledGrid
    //  */
    // public boolean isEnabledGrid() {
    //     return enabledGrid;
    // }

    // /**
    //  * @param enabledGrid the enabledGrid to set
    //  */
    // public void setEnabledGrid(boolean enabledGrid) {
    //     this.enabledGrid = enabledGrid;
    // }

    // /**
    //  * @return the selectionManager
    //  */
    // public SelectionManager getSelectionManager() {
    //     return selectionManager;
    // }
    // // Variables declaration - do not modify//GEN-BEGIN:variables
    // // End of variables declaration//GEN-END:variables
}