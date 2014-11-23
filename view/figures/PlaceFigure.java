
package view.figures;

import model.*;
import view.Grid;
import controller.*;

import java.lang.Math;



import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

import javax.swing.*;


public class PlaceFigure extends Positionable {


    private Ellipse2D ellipse;
    private Ellipse2D tokenPoint;
    private String labelFigureId;



    final public static double DIAMETER_BASE = Grid.cellSize/1;
    public static double DIAMETER = DIAMETER_BASE;
    //protected TokenSetFigure tokenFigure;

    public PlaceFigure(Place place) {
        this.setId(place.getId());
        this.element = (PetriNetElement)place;
        register();

        setEllipse(generateEllipse());

        LabelFigure labelFigure = new LabelFigure(this, this.getPlace().getPosition());
        this.labelFigureId = labelFigure.getId();
        CanvasController.addLabelFigure(labelFigureId, labelFigure);
    }

    public void register() {
        CanvasController.addFigure(this, CanvasController.FIGURE_PLACE);
    }

    public void delete() {
        CanvasController.removeLabelFigure(this.labelFigureId);
        CanvasController.removePlaceFigure(this.getId());
    }


    public void showPopup(MouseEvent e) {
        MainWindowController.showPlacePopupMenu(e, this.getId());
    }


    public Place getPlace() {
        return (Place)this.getElement();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean contains(Point2D position) {
        return this.ellipse.contains(position);
    }

    public boolean intersects(Rectangle2D rect) {
        return this.ellipse.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }


    public void drawLabel(Graphics2D g) {
        this.getLabelFigure().draw(g);
    }



    public void draw(Graphics2D g) {

        // regenrate Ellipse token
        setEllipse(generateEllipse());

        drawFill(g);
        drawBorder(g);

        drawLabel(g);


        Integer tokenCount = this.getPlace().getTokenCount();
        if (tokenCount == 1 && !isSelected()) {
            this.tokenPoint = generateTokenPoint();
            drawToken(g);
        } else if (tokenCount > 1 && !isSelected()) {
            int fontSize = 14;
            Font font = new Font(null, java.awt.Font.BOLD, fontSize);
            g.setFont(font);
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            TextLayout textLayout = new TextLayout(tokenCount.toString(), font, fontRenderContext);

            Rectangle2D textBounds = textLayout.getPixelBounds(fontRenderContext,0,0);

            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(strokeColor);
            g.drawString(tokenCount.toString(),
                (float) (getPlace().getPosition().getX() - textBounds.getWidth()/2),
                (float) (getPlace().getPosition().getY() + (fontSize*3/8))
            );
        }

    }


    public void drawFill(Graphics2D g) {
        if (selected) {
            g.setPaint(selectedColor);
        } else {
            g.setPaint(fillColor);
        }
        g.fill(ellipse);
    }


    public void drawBorder(Graphics2D g) {

        if (selected) {
            float dash1[] = {5f, 3f};
            g.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1f, dash1, 0f));
        } else {
            g.setStroke(new BasicStroke(2f));
        }

        if (highlighted) {
            g.setPaint(highlightedColor);
        } else {
            g.setPaint(strokeColor);
        }
        g.draw(ellipse);
    }





    public void drawToken(Graphics2D g) {
        g.setPaint(new Color(0, 0, 0));
        g.fill(tokenPoint);

        // System.out.println("PAINT TOKEN!");
    }




    public Ellipse2D generateTokenPoint() {
        return new Ellipse2D.Double(
            getPlace().getPosition().getX() - DIAMETER / 12,
            getPlace().getPosition().getY() - DIAMETER / 12,
            DIAMETER/6,
            DIAMETER/6
        );
    }


    public Ellipse2D generateEllipse() {
        return new Ellipse2D.Double(
            getPlace().getPosition().getX() - DIAMETER / 2,
            getPlace().getPosition().getY() - DIAMETER / 2,
            DIAMETER,
            DIAMETER
        );
    }


    public void updatePosition() {
        getLabelFigure().updatePosition();
    }



    public void setEllipse(Ellipse2D e) {
        this.ellipse = e;
    }

    public LabelFigure getLabelFigure() {
        return (LabelFigure)CanvasController.getFigureById(this.labelFigureId);
    }

    public Rectangle2D getBounds() {
        return this.ellipse.getBounds();
    }

    public Point2D getPosition() { return getElement().getPosition(); }

    public void setPosition(Point2D position) { getElement().setPosition(position); }

    public Connectable getElement() {
        return (Connectable)super.getElement();
    }

}
