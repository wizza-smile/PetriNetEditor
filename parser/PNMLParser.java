package parser;


import model.*;
import view.*;
import view.figures.*;
import controller.*;

import java.util.*;

import java.awt.geom.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Diese Klasse implementiert die Grundlage für einen einfachen PNML Parser.
 */
public class PNMLParser {

    private ArrayList<String[]> arcs_info = new ArrayList<String[]>();

    /**
     * Mit dieser Main Methode kann der Parser zum Testen
     * aufgerufen werden. Als erster und einziger Paramter muss
     * dazu der Pfad zur PNML Datei angegeben werden.
     *
     * @param args
     *      Die Konsolen Parameter, mit denen das Programm aufgerufen wird.
     */
    public static void main(final String[] args) {
        if (args.length > 0) {
            File pnmlDatei = new File(args[0]);
            if (pnmlDatei.exists()) {
                PNMLParser pnmlParser = new PNMLParser(pnmlDatei);
                pnmlParser.initParser();
                pnmlParser.parse();
                pnmlParser.addArcs();
            } else {
                System.err.println("Die Datei " + pnmlDatei.getAbsolutePath()
                        + " wurde nicht gefunden!");
            }
        } else {
            System.out.println("Bitte eine Datei als Parameter angeben!");
        }
    }

    /**
     * Dies ist eine Referenz zum Java Datei Objekt.
     */
    private File           pnmlDatei;

    /**
     * Dies ist eine Referenz zum XML Parser. Diese Referenz wird durch die
     * Methode parse() initialisiert.
     */
    private XMLEventReader xmlParser = null;

    /**
     * Diese Variable dient als Zwischenspeicher für die ID des zuletzt gefundenen Elements.
     */
    private String         lastId    = null;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Token Elements liest.
     */
    private boolean        isToken   = false;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Name Elements liest.
     */
    private boolean        isName    = false;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Value Elements liest.
     */
    private boolean        isValue   = false;

    /**
     * Dieser Konstruktor erstellt einen neuen Parser für PNML Dateien,
     * dem die PNML Datei als Java {@link File} übergeben wird.
     *
     * @param pnml
     *      Java {@link File} Objekt der PNML Datei
     */
    public PNMLParser(final File pnml) {
        super();

        this.pnmlDatei = pnml;
    }

