
package view.figures;

import model.*;
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
    private LabelFigure labelFigure;

    final public static double DIAMETER_BASE = 50;
    public static double DIAMETER = DIAMETER_BASE;


    public PlaceFigure(Place place) {
        this.setId(place.getId());
        this.element = (PetriNetElement)place;
        register();

        labelFigure = new LabelFigure(this, this.getPlace().getPosition());
    }

    public void register() {
        CanvasController.addFigure(this, CanvasController.FIGURE_PLACE);
    }

    public void delete() {
        getLabelFigure().delete();
        CanvasController.removeFigure(this.getId(), CanvasController.FIGURE_PLACE);
    }

    public int getFigureType() {
        return CanvasController.FIGURE_PLACE;
    }

    public Place getPlace() {
        return (Place)this.getElement();
    }

    public LabelFigure getLabelFigure() {
        return labelFigure;
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
        setEllipse(generateEllipse());

        drawFill(g);
        drawBorder(g);

        drawLabel(g);

        Integer tokenCount = this.getPlace().getTokenCount();
        if (tokenCount == 1 && !isSelected()) {
            this.tokenPoint = generateTokenPoint();
            drawToken(g);
        } else if (tokenCount > 1 && !isSelected()) {
            Double fontSizeD = 14 * (GlobalController.size+0.3);
            int fontSize = fontSizeD.intValue();
            Font font = new Font(null, java.awt.Font.BOLD, fontSize);
            g.setFont(font);
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            TextLayout textLayout = new TextLayout(tokenCount.toString(), font, fontRenderContext);

            Rectangle2D textBounds = textLayout.getPixelBounds(fontRenderContext,0,0);

            g.setStroke(new java.awt.BasicStroke(1f));
            g.setPaint(strokeColor);
            g.drawString(tokenCount.toString(),
                (float) (getPlace().getPosition().getX() - textBounds.getWidth()/1.7),
                (float) (getPlace().getPosition().getY() + (fontSize*3/8))
            );
        }
    }

    public void drawFill(Graphics2D g) {
        if (selected) {
            g.setPaint(GlobalController.opacity ? selectedColor : selectedColorAlpha);
        } else {
            g.setPaint(GlobalController.opacity ? fillColor : fillColorAlpha);
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

        g.setPaint(strokeColor);
        g.draw(ellipse);
    }

    public void drawToken(Graphics2D g) {
        g.setPaint(new Color(0, 0, 0));
        g.fill(tokenPoint);
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

    public Rectangle2D getBounds() {
        return this.ellipse.getBounds();
    }


    ///////////////
    //POPUP    ////

    public void showPopup(Point2D position) {
        JPopupMenu contextMenu = this.getPopup();
        Double position_x = position.getX();
        Double position_y = position.getY();
        contextMenu.show(MainWindowController.main_window, position_x.intValue(), position_y.intValue());
    }

    public JPopupMenu getPopup() {
        final String place_id = this.getId();
        JPopupMenu placePopupMenu = new JPopupMenu();

        //menu point "set token"
        JMenuItem menuItemToken = new JMenuItem();
        menuItemToken.setText("set TokenCount");
        menuItemToken.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String input = JOptionPane.showInputDialog("Enter tokenCount:");
                if (input != null) {
                    Place place = (Place)PetriNetController.getElementById(place_id);
                    place.setTokenCount(Integer.parseInt(input));
                }
                CanvasController.repaintCanvas();
            }
        });
        placePopupMenu.add(menuItemToken);

        //menu point "add label"
        JMenuItem menuItemAddLabel = new JMenuItem(new AddLabelToConnectableAction(this.getElement()));
        if (!this.getElement().hasLabel()) {
            menuItemAddLabel.setText("Add Label");
            placePopupMenu.add(menuItemAddLabel);
        }

        placePopupMenu.addSeparator();

        //menu point "delete place"
        JMenuItem menuItemDelete = new JMenuItem(new DeletePetriNetElementAction(this.getElement()));
        menuItemDelete.setText("Delete Place");
        placePopupMenu.add(menuItemDelete);

        return placePopupMenu;
    }


}
