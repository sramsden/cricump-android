package com.cricump.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.cricump.net.Client;

public class CancelableDialog extends ProgressDialog
{

    private Context _applicationContext;

    public CancelableDialog(Context context, Context applicationContext, String message ) {
        super(context);
        _applicationContext = applicationContext;
        setMessage(message);
        setCancelable(false);
        setIndeterminate(true);
        setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Client.cancelRequests(_applicationContext);
            }
        });
    }

    public static CancelableDialog show(Context context, Context applicationContext, String message){
        CancelableDialog progressDialog = new CancelableDialog(context, applicationContext, message);
        progressDialog.show();
        return progressDialog;
    }

}
