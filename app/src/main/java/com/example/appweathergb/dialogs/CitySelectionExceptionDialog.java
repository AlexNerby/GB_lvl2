package com.example.appweathergb.dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.appweathergb.Constants;
import com.example.appweathergb.R;
import com.example.appweathergb.singleton.SimpleSingleton;

public class CitySelectionExceptionDialog  extends DialogFragment implements Constants {


    private TextView titleDialog;
    private TextView bodyTextDialog;
    private ImageView imageIcon;
    private ImageButton ok;
    private ImageButton change;

    private OnDialogListenerExceptionConnection dialogListenerExceptionConnection;
    private SimpleSingleton singleton;

    public void setDialogListenerExceptionConnection(OnDialogListenerExceptionConnection dialogListenerExceptionConnection) {
        this.dialogListenerExceptionConnection = dialogListenerExceptionConnection;

    }

    public static CitySelectionExceptionDialog newInstance() {
        return new CitySelectionExceptionDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_exception_dialog, container, false);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);

        singleton = SimpleSingleton.getInstance();

        setCancelable(false);

        titleDialog = view.findViewById(R.id.textTitleWarningDialog);
        bodyTextDialog = view.findViewById(R.id.textMessageDialogWarning);
        imageIcon = view.findViewById(R.id.imageIconDialogWarning);
        change = view.findViewById(R.id.buttonCitySelectSuccessDialogWarning);

        //TODO: переделать ниже

        titleDialog.setText(R.string.Connection_exception);
        bodyTextDialog.setText(String.format(getString(R.string.body_warning_sity_dialog), singleton.getCity()));


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                if (dialogListenerExceptionConnection != null) dialogListenerExceptionConnection.change();
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
}
