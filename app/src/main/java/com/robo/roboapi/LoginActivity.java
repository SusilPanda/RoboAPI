package com.robo.roboapi;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import androidx.annotation.NonNull;
//import androidx.design.widget.Snackbar;

import androidx.core.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;


import static android.Manifest.permission.READ_CONTACTS;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.referencecode.database.models.Post;
//import com.google.firebase.referencecode.database.models.User;
import com.robo.bean.GoTo;
import com.robo.bean.RoboLocation;
//import com.robo.temi.TemiMainActivity;

import java.util.HashMap;
import java.util.Map;

import com.robo.bean.TemiNavigation;
import com.robo.bean.Temperature;
import com.robo.bean.Zoom;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.navigation.model.Position;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, LocationListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String URL = "http://172.16.141.12:8080/api/users";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    private int locationPermissionCode = 2;
   // private int analytics = FirebaseAnalytics.
    private DatabaseReference mDatabase;
    private static final String TAG = "ReadAndWriteSnippets";
    private EditText etSpeak, etSaveLocation, etGoTo, etGetLocation, etDistance, etX, etY, etYaw, etNlu;
    private EditText mEdtMeetingNo, mEdtMeetingPassword, mEdtVanityId;

    //private TemiMainActivity temiAdapter;
    private Robot robot;

    //Zoom
    Button closeButton;
    AlertDialog.Builder builder;
    private ZoomSDK mZoomSDK;

    public void initViews() {
        //etSpeak = findViewById(R.id.etSpeak);
        etSaveLocation = findViewById(R.id.etSaveLocation);
        etGoTo = findViewById(R.id.etGoTo);
        etGetLocation = findViewById(R.id.etGetLocation);
       // etDistance = findViewById(R.id.etDistance);
       // tvLog = findViewById(R.id.tvLog);
       // tvLog.setMovementMethod(new ScrollingMovementMethod());
       // etX = findViewById(R.id.etX);
       // etY = findViewById(R.id.etY);
       // etYaw = findViewById(R.id.etYaw);
       // etNlu = findViewById(R.id.etNlu);
       // ivFace = findViewById(R.id.imageViewFace);
      //  ttsVisualizerView = findViewById(R.id.visualizerView);
       // mEdtMeetingNo = (EditText) findViewById(R.id.edtMeetingNo);
      //  mEdtVanityId = (EditText) findViewById(R.id.edtVanityUrl);
      //  mEdtMeetingPassword = (EditText) findViewById(R.id.edtMeetingPassword);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_api);
        initViews();
        // Set up the login form.
        //temiAdapter = new TemiMainActivity();
        //temiAdapter.onRobotReady(true);
        robot = Robot.getInstance();
/*
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
*/

        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);*/
        //findLocation();
