package com.example.gagan.bloodbank;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BloodBank extends AppCompatActivity {

    String URL;
    private RecyclerView mRecyclerView;
    private BloodBankAdapter mListAdapter;
    private List<BloodBankItem> mListData;
    private ProgressBar mProgressBar;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        URL = "https://data.gov.in/node/356981/datastore/export/json";

        mRecyclerView = findViewById(R.id.bank_recyclerview);
        mProgressBar = findViewById(R.id.bank_progressbar);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        mListData = new ArrayList<>();
        mListAdapter = new BloodBankAdapter(mListData, this);
        mRecyclerView.setAdapter(mListAdapter);
        new AsyncTaskBank().execute(URL);
        mProgressBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.bank_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Banks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    public class AsyncTaskBank extends AsyncTask<String, Void, String>{
        public AsyncTaskBank() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {

                // mListAdapter.setListData(mListData);
                mListAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(BloodBank.this, "Failed to load data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String weather = "UNDEFINED";
                    try {
                        URL url = new URL(strings[0]);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                        StringBuilder builder = new StringBuilder();

                        String inputString;
                        while ((inputString = bufferedReader.readLine()) != null) {
                            builder.append(inputString);
                        }

                        JSONObject root = new JSONObject(builder.toString());
                        JSONArray banksList = root.getJSONArray("data");

                        BloodBankItem item;
                        for (int i = -0; i < banksList.length(); i++) {
                            JSONArray bloodBank = banksList.getJSONArray(i);
                            //JsonArray bloodBank = bank.getAsJsonArray();
                            String state = bloodBank.get(1).toString();
                            String city = bloodBank.get(2).toString();
                            String district = bloodBank.get(3).toString();
                            String bankName = bloodBank.get(4).toString();
                            String address = bloodBank.get(5).toString();
                            String pincode = bloodBank.get(6).toString();
                            String contactno = bloodBank.get(7).toString();
                            String helpline = bloodBank.get(8).toString();
                            String website = bloodBank.get(11).toString();
                            String email = bloodBank.get(12).toString();
                            String servicetime = bloodBank.get(15).toString();


                            item = new BloodBankItem();
                            item.setBloodBankName(bankName);
                            item.setDistrict(district);
                            item.setCity(city);
                            item.setState(state);
                            item.setAddress(address);
                            item.setPincode(pincode);
                            item.setHelpline(helpline);
                            item.setEmail(email);
                            item.setWebsite(website);
                            item.setContactno(contactno);
                            item.setServicetime(servicetime);


                            mListData.add(item);
                            // mListAdapter.notifyDataSetChanged();


                        }

                        urlConnection.disconnect();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return weather;
                }



    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu_search, menu);
            MenuItem search = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) search.getActionView();
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    mListAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    try {
                        mListAdapter.getFilter().filter(newText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        if(item.getItemId()==R.id.action_userhome)
        {
            startActivity(new Intent(BloodBank.this,UserHome.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
