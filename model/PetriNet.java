package model;

import controller.*;
import view.figures.*;

import java.util.*;
import java.awt.geom.Point2D;

public class PetriNet {

    /** Figures that are painted and represent the Petri Net */
    private HashMap<String, PetriNetElement> elements = new HashMap<String, PetriNetElement>();


    //returnes a deep copy of contained elements
    public HashMap<String, PetriNetElement> getElements() {
        HashMap<String, PetriNetElement> outputElements = new HashMap<String, PetriNetElement>();

        Iterator it = elements.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();

            outputElements.put((String)pairs.getKey(), ((PetriNetElement)pairs.getValue()).cloneElement());
        }

        return outputElements;
    }


    public void addElement(Point2D position, int type) {

        if (PetriNetController.ELEMENT_PLACE == type) {
            String placeId = "ONE" + position.getX();
            Place place = new Place(placeId, new Point2D.Double(position.getX(), position.getY()));

            elements.put(placeId, place);
        }


        // elements.put(placeId + "label", place.getLabel());

        // switch (element) {
        //     case Global.PLACEMODE:
        //         // Place place = new Place();
        //         // Global.petriNet.addPlace(place);
        //         String placeId = "ONE";
        //         PlaceFigure placeFigure = new PlaceFigure(placeId, new Point2D.Double(100, 100));//place.getId(), position
        //         // figures.put(placeId, placeFigure);//place.getId()
        //         // figures.put(placeId + "label", placeFigure.getLabel());
        //         break;
        //         // case Global.TRANSITIONMODE:
        //         //     Transition transition = new Transition();
        //         //     Global.petriNet.addTransition(transition);
        //         //     TransitionFigure transitionFigure = new TransitionFigure(transition.getId(), position);
        //         //     figures.put(transition.getId(), transitionFigure);
        //         //     figures.put(transition.getId() + "label", transitionFigure.getLabel());
        //         //     break;
        //         // case Global.NORMALARCMODE:
        //         //     AbstractFigure start = arcFigure.getStartConnector();
        //         //     AbstractFigure end = arcFigure.getEndConnector();
        //         //     String id;
        //         //     if (Global.petriNet.getNetElement(start.getElementId()) instanceof Place) {
        //         //         Place p = (Place) Global.petriNet.getNetElement(start.getElementId());
        //         //         Transition t = (Transition) Global.petriNet.getNetElement(end.getElementId());
        //         //         InputArc arc = new InputArc(p, t);
        //         //         Global.petriNet.addInputArc(arc);
        //         //         id = arc.getId();
        //         //     } else {
        //         //         Place p = (Place) Global.petriNet.getNetElement(end.getElementId());
        //         //         Transition t = (Transition) Global.petriNet.getNetElement(start.getElementId());
        //         //         OutputArc arc = new OutputArc(p, t);
        //         //         Global.petriNet.addOutputArc(arc);
        //         //         id = arc.getId();
        //         //     }
        //         //     figures.put(id, arcFigure);
        //         //     arcFigure.setElementId(id);
        //         //     Iterator it = arcFigure.getPoints().iterator();
        //         //     int i = 0;
        //         //     while (it.hasNext()) {
        //         //         PathPoint pathPoint = (PathPoint) it.next();
        //         //         if (i != 0 && i != arcFigure.getPoints().size() - 1) {
        //         //             pathPoint.setElementId(arcFigure.getElementId() + "_pathpoint_" + i);
        //         //             figures.put(pathPoint.getElementId(), pathPoint);
        //         //         }
        //         //         i++;
        //         //     }
        //         //     break;
        // }
    }



}