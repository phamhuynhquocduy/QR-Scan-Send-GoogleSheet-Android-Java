package com.example.qrcode;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DialogCustomFragment extends DialogFragment {


    EditText edtUrl, edtSheetID;
    Button btnLogin;

    OnFinishListener listener;

    public DialogCustomFragment() {
        // Required empty public constructor
    }

    public static DialogCustomFragment newInstance(String tmp,String tmp2){
        Bundle bundle = new Bundle();
        bundle.putString("url", tmp);
        bundle.putString("sheet", tmp2);


        DialogCustomFragment dialogCustomFragment = new DialogCustomFragment();
        dialogCustomFragment.setArguments(bundle);


        return dialogCustomFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout__custom_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtUrl = view.findViewById(R.id.url);
        edtSheetID = view.findViewById(R.id.sheetID);

        Bundle bundle = getArguments();
        edtUrl.setText(bundle.getString("url"));
        edtSheetID.setText(bundle.getString("sheet"));

        btnLogin = view.findViewById(R.id.button4);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = edtUrl.getText().toString();
                String sheet = edtSheetID.getText().toString();

                listener = (OnFinishListener) getActivity();

                listener.OnFinishDialog(url , sheet);

                dismiss();
                Toast.makeText(getContext(), "Đã thiết lập thành công", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT  );
    }
}