// ...
        mDatabase = FirebaseDatabase.getInstance("https://gyantemi-default-rtdb.firebaseio.com/").getReference();
        writeDefaultGoToLocation("1", "Base");
        writeDefaultZoomDetails("1", "123456", "999888");
        writeDefaultNavigationDetails("1", "UP");
        //writeNewLocation("2", "room2", "2");
        //addGoToEventListener(mDatabase);
        //addGoToEventListener1(mDatabase);
        checkingTargetLocationChange(mDatabase);
        checkingTemperatureChange(mDatabase);
        autoJoinZoomMeeting(mDatabase);
        addNavigationListenerChange(mDatabase);
        getLocationFromFB("1");

        // Call this method to initialize the SDK
        initializeSdk(this);
        //navigateTheRobo(robot);
    }

    public void initializeSdk(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        // TODO: For the purpose of this demo app, we are storing the credentials in the client app itself. However, you should not use hard-coded values for your key/secret in your app in production.
        ZoomSDKInitParams params = new ZoomSDKInitParams();
        params.appKey = "UfKz8kTcoqHO3K8yxN70P7TmIsJrSD6JnHg6"; // TODO: Retrieve your SDK key and enter it here
        params.appSecret = "IEl1NoeBQ7f6aZhnPSvewis6xiIgBTtIoTlM"; // TODO: Retrieve your SDK secret and enter it here
        params.domain = "zoom.us";
        params.enableLog = true;
        // TODO: Add functionality to this listener (e.g. logs for debugging)
        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
            /**
             * @param errorCode {@link us.zoom.sdk.ZoomError#ZOOM_ERROR_SUCCESS} if the SDK has been initialized successfully.
             */
            @Override
            public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
                Log.d("ZOOM INITIALIZED :", String.valueOf(errorCode));
            }

            @Override
            public void onZoomAuthIdentityExpired() { }
        };
        sdk.initialize(context, listener, params);

    }


    public void joinMeeting(View view) {
        LayoutInflater layoutInflater = getLayoutInflater();
        mZoomSDK = ZoomSDK.getInstance();
        closeButton = (Button) findViewById(R.id.button);
        builder = new AlertDialog.Builder(this);
        Log.d("firebase location key :", String.valueOf("Inside JoinMeeting"));
        Toast.makeText(this, "Inside altert Dialog", Toast.LENGTH_SHORT).show();

        //Creating dialog box
        final View view1 = layoutInflater.inflate(R.layout.login_user_start_join, null);
        final AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("AlertDialogExample");

        mEdtMeetingNo = (EditText) view1.findViewById(R.id.edtMeetingNo);
        mEdtMeetingPassword = (EditText) view1.findViewById(R.id.edtMeetingPassword);
        mEdtVanityId = (EditText) view1.findViewById(R.id.edtVanityUrl);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });


        alert.setView(view1);
        alert.show();
        /*closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Toast.makeText(getApplicationContext(), "you choose yes action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
            }
        });*/

    }

    public void onClickBtnJoinMeeting(View view) {
        String meetingNo = mEdtMeetingNo.getText().toString().trim();
        String meetingPassword = mEdtMeetingPassword.getText().toString().trim();

        String vanityId = mEdtVanityId.getText().toString().trim();

        if (meetingNo.length() == 0 && vanityId.length() == 0) {
            Toast.makeText(this, "You need to enter a meeting number/ vanity id which you want to join.", Toast.LENGTH_LONG).show();
            return;
        }

        if (meetingNo.length() != 0 && vanityId.length() != 0) {
            Toast.makeText(this, "Both meeting number and vanity id have value,  just set one of them", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();


        int ret = -1;
        if (vanityId.length() != 0) {
            ret = joinMeetingWithVanityId(this, vanityId, meetingPassword);
        } else {
            ret = joinMeetingWithNumber(this, zoomSDK, meetingNo, meetingPassword);
        }

        Log.i(TAG, "onClickBtnJoinMeeting, ret=" + ret);
    }

    public int joinMeetingWithNumber(Context context, ZoomSDK mZoomSDK, String meetingNo, String meetingPassword) {
        int ret = -1;
        MeetingService meetingService = mZoomSDK.getMeetingService();
        if(meetingService == null) {
            return ret;
        }

        JoinMeetingOptions opts = null;

        JoinMeetingParams params = new JoinMeetingParams();

        params.displayName = "GyanRover";
        params.meetingNo = meetingNo;
        params.password = meetingPassword;
        return meetingService.joinMeetingWithParams(context, params,opts);
    }

    public int joinMeetingWithVanityId(Context context, String vanityId, String meetingPassword) {
        int ret = -1;
        MeetingService meetingService = mZoomSDK.getMeetingService();
        if(meetingService == null) {
            return ret;
        }

        JoinMeetingOptions opts = null;//ZoomMeetingUISettingHelper.getJoinMeetingOptions();
        JoinMeetingParams params = new JoinMeetingParams();
        params.displayName = "GyanRover";
        params.vanityID = vanityId;
        params.password = meetingPassword;
        return meetingService.joinMeetingWithParams(context, params,opts);
    }

    // Working event Listener from Firebase Db change
    public void autoJoinZoomMeeting(DatabaseReference mPostReference){
        // DatabaseReference mDatabaseReference =
        //        FirebaseDatabase.getInstance().getReference().child("GoTo");
        final ZoomSDK zoomSDK = ZoomSDK.getInstance();
        final Query query = mPostReference.child("Zoom");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    Zoom mUser = dataSnapshot.getValue(Zoom.class);
                    Log.d("Zoom meeting details: ", String.valueOf(mUser));
                    // String uId = mUser.getUid();
                    // usersDatabase.child(mUser.getUid());
                    // Log.d("TAG",uId);

                } else {
                    Log.i("GoTo Firebase child : ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    Zoom tempDataStr = dataSnapshot.getValue(Zoom.class);
                    Log.d("Zoom meeting details: ", String.valueOf(tempDataStr.getMeetingId() + tempDataStr.getMeetingPassword()));
                    if (tempDataStr.getMeetingId() != null && !tempDataStr.getMeetingId().isEmpty()
                            && tempDataStr.getMeetingPassword() != null && !tempDataStr.getMeetingPassword().isEmpty()
                            && !"123456".equalsIgnoreCase(tempDataStr.getMeetingId())
                            && !"999888".equalsIgnoreCase(tempDataStr.getMeetingPassword())) {
                        int ret = joinMeetingWithNumber(getBaseContext(), zoomSDK, tempDataStr.getMeetingId(), tempDataStr.getMeetingPassword());
                    }
                } else {
                    Log.i("OnChanged Targt child: ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void navigateTheRobo(Robot robot, String command, Date lastUpdatedDate) {
        switch(command) {
            case "UP":
                Log.d("Navigation UP ", String.valueOf(command));
                robot.skidJoy(1,0);
                break;
            case "DOWN":
                Log.d("Navigation DOWN ", String.valueOf(command));
                robot.skidJoy(-1,0);
                break;
            case "LEFT":
                Log.d("Navigation LEFT ", String.valueOf(command));
                robot.skidJoy(0,1);
                break;
            case "RIGHT":
                Log.d("Navigation RIGHT ", String.valueOf(command));
                robot.skidJoy(0,-1);
                break;
            default:
                robot.stopMovement();
        }
        //Sleep for 500ms
        //robot.skidJoy(1,0);
    }

    // Working event Listener from Firebase Db change
    public void addNavigationListenerChange(DatabaseReference mPostReference){
        // DatabaseReference mDatabaseReference =
        //        FirebaseDatabase.getInstance().getReference().child("GoTo");
        final Query query = mPostReference.child("TemiNavigation");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    TemiNavigation mUser = dataSnapshot.getValue(TemiNavigation.class);
                    Log.d("Navigation Move ", String.valueOf(mUser.getDirection()));
                    // String uId = mUser.getUid();
                    // usersDatabase.child(mUser.getUid());
                    // Log.d("TAG",uId);

                } else {
                    Log.i("GoTo Firebase child : ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    TemiNavigation mUser = dataSnapshot.getValue(TemiNavigation.class);
                    Log.d("Navigation move ", String.valueOf(mUser.getDirection())+mUser.getLastUpdatedDate());

                    // Call Robot goto method
                    Position pos = new Position(1, 0, 0, 1);
                    //robot.goToPosition(pos);
                    //robot.skidJoy(1,0);
                    navigateTheRobo(robot, mUser.getDirection(), mUser.getLastUpdatedDate());

                } else {
                    Log.i("OnChanged Targt child: ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void writeNewLocation(String locationId, String name, String number) {
        RoboLocation loc = new RoboLocation(name, number);

        mDatabase.child("locations").child(locationId).setValue(loc);
    }

    public void writeDefaultGoToLocation(String targetId, String name) {
        GoTo loc = new GoTo(name);

        mDatabase.child("GoTo").child(targetId).setValue(loc);
    }

    public void writeDefaultZoomDetails(String targetId, String meetingId, String meetingPass) {
        Zoom zm = new Zoom(meetingId, meetingPass);

        mDatabase.child("Zoom").child(targetId).setValue(zm);
    }
    public void writeDefaultNavigationDetails(String navId, String direction) {
        TemiNavigation tNav = new TemiNavigation(direction, new Date());

        mDatabase.child("TemiNavigation").child(navId).setValue(tNav);
    }

    public void getLocationFromFB(String locationId) {
        String key = mDatabase.child("locations").child(locationId).getKey();
        //String locationname = key.g
        Log.d("firebase location key :", String.valueOf(key));
        /*mDatabase.child("locations").child(locationId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });*/

    }

    public void getLocationByName(String locationNm) {

        final DatabaseReference locationRef = mDatabase.getRef();
        locationRef.orderByChild("locations").equalTo(locationNm).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println(dataSnapshot.getKey());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                System.out.println(dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
               // System.out.println(dataSnapshot.getKey());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
               // System.out.println(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
            // ...
        });
        String key = mDatabase.child("locations").child(locationNm).getKey();
        //String locationname = key.g
        Log.d("firebase location key :", String.valueOf(key));
        /*mDatabase.child("locations").child(locationId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });*/

    }

    // Working event Listener from Firebase Db change
    public void checkingTargetLocationChange(DatabaseReference mPostReference){
        // DatabaseReference mDatabaseReference =
        //        FirebaseDatabase.getInstance().getReference().child("GoTo");
        final Query query = mPostReference.child("GoTo");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    GoTo mUser = dataSnapshot.getValue(GoTo.class);
                    Log.d("Goto Firebase target ", String.valueOf(mUser.getTarget()));
                    // String uId = mUser.getUid();
                    // usersDatabase.child(mUser.getUid());
                    // Log.d("TAG",uId);

                } else {
                    Log.i("GoTo Firebase child : ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    GoTo mUser = dataSnapshot.getValue(GoTo.class);
                    Log.d("Goto OnChanged target ", String.valueOf(mUser.getTarget()));

                    // Call Robot goto method
                    robot.goTo(mUser.getTarget());

                } else {
                    Log.i("OnChanged Targt child: ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Working event Listener from Firebase Db change
    public void checkingTemperatureChange(DatabaseReference mPostReference){
        // DatabaseReference mDatabaseReference =
        //        FirebaseDatabase.getInstance().getReference().child("GoTo");
        final Query query = mPostReference.child("Data");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    String mUser = dataSnapshot.getValue(String.class);
                    Log.d("Temp Firebase temp ", String.valueOf(mUser));
                    // String uId = mUser.getUid();
                    // usersDatabase.child(mUser.getUid());
                    // Log.d("TAG",uId);

                } else {
                    Log.i("GoTo Firebase child : ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    String tempDataStr = dataSnapshot.getValue(String.class);
                    Log.d("Temperature OnChanged: ", String.valueOf(tempDataStr));
                    Temperature tempData = parseString(tempDataStr);
                    // update the value in the view
                    updateTeperatureTextView(tempData);


                } else {
                    Log.i("OnChanged Targt child: ", "NULL");
                    //Toast.makeText(getApplicationContext(), "****NOT FOUND****", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateTeperatureTextView(Temperature tempData) {
        TextView textTopTemp = (TextView) findViewById(R.id.topTemp);
        textTopTemp.setText(tempData.topTemp);
        TextView textBtmTemp = (TextView) findViewById(R.id.btmTemp);
        textBtmTemp.setText(tempData.getBtmTemp());
        TextView textTopHm = (TextView) findViewById(R.id.topHumid);
        textTopHm.setText(tempData.getTopHumid());
        TextView textBtmHm = (TextView) findViewById(R.id.btmHumid);
        textBtmHm.setText(tempData.getBtmHumid());
        TextView textSmoke = (TextView) findViewById(R.id.smoke);
        textSmoke.setText(tempData.getSmokeSensor());
        TextView textSound = (TextView) findViewById(R.id.sound);
        textSound.setText(tempData.getSound());
    }

    private Temperature parseString(String tempDataStr) {
        Temperature temp = new Temperature();
        String topTempVal = "", btmTempVal = "", topHumidVal = "", btmHumidVal = "", smokeVal = "", soundVal="";
        String[] strArray = TextUtils.split(tempDataStr, ",");
        for (int i = 0; i < strArray.length ; i=i+6)  {
            topHumidVal = strArray[i];
            topTempVal = strArray[i+1];
            btmHumidVal = strArray[i+2];
            btmTempVal = strArray[i+3];
            smokeVal = strArray[i+4];
            soundVal = strArray[i+5];
        }

        // split the value now
       temp.setTopHumid(parseAndFetchValue(topHumidVal));
        temp.setBtmHumid(parseAndFetchValue(btmHumidVal));
        temp.setTopTemp(parseAndFetchValue(topTempVal));
        temp.setBtmTemp(parseAndFetchValue(btmTempVal));
        temp.setSmokeSensor(parseAndFetchValue(smokeVal));
        temp.setSound(soundVal);
        return temp;
    }

    private String parseAndFetchValue(String str) {
        String result = "";
        String[] strArrayVal = TextUtils.split(str, "=");
        return strArrayVal[1];
    }
    private void addGoToEventListener(DatabaseReference mPostReference) {
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            String targetLoc = "empty";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("target")) {
                        targetLoc = snapshot.child("target").getValue(String.class);
                        robot.goTo(targetLoc);
                        Log.d("Goto Firebase target ", String.valueOf(targetLoc));
                    } else {
                        Log.i("GoTo Firebase child : ", "NULL");
                    }
                }
                Log.i("Goto Firebase event: ", "NULL");
               /* if (dataSnapshot.child("GoTo").exists()) {
                    GoTo post = dataSnapshot.getValue(GoTo.class);
                    if (post != null) {
                        Log.d("Goto Firebase event ", String.valueOf(post));
                    }
                    // call robot goto location method
                } else {
                    Log.i("Goto Firebase event: ", "NULL");
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]
    }


    // [START write_fan_out]
    private void writeNewPost(String locationId, String name, String number) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("locations").push().getKey();
        RoboLocation post = new RoboLocation(name, number);
        Map<String, Object> postValues = new HashMap<>();
        postValues.put(locationId, post);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + locationId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public void saveLocation(View view) {


        //volleyGet();
        String location = etSaveLocation.getText().toString().toLowerCase().trim();

        //temiAdapter.saveLocation(view);

        writeNewLocation("2", location, "2");
        /*boolean result = robot.saveLocation(location);
        if (result) {
            robot.speak(TtsRequest.create("I've successfully saved the " + location + " location.", true));
        } else {
            robot.speak(TtsRequest.create("Saved the " + location + " location failed.", true));
        }*/
        Toast.makeText(this, "RoboLocation cordinates pushed to DB", Toast.LENGTH_SHORT).show();
    }

    public void getLocation(View view) {

        //volleyGet();
        String locationNm = etGetLocation.getText().toString().toLowerCase().trim();
        String key = mDatabase.child("locations").child(locationNm).getKey();
        //String locationname = key.g

        Log.d("firebase location key :", String.valueOf(key));
        Toast.makeText(this, "RoboLocation cordinates fetched from DB", Toast.LENGTH_SHORT).show();
    }

    public void getAllLocation(View view) {
        String key = "";
        //volleyGet();
        //String locationNm = etGetAllLocation.getText().toString().toLowerCase().trim();
        //String key = mDatabase.child("locations").child(locationNm).getKey();
        //String locationname = key.g
        /*Arrays.asList(robot.getLocations()).stream().forEach(
                s -> writeNewLocation(s)
        );*/
        int i = 1;
        for (String str : robot.getLocations()) {
            writeNewLocation(String.valueOf(i), str, String.valueOf(i));
            i++;
        }

        Log.d("firebase location key :", String.valueOf(key));
        Toast.makeText(this, "RoboLocation cordinates fetched from DB", Toast.LENGTH_SHORT).show();
    }

    public void goTo(View view) {
        //volleyPost();
        //temiAdapter.goTo(view);
        String locationNm = etGoTo.getText().toString().toLowerCase().trim();
        robot.goTo(locationNm);
        Toast.makeText(this, "TEMI Goto RoboLocation", Toast.LENGTH_SHORT).show();
    }

    public void volleyGet(){
        String url = "http://172.16.141.12:8080/api/users/5fffaf6c82db476bf4583a6b";
        final List<String> jsonResponses = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /*try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String email = jsonObject.getString("email");

                        jsonResponses.add(email);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //String res = new Gson().
                Log.e("Rest Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public void volleyPost(){
        String postUrl = "http://172.16.141.12:8080/api/user/register";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("first_name", "Jonathan");
            postData.put("last_name", "Rob");
            postData.put("email_id", "jonathan@gmail1.com");
            postData.put("password", "ps12345");

        } catch (JSONException e) {
            Log.e("POST Err Rest Response", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("POST Rest Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POST Err Rest Response", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void findLocation() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

      //  if (ContextCompat.checkSelfPermission(
      //          this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
       //     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);;
        //} else if (shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
        //    showInContextUI(...);
       // } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
        //    requestPermissionLauncher.launch(
        //            Manifest.permission.REQUESTED_PERMISSION);
      //  }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }


  /*  public static boolean shouldShowRequestPermissionRationale (Activity activity,
                                                                String permission) {

    }*/
    @Override
    public void onLocationChanged(Location location) {
        //txtLat = (TextView) findViewById(R.id.textview1);
        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }



   /* private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/

    /**
     * Callback received when a permissions request has been completed.
     */



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}

