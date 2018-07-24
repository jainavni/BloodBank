package com.example.gagan.bloodbank;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class hello extends AppCompatActivity {

    private EditText ename;
    private EditText emob;
    private EditText eaddress;
    private EditText eemail;
    private Button esignup;
    private Spinner dropdown;
    private Spinner stateSpinner;
    private Spinner citySpinner;
    private RadioGroup radioGroup;
    private ProgressBar mProgressBar;
    private FirebaseAuth mfirebaseauth;
    private String user_donor = null;
    private String states=null;
    private String city=null;
    private Spinner donorSpinner;
    private String userid;
    private String URL="http://api.androiddeft.com/cities/cities_array.json";
    String user_gender = null, user_bloodgroup = null;
    Boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"A-", "A+", "B-", "B+", "AB-", "AB+", "O-", "O+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.state_list,R.id.spinnerText, items);
        dropdown.setAdapter(adapter);
        mProgressBar = findViewById(R.id.signup_progressbar);
        mfirebaseauth = FirebaseAuth.getInstance();
        ename = findViewById(R.id.name);
        eemail = findViewById(R.id.email);
        eaddress = findViewById(R.id.address);
        stateSpinner=findViewById(R.id.statespinner);
        citySpinner=findViewById(R.id.cityspinner);
        emob = findViewById(R.id.mob);
        esignup = findViewById(R.id.signup);
        radioGroup = findViewById(R.id.radiogroup);
        donorSpinner=findViewById(R.id.donorspinner);

        String[] ditems = new String[]{"Donor", "Receiver"};
        ArrayAdapter<String> dadapter = new ArrayAdapter<>(this,R.layout.state_list,R.id.spinnerText, ditems);
        donorSpinner.setAdapter(dadapter);


        FirebaseUser firebaseUser = mfirebaseauth.getInstance().getCurrentUser();
        try {
            emob.setText(firebaseUser.getPhoneNumber());
        } catch (NullPointerException e) {
            Toast.makeText(this, "Error In Phone Number", Toast.LENGTH_SHORT).show();
        }
        emob.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male) {
                    user_gender = "Male";
                } else if (checkedId == R.id.female) {
                    user_gender = "Female";
                }
            }
        });
        loadStateandCity();

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_bloodgroup = dropdown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        esignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);

                if (user_bloodgroup == null) {
                    Toast.makeText(hello.this, "Please Select The Blood group", Toast.LENGTH_SHORT).show();

                }
                if (user_gender == null) {
                    Toast.makeText(hello.this, "Please Select The Gender", Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(ename.getText().toString().trim()) || TextUtils.isEmpty(eemail.getText().toString().trim()) || TextUtils.isEmpty(emob.getText().toString().trim()) || TextUtils.isEmpty(eaddress.getText().toString()) ) {
                    Toast.makeText(hello.this, "Please Fill All The  Details", Toast.LENGTH_SHORT).show();

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(eemail.getText().toString()).matches()) {
                    Toast.makeText(hello.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d("GAGAN", "ÄDD DATA TO CALL");
                    Cities state = (Cities) stateSpinner.getSelectedItem();
                    states = state.getState();
                    city = citySpinner.getSelectedItem().toString();
                    user_donor=donorSpinner.getSelectedItem().toString();
                    if(states==null||city==null)
                    {
                        Toast.makeText(hello.this, "Please Select State and City", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        userid = firebaseUser.getUid();

                        addData(ename.getText().toString().trim(), eemail.getText().toString().trim(), eaddress.getText().toString().trim(), states, city, emob.getText().toString().trim(), user_bloodgroup, user_gender, user_donor, userid);
                        // startActivity(new Intent(hello.this, ViewPagerActivity.class));

                        Intent intent = new Intent(hello.this, UserHome.class);
                        //intent.putExtra("ID",userid);
                        startActivity(intent);
                    }
                }
                mProgressBar.setVisibility(View.GONE);
                }



        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void loadStateandCity() {
        final List<Cities> stateList = new ArrayList<>();
        final List<String> states = new ArrayList<>();
        if (isNetworkAvailable()) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String state = jsonObject.getString("state");
                            Log.d("GAGAN", "STATE IS " + state);
                            JSONArray cities = jsonObject.getJSONArray("cities");
                            List<String> citiesList = new ArrayList<>();
                            for (int j = 0; j < cities.length(); j++) {
                                citiesList.add(cities.getString(j));
                                Log.d("GAGAN", "CITIES IS " + cities.getString(j));
                            }
                            stateList.add(new Cities(state, citiesList));
                            states.add(state);


                        }
                        final CityAdapter cityAdapter = new CityAdapter(hello.this,
                                R.layout.state_list, R.id.spinnerText, stateList);
                        stateSpinner.setAdapter(cityAdapter);
                        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Cities cityDetails = cityAdapter.getItem(position);
                                List<String> cityList = cityDetails.getCities();
                                ArrayAdapter citiesAdapter = new ArrayAdapter<>(hello.this,
                                        R.layout.city_list, R.id.cityspinnerText, cityList);
                                citySpinner.setAdapter(citiesAdapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            Singleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
        } else {
            //Toast.makeText(this, "Please Check Your Internet,Before Proceeding", Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(hello.this).create();
            alertDialog.setTitle("No Internet Connection");
            alertDialog.setMessage("Check Internet Connection and Click Retry");
            alertDialog.setCancelable(false);
            alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    loadStateandCity();
                }
            });
            alertDialog.show();
        }
    }




    public boolean isConnected()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }
    public void addData(String name, String email, String address, String state ,String city, String mobile, String bloodgroup, String gender, String donor,String userid) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(userid);
        Userinfo info = new Userinfo(name, email, address, state, city, gender, bloodgroup, mobile, donor,userid);
        databaseReference.setValue(info);

        }


}
