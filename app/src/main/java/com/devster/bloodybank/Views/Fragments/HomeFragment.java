package com.devster.bloodybank.Views.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devster.bloodybank.Database.SharedPreference;
import com.devster.bloodybank.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView top_name=(TextView) view.findViewById(R.id.top_name);
        final TextView name=(TextView) view.findViewById(R.id.profle_name);
        final TextView number=(TextView) view.findViewById(R.id.profile_number);
        final TextView bloodT=(TextView) view.findViewById(R.id.profle_bloodT);
        final TextView email=(TextView) view.findViewById(R.id.profle_email);
        final TextView loc=(TextView) view.findViewById(R.id.profle_location);


        SharedPreference details=SharedPreference.getInstance();
        top_name.setText(details.getUserName());
        name.setText(details.getUserName());
        number.setText(details.getNumber());
        bloodT.setText(details.getUserBloodType());
        email.setText(details.getuserEmail());
        if (details.getCountry().equals("")|| details.getCity().equals(" ")){
            loc.setVisibility(View.GONE);
        }
        else
            loc.setText(details.getCity()+", "+details.getCountry());
        return view;

    }

}
