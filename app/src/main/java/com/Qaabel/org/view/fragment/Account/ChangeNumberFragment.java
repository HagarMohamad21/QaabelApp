package com.Qaabel.org.view.fragment.Account;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.Qaabel.org.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeNumberFragment extends Fragment {

   ImageView backButton;
    public ChangeNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View  view= inflater.inflate(R.layout.fragment_change_number, container, false);
        init(view);
        actions();
        return view;
    }

    private void init(View view) {
        backButton=view.findViewById(R.id.backButton);
    }

    private void actions() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

}
