package com.scitech.codegram;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;


public class profile_show extends Fragment {
    // the fragment initialization parameters, e.g.
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public profile_show() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static profile_show newInstance(String param1, String param2) {
        profile_show fragment = new profile_show();
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

    CircularImageView profilePic;
    ImageButton edit;
    TextView username, csRating, followers, following, status, email, mobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_show, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profilePic = getView().findViewById(R.id.profile_pic);
        username = (TextView)getView().findViewById(R.id.username);
        csRating = (TextView)getView().findViewById(R.id.cs_rating);
        followers = (TextView)getView().findViewById(R.id.followers);
        following = (TextView)getView().findViewById(R.id.following);
        email = (TextView)getView().findViewById(R.id.email);
        status = (TextView)getView().findViewById(R.id.status);
        mobile = (TextView)getView().findViewById(R.id.mobile);
        edit = getView().findViewById(R.id.edit_profile);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), edit_profile.class).putExtra("Intent", "Profile"));
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("CodeStalk_users/"+ user.getUid());

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Codegram_user cUser = dataSnapshot.getValue(Codegram_user.class);

                username.setText(cUser.getUsername());
                csRating.setText(cUser.getRating());
                followers.setText(cUser.getFollowers());
                following.setText(cUser.getFollowing());
                email.setText(cUser.getEmail());
                status.setText(cUser.getStatus());
                mobile.setText(cUser.getMobile());

                Glide.with(getContext()).load(cUser.getPicUrl()).into(profilePic);
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
