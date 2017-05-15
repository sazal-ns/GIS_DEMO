package com.sazal.siddiqui.gisdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.sazal.siddiqui.gisdemo.DBHelper.DBHelper;
import com.sazal.siddiqui.gisdemo.Model.Package;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOCATION_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    @BindView(R.id.nameEditText) EditText ms_NameEditText;
    @BindView(R.id.addressEditText) EditText ms_AddressEditText;
    @BindView(R.id.latEditText) EditText ms_LatEditText;
    @BindView(R.id.lngEditText) EditText ms_LngEditText;
    @BindView(R.id.saveAppCompatButton) AppCompatButton ms_SaveAppCompatButton;
    Unbinder unbinder;
    private String name, address;
    private double lat, lng, gpsLat, gpsLng;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Location location;
    private GoogleApiClient googleApiClient;
    private OnFragmentInteractionListener mListener;

    private SharedPreferences permissionStatus;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        buildGoogleApiClient();
        permissionStatus = getActivity().getSharedPreferences("permissionStatuss", Context.MODE_PRIVATE);
        return view;
    }

    protected synchronized void buildGoogleApiClient() {
        // 5
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        googleApiClient.disconnect();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.saveAppCompatButton)
    public void onViewClicked() {
        getDate();
    }

    private void getDate() {
        name = ms_NameEditText.getText().toString().trim();
        address = ms_AddressEditText.getText().toString().trim();

        try {
            lat = Double.parseDouble(ms_LatEditText.getText().toString().trim());
            lng = Double.parseDouble(ms_LngEditText.getText().toString().trim());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        saveData();
    }

    private void saveData() {
        if (!validate()) return;

        Package aPackage = new Package();
        aPackage.setName(name);
        aPackage.setLat(lat);
        aPackage.setLng(lng);
        aPackage.setAddress(address);

        DBHelper dbHelper = new DBHelper(getContext());
        long l = dbHelper.insertPackage(aPackage);

        if (l!=-1) new MaterialDialog.Builder(getContext())
                .title("Result")
                .content("Data Save")
                .show();
        else new MaterialDialog.Builder(getContext())
                .title("Result")
                .content("Error On Data Save")
                .show();
    }

    private boolean validate() {
        boolean valid = true;

        if (name.isEmpty()){
            ms_NameEditText.setError(getString(R.string.error_required));
            valid = false;
        }else ms_NameEditText.setError(null);

        if (address.isEmpty()){
            ms_AddressEditText.setError(getString(R.string.error_required));
            valid = false;
        }else ms_AddressEditText.setError(null);

        if (String.valueOf(lng).isEmpty()){
            ms_LngEditText.setError(getString(R.string.error_required));
            valid = false;
        }else ms_LngEditText.setError(null);

        if (String.valueOf(lat).isEmpty()){
            ms_LatEditText.setError(getString(R.string.error_required));
            valid = false;
        }else ms_LatEditText.setError(null);


        return valid;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (location == null) {

            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need to access Location Permission");
                builder.setMessage("To search cleaner nearest you location, this app needs call phone permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                LOCATION_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }else {
                loc();
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.ACCESS_COARSE_LOCATION,true);
            editor.apply();
        }else loc();
    }

    private void loc() {
        lat = location.getLatitude();
        lng = location.getLongitude();

        ms_LatEditText.setText(String.valueOf(lat));
        ms_LngEditText.setText(String.valueOf(lng));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                loc();
            }
        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        loc();
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CONSTANT){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                loc();
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need to access Location Permission");
                    builder.setMessage("To search cleaner nearest you location, this app needs call phone permission.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                                    LOCATION_PERMISSION_CONSTANT);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }else Toast.makeText(getContext(), "Sorry", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
