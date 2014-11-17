
package view.figures;

import controller.*;


import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class LabelFigure extends BaseFigure {

    protected String labelText;
    public Point2D position;
    private Point2D offsetToLabeledFigure = new Point2D.Double(50, 0);
    private String labeledFigureId;
    // private Rectangle2D rectangle;

    public LabelFigure(String labeledFigureId, Point2D labeledFigurePosition) {
        this.elementId = labeledFigureId;
        this.position = new Point2D.Double(offsetToLabeledFigure.getX() + labeledFigurePosition.getX(), offsetToLabeledFigure.getY() + labeledFigurePosition.getY());
    }


    public boolean contains(Point2D position) {
        // return this.rectangle.contains(position);
        return false;
    }

    public void updatePosition() {
        Point2D labeldFigurePosition = getLabeledFigure().getPosition();
        Point2D newPosition = new Point2D.Double(labeldFigurePosition.getX() + offsetToLabeledFigure.getX(), labeldFigurePosition.getY() + offsetToLabeledFigure.getY());
        this.setPosition(newPosition);
    }

    public boolean intersects(Rectangle2D r) {
        return false;
    };

    public void draw(Graphics2D g) {

        Double labelPadding = 6.;
        String label = "Label muss sein";

        Font font = new Font(null, java.awt.Font.BOLD, 12);
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(label, font, fontRenderContext);

        Rectangle2D textBounds = textLayout.getBounds();

        Double label_y_offset = 37.;

        Rectangle2D label_border_rect = new Rectangle2D.Double(
            getPosition().getX() - (textBounds.getWidth()+labelPadding) / 2,
            getPosition().getY() - (textBounds.getHeight()+labelPadding) / 2 - label_y_offset,// ,
            textBounds.getWidth()+labelPadding,
            textBounds.getHeight()+labelPadding);

        g.setPaint(labelFillColor);
        g.fill(label_border_rect);

        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(labelStrokeColor);

        g.drawString(label,
                (float) (getPosition().getX() - 1 - (textBounds.getWidth()) / 2),
                (float) (getPosition().getY() + textBounds.getHeight()/2 - label_y_offset)// + (textBounds.getHeight()+labelPadding/2) / 2)
        );

    }



    public Point2D getPosition() { return position; }
    public void setPosition(Point2D position) { this.position = position; }


    public BaseFigure getLabeledFigure() {
        return CanvasController.getFigureById(elementId);
    }










    public void drawStroke(Graphics2D g) {
        drawText(g);
    }

    public String getText() {
        // String lbl = Global.petriNet.getNetElement(parent.getElementId()).getLabel();
        // if(!lbl.equals(parent.getElementId())){
        //     lbl = parent.getElementId()+":"+lbl;
        // }
        return labelText;
    }

    public String getTextLabel() {
        return "QCIAO";
        // return controller.PetriNetController.petriNet.getNetElement(parent.getElementId()).getLabel();
    }

    public void drawText(Graphics2D g) {

        // g.setStroke(new java.awt.BasicStroke(1f));
        // g.setPaint(strokeColor);

        // Font font = new Font(null, java.awt.Font.BOLD, 12);
        // FontRenderContext fontRenderContext = g.getFontRenderContext();
        // TextLayout textLayout = new TextLayout(getText(),
        //         font, fontRenderContext);

        // Rectangle2D rectangle2D = textLayout.getBounds();

        // rectangle = new Rectangle2D.Double(position.getX() -
        //         rectangle2D.getWidth() / 2, position.getY() -
        //         rectangle2D.getHeight() / 2, rectangle2D.getWidth(),
        //         rectangle2D.getHeight());

        // g.drawString(getText(),
        //         (float) (position.getX() - rectangle2D.getWidth() / 2),
        //         (float) (position.getY() + rectangle2D.getHeight() / 2));

    }


}
