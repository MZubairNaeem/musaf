package com.example.emotiondetector.facedetector;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.INVISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotiondetector.R;
import com.example.emotiondetector.utils.compass.Compass;
import com.example.emotiondetector.utils.compass.GPSTracker;

import java.util.Locale;

public class BookmarkFragment extends Fragment {
    private Compass compass;
    private ImageView qiblatIndicator;
    private ImageView imageDial;
    private TextView tvAngle;
    private TextView tvYourLocation;

    private float currentAzimuth;
    SharedPreferences prefs;
    GPSTracker gps;
    private final int RC_Permission = 1221;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        /////////////////////////////////////////////////
        prefs = getActivity().getSharedPreferences("", MODE_PRIVATE);
        gps = new GPSTracker(getActivity());
        //////////////////////////////////////////
        qiblatIndicator = view.findViewById(R.id.qibla_indicator);
        imageDial = view.findViewById(R.id.dial);
        tvAngle = view.findViewById(R.id.angle);
        tvYourLocation = view.findViewById(R.id.your_location);

        //////////////////////////////////////////
        qiblatIndicator.setVisibility(INVISIBLE);
        qiblatIndicator.setVisibility(View.GONE);

        setupCompass();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (compass != null) {
            compass.start(getActivity());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (compass != null) {
            compass.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (compass != null) {
            compass.start(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (compass != null) {
            compass.stop();
        }
        if (gps != null) {
            gps.stopUsingGPS();
            gps = null;
        }
    }



    private void setupCompass() {
        Boolean permission_granted = GetBoolean("permission_granted");
        if (permission_granted) {
            getBearing();
        } else {
            tvAngle.setText(getResources().getString(R.string.msg_permission_not_granted_yet));
            tvYourLocation.setText(getResources().getString(R.string.msg_permission_not_granted_yet));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        RC_Permission);
            } else {
                fetch_GPS();
            }
        }


        compass = new Compass(getActivity());
        Compass.CompassListener cl = new Compass.CompassListener() {

            @Override
            public void onNewAzimuth(float azimuth) {
                // adjustArrow(azimuth);
                adjustGambarDial(azimuth);
                adjustArrowQiblat(azimuth);
            }
        };
        compass.setListener(cl);

    }


    public void adjustGambarDial(float azimuth) {

        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        imageDial.startAnimation(an);
    }

    public void adjustArrowQiblat(float azimuth) {


        float kiblat_derajat = GetFloat("kiblat_derajat");
        Animation an = new RotateAnimation(-(currentAzimuth) + kiblat_derajat, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = (azimuth);
        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);
        qiblatIndicator.startAnimation(an);
        if (kiblat_derajat > 0) {
            qiblatIndicator.setVisibility(View.VISIBLE);
        } else {
            qiblatIndicator.setVisibility(INVISIBLE);
            qiblatIndicator.setVisibility(View.GONE);
        }
    }

    @SuppressLint("MissingPermission")
    public void getBearing() {
        // Get the location manager

        float kaabaDegs = GetFloat("kiblat_derajat");
        if (kaabaDegs > 0.0001) {
            String strYourLocation;
            if (gps.getLocation() != null)
                strYourLocation = getResources().getString(R.string.your_location)
                        + " " + gps.getLocation().getLatitude() + ", " + gps.getLocation().getLongitude();
            else
                strYourLocation = getResources().getString(R.string.unable_to_get_your_location);
            tvYourLocation.setText(strYourLocation);
            String strKaabaDirection = String.format(Locale.ENGLISH, "%.0f", kaabaDegs)
                    + " " + getResources().getString(R.string.degree) + " " + getDirectionString(kaabaDegs);
            tvAngle.setText(strKaabaDirection);

            qiblatIndicator.setVisibility(View.VISIBLE);
        } else {
            fetch_GPS();
        }
    }

    private String getDirectionString(float azimuthDegrees) {
        String where = "NW";

        if (azimuthDegrees >= 350 || azimuthDegrees <= 10)
            where = "N";
        if (azimuthDegrees < 350 && azimuthDegrees > 280)
            where = "NW";
        if (azimuthDegrees <= 280 && azimuthDegrees > 260)
            where = "W";
        if (azimuthDegrees <= 260 && azimuthDegrees > 190)
            where = "SW";
        if (azimuthDegrees <= 190 && azimuthDegrees > 170)
            where = "S";
        if (azimuthDegrees <= 170 && azimuthDegrees > 100)
            where = "SE";
        if (azimuthDegrees <= 100 && azimuthDegrees > 80)
            where = "E";
        if (azimuthDegrees <= 80 && azimuthDegrees > 10)
            where = "NE";

        return where;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_Permission) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                SaveBoolean("permission_granted", true);
                tvAngle.setText(getResources().getString(R.string.msg_permission_granted));
                tvYourLocation.setText(getResources().getString(R.string.msg_permission_granted));
                qiblatIndicator.setVisibility(INVISIBLE);
                qiblatIndicator.setVisibility(View.GONE);

                fetch_GPS();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_permission_required), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void SaveBoolean(String Judul, Boolean bbb) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(Judul, bbb);
        edit.apply();
    }

    public Boolean GetBoolean(String Judul) {
        return prefs.getBoolean(Judul, false);
    }


    public void SaveFloat(String Judul, Float bbb) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(Judul, bbb);
        edit.apply();
    }

    public Float GetFloat(String Judul) {
        return prefs.getFloat(Judul, 0);
    }



    public void fetch_GPS() {
        double result;
        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            double myLat = gps.getLatitude();
            double myLng = gps.getLongitude();
            // \n is for new line
            String strYourLocation = getResources().getString(R.string.your_location)
                    + " " + myLat + ", " + myLng;
            tvYourLocation.setText(strYourLocation);
            Log.e("TAG", "GPS is on");
            if (myLat < 0.001 && myLng < 0.001) {
                qiblatIndicator.setVisibility(INVISIBLE);
                qiblatIndicator.setVisibility(View.GONE);
                tvAngle.setText(getResources().getString(R.string.location_not_ready));
                tvYourLocation.setText(getResources().getString(R.string.location_not_ready));
            } else {

                double kaabaLng = 39.826206; // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double kaabaLat = Math.toRadians(21.422487); // ka'bah Position https://www.latlong.net/place/kaaba-mecca-saudi-arabia-12639.html
                double myLatRad = Math.toRadians(myLat);
                double longDiff = Math.toRadians(kaabaLng - myLng);
                double y = Math.sin(longDiff) * Math.cos(kaabaLat);
                double x = Math.cos(myLatRad) * Math.sin(kaabaLat) - Math.sin(myLatRad) * Math.cos(kaabaLat) * Math.cos(longDiff);
                result = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
                SaveFloat("kiblat_derajat", (float) result);
                String strKaabaDirection = String.format(Locale.ENGLISH, "%.0f", (float) result)
                        + " " + getResources().getString(R.string.degree) + " " + getDirectionString((float) result);
                tvAngle.setText(strKaabaDirection);
                qiblatIndicator.setVisibility(View.VISIBLE);


            }
        } else {

            gps.showSettingsAlert();


            qiblatIndicator.setVisibility(INVISIBLE);
            qiblatIndicator.setVisibility(View.GONE);
            tvAngle.setText(getResources().getString(R.string.pls_enable_location));
            tvYourLocation.setText(getResources().getString(R.string.pls_enable_location));

        }
    }

}
