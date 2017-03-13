package project.bsts.semut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.bsts.semut.helper.BroadcastManager;
import project.bsts.semut.helper.PermissionHelper;
import project.bsts.semut.map.osm.MapUtilities;
import project.bsts.semut.map.osm.MarkerClick;
import project.bsts.semut.services.LocationService;
import project.bsts.semut.setup.Constants;
import project.bsts.semut.utilities.CheckService;

public class MainActivity extends AppCompatActivity implements BroadcastManager.UIBroadcastListener, Marker.OnMarkerClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.maposm)
    MapView mapset;
    @BindView(R.id.markerdetail_layout)
    RelativeLayout markerDetailLayout;

    MapUtilities mapUitilities;
    Context context;

    private IMapController mapController;
    private BroadcastManager broadcastManager;
    private PermissionHelper permissionHelper;
    private String TAG = this.getClass().getSimpleName();
    private Intent locService;
    private MarkerClick markerClick;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        broadcastManager.unSubscribeToUi();
        if(CheckService.isLocationServiceRunning(context)){
            stopService(locService);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        context = this;
        mapUitilities = new MapUtilities(mapset);
        broadcastManager = new BroadcastManager(context);
        broadcastManager.subscribeToUi(this);
        locService = new Intent(context, LocationService.class);
        permissionHelper = new PermissionHelper(context);
        markerClick = new MarkerClick(context, markerDetailLayout);
        if (permissionHelper.requestFineLocation()) startService(locService);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionHelper.REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Location granted");
                    startService(locService);
                } else {
                    Log.i(TAG, "Location Rejected");
                }
                break;
        }
    }

    @Override
    public void onMessageReceived(String type, String msg) {
        Log.i(TAG, "-------------------------------------");
        Log.i(TAG, "Receive on UI : Type : "+type);
        Log.i(TAG, msg);
        switch (type){
            case Constants.BROADCAST_MY_LOCATION:
                mapUitilities.setMyLocationGeo(msg);
                if(!mapUitilities.isReady()) {
                    mapController = mapUitilities.init();
                }
                break;
            case Constants.MQ_INCOMING_TYPE_MAPVIEW:
                mapset.getOverlays().clear();
                mapUitilities.setMapObjectsMarkers(msg);
                for(int i = 0 ; i < mapset.getOverlays().size(); i++){
                    if (mapset.getOverlays().get(i) instanceof Marker) ((Marker) mapset.getOverlays().get(i)).setOnMarkerClickListener(this);
                }
                mapset.invalidate();
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        markerClick.checkMarker(marker);
        return false;
    }
}
