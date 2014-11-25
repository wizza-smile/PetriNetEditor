
package view.figures;

import model.*;
import controller.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

import javax.swing.*;


public class LabelFigure extends Positionable {


    protected Color labelStrokeColor = new Color(0, 0, 0);
    protected Color labelFillColor = new Color(245, 245, 245, 245);

    protected String labelText;
    public Point2D position;
    private Point2D offsetToLabeledFigure = new Point2D.Double(30, 50);
    // private String labeledFigureId;
    private RoundRectangle2D label_border_rect;

    public LabelFigure(BaseFigure labeledFigure, Point2D labeledFigurePosition) {
        this.elementId = labeledFigure.getId();
        this.element = labeledFigure.getElement();
        this.id = "label_" + this.elementId;
        this.position = new Point2D.Double(offsetToLabeledFigure.getX() + labeledFigurePosition.getX(), offsetToLabeledFigure.getY() + labeledFigurePosition.getY());
    }

    public void delete() {

    }


    public boolean contains(Point2D position) {
        return this.label_border_rect.contains(position);
    }

    public void updatePosition() {
        Point2D labeldFigurePosition = getLabeledFigure().getPosition();
        Point2D newPosition = new Point2D.Double(labeldFigurePosition.getX() + offsetToLabeledFigure.getX(), labeldFigurePosition.getY() + offsetToLabeledFigure.getY());
        this.setPosition(newPosition);
    }

    public boolean intersects(Rectangle2D r) {
        return false;
    }

    public void draw(Graphics2D g) {
        Double labelPadding = 6.;

        String label = getLabel();

        int fontSize = 13;
        Font font = new Font(null, java.awt.Font.PLAIN, fontSize);
        g.setFont(font);
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(label, font, fontRenderContext);

        Rectangle2D textBounds = textLayout.getPixelBounds(fontRenderContext,0,0);

        Double label_y_offset = 0.;

        label_border_rect = new RoundRectangle2D.Double(
            (Double)(getPosition().getX() - (textBounds.getWidth()+labelPadding) / 2),
            (Double)(getPosition().getY() - (textBounds.getHeight()+labelPadding) / 2 ),// - label_y_offset,
            (Double)(textBounds.getWidth()+labelPadding),
            (Double)(textBounds.getHeight()+labelPadding),
            5,
            5
        );

        g.setPaint(labelFillColor);
        g.fill(label_border_rect);

        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(labelStrokeColor);

        g.drawString(label,
            (float) (getPosition().getX() - 1 - textBounds.getWidth()/2),
            (float) (getPosition().getY() + (fontSize*3/8))//
        );

    }



    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
        this.offsetToLabeledFigure = new Point2D.Double(this.position.getX() - this.getLabeledFigure().getPosition().getX(), this.position.getY() - this.getLabeledFigure().getPosition().getY());

    }


    public Positionable getLabeledFigure() {
        return (Positionable)CanvasController.getFigureById(elementId);
    }



    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getLabeledFigureId() {
        return this.elementId;
    }

    public String getLabel() {
        return getElement().getLabel();
    }

    public void setLabel(String label) {
        getElement().setLabel(label);
    }

    public Rectangle2D getBounds() {
        return label_border_rect.getBounds();
    }

    public Connectable getElement() {
        return (Connectable)super.getElement();
    }



    ///////////////
    //POPUP    ////

    public void showPopup(MouseEvent e) {
        //Input dialog with a text field
        String input = JOptionPane.showInputDialog(e.getComponent(), "Enter a new Label:", this.getLabel());
        if (input != null) {
            this.setLabel(input);
        }
    }

}
