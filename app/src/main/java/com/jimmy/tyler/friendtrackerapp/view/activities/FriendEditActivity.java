package com.jimmy.tyler.friendtrackerapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.jimmy.tyler.friendtrackerapp.R;
import com.jimmy.tyler.friendtrackerapp.controller.friend.FriendBirthdayButtonListener;
import com.jimmy.tyler.friendtrackerapp.model.ModelImpl;
import com.jimmy.tyler.friendtrackerapp.model.interfaces.Friend;
import com.jimmy.tyler.friendtrackerapp.model.supportcode.ContactDataManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * activity for handling editing friends details
 */
public class FriendEditActivity extends AppCompatActivity {

    public static final String NEW_CONTACT = "NEW_CONTACT";
    public static final String CONTACT = "CONTACT";

    private static final int PICK_CONTACT = 688;

    private String contactName;
    private String contactEmail;
    private Calendar contactBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout to display
        setContentView(R.layout.activity_friend_edit);

        //set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);

        //check if the contact is new or already exists
        if (getIntent().getBooleanExtra(NEW_CONTACT, true)) {
            //start the system contact picker activity
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        } else {
            //get the friends instance variables
            Friend friend = ((Friend) getIntent().getSerializableExtra(CONTACT));
            contactName = friend.getName();
            contactEmail = friend.getEmail();
            contactBirthday = friend.getBirthday();

            //set the birthday field click listener
            findViewById(R.id.friend_add_birthday).setOnClickListener(new FriendBirthdayButtonListener(contactBirthday));

            //populate the fields
            populateFields();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //fixes fragment refresh bug
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the friend edit options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friend_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //save details clicked
            case R.id.friend_save:
                //convert the birthday into a calender instance
                String stringNums[] = ((TextView) findViewById(R.id.friend_add_birthday)).getText().toString().split("/");
                int day = Integer.parseInt(stringNums[0]);
                int month = Integer.parseInt(stringNums[1]);
                int year = Integer.parseInt(stringNums[2]);
                Calendar birthdayDate = new GregorianCalendar(year, month - 1, day);

                //check if the contact is new or already exists
                if (getIntent().getBooleanExtra("NEW_CONTACT", true)) {
                    //add the friend to the model
                    ModelImpl.getSingletonInstance().addFriend(contactName, contactEmail, birthdayDate);
                } else {
                    //get the existing friend and update its instance variables
                    Friend friend = ModelImpl.getSingletonInstance().getFriend(((Friend) getIntent().getSerializableExtra("CONTACT")).getFriendId());
                    friend.setBirthday(birthdayDate);
                }

                //exit the activity
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //pick system contact result returned
            case PICK_CONTACT:
                //check if the result was ok
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactUri = data.getData();

                    //get an instance of the contact manager support code
                    Intent intent = new Intent();
                    intent.setData(contactUri);
                    ContactDataManager cdm = new ContactDataManager(this, intent);

                    //get the contacts instance variables
                    try {
                        contactName = cdm.getContactName();
                        contactEmail = cdm.getContactEmail();
                        contactBirthday = new GregorianCalendar();

                        //set the birthday field click listener
                        findViewById(R.id.friend_add_birthday).setOnClickListener(new FriendBirthdayButtonListener(contactBirthday));

                        //populate the fields
                        populateFields();
                    } catch (ContactDataManager.ContactQueryException cqe) {
                        cqe.printStackTrace();
                    }
                } else {
                    //exit the activity
                    finish();
                }
        }
    }

    /**
     * populate the fields with the data
     */
    private void populateFields() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        //populate the fields in the layout
        ((TextView) findViewById(R.id.friend_add_name)).setText(contactName);
        ((TextView) findViewById(R.id.friend_add_email)).setText(contactEmail);
        ((TextView) findViewById(R.id.friend_add_birthday)).setText(sdf.format(contactBirthday.getTime()));
    }
}
