package com.example.viewslotadvance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;

public class by_zip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_zip);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        EditText editText = findViewById(R.id.enter_date);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        by_zip.this, new DatePickerDialog.OnDateSetListener() {
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


    public void getMySlot(View view) {


        EditText editText_pincode = findViewById(R.id.enter_pincode);
        EditText editText_date = findViewById(R.id.enter_date);

        String pincode = editText_pincode.getText().toString();
        String date = editText_date.getText().toString();

        TextView SlotTextView = findViewById(R.id.slotData);
        SlotTextView.setMovementMethod(new ScrollingMovementMethod());
        SlotTextView.setText("Please Hold On...");

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=" +
                        pincode + "&date=" + date, null, new Response.Listener<JSONObject>() {
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
                                    "\n\n");
                            j++;
                            //found = 1;
                        }
                    }

                    if (j == 0){
                        SlotTextView.append("Slot Not Found!");
                        Toast.makeText(by_zip.this, "Slot Not Found!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(by_zip.this, "Please Fill Correct Details!", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);

    }


}