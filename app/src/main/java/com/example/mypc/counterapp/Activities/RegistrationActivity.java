package com.example.mypc.counterapp.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mypc.counterapp.Fonts.TextViewRegular;
import com.example.mypc.counterapp.Model.Religion;
import com.example.mypc.counterapp.PushNotification.Constants;
import com.example.mypc.counterapp.R;
import com.example.mypc.counterapp.ServerApiInterface.ServerApiInterface;
import com.example.mypc.counterapp.ServerObject.AddRelegionObject;
import com.example.mypc.counterapp.ServerObject.CounterController;
import com.example.mypc.counterapp.ServerObject.RegistrationObjects;
import com.example.mypc.counterapp.ServerObject.UserLoginObjects;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RegistrationActivity extends AppCompatActivity {
    ImageButton calender_button, image_down, calender_annive, downarow_nakshatra, downarow_pada;
    Spinner spinner_religion, spinner_nakshatra, spinner_pada;
    TextView text_dob, displaydate_anniv, text_religion_display3;
    MaterialDialog mProgress;
    Button button_register;
    TextView toolbar_title;
    RadioButton radio_married, radiosingle;
    RelativeLayout relative_aniv;
    Boolean isNetworkAvailable = true;
    EditText edit_name, edit_gothra, editmobile_number, edit_surname, edit_firstname, edit_lastname, edit_fathers, edit_mothers;
    RadioGroup radioGroup_marital;
    String dateofbirth, gothra, maritalstatus, user_name, user_email, dateofanniversary, user_religion, user_fname, user_lname, user_ftharename, user_mothername, user_padha, user_nakshtra, mobile_number, user_surname;
    public ArrayList<Religion> list;
    String html;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        intialise_ui();
        list = new ArrayList<>();

        CounterController.getInstance().fillcontext(RegistrationActivity.this);
        setup();
    }

    public void setup() {
        defaults();
        dob();
        anniversary();
        set_nakshatra();
        set_pada();
        submit();
        check_married();
        fetchData();
    }


    public void validations() {
        user_mothername = edit_mothers.getText().toString().trim();
        user_ftharename = edit_fathers.getText().toString().trim();
        user_lname = edit_lastname.getText().toString().trim();
        user_fname = edit_firstname.getText().toString().trim();
        gothra = edit_gothra.getText().toString().trim();
        user_surname = edit_surname.getText().toString().trim();
        int id = radioGroup_marital.getCheckedRadioButtonId();
        RadioButton radioButto = findViewById(id);
        Log.e("radiocheck", String.valueOf(radioButto.getText()));
        user_name = edit_name.getText().toString().trim();
        mobile_number = editmobile_number.getText().toString().trim();
        if (user_name.isEmpty()) {
            edit_name.setError("Enter Name");
        } else if (user_religion.equals("")) {
            Toast toast = Toast.makeText(this, "Select Religion", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (dateofbirth.equals("")) {
            Toast toast = Toast.makeText(this, "Select DOB", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (mobile_number.equals("") || mobile_number.length() < 10) {
            editmobile_number.setError("Invalid Number");
        } else if (radio_married.isChecked() && dateofanniversary.equals("")) {
            Toast toast = Toast.makeText(this, "Enter Anniversary date", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            register_user();
        }


        Log.e("wholedata", user_name + user_surname + user_fname + user_lname + user_ftharename + user_mothername + user_religion + dateofbirth + mobile_number + dateofanniversary + gothra + user_nakshtra + user_padha);

    }

    public void set_pada() {
        final ArrayList<String> pada = new ArrayList<>();
        pada.add("Select Padha");
        pada.add("1");
        pada.add("2");
        pada.add("3");
        pada.add("4");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, pada);
        // Setting the array adapter containing country list to the spinner widget
        spinner_pada.setAdapter(adapter);
        AdapterView.OnItemSelectedListener religion = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    Object item = parent.getItemAtPosition(position);
                    user_padha = item.toString();
                    Log.e("padham", user_padha);
                } else {
                    user_padha = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner_pada.setOnItemSelectedListener(religion);

    }

    public void set_nakshatra() {
        final ArrayList<String> nakshatra = new ArrayList<>();
        nakshatra.add("Select Nakshatra");
        nakshatra.add("Ashwini (अश्विनि)");
        nakshatra.add("Bharani (भरणी)");
        nakshatra.add("Krittika (कृत्तिका)");
        nakshatra.add("Rohini (रोहिणी)");
        nakshatra.add("Mrigashīrsha (म्रृगशीर्षा)");
        nakshatra.add("Ārdrā (आर्द्रा)");
        nakshatra.add("Punarvasu (पुनर्वसु)");
        nakshatra.add("Pushya (पुष्य)");
        nakshatra.add("Āshleshā (आश्लेषा)");
        nakshatra.add("Maghā (मघा)");
        nakshatra.add("Pūrva (पूर्व फाल्गुनी)");
        nakshatra.add("Uttara (उत्तर फाल्गुनी)");
        nakshatra.add("Hasta (हस्त)");
        nakshatra.add("Chitra (चित्रा)");
        nakshatra.add("Svātī (स्वाति)");
        nakshatra.add("Viśākhā (विशाखा)");
        nakshatra.add("Anurādhā (अनुराधा)");
        nakshatra.add("Jyeshtha (ज्येष्ठा)");
        nakshatra.add("Mula (मूल)");
        nakshatra.add("Pūrva Ashādhā (पूर्वाषाढ़ा)");
        nakshatra.add("Uttara Aṣāḍhā (उत्तराषाढ़ा)");
        nakshatra.add("Śrāvaṇa (श्रावण)");
        nakshatra.add("Śrāvistha (श्रविष्ठा) or Dhanishta");
        nakshatra.add("Shatabhisha (शतभिषा)or Śatataraka");
        nakshatra.add("Pūrva Bhādrapadā (पूर्वभाद्रपदा)");
        nakshatra.add("Uttara Bhādrapadā (उत्तरभाद्रपदा)");
        nakshatra.add("Revati (रेवती)");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nakshatra);

        spinner_nakshatra.setAdapter(adapter);
        AdapterView.OnItemSelectedListener religion = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    Object item = parent.getItemAtPosition(position);

                    user_nakshtra = item.toString();
                    Log.e("nakshatra", user_nakshtra);
                } else {
                    user_nakshtra = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner_nakshatra.setOnItemSelectedListener(religion);

    }

    public void set_relgion() {

        displayProgressDialog("Processing..");
        image_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_religion.performClick();
            }
        });

        list = CounterController.getInstance().religionArrayList;
        Log.e("sizer", String.valueOf(list.size() + 2));
        final ArrayList<CharSequence> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                data.add("Select Region");
            } else if (i == 1) {
                for (int c = 0; c < list.size(); c++) {
                    data.add(list.get(c).getReligion_name());
                }
            } else {
                data.add(Html.fromHtml(html));
            }

        }

        Log.e("registerrelig", String.valueOf(list.size()));
        Log.e("datasize", String.valueOf(data.size()));

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_dropdown_item_1line, data);

        // Setting the array adapter containing country list to the spinner widget
        spinner_religion.setAdapter(adapter);
        AdapterView.OnItemSelectedListener religion = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Object item = parent.getItemAtPosition(position);
                    user_religion = item.toString();
                    text_religion_display3.setText(user_religion);
                    Log.e("religion", user_religion);
                    if (user_religion.contains("Not Found ? Please add..")) {
                        user_religion = "";
                        text_religion_display3.setText("Select Region");
                        showReligiondialog();
                    }
                } else {
                    user_religion = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner_religion.setOnItemSelectedListener(religion);
        hideProgressDialog();
    }

    public void fetchData() {
        CounterController.getInstance().fetchReligions();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginActivity.MessageEvent event) {
        Log.e("filesevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("conttrollerreligion")) {
            set_relgion();
        } else if (resultData.equals("error")) {
            Toast.makeText(this, "Failed to fetch", Toast.LENGTH_SHORT).show();
        }
    }

    public void dob() {
        calender_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                + "/" + String.valueOf(year);

                        dateofbirth = date;
                        text_dob.setText(dateofbirth);
                    }
                }, yy, mm, dd);
                datePicker.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                datePicker.setTitle("Choose Date of Birth");
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePicker.show();
            }
        });
    }

    public void anniversary() {
        calender_annive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(RegistrationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1)
                                + "/" + String.valueOf(year);

                        displaydate_anniv.setText(date);
                        dateofanniversary = date;
                    }
                }, yy, mm, dd);
                datePicker.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
                datePicker.setTitle("Choose Date of Birth");
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePicker.show();
                dateofanniversary = "";
            }
        });
    }

    public void submit() {
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validations();
            }
        });
    }

    public void showReligiondialog() {
        TextViewRegular save, cancel;
        final EditText relegion_data;
        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_religion_dialog);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);
        relegion_data = dialog.findViewById(R.id.dialog_text);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String relegion = relegion_data.getText().toString().trim();
                if (isNetworkAvailable == true) {
                    if (!relegion.equals("")) {

                        add_relegion(relegion);
                        Log.e("dialogueyes", "dialog yes" + relegion);
                        dialog.dismiss();
                    } else

                    {
                        Toast toast = Toast.makeText(RegistrationActivity.this, " Enter Religion", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(RegistrationActivity.this, " Check Internet Connection", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    dialog.dismiss();
                }
                // startActivity(new Intent(getApplicationContext(),LoginActivity.class));


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("dialoguecancel", "dialog no");
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void add_relegion(String relegion) {
        displayProgressDialog("Adding religion...");
        AddRelegionObject relegion_object = new AddRelegionObject();
        relegion_object.relegion = relegion;

        Log.e("helpObj", " " + relegion);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url).
                addConverterFactory(GsonConverterFactory.create()).build();
        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<AddRelegionObject> helpus = api.add_relegion(relegion_object);
        helpus.enqueue(new Callback<AddRelegionObject>() {
            @Override
            public void onResponse(Call<AddRelegionObject> call, Response<AddRelegionObject> response) {
                hideProgressDialog();
                if (response.body() != null)
                    Log.e("responsecalling", " " + response.body().getResponse());
                String status_code = response.body().getResponse();
                {
                    if (status_code.equals("3")) {
                        Toast toast = Toast.makeText(RegistrationActivity.this, "Relegion added successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        CounterController.getInstance().fetchReligions();
                    } else if (status_code.equals("2")) {
                        Toast toast = Toast.makeText(RegistrationActivity.this, "Relegion exist ", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        Toast toast = Toast.makeText(RegistrationActivity.this, " Failed to add Relegion ", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    Log.e("checking", "onrespo");
                }
            }

            @Override
            public void onFailure(Call<AddRelegionObject> call, Throwable t) {
                Toast toast = Toast.makeText(RegistrationActivity.this, " Failed to add Relegion ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Log.e("helpFailure", "Failed");
                hideProgressDialog();
            }
        });

    }

    private void hideProgressDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    public void defaults() {

        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        user_name = prefs.getString("name", "No name defined");
        user_email = prefs.getString("email", "No name defined");

        edit_name.setText(user_name);
        maritalstatus = "Single";
        user_religion = "";
        user_nakshtra = "";
        user_padha = "";
        dateofbirth = "";
        dateofanniversary = "";


    }

    public void displayProgressDialog(String msg) {
        mProgress = new MaterialDialog.Builder(RegistrationActivity.this).content(msg).canceledOnTouchOutside(false).progress(true, 0).show();
    }


    public void check_married() {

        radiosingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiosingle.isChecked()) {
                    maritalstatus = "Single";
                    relative_aniv.setVisibility(View.GONE);
                }
            }
        });

        radio_married.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_married.isChecked()) {
                    dateofanniversary = "";
                    maritalstatus = "Married";
                    relative_aniv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void intialise_ui() {
        html = "<font color='#FF6600'><b>" + "Not Found ? Please add.." + " </b></font>";
        relative_aniv = findViewById(R.id.relative_aniv);
        toolbar_title = findViewById(R.id.toolabr_title);
        toolbar_title.setText("Registration");
        calender_button = findViewById(R.id.image_calender);
        calender_annive = findViewById(R.id.calender_annive);
        radio_married = findViewById(R.id.radiomarried);
        radiosingle = findViewById(R.id.radiosingle);
        image_down = findViewById(R.id.iamge_down);
        downarow_pada = findViewById(R.id.downarow_pada);
        downarow_nakshatra = findViewById(R.id.downarow_nakshatra);
        radioGroup_marital = findViewById(R.id.radioprofile);
        button_register = findViewById(R.id.submit_register);
        text_dob = findViewById(R.id.text_displaydate);
        text_religion_display3 = findViewById(R.id.text_religion_display3);
        edit_name = findViewById(R.id.edit_name);
        edit_gothra = findViewById(R.id.edit_gothra);
        edit_fathers = findViewById(R.id.edit_fathers);
        edit_mothers = findViewById(R.id.edit_mothers);
        edit_lastname = findViewById(R.id.edit_lastname);
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_surname = findViewById(R.id.edit_surname);
        editmobile_number = findViewById(R.id.edit_mobile_number);
        displaydate_anniv = findViewById(R.id.displaydate_anniv);
        spinner_religion = findViewById(R.id.spinner_religion);
        spinner_pada = findViewById(R.id.spinner_pada);
        spinner_nakshatra = findViewById(R.id.spinner_nakshatra);
        relative_aniv = findViewById(R.id.relative_aniv);
        calender_button = findViewById(R.id.image_calender);
        calender_annive = findViewById(R.id.calender_annive);
        radio_married = findViewById(R.id.radiomarried);
        radiosingle = findViewById(R.id.radiosingle);
        image_down = findViewById(R.id.iamge_down);
        downarow_pada = findViewById(R.id.downarow_pada);
        downarow_nakshatra = findViewById(R.id.downarow_nakshatra);
        radioGroup_marital = findViewById(R.id.radioprofile);
        button_register = findViewById(R.id.submit_register);
        text_dob = findViewById(R.id.text_displaydate);
        text_religion_display3 = findViewById(R.id.text_religion_display3);
        edit_name = findViewById(R.id.edit_name);
        edit_gothra = findViewById(R.id.edit_gothra);
        edit_fathers = findViewById(R.id.edit_fathers);
        edit_mothers = findViewById(R.id.edit_mothers);
        edit_lastname = findViewById(R.id.edit_lastname);
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_surname = findViewById(R.id.edit_surname);
        editmobile_number = findViewById(R.id.edit_mobile_number);
        displaydate_anniv = findViewById(R.id.displaydate_anniv);
        spinner_religion = findViewById(R.id.spinner_religion);
        spinner_pada = findViewById(R.id.spinner_pada);
        spinner_nakshatra = findViewById(R.id.spinner_nakshatra);
        onclickactions();
    }

    public void onclickactions() {
        text_religion_display3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_religion.performClick();
            }
        });
        downarow_nakshatra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_nakshatra.performClick();
            }
        });

        downarow_pada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_pada.performClick();
            }
        });

    }


    public void register_user() {
        displayProgressDialog("Registering...");
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, 0);
        String token = prefs.getString("regId", "No name defined");
        @SuppressLint("HardwareIds") String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        RegistrationObjects user_register = new RegistrationObjects();
        user_register.email = user_email;
        user_register.name = user_name;
        user_register.surname = user_surname;
        user_register.firstname = user_fname;
        user_register.lastname = user_lname;
        user_register.fathername = user_ftharename;
        user_register.mothername = user_mothername;
        user_register.religion = user_religion;
        user_register.dob = dateofbirth;
        user_register.mobile = mobile_number;
        user_register.maritalstatus = maritalstatus;
        user_register.anniversary = dateofanniversary;
        user_register.gotra = gothra;
        user_register.nakshtra = user_nakshtra;
        user_register.pada = user_padha;
        user_register.device_id = device_id;
        user_register.device_token = token;

        Log.e("tokengenbe", user_email + "\n" + user_name + "\n" + device_id + "\n" + token + dateofbirth + user_religion + mobile_number + maritalstatus);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerApiInterface.Base_Url)
                .addConverterFactory(GsonConverterFactory.create()).build();

        ServerApiInterface api = retrofit.create(ServerApiInterface.class);
        Call<RegistrationObjects> login_user = api.register_user(user_register);
        login_user.enqueue(new Callback<RegistrationObjects>() {
            @Override
            public void onResponse(Call<RegistrationObjects> call, Response<RegistrationObjects> response) {
                hideProgressDialog();

                if (response.body() != null) {
                    int status_code = Integer.parseInt(response.body().getResponse());
                    Log.e("lloginuser", " " + response.body().getMessage());
                    Log.e("llogistatus", " " + response.body().getResponse());
                    if (status_code == 3) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Failed" + "\n" + response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationObjects> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Failed to register try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
