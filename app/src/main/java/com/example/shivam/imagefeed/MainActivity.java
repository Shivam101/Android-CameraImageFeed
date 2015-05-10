package com.example.shivam.imagefeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    Uri imageUri;
    int IMAGE_CONST = 1;
    ArrayList<String> sqluri,sqlcoordinate,sqladdress,sqltime;
    PostORM p = new PostORM();
    ArrayList<ArrayList<String>> holder;
    ListView feedList;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqluri = new ArrayList<>();
        sqladdress = new ArrayList<>();
        sqlcoordinate = new ArrayList<>();
        sqltime = new ArrayList<>();
        feedList = (ListView)findViewById(R.id.feedList);
        holder = new ArrayList<ArrayList<String>>();
        new SQLTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageUri = getOutputUri(IMAGE_CONST);
            if (imageUri == null) {
                Toast.makeText(MainActivity.this, R.string.storage_access_error, Toast.LENGTH_SHORT).show();
            } else {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(pictureIntent, IMAGE_CONST);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class SQLTask extends AsyncTask<String,String,ArrayList<ArrayList<String>>>
    {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading your feed ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected ArrayList<ArrayList<String>> doInBackground(String... params) {
            sqluri = p.getUrifromDB(MainActivity.this);
            sqlcoordinate = p.getCoordinatesfromDB(MainActivity.this);
            sqladdress = p.getAddressfromDB(MainActivity.this);
            sqltime = p.getTimefromDB(MainActivity.this);
            holder.add(sqluri);
            holder.add(sqlcoordinate);
            holder.add(sqladdress);
            holder.add(sqltime);
//            adapter.notifyDataSetChanged();
            return holder;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
            ArrayList<String> uris = sqluri;
            ArrayList<String> coordinates = sqlcoordinate;
            ArrayList<String> addresses = sqladdress;
            ArrayList<String> times = sqltime;
            adapter = new PostAdapter(MainActivity.this,R.layout.list_item,uris,coordinates,addresses,times);
            feedList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            pDialog.dismiss();

        }
    }

    private Uri getOutputUri(int mediaType) {
        if (hasExternalStorage()) {
            // get external storage directory
            String appName = MainActivity.this.getString(R.string.app_name);
            File extStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),appName);

            // create subdirectory
            if(!extStorageDir.exists())
            {
                if(!extStorageDir.mkdirs())
                {
                    Toast.makeText(MainActivity.this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                }
            }
            File mFile;
            Date mCurrentDate = new Date();
            String mTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(mCurrentDate);
            String path = extStorageDir.getPath() + File.separator;
            if(mediaType == IMAGE_CONST) {
                mFile = new File(path + "FEEDIMG_" + mTimestamp + ".jpg");
            }
            else
            {
                return null;
            }
            // return the file's URI
            return Uri.fromFile(mFile);
        } else {
            return null;
        }
    }

    private boolean hasExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Intent galleryAddIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            galleryAddIntent.setData(imageUri);
            sendBroadcast(galleryAddIntent);

            Intent sendIntent = new Intent(MainActivity.this,DetailsActivity.class);
            sendIntent.setData(imageUri);
            //sendIntent.putExtra("FILE_URI", imageUri);
            startActivity(sendIntent);
        }
        else if(resultCode != RESULT_CANCELED)
        {
            Toast.makeText(this, "There was an error", Toast.LENGTH_SHORT).show();
        }
    }
}
