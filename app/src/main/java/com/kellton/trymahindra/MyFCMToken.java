package com.kellton.trymahindra;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * <h1><font color="orange">MyFCMToken</font></h1>
 * Service class to register app in fcm and generate a token Id.
 * @author Divya Khanduri
 */

public class MyFCMToken extends FirebaseInstanceIdService {
    private String TAG = String.valueOf(getClass());

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

      /*   If you want to send messages to this application instance or
         manage this apps subscriptions on the server side, send the
         Instance ID token to your app server.*/

        // send token if you have any app server, in this case we do not have any
        //sendRegistrationToServer(refreshedToken);

    }
}
