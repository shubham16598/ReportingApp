package hackathon.hacklocation;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    //Google API client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to clock periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    //UI elements
    private TextView lblLocation, exactLocation;
    private TextView mTimeText;
    private String mLastUpdateTime;
    private Button btnStartLocationUpdates;

    //Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; //10 sec
    private static int FASTEST_INTERVAL = 5000; //5 sec

    private boolean isLocationPermissionEnable = false;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 7777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create location request
        //create google api client

        lblLocation = (TextView) findViewById(R.id.lblLocation);
        exactLocation = (TextView) findViewById(R.id.exactLocation);
        btnStartLocationUpdates = (Button) findViewById(R.id.btnLocationUpdates);
        mTimeText = (TextView) findViewById(R.id.last_updated_time);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        // Toggling the periodic location updates
        btnStartLocationUpdates.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });

    }

    protected void onResume() {
        super.onResume();
        Log.d("[HSA]", "onResume");
        checkPlayServices();

        createLocationRequest();
        Log.d("[HSA]", " In onStart HomeScreenActivity");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        stopLocationUpdates();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            //Changing the button text
            btnStartLocationUpdates
                    .setText(getString(R.string.btn_stop_location_updates));

            mRequestingLocationUpdates = true;

            //starting location updates
            startLocationUpdates();
            Log.d(TAG, "Periodic location updates started!");
        } else {
            // Changing the button text
            btnStartLocationUpdates
                    .setText(getString(R.string.btn_start_location_updates));

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        Log.d("[HSA]", "In createLocationRequest ");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
      //  mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Method to check google play services availability on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        // Displaying the new location on UI
        try {
            displayLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() throws IOException {
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();

        mTimeText.setText("Last Updated time: " + mLastUpdateTime + "");
        lblLocation.setText(latitude + "," + longitude);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String subLocality=addresses.get(0).getSubLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        exactLocation.setText("KNOWN NAME: "+knownName+"\n"+
                "ADDRESS: "+address+"\n"+
                "POSTAL CODE: "+postalCode+"\n"+
                "SUBLOCALITY: "+subLocality+"\n"+
                "CITY: "+city+"\n"+
                "STATE: "+state+"\n"+
                "COUNTRY: "+country);

    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        Log.d("[HSA]", " In getGoogleApiClientInstance");
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.d("[HSA]", " In mGoogleApiClient: " + mGoogleApiClient);
        }
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        Log.d("[HSA]", " In startLocationUpdates");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("[HSA]", " In Android version grater than Marshmallow");
            isLocationPermissionEnable = PermissionUtility.checkLocationPermission(MainActivity.this);
            if (!isLocationPermissionEnable) {
                Log.d("[HSA]", " Location isLocationPermissionEnable: " + isLocationPermissionEnable);
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            }
        } else {
            isLocationPermissionEnable = true;
        }


        if (mGoogleApiClient.isConnected() && isLocationPermissionEnable) {
            Log.d("[HSA]", "client connected and location permission provided");
            Log.d("[HSA]", "calling command to get location");
            //noinspection MissingPermission
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationPermissionEnable=true;
                    startLocationUpdates();
                }
                break;
        }
    }


    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


}
