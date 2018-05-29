package alban.gps;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import lambert.Lambert;
import lambert.LambertPoint;
import lambert.LambertZone;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    /** Google maps object, used for the display and modification */
    private GoogleMap gMap;

    private UiSettings uiSettings;

    /** */
    private LambertPoint a = Lambert.convertToWGS84Deg(887990,2324046, LambertZone.LambertIIExtended);
    private LambertPoint b = Lambert.convertToWGS84Deg(971518,2272510, LambertZone.LambertIIExtended);

    /** Coordinates of Belfort */
    private LatLngBounds BELFORT = new LatLngBounds(new LatLng(b.getY(),a.getX()),new LatLng(a.getY(),b.getX()));

    /** Layout with button, edittext and coordinates */
    private RelativeLayout relativeLayout;

    /** Address of the start and the end of the path */
    private Spinner spinnerFrom,spinnerTo;

    private ArrayAdapter<String> adaptSpinnerFrom,adaptSpinnerTo;

    /** Coordinates of the start and the of the path */
    private TextView coordoFrom,coordoTo;

    /** Boolean check if the relativeLayout is in the bottom of the screen */
    private boolean inBottom = true;

    int sizeY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get all widget in the XML file */
        relativeLayout = (RelativeLayout)findViewById(R.id.relative);
        spinnerFrom = (Spinner)findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner)findViewById(R.id.spinnerTo);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        sizeY = size.y;

        /* Move down the layout and set the visibility on true*/
        relativeLayout.animate().translationY(sizeY  - sizeY/11/*- 200*/);
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

        /* Cree un adaptateur pour le spinner avec la liste de mots coché */
        ArrayList<String> roadCity = new ArrayList<>();
        roadCity.add("Test");
        roadCity.add("Trois");
        adaptSpinnerFrom = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,roadCity);
        adaptSpinnerFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adaptSpinnerFrom);

        adaptSpinnerTo = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,roadCity);
        adaptSpinnerTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adaptSpinnerTo);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        uiSettings = gMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        /* Set the camera on the coordinates of Belfort */
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BELFORT.getCenter(),10));
        gMap.setPadding(0,0,0,500);
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
     * Method call when the user click on the button Show Datails
     * @param view
     */
    public void showDetails(View view){
        /* Create a pop-up with all the information for the path */
        /* TODO use data from the Dijkstra */
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("Direction Nord-Est sur rue Quand Même" +
                        "\n\nPrendre à gauche sur rue de la Prospérité" +
                        "\n\nPrendre à gauche sur rue de la 1ere Armée" +
                        "\n\nPrendre à gauche sur avenue des Sciences" +
                        "\n\nContinuer sur avenue du Maréchal Juin" +
                        "\n\nPrendre à gauche sur Rue Ernest Thierry Mieg").show();
    }

    /**
     * Method call when the user click on the button run
     * @param view
     */
    public void run(View view){
        /* Create line between two points on the map */
        /* Todo call Dijkstra and draw the path */
       /*
        Dijkstra();
        ArrayList<Point> listToDraw = new ArrayList<>();
        for(int i = 0; i < listToDraw.size();i++){
            Polyline line = gMap.addPolyline(
                    new PolylineOptions()
                            .add(new LatLng(listToDraw.get(i).getX(),listToDraw.get(i).getY())
                                    ,new LatLng(listToDraw.get(i+1).getX(),listToDraw.get(i+1).getY()))
                            .width(15)
                            .color(Color.RED)
            );
        }*/
        Polyline line = gMap.addPolyline(
                new PolylineOptions()
                        .add(new LatLng(47.653712,6.848094),new LatLng(47.654048,6.848703))
                        .width(15)
                        .color(Color.RED)
        );
    }

    public void onBackPressed(){
        relativeLayout.animate().translationY(sizeY - 200);
    }

}
