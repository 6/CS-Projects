/*
 * File:         FileHandler.java
 * Author:       Martha Witick, Peter Graham 
 * Prev Authors: Dale Skrien, Kevin Septor, Katelyn Mann
 * Class:        CS 361, Fall 2010
 * Project:      8
 * Date:	 12 November
 */
package project8;


import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Class for handling the loading and saving of composition data in XML format.
 * 
 * @author Dale Skrien
 * @author mmwitick
 * @author pagraham
 * @author Kevin Septor
 * @author Katelyn Mann
 * @author Paul Wheaton
 */
public class FileHandler {

    /** document builder to build XML */
    private DocumentBuilder docBuilder;

    /** reference to the control panel to add gestures to */
    private JControlPanel controlPanel;

    /** reference to the control panel to add gestures to */
    private CompositionModel model;

    /**
     * Constructs a file handler for loading and saving composition data.
     *
     * @param mdl CompositionModel to set as model
     *
     * @throws ParserConfigurationException when parser configured incorrectly
     */
    public FileHandler(CompositionModel mdl)
            throws ParserConfigurationException {
        this(null, mdl);
    }

    /**
     * Constructs a file handler for loading and saving composition data.
     *
     * @param ctrl JControlPanel to edit
     * @param mdl CompositionModel to set as model
     *
     * @throws ParserConfigurationException when parser configured incorrectly
     */
    public FileHandler(JControlPanel ctrl, CompositionModel mdl)
            throws ParserConfigurationException {
        controlPanel = ctrl;
        model = mdl;
        docBuilder = new DocumentBuilderFactoryImpl().newDocumentBuilder();
    }

    /**
     * Loads a given XML file and returns a list of its SoundResources.
     *
     * @param filePath location of XML file to load.
     * @return list of SoundResources
     * @throws IOException when an I/O error occurs
     * @throws SAXException when an XML parsing error is encountered
     */
    public List<SoundResource> loadFromFile(String filePath) throws
            SAXException, IOException {
        FileInputStream stream = new FileInputStream(filePath);
        Document document = docBuilder.parse(stream);

        //get all child sounds, start at the root node from the Document
        return getSoundResources(document.getFirstChild().getChildNodes());
    }

    /**
     * Loads a given XML string and returns a list of its SoundResources.
     *
     * @param xmlString of XML to parse.
     * @return list of SoundResources
     * @throws IOException when an I/O error occurs
     * @throws SAXException when an XML parsing error is encountered
     */
    public List<SoundResource> loadFromString(String xmlString) throws
        SAXException, IOException {

        // Convert the string to a Document
        StringReader reader = new StringReader( xmlString );
        InputSource inputSource = new InputSource( reader );
        Document document = docBuilder.parse(inputSource);

        //get all child sounds, start at the root node from the Document
        return getSoundResources(document.getFirstChild().getChildNodes());
    }

    /**
     * Recursively loads the sounds within a given list of nodes representing
     * sound resources. Adds user-defined gestures to the control panel if this
     * program is not being run from the command line.
     *
     * @param soundNodes a NodeList representing SoundResources to be loaded.
     * @return a list of Sounds that make up the gesture.
     */
    private List<SoundResource> getSoundResources(NodeList soundNodes) {

        List<SoundResource> soundResources = new ArrayList<SoundResource>();

        for(int i = 0; i < soundNodes.getLength(); i++) {
            Node soundNode = soundNodes.item(i);

            //ignore all nodes that are not element nodes
            if( soundNode.getNodeType() != Node.ELEMENT_NODE ) continue;

            // get the attributes of the sound resource
            NamedNodeMap soundAttr = soundNode.getAttributes();

            // get the delay and track of this sound
            int delay = Integer.parseInt(soundAttr.getNamedItem("delay"
                ).getNodeValue());
            int track = Integer.parseInt(soundAttr.getNamedItem("track"
                ).getNodeValue());

            // create or reuse the sound resource
            SoundResource resource;
            if( soundNode.getNodeName().equals("gesture") ) {

                String gestureName = soundAttr.getNamedItem(
                                "name").getNodeValue();
                if(model.isGestureNameTaken(gestureName)) {
                    // gesture is already built, so just reuse it
                    resource = model.getResourceFromGestureName(gestureName);
                }
                else {
                    // build gesture recursively
                    List<SoundResource> resourcesInGesture = getSoundResources(
                                    soundNode.getChildNodes());

                    // get the delays associated with the resources in gesture
                    List<Integer> delays = new ArrayList<Integer>();
                    for(int j=0; j<resourcesInGesture.size(); j++) {
                        delays.add(resourcesInGesture.get(j).getDelay());
                    }
                    GestureSound gestureSound = new GestureSound(
                                    resourcesInGesture, delays);
                    resource = new SoundResource(gestureName, gestureSound,
                                    delay, track);

                    // add it to the model and possibly control panel
                    if(controlPanel == null) {
                        // playing from command line, don't update JControlPanel
                        model.addUserDefinedGesture(resource);
                    }
                    else {
                        controlPanel.addUserDefinedGesture(gestureName,
                                        resource, model);
                    }
                }
            }
            else {
                // this is a ClipSound
                String soundFilename = soundAttr.getNamedItem(
                                "audio").getNodeValue();
                resource = model.getResourceFromSoundFilename(soundFilename);
            }

            // wrap the resource
            SoundResource wrappedResource = new SoundResource(resource, delay,
                            track);
            soundResources.add(wrappedResource);
        }
        return soundResources;
    }

