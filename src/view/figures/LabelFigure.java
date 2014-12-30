package view.figures;

import model.*;
import controller.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

import javax.swing.*;


/** the labelFigure of a Connectable PetriNetElement (Place/Transition). */
public class LabelFigure extends Positionable {
    /** font size used for the label text */
    protected int fontSize = 13;

    /** font used for the label text */
    Font font = new Font(null, java.awt.Font.PLAIN, fontSize);

    /** the text of the LabelFigure */
    protected String labelText;

    /** defines the position of the LabelFigure by the offset to the labeled figure */
    private Point2D offsetToLabeledFigure = new Point2D.Double(30, 50);

    /** caches the position defined by offsetToLabeledFigure */
    public Point2D position;

    /** the rounded rectangle that contains the label */
    private RoundRectangle2D label_border_rect;


    public LabelFigure(BaseFigure labeledFigure, Point2D labeledFigurePosition) {
        this.element = labeledFigure.getElement();
        this.setId("label_" + GlobalController.getUUID());
        register();
        strokeColor = new Color(0, 0, 0);
        fillColor = new Color(245, 245, 245, 245);
        selectedColor = new Color(227, 168, 188);

        this.position = new Point2D.Double(offsetToLabeledFigure.getX() + labeledFigurePosition.getX(), offsetToLabeledFigure.getY() + labeledFigurePosition.getY());
    }

    public void register() {
        CanvasController.addFigure(this, CanvasController.FIGURE_LABEL);
    }

    public void delete() {
        CanvasController.removeFigure(this.getId(), CanvasController.FIGURE_LABEL);
    }

    public int getFigureType() {
        return CanvasController.FIGURE_LABEL;
    }

    public boolean intersects(Rectangle2D r) {
        return false;
    }

    public boolean contains(Point2D position) {
        return hasLabel() ? this.label_border_rect.contains(position) : false;
    }

    public Rectangle2D getBounds() {
        return hasLabel() ? label_border_rect.getBounds() : null;
    }

    public void updatePosition() {
        Point2D labeldFigurePosition = getLabeledFigure().getPosition();
        Point2D newPosition = new Point2D.Double(labeldFigurePosition.getX() + offsetToLabeledFigure.getX(), labeldFigurePosition.getY() + offsetToLabeledFigure.getY());
        this.setPosition(newPosition);
    }

    /**
     * checks whether the label text is empty
     * @return boolean
     */
    public boolean hasLabel() {
        return getElement().hasLabel();
    }

    public void draw(Graphics2D g) {
        Double labelPadding = 6.;
        if (hasLabel()) {
            //compute the size that the label text will have
            String label = getLabel();
            g.setFont(font);
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            TextLayout textLayout = new TextLayout(label, font, fontRenderContext);
            Rectangle2D textBounds = textLayout.getPixelBounds(fontRenderContext, 0, 0);

            //create a matching rounded rectangle
            label_border_rect = new RoundRectangle2D.Double(
                (Double)(getPosition().getX() - (textBounds.getWidth()+labelPadding) / 2),
                (Double)(getPosition().getY() - (textBounds.getHeight()+labelPadding) / 2 ),
                (Double)(textBounds.getWidth()+labelPadding),
                (Double)(textBounds.getHeight()+labelPadding),
                5,
                5
            );

            drawFill(g);
            drawBorder(g);

            g.drawString(label,
                (float) (getPosition().getX() - 1 - textBounds.getWidth()/2),
                (float) (getPosition().getY() + (fontSize*3/8))//
            );
        }
    }


    public void drawFill(Graphics2D g) {
        if (this.getLabeledFigure().isSelected()) {
            g.setPaint(selectedColor);
        } else {
            g.setPaint(fillColor);
        }
        g.fill(label_border_rect);
    }

    public void drawBorder(Graphics2D g) {
        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(strokeColor);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
        this.offsetToLabeledFigure = new Point2D.Double(this.position.getX() - this.getLabeledFigure().getPosition().getX(), this.position.getY() - this.getLabeledFigure().getPosition().getY());
    }

    /**
     * returns the labeled figure
     * @return labeled figure
     */
    public Positionable getLabeledFigure() {
        return (Positionable)getElement().getFigure();
    }

    /** set the 'selected' properties of this LabelFigure and of the labeled Figure to 'true'. */
    public void markSelected(boolean selected) {
        this.selected = selected;
        this.getLabeledFigure().markSelected(selected);
    }

    public LabelFigure getLabelFigure() {
        return this;
    }


    ///////////////
    //POPUP    ////

    public void showPopup(Point2D position) {
        //Input dialog with a text field
        String preset = this.hasLabel() ? this.getLabel() : "";
        String input = JOptionPane.showInputDialog(MainWindowController.getMainWindow(), "Enter a new Label:", preset);
        if(input != null) {
            if (!input.isEmpty()) {
                this.setLabel(input);
            } else {
                getElement().setLabel(Connectable.NO_LABEL_IDENTIFIER);
            }
        }
    }

}
