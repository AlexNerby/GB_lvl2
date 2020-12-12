package com.example.appweathergb.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.appweathergb.Constants;
import com.example.appweathergb.R;
import com.example.appweathergb.observers.MyDialogFragment;
import com.example.appweathergb.singleton.SimpleSingleton;

public class CitySelectionSuccessDialog extends DialogFragment implements MyDialogFragment, Constants {


    private TextView titleDialog;
    private TextView bodyTextDialog;
    private ImageView imageIcon;
    private ImageButton ok;
    private ImageButton cancel;

    private OnDialogListenerCitySelect dialogListenerCitySelect;
    private SimpleSingleton singleton;

    public void setDialogListenerCitySelect(OnDialogListenerCitySelect dialogListenerCitySelect) {
        this.dialogListenerCitySelect = dialogListenerCitySelect;

    }

    public static CitySelectionSuccessDialog newInstance() {
        return new CitySelectionSuccessDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_succes_dialog, container, false);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        singleton = SimpleSingleton.getInstance();

        setCancelable(false);

        titleDialog = view.findViewById(R.id.textTitleSuccessDialog);
        bodyTextDialog = view.findViewById(R.id.textMessageDialog);
        imageIcon = view.findViewById(R.id.imageIconDialog);
        ok = view.findViewById(R.id.buttonCitySelectSuccessDialogYes);
        cancel = view.findViewById(R.id.buttonCitySelectSuccessDialogNo);


        titleDialog.setText(String.format(getString(R.string.title_succes_city_dialog), singleton.getCity()));
        bodyTextDialog.setText(String.format(getString(R.string.body_success_sity_dialog), singleton.getCity()));


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                if (dialogListenerCitySelect != null) dialogListenerCitySelect.onDialogOk();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                if (dialogListenerCitySelect != null) dialogListenerCitySelect.onDialogNo();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        final Drawable drawable = new ColorDrawable(transparent);
        drawable.setAlpha(alpha0);

        dialog.getWindow().setBackgroundDrawable(drawable);
        dialog.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);

        return dialog;
    }


    @Override
    public void updateText(String text) {

    }
}