    /**
     * Diese Methode öffnet die PNML Datei als Eingabestrom und initialisiert den XML
     * Parser.
     */
    public final void initParser() {
        try {
            InputStream dateiEingabeStrom = new FileInputStream(pnmlDatei);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            try {
                xmlParser = factory.createXMLEventReader(dateiEingabeStrom);

            } catch (XMLStreamException e) {
                System.err
                        .println("XML Verarbeitungsfehler: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Die Datei wurde nicht gefunden! "
                    + e.getMessage());
        }
    }

    /**
     * Diese Methode liest die XML Datei und delegiert die
     * gefundenen XML Elemente an die entsprechenden Methoden.
     */
    public final void parse() {
        while (xmlParser.hasNext()) {
            try {
                XMLEvent event = xmlParser.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        handleStartEvent(event);
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        String name = event.asEndElement().getName().toString()
                                .toLowerCase();
                        if (name.equals("token")) {
                            isToken = false;
                        } else if (name.equals("name")) {
                            isName = false;
                        } else if (name.equals("value")) {
                            isValue = false;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (isValue && lastId != null) {
                            Characters ch = event.asCharacters();
                            if (!ch.isWhiteSpace()) {
                                handleValue(ch.getData());
                            }
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        //schließe den Parser
                        xmlParser.close();
                        break;
                    default:
                }
            } catch (XMLStreamException e) {
                System.err.println("Fehler beim Parsen des PNML Dokuments. "
                        + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Diese Methode behandelt den Start neuer XML Elemente, in dem der Name des
     * Elements überprüft wird und dann die Behandlung an spezielle Methoden
     * delegiert wird.
     *
     * @param event
     *            {@link XMLEvent}
     */
    private void handleStartEvent(final XMLEvent event) {
        StartElement element = event.asStartElement();
        if (element.getName().toString().toLowerCase().equals("transition")) {
            handleTransition(element);
        } else if (element.getName().toString().toLowerCase().equals("place")) {
            handlePlace(element);
        } else if (element.getName().toString().toLowerCase().equals("arc")) {
            handleArc(element);
        } else if (element.getName().toString().toLowerCase().equals("name")) {
            isName = true;
        } else if (element.getName().toString().toLowerCase()
                .equals("position")) {
            handlePosition(element);
        } else if (element.getName().toString().toLowerCase().equals("token")) {
            isToken = true;
        } else if (element.getName().toString().toLowerCase().equals("value")) {
            isValue = true;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn Text innerhalb eines Value Elements gelesen wird.
     *
     * @param value
     *      Der gelesene Text als String
     */
    private void handleValue(final String value) {
        if (isName) {
            setName(lastId, value);
        } else if (isToken) {
            setMarking(lastId, value);
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Positionselement gelesen wird.
     *
     * @param element
     *      das Positionselement
     */
    private void handlePosition(final StartElement element) {
        String x = null;
        String y = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("x")) {
                x = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("y")) {
                y = attr.getValue();
            }
        }
        if (x != null && y != null && lastId != null) {
            setPosition(lastId, x, y);
        } else {
            System.err.println("Unvollständige Position wurde verworfen!");
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Transitionselement gelesen wird.
     *
     * @param element
     *      das Transitionselement
     */
    private void handleTransition(final StartElement element) {
        String transitionId = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                transitionId = attr.getValue();
                break;
            }
        }
        if (transitionId != null) {
            newTransition(transitionId);
            lastId = transitionId;
        } else {
            System.err.println("Transition ohne id wurde verworfen!");
            lastId = null;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Stellenelement gelesen wird.
     *
     * @param element
     *      das Stellenelement
     */
    private void handlePlace(final StartElement element) {
        String placeId = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                placeId = attr.getValue();
                break;
            }
        }
        if (placeId != null) {
            newPlace(placeId);
            lastId = placeId;
        } else {
            System.err.println("Stelle ohne id wurde verworfen!");
            lastId = null;
        }
    }

    /**
     * Diese Methode wird aufgerufen, um am Schluß alle Arcs hinzuzufügen.
     */
    private void addArcs() {
        for (String[] arc_info : arcs_info) {
            newArc(arc_info[0], arc_info[1], arc_info[2]);
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Kantenelement gelesen wird.
     *
     * @param element
     *      das Kantenelement
     */
    private void handleArc(final StartElement element) {
            System.out.println( "ARC" );
        String arcId = null;
        String source = null;
        String target = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                arcId = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("source")) {
                source = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("target")) {
                target = attr.getValue();
            }
        }
        if (arcId != null && source != null && target != null) {
            System.out.println( "ARC" );
            String arc_info[] = {arcId, source, target};
            arcs_info.add(arc_info);
        } else {
            System.err.println("Unvollständige Kante wurde verworfen!");
        }
        //Die id von Kanten wird nicht gebraucht
        lastId = null;
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Transitionen zu erstellen.
     *
     * @param id
     *      Identifikationstext der Transition
     */
    public void newTransition(final String id) {
        new Transition(id);
        System.out.println("Transition mit id " + id + " wurde gefunden.");
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Stellen zu erstellen.
     *
     * @param id
     *      Identifikationstext der Stelle
     */
    public void newPlace(final String id) {
        new Place(id);
        System.out.println("Stelle mit id " + id + " wurde gefunden.");
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Kanten zu erstellen.
     *
     * @param id
     *      Identifikationstext der Kante
     * @param source
     *      Identifikationstext des Startelements der Kante
     * @param target
     *      Identifikationstext des Endelements der Kante
     */
    public void newArc(final String id, final String source, final String target) {
        new Arc(id, source, target);
        System.out.println("Kante mit id " + id + " von " + source + " nach "
                + target + " wurde gefunden.");
    }

    /**
     * Diese Methode kann überschrieben werden, um die Positionen der geladenen
     * Elemente zu aktualisieren.
     *
     * @param id
     *      Identifikationstext des Elements
     * @param x
     *      x Position des Elements
     * @param y
     *      y Position des Elements
     */
    public void setPosition(final String id, final String x, final String y) {
        Positionable figure = (Positionable)CanvasController.getFigureById(id);
        figure.setPosition(new Point2D.Double(Double.parseDouble(x), Double.parseDouble(y) ));
        System.out.println("Setze die Position des Elements " + id + " auf ("
                + x + ", " + y + ")");
    }

    /**
     * Diese Methode kann überschrieben werden, um den Beschriftungstext der geladenen
     * Elemente zu aktualisieren.
     *
     * @param id
     *      Identifikationstext des Elements
     * @param name
     *      Beschriftungstext des Elements
     */
    public void setName(final String id, final String name) {
        Connectable connectable = (Connectable)PetriNetController.getElementById(id);
        connectable.setLabel(name);
        System.out.println("Setze den Namen des Elements " + id + " auf "
                + name);
    }

    /**
     * Diese Methode kann überschrieben werden, um die Markierung der geladenen
     * Elemente zu aktualisieren.
     *
     * @param id
     *      Identifikationstext des Elements
     * @param marking
     *      Markierung des Elements
     */
    public void setMarking(final String id, final String marking) {
        Place place = (Place)PetriNetController.getElementById(id);
        place.setTokenCount(Integer.parseInt(marking));
        System.out.println("Setze die Markierung des Elements " + id + " auf "
                + marking);
    }
}
