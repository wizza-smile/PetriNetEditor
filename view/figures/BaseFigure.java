package view.figures;

import model.*;
import controller.*;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.Font;
import java.awt.font.*;
import java.awt.geom.*;

///SELECTABLE FIGURES   PLACE / TRANSIITONS
public abstract class BaseFigure {
    //the element the figure represents
    protected String elementId;
    protected PetriNetElement element;

    protected String label = "Label muss sein";

    protected Point2D offset;

    protected boolean selected = false;
    protected boolean highlighted = false;

    protected Color strokeColor = new Color(0, 0, 0);
    protected Color fillColor = new Color(255, 255, 255, 195);
    protected Color selectedColor = new Color(183, 55, 55, 40);
    protected Color highlightedColor = new Color(115, 230, 0);

    protected Color labelStrokeColor = new Color(0, 0, 0);
    protected Color labelFillColor = new Color(255, 255, 255, 195);


    public abstract boolean intersects(Rectangle2D r);
    public abstract boolean contains(Point2D position);
    public abstract Point2D getLowerRightCorner();
    public abstract void updatePosition();


    public void draw(Graphics2D g) {
        // PetriNetController.checkLowerRightCorner(this.getLowerRightCorner());
    }



    public void drawLabel(Graphics2D g) {

        Font font = new Font(null, java.awt.Font.BOLD, 12);
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(label,
                font, fontRenderContext);

        Rectangle2D rectangle2D = textLayout.getBounds();

        Double label_y_offset = 37.;

        Rectangle2D rectangle = new Rectangle2D.Double(
            getPosition().getX() - rectangle2D.getWidth() / 2,
            getPosition().getY() - rectangle2D.getHeight() / 2 - label_y_offset,
            rectangle2D.getWidth()/2,
            rectangle2D.getHeight());

        g.setPaint(labelFillColor);
        g.fill(rectangle);

        g.setStroke(new java.awt.BasicStroke(1f));
        g.setPaint(labelStrokeColor);

        g.drawString(label,
                (float) (getPosition().getX() - rectangle2D.getWidth() / 2),
                (float) (getPosition().getY() + rectangle2D.getHeight() / 2 - label_y_offset));





        AttributedString vanGogh = new AttributedString("If you have a paragraph of styled text that you would like to fit within a specific width, you can use the LineBreakMeasurer class.");

        AttributedCharacterIterator paragraph = vanGogh.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = g.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        // Set break width to width of Component.
        float breakWidth = (float)50;
        float drawPosY = 0;
        // Set position to the index of the first
        // character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);

        // Get lines from until the entire paragraph
        // has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {

            TextLayout layout = lineMeasurer.nextLayout(breakWidth);

            // Compute pen x position. If the paragraph
            // is right-to-left we will align the
            // TextLayouts to the right edge of the panel.
            double drawPosX = getPosition().getX() + 50.;

            // Move y-coordinate by the ascent of the
            // layout.
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX,drawPosY).
            layout.draw(g, (float)drawPosX, drawPosY+(float)getPosition().getY()-50);

            // Move y-coordinate in preparation for next
            // layout.
            drawPosY += layout.getDescent() + layout.getLeading();
        }

    }



    //getter / setter
    public Point2D getPosition() { return getElement().getPosition(); }
    public void setPosition(Point2D position) { getElement().setPosition(position); }

    public Point2D getOffset() { return offset; }

    public void setOffsetToPoint(Point2D point) {
        Double offset_x = this.getPosition().getX() - point.getX();
        Double offset_y = this.getPosition().getY() - point.getY();

        this.offset = new Point2D.Double(offset_x, offset_y);
    }


    public PetriNetElement getElement() { return element; }
    public String getId() { return element.getId(); }



}
