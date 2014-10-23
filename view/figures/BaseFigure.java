package view.figures;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class BaseFigure {

    protected String elementId;
    protected Point2D position;
    protected Color strokeColor = new Color(0, 0, 0);

    public void draw(Graphics2D g) {}

    public String getElementId(){
        return elementId;
    }

    public Point2D getPosition(){

        return position;
    }

}

