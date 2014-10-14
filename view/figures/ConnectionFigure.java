/* Copyright viewllem Catala. www.viewllemcatala.com/petrinetsim. Licensed http://creativecommons.org/licenses/by-nc-sa/3.0/ */
package view.figures;

/**
 *
 * @author viewllem
 */
public interface ConnectionFigure {

    public void setConnectionStart(AbstractFigure start);

    public AbstractFigure getStartConnector();

    public void setConnectionEnd(AbstractFigure end);

    public AbstractFigure getEndConnector();
}
