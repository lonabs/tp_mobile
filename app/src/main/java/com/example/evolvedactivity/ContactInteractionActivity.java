package com.example.evolvedactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInteractionActivity extends AppCompatActivity {

    Button contactIdButton;
    Button contactDetailButton;
    Button callContactButton;
    TextView contactDisplayTextView;

    int Perm_CTC=1;
    private final int CONTACT_PICK_REQUEST_CODE=1;

    String contactName;
    String phoneNumberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_interaction);

        contactIdButton = (Button) findViewById(R.id.contactIdButton);
        contactDetailButton = (Button) findViewById(R.id.contactDetailButton);
        callContactButton = (Button) findViewById(R.id.callContactButton);
        contactDisplayTextView = (TextView) findViewById(R.id.contactDisplayTextView);

        contactIdButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contactDisplayTextView.setText("Le résultat sera affiché ici");
                        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people"));
                        startActivityForResult(pickContactIntent, CONTACT_PICK_REQUEST_CODE);
                    }
                }
        );

        contactDetailButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String contactDisplayText = "nom du contact : "+getContactName()+"\n tel :"+getPhoneNumberStr();
                        contactDisplayTextView.setText(contactDisplayText);
                        callContactButton.setEnabled(true);
                    }
                }
        );

        callContactButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL);
                        phoneCallIntent.setData(Uri.parse("tel:"+getPhoneNumberStr()));
                        startActivity(phoneCallIntent);
                    }
                }
        );

        contactDetailButton.setEnabled(false);
        callContactButton.setEnabled(false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check the permission type using the requestCode
        if (requestCode == Perm_CTC) {
            //the array is empty if not granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GRANTED READ CONTACTS",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uriContact = data.getData();
            Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                cursor.close();

                Toast.makeText(this, "Contact: " + contactName + " (ID: " + contactId + ")", Toast.LENGTH_SHORT).show();


                String[] projection = new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE
                };
                Cursor cursor2 = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null);

                if (cursor2 != null && cursor2.moveToFirst()) {
                    String phoneNumberStr = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    setContactName(contactName);
                    setPhoneNumberStr(phoneNumberStr);
                    contactDetailButton.setEnabled(true);
                }
            }
        }
        else{
            contactDisplayTextView.setText("Opération annulée");
        }
    }

    //GETTERS AND SETTERS
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumberStr() {
        return phoneNumberStr;
    }

    public void setPhoneNumberStr(String phoneNumberStr) {
        this.phoneNumberStr = phoneNumberStr;
    }
}