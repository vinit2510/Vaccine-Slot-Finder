package com.example.viewslotadvance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

@SuppressWarnings("unchecked")
public class by_city extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] states = {"--Select State--", "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh",
                       "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Delhi", "Goa", "Gujarat",
                       "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshadweep",
                       "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
                       "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
                       "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Daman and Diu"};

    String[] dis_name = {"--Select District--"};
    Integer[] dis_id = {-1};

    ArrayList<String> dis_Name_list = new ArrayList<String>(Arrays.asList(dis_name));
    ArrayList<Integer> dis_Id_list = new ArrayList<Integer>(Arrays.asList(dis_id));


    public void getMySlot(View view) {

        Spinner spinner = findViewById(R.id.spinner_district);
        String district_name = spinner.getSelectedItem().toString();

        Integer index_no = dis_Name_list.indexOf(district_name);

        Integer district_id = dis_Id_list.get(index_no);


        EditText editText_date = findViewById(R.id.enter_date);
        String date = editText_date.getText().toString();

        TextView SlotTextView = findViewById(R.id.slotData);
        SlotTextView.setMovementMethod(new ScrollingMovementMethod());
        SlotTextView.setText("Please Hold on...");

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=" +
                        district_id + "&date=" + date, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    SlotTextView.setText("");
                    //Log.d("myapp", "The response: " + (response.getString("sessions")));

                    JSONArray jsonArray = response.getJSONArray("sessions");

                    int length = jsonArray.length();
                    int j = 0;


                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.getInt("min_age_limit") == 18 && jsonObject.getInt("available_capacity") != 0 && jsonObject.getString("fee_type").equals("Free")) {

                            SlotTextView.append("[" + (j + 1) + "] " +
                                    "\nName     : " + jsonObject.getString("name") +
                                    "\nAddress : " + jsonObject.getString("address") +
                                    "\nDistrict   : " + jsonObject.getString("district_name") +
                                    "\nPincode : " + jsonObject.getInt("pincode") +
                                    "\nTime       : " + jsonObject.getString("from") + " - " + jsonObject.getString("to") +
                                    "\nCapacity: " + jsonObject.getInt("available_capacity") +
                                    "\nDose1    : " + jsonObject.getInt("available_capacity_dose1") +
                                    "\nDose2    : " + jsonObject.getInt("available_capacity_dose2") +
                                    "\nVaccine : " + jsonObject.getString("vaccine") +
                                    "\nFee        : " + jsonObject.getString("fee_type") +
                                    "\n\n");
                            j++;

                        }
                    }

                    if (j == 0){
                        SlotTextView.append("Slot Not Found!");
                        Toast.makeText(by_city.this, "Slot Not Found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    SlotTextView.setText("Error!");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SlotTextView.setText("Please Fill Correct Details!");
                Toast.makeText(by_city.this, "Please Fill Correct Details!", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_city);

        Spinner spinner_state = findViewById(R.id.spinner_state);

        ArrayAdapter adapter_state = new ArrayAdapter(this, android.R.layout.simple_spinner_item, states );
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_state.setAdapter(adapter_state);
        spinner_state.setOnItemSelectedListener(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        EditText editText = findViewById(R.id.enter_date);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        by_city.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        String date = i2 + "-" + i1 + "-" + i;
                        editText.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();

        dis_Name_list.clear();
        dis_Id_list.clear();

        dis_Name_list.add("--Select District--");
        dis_Id_list.add(-1);


        if (i != 0) {

            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    "https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + i,
                             null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        //Log.d("myapp", "The response: " + (response.getString("sessions")));

                        JSONArray jsonArray = response.getJSONArray("districts");

                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            dis_Name_list.add(jsonObject.getString("district_name"));
                            dis_Id_list.add(jsonObject.getInt("district_id"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(by_city.this, "Error!", Toast.LENGTH_SHORT).show();

                }
            });

            requestQueue.add(jsonObjectRequest);

            Spinner spinner_district = findViewById(R.id.spinner_district);

            ArrayAdapter<CharSequence> adapter_district = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dis_Name_list );

            adapter_district.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_district.setAdapter(adapter_district);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}