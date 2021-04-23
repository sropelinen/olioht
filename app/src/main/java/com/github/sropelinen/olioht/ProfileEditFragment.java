package com.github.sropelinen.olioht;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

public class ProfileEditFragment extends Fragment {
    private View view;
    private ImageView imageView, imageView2, imageView3;
    private TextView tvName, tvDob, tvUsername;
    private final EditText[] textFields = new EditText[5];
    private final Object[] newValues = new Object[5];
    private final Object[] oldValues = new Object[5];
    private final String[] keys = new String[]{
            "firstName", "lastName", "height", "weight", "home"
    };
    private final int[] ids = new int[]{
            R.id.et_edit_firstname, R.id.et_edit_lastname, R.id.et_edit_height,
            R.id.et_edit_weight, R.id.et_edit_home
    };

    private Button btnSave, btnGoBack, btnAddPic;
    private Profile profile;
    private HashMap<String, Object> updatedValues = new HashMap<>();


    public ProfileEditFragment(Profile profile) {
        this.profile = profile;
        for (int i = 0; i < keys.length; i++) {
            Object value = profile.getValue(keys[i]);
            newValues[i] = value;
            oldValues[i] = value;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        for (int i = 0; i < 5; i++) {
            textFields[i] = view.findViewById(ids[i]);
            if (oldValues[i] != null) textFields[i].setText(oldValues[i].toString());
        }

        btnSave = view.findViewById(R.id.btn_save_profile);
        btnGoBack = view.findViewById(R.id.btn_back_to_settings);
        btnAddPic = view.findViewById(R.id.btn_add_pic);
        tvName = view.findViewById(R.id.tv_name);
        tvDob = view.findViewById(R.id.tv_dob);
        tvUsername = view.findViewById(R.id.tv_username);
        imageView = view.findViewById(R.id.iv_profile_pic);
        imageView2 = view.findViewById(R.id.iv_profile_pic_2);
        imageView3 = view.findViewById(R.id.iv_profile_pic_settings);

        String name = profile.getValue("firstName").toString() + " " +
                profile.getValue("lastName").toString();
        tvName.setText(name);
        tvUsername.setText(profile.getValue("userName").toString());
        tvDob.setText(profile.getValue("birthDate").toString());
        setListeners();
        return view;
    }

    private void setListeners() {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            textFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (finalI == 2 || finalI == 3) {
                        if (!s.toString().equals("")) {
                            newValues[finalI] = Integer.parseInt(s.toString());
                        }
                    } else {
                        if (!s.toString().equals("")) {
                            newValues[finalI] = s.toString();
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        btnSave.setOnClickListener(v -> {
            boolean changed = false;
            for (int i = 0; i < 5; i++) {
                if (!newValues[i].equals(oldValues[i])) {
                    updatedValues.put(keys[i], newValues[i]);
                    oldValues[i] = newValues[i];
                    changed = true;
                }
            }
            if (changed) {
                profile.setValues(updatedValues);
                updatedValues.clear();
                Toast.makeText(super.getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        // set profile pic
        btnAddPic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra("crop", "true");
            intent.putExtra("scale", true);
            intent.putExtra("outputX", 256);
            intent.putExtra("outputY", 256);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 1);
        });

        // return
        btnGoBack.setOnClickListener(v -> ((MainActivity)
                Objects.requireNonNull(getActivity())).setSettingsFragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < keys.length; i++) {
            Object value = profile.getValue(keys[i]);
            if (value != null) textFields[i].setText(value.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1
                && resultCode == MainActivity.RESULT_OK) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap pic = extras.getParcelable("data");
                imageView.setImageBitmap(pic);
                imageView2.setImageBitmap(pic);
                imageView3.setImageBitmap(pic);
                // TODO save profile pic to profile (not important)
            }
        }
    }
}