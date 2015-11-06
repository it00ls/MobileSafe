package com.it00ls.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.it00ls.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 联系人列表
 */
public class ContactActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final ArrayList<Map<String, String>> list = readContact();
        ListView lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setAdapter(new SimpleAdapter(this, list, R.layout.item_contact_list,
                new String[]{"name", "phone"}, new int[]{R.id.tv_name, R.id.tv_phone}));
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = list.get(position).get("phone");
                setResult(RESULT_OK, getIntent().putExtra("phone", phone));
                finish();
            }
        });
    }

    private ArrayList<Map<String, String>> readContact() {
        ContentResolver cr = getContentResolver();
        Cursor idCursor = cr.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"},
                null, null, null);
        ArrayList<Map<String, String>> contactList = new ArrayList<>();
        if (idCursor != null) {
            while (idCursor.moveToNext()) {
                String contact_id = idCursor.getString(idCursor.getColumnIndex("contact_id"));
                Cursor cursor = cr.query(Uri.parse("content://com.android.contacts/data"),
                        new String[]{"data1", "mimetype"}, "raw_contact_id = ?",
                        new String[]{contact_id}, null);
                if (cursor != null) {
                    Map<String, String> map = new HashMap<>();
                    while (cursor.moveToNext()) {
                        String data1 = cursor.getString(cursor.getColumnIndex("data1"));
                        String mimetype = cursor.getString(cursor.getColumnIndex("mimetype"));
                        if (mimetype.equals("vnd.android.cursor.item/name")) {
                            map.put("name", data1);
                        } else if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            map.put("phone", data1);
                        }
                    }
                    contactList.add(map);
                }
            }
        }
        return contactList;
    }
}
