package com.sazal.siddiqui.gisdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
public class AddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    private OnFragmentInteractionListener mListener;

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
        return view;
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
    public void onDetach() {
        super.onDetach();
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
        }else ms_LngEditText.setError(getString(R.string.error_required));

        if (String.valueOf(lat).isEmpty()){
            ms_LatEditText.setError(getString(R.string.error_required));
            valid = false;
        }else ms_LatEditText.setError(getString(R.string.error_required));


        return valid;
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