    /**
     * Saves a given list of SoundResources to an XML file.
     *
     * @param filePath where to save the XML file to.
     * @param soundResources list of SoundResources to save.
     *
     * @throws FileNotFoundException when a file is not found
     * @throws TransformerException
     * @throws IOException when an I/O error occurs
     */
    public void saveToFile(String filePath, List<SoundResource> soundResources)
            throws TransformerException, IOException {
        String xmlString = soundResourcesToString(soundResources);

        // write XML to a file
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(xmlString);
        fileWriter.close();
    }

    /**
     * Converts a given list of SoundResources to an XML string.
     *
     * @param filePath where to save the XML file to.
     * @param soundResources list of SoundResources to save.
     * @return XML string of converted SoundResources
     *
     * @throws FileNotFoundException when a file is not found
     * @throws TransformerException
     * @throws IOException when an I/O error occurs
     */
    public String saveToString(List<SoundResource> soundResources)
            throws TransformerException, IOException {
        return soundResourcesToString(soundResources);
    }

    /**
     * Converts a given list of SoundResources to an XML string.
     *
     * @param a list of SoundResources to convert to an XML string
     * @return an XML String representation of the given SoundResources.
     *
     * @throws TransformerFactoryConfigurationError when transformer factory is
     * 		configured incorrectly
     * @throws TransformerConfigurationException when transformer is configured
     * 		incorrectly
     * @throws TransformerException when an exceptional condition occurs during
     * 		the transformation process
     */
    private String soundResourcesToString(List<SoundResource> soundResources)
        throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, TransformerException {
        // create XML document and add the root element
        Document xml = docBuilder.newDocument();
        Element composition = xml.createElement("composition");
        xml.appendChild(composition);

        // create sound elements recursively
        xml = buildXML(xml, composition, soundResources);

        // indent 4 spaces and remove XML declaration
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer xmlTransformer = transFactory.newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTransformer.setOutputProperty(
                        "{http://xml.apache.org/xslt}indent-amount", "4");

        // convert XML to String
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(xml);
        xmlTransformer.transform(source, result);
        String xmlString = writer.toString();
                return xmlString;
    }

    /**
     * Recursively adds sound elements to the given XML file.
     *
     * @param soundNode the Element to add sound elements to
     * @param soundResources the list of SoundResources to add to the XML file
     * @return Element with added sound elements.
     */
    private Document buildXML(Document xml, Element soundNode,
            List<SoundResource> soundResources) {
        for(SoundResource soundResource : soundResources) {

            // get the track and delay
            String track = Integer.toString(soundResource.getTrack());
            String delay = Integer.toString(soundResource.getDelay());

            // if sound resource is a GestureSound
            if(soundResource.getIcon() == null) {

                // get a list of this gesture's child SoundResources
                GestureSound gesture = (GestureSound)((SoundResource
                                                                )soundResource.getSound()).getSound();
                List<SoundResource> childSounds = gesture.getSounds();

                // configure and add gesture to XML
                Element gestureElement = xml.createElement("gesture");
                gestureElement.setAttribute("name", soundResource.getName());

                gestureElement.setAttribute("track", track);
                gestureElement.setAttribute("delay", delay);
                soundNode.appendChild(gestureElement);

                // recurse on the gesture's sounds
                xml = buildXML(xml, gestureElement, childSounds);
            }
            else {
                // get ClipSound properties
                String iconFilename = soundResource.getIconFilename();
                String audioFilename = soundResource.getSoundFilename();

                // configure and add clip to XML
                Element clip = xml.createElement("clip");
                clip.setAttribute("audio", audioFilename);
                clip.setAttribute("icon", iconFilename);
                clip.setAttribute("track", track);
                clip.setAttribute("delay", delay);
                soundNode.appendChild(clip);
            }
        }
        return xml;
    }
}