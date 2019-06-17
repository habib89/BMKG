package com.geosat.bmkg;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    String URL = "http://isatdatapro.skywave.com/GLGW/GWServices_v1/RestMessages.svc/get_return_messages.xml/?access_id=70000906&password=NVTOPFMU&start_utc=2017-08-07%2010:00:00&end_utc=2019-10-25%2017:25:00&";

    TextView idText;
    NodeList nodelist;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DownloadXML().execute(URL);
        idText = (TextView) findViewById(R.id.text);
    }
    private class DownloadXML extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(MainActivity.this);
            // Set progressbar title
//            pDialog.setTitle("Android Simple XML Parsing using DOM Tutorial");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }
        @Override
        protected Void doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();


                // Locate the Tag Name
                nodelist = doc.getElementsByTagName("ReturnMessage");

//                nodelist = doc.getElementsByTagName("MobileID");



            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {

            for (int temp = 0; temp < nodelist.getLength(); temp++) {
                Node nNode = nodelist.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
//                    // Set the texts into TextViews from item nodes
//                    // Get the title
//                    System.out.println("Node Name =" + eElement);

                    Log.d("Tag","MSP"+getNode("acsses_id",eElement));
//                    Log.e("Message checking "," ===> "+getNode("access_id", eElement));
//                    idText.setText(getNode("acsses_id",eElement));
                }
            }
            // Close progressbar
            pDialog.dismiss();
        }
    }
    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getChildNodes().item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }
}
