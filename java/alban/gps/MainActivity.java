package alban.gps;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import graph.Dijkstra;
import graph.NoPathException;
import graph.SetOfStreets;
import graph.XMHelper;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    /** Google maps object, used for the display and modification */
    private GoogleMap gMap;

    /** Object use for the modification of the google maps object */
    private UiSettings uiSettings;

    /** Button use for showing details */
    private Button btnDetails;

    /** Layout with button, edittext and coordinates */
    private RelativeLayout relativeLayout;

    /** Address of the start and the end of the path */
    private Spinner spinnerFrom,spinnerTo;

    /** This object contains data and put them in the Spinner */
    private ArrayAdapter<String> adaptSpinnerFrom,adaptSpinnerTo;

    /** List contains Marker draw when the user select a draw */
    private ArrayList<Marker> markerDrawFrom = new ArrayList<>();
    private ArrayList<Marker> markerDrawTo = new ArrayList<>();

    /** Graph contains roads and points */
    private SetOfStreets wholegraph;

    /** Instance of Dijkstra, contains algorithm shortest path */
    private Dijkstra graphD;

    /** Boolean check if the relativeLayout is in the bottom of the screen */
    private boolean inBottom = true;

    /** Check if there is already a path drew*/
    private boolean nothingDrawFrom = true;
    private boolean nothingDrawTo = true;

    /** Number of points, use for dijkstra */
    private int nbPointTFrom = -1, nbPointTo = -1;

    /** Size of the screen in Y axis*/
    private int sizeY;

    /** List contains all line drew in the method run */
    private ArrayList<Polyline> line = new ArrayList<>();

    /** Point receive the conversion Lambert 2 extend to WGS84 */
    private double[][] corner = {{6.31946718153,47.847860155},{7.2568606268,47.347630014}};

    /** Coordinates of Belfort */
    private LatLngBounds BELFORT = new LatLngBounds(new LatLng(corner[1][1],corner[0][0]),new LatLng(corner[0][1],corner[1][0]));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get all widget in the XML file */
        relativeLayout = (RelativeLayout)findViewById(R.id.relative);
        spinnerFrom = (Spinner)findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner)findViewById(R.id.spinnerTo);
        btnDetails = (Button)findViewById(R.id.btnDetails);

        /* Get the size of the screen */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sizeY = size.y;


        /* Move down the layout and set the visibility on true*/
        relativeLayout.animate().translationY(sizeY  - sizeY/11);
        relativeLayout.setVisibility(View.VISIBLE);

        /* Get the instance of the Google Maps object with the fragment */
        /* Set the instance in the container my_container */
        MapFragment mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.my_container, mMapFragment);
        fragmentTransaction.commit();

        /* When the map is ready call the method onMapReady on this activity */
        mMapFragment.getMapAsync(MainActivity.this);

        try {
            // Call the XML util, he will read the XML and create the graph
            XMHelper utils = new XMHelper();
            wholegraph = utils.parse(getResources().openRawResource(R.raw.region_belfort_streets));

            // Set the coordinates in WGS84
            wholegraph.setCorners(corner[0],corner[1]);
            wholegraph.pixelToCoord();

            graphD = new Dijkstra(wholegraph);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Get all roads and put them in spinners */
        ArrayList<String> roadCity = wholegraph.getRoads();

        adaptSpinnerFrom = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,roadCity);
        adaptSpinnerFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adaptSpinnerFrom);

        adaptSpinnerTo = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,roadCity);
        adaptSpinnerTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adaptSpinnerTo);

        /* Set two listeners for spinners */
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!inBottom) {
                    if (!nothingDrawFrom) {
                        undrawPath();
                        for (Marker toDelete:markerDrawFrom) {
                            toDelete.remove();
                        }
                        markerDrawFrom.clear();
                        nothingDrawFrom = true;
                    }

                    ArrayList<Integer> pts =
                            wholegraph.getRoad(spinnerFrom.getSelectedItem().toString()).getPoints();

                    for(int i = 0;i<pts.size();i++){
                        markerDrawFrom.add(gMap.addMarker(
                                new MarkerOptions().position(
                                        new LatLng(wholegraph.getPoint(pts.get(i)).getY(),
                                                wholegraph.getPoint(pts.get(i)).getX()))
                                        .title(pts.get(i).toString())
                                        .alpha((float)0.5)
                        ));
                        markerDrawFrom.get(i).setTag(0);
                    }

                    nothingDrawFrom = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!inBottom) {
                    if (!nothingDrawTo) {
                        undrawPath();
                        for (Marker toDelete:markerDrawTo) {
                            toDelete.remove();
                        }
                        markerDrawTo.clear();
                        nothingDrawTo = true;
                    }


                    ArrayList<Integer> pts =
                            wholegraph.getRoad(spinnerTo.getSelectedItem().toString()).getPoints();

                    for(int i = 0;i<pts.size();i++){
                        markerDrawTo.add(gMap.addMarker(
                                new MarkerOptions().position(
                                        new LatLng(wholegraph.getPoint(pts.get(i)).getY(),
                                                wholegraph.getPoint(pts.get(i)).getX())).
                                        icon(BitmapDescriptorFactory.
                                                defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                        .title(pts.get(i).toString())
                                        .alpha((float)0.5)
                        ));
                        markerDrawTo.get(i).setTag(1);
                    }

                    nothingDrawTo = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        uiSettings = gMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        /* Set the camera on the coordinates of Belfort */
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BELFORT.getCenter(),10));
        gMap.setPadding(0,0,0,500);


        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if((int)marker.getTag() == 0){ //From
                    for (Marker markunSelected:markerDrawFrom) {
                        markunSelected.setAlpha((float)0.5);
                    }
                    marker.setAlpha(1);
                    nbPointTFrom = Integer.parseInt(marker.getTitle());
                } else if ((int)marker.getTag() == 1){ // To
                    for (Marker markunSelected:markerDrawTo) {
                        markunSelected.setAlpha((float)0.5);
                    }
                    marker.setAlpha(1);
                    nbPointTo = Integer.parseInt(marker.getTitle());
                }
                return true;
            }
        });
    }

    /**
     * Method call when the user click on the arrow to up or down the layout
     * @param view
     */
    public void translationClick(View view){
        if(inBottom) {
            view.animate().translationY(sizeY-(view.getHeight()+sizeY/29));
            inBottom = false;
        } else {
            view.animate().translationY(sizeY - sizeY/11);
            inBottom = true;
        }
    }

    /**
     * Method call when the user click on the button Show Details
     * @param view
     */
    public void showDetails(View view){
        /* Create a pop-up with all the information for the path */

        ArrayList<String> roadsPath = graphD.pathRoads;
        StringBuilder str = new StringBuilder("Direction : \n\n");
        for (String road:roadsPath) {
            str.append("\n\nPrendre "+ road);
        }

        new AlertDialog.Builder(MainActivity.this).setMessage(str).show();
    }

    /**
     * If the user want to draw a path and there is already a path
     * This method is call for undraw the old path
     */
    private void undrawPath(){
        if(!line.isEmpty()){
            for (Polyline oneLine: line) {
                oneLine.remove();
            }
            line.clear();
        }
    }

    /**
     * Draw one path between two points define by the user
     * @param listToDraw contains points which allows to draw the path
     */
    private void drawPath(ArrayList<graph.Point> listToDraw){
        for(int i = 0; i < listToDraw.size()-1;i++) {
            // Draw one line between two points
            line.add(gMap.addPolyline(
                    new PolylineOptions()
                            .add(new LatLng(listToDraw.get(i).getY(), listToDraw.get(i).getX())
                                    , new LatLng(listToDraw.get(i + 1).getY(), listToDraw.get(i + 1).getX
                                            ()))
                            .width(15)
                            .color(Color.RED)
            ));
        }
    }

    /**
     * Method call when the user click on the button run
     * @param view
     */
    public void run(View view) {

        if(nbPointTFrom != -1 || nbPointTo != -1) {
            try {

                //Found a path with the weight given by dijkstra
                graphD.shortestPath(nbPointTFrom, nbPointTo);
                ArrayList<graph.Point> listToDraw = graphD.pathPoints;
                drawPath(listToDraw);

                // We can now look the details
                btnDetails.setEnabled(true);
            }catch (NoPathException e){
                Toast.makeText(
                        getApplicationContext(),
                        "There is no path between the two point",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    " You need to select one point on each road",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * When the user click on the arrow to go back this method is call
     */
    public void onBackPressed(){
        // Set the layout down
        relativeLayout.animate().translationY(sizeY - 200);
    }
}