package graph;

/**
 * Created by Alban on 30/05/2018.
 */

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

public class XMHelper{
    private XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
    private XmlPullParser myParser = xmlFactoryObject.newPullParser();

    public XMHelper() throws XmlPullParserException {
    }

    public SetOfStreets parse(InputStream xmlFilePath) throws XmlPullParserException, IOException {

        SetOfStreets streets = new SetOfStreets();
        Road temp = new Road();

        myParser.setInput(xmlFilePath, null);

        int eventType = myParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(myParser.getName().equals("point")) {
                    double[] coord =
                            {Double.parseDouble(myParser.getAttributeValue(null,"x")),
                                    Double.parseDouble(myParser.getAttributeValue(null,"y"))};
                    streets.addPoint(new Point(Integer.parseInt(myParser.getAttributeValue(null,"num")),coord));
                } else if(myParser.getName().equals("rue")) {
                    Road rd = new Road();
                    rd.setName(myParser.getAttributeValue(null,"nom"));
                    temp = rd;
                    streets.addRoad(rd);
                } else if(myParser.getName().equals("pt")){
                   streets.getRoad(temp.getName()).addPoint(
                            Integer.parseInt(myParser.getAttributeValue(null,"num"))
                   );

                   streets.getPoint(Integer.parseInt(
                            myParser.getAttributeValue(null,"num"))).addRoad(temp.getName()
                   );
                }

            }
            eventType = myParser.next();
        }

        return streets;
    }

}
