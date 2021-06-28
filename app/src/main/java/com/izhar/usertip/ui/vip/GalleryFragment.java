package com.izhar.usertip.ui.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.izhar.usertip.MainActivity;
import com.izhar.usertip.R;
import com.izhar.usertip.auth.Login;

public class GalleryFragment extends Fragment {

    FirebaseAuth auth;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null)
            getContext().startActivity(new Intent(getContext(), Login.class));
        else
            getContext().startActivity(new Intent(getContext(), MainActivity.class));

        getActivity().finish();
        getFragmentManager().popBackStack();
        return root;
    }
}