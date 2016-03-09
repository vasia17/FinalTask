package com.project.vasia.quotations;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    final Uri THEMES_URI = Uri.parse("content://ua.vasia.Quotations/themes");
    final Uri QUOTES_URI = Uri.parse("content://ua.vasia.Quotations/quotes");
    final Uri AUTHORS_URI = Uri.parse("content://ua.vasia.Quotations/authors");
    final Uri FAVOURITES_URI = Uri.parse("content://ua.vasia.Quotations/favourites");
    private ProgressDialog pDialog;
    // URL to get contacts JSON
    private static String url = "https://quote-collection.appspot.com/_ah/api/themeApi/v1/theme";
    // contacts JSONArray
    JSONArray themes = null;
    JSONArray quotes = null;
    JSONArray authors = null;
    // JSON Arrays names
    private static final String TAG_THEMES = "themes";
    private static final String TAG_QUOTES = "quotes";
    private static final String TAG_AUTHORS = "authors";
    // JSON Parameters names
    private static final String TAG_THEMES_ID = "id";
    private static final String TAG_THEMES_NAME = "name";
    private static final String TAG_THEMES_TIME = "udateTime";
    private static final String TAG_QUOTES_ID = "id";
    private static final String TAG_QUOTES_NAME = "quote";
    private static final String TAG_QUOTES_THEME = "themeID";
    private static final String TAG_QUOTES_TIME = "updateTimeStamp";
    private static final String TAG_QUOTES_AUTHOR = "authorID";
    private static final String TAG_AUTHORS_ID = "id";
    private static final String TAG_AUTHORS_NAME = "name";
    private static final String TAG_AUTHORS_TIME = "updateTime";

    private static ListView lvContact;
    private static Uri NEED_URI;
    private static String NEED_SELECTION;
    private static String[] NEED_PROJECTION;
    private static int Action;
    private static final int Show = 0;
    private static final int Select = 1;
    private static String SELECTED_NAME;
    private static SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvContact = (ListView) findViewById(R.id.themes_list);
        NEED_URI = THEMES_URI;
        NEED_SELECTION = null;
        NEED_PROJECTION = null;
        Action = Show;
        getLoaderManager().initLoader(0, null, this);

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (NEED_URI == THEMES_URI) {
                    String item = ((TextView) view).getText().toString();
                    Action = Select;
                    SELECTED_NAME = item;
                    NEED_PROJECTION = new String[]{"_id", "name"};
                    NEED_SELECTION = "name = '" + SELECTED_NAME + "'";
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                } else if (NEED_URI == AUTHORS_URI) {
                    String item = ((TextView) view).getText().toString();
                    Action = Select;
                    SELECTED_NAME = item;
                    NEED_PROJECTION = new String[]{"_id", "name"};
                    NEED_SELECTION = "name = '" + SELECTED_NAME + "'";
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                } else if (NEED_URI == QUOTES_URI) {

                } else if (NEED_URI == FAVOURITES_URI) {

                }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getApplicationContext(),   // Parent activity context
                NEED_URI,                  // Table to query
                NEED_PROJECTION,           // Projection to return
                NEED_SELECTION,            // No selection clause
                null,                      // No selection arguments
                null                       // Default sort order
        );


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (Action) {
            case Show:
                if (NEED_URI == THEMES_URI || NEED_URI == AUTHORS_URI) {
                    String from[] = {"name"};
                    int to[] = {R.id.text11};
                    adapter = new SimpleCursorAdapter(this,
                            R.layout.item, data, from, to);
                    lvContact.setAdapter(adapter);
                } else if (NEED_URI == QUOTES_URI) {
                    String from[] = {"quotes"};
                    int to[] = {R.id.text12};
                    adapter = new MyCursorAdapter(this,
                            R.layout.favourite_item, data, from, to, 0);
                    lvContact.setAdapter(adapter);
                } else if (NEED_URI == FAVOURITES_URI) {
                    String from[] = {"quote"};
                    int to[] = {R.id.text12};
                    adapter = new MyCursorAdapter(this,
                            R.layout.favourite_item, data, from, to, 0);
                    lvContact.setAdapter(adapter);
                }
                break;
            case Select:
                if (NEED_URI == THEMES_URI) {
                    data.moveToFirst();
                    String myitem = data.getString(0);
                    NEED_SELECTION = "theme_id = " + myitem;
                    NEED_URI = QUOTES_URI;
                    NEED_PROJECTION = null;
                    Action = Show;
                    getLoaderManager().restartLoader(0, null, this);
                } else if (NEED_URI == AUTHORS_URI) {
                    data.moveToFirst();
                    String myitem = data.getString(0);
                    NEED_SELECTION = "author_id = " + myitem;
                    NEED_URI = QUOTES_URI;
                    NEED_PROJECTION = null;
                    Action = Show;
                    getLoaderManager().restartLoader(0, null, this);
                }
                break;
            case 3:

                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public class GetContacts extends AsyncTask<Void, Void, Void> {
        private static final String LOG_TAG = "ContentProvider";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            ContentValues values1 = new ContentValues();
            ContentValues values2 = new ContentValues();
            ContentValues values3 = new ContentValues();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array THEMES
                    themes = jsonObj.getJSONArray(TAG_THEMES);
                    quotes = jsonObj.getJSONArray(TAG_QUOTES);
                    authors = jsonObj.getJSONArray(TAG_AUTHORS);
                    Cursor cursor = getContentResolver().query(THEMES_URI, null, null,
                            null, null);
                    startManagingCursor(cursor);
                    // looping through All Contacts
                    for (int i = 0; i < themes.length(); i++) {
                        JSONObject c = themes.getJSONObject(i);
                        String id = c.getString(TAG_THEMES_ID);
                        String name = c.getString(TAG_THEMES_NAME);
                        String time = c.getString(TAG_THEMES_TIME);
                        values1.put("_id", id);
                        values1.put("name", name);
                        values1.put("udatetime", time);
                        Uri newUri = getContentResolver().insert(THEMES_URI, values1);
                        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
                    }
                    cursor = getContentResolver().query(QUOTES_URI, null, null,
                            null, null);
                    startManagingCursor(cursor);
                    for (int i = 0; i < quotes.length(); i++) {
                        JSONObject c = quotes.getJSONObject(i);
                        String id = c.getString(TAG_QUOTES_ID);
                        String name = c.getString(TAG_QUOTES_NAME);
                        String themeid = c.getString(TAG_QUOTES_THEME);
                        String time = c.getString(TAG_QUOTES_TIME);
                        String authorid = c.getString(TAG_QUOTES_AUTHOR);
                        values2.put("_id", id);
                        values2.put("quotes", name);
                        values2.put("theme_id", themeid);
                        values2.put("timestamp", time);
                        values2.put("author_id", authorid);
                        Uri newUri = getContentResolver().insert(QUOTES_URI, values2);
                        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
                    }
                    cursor = getContentResolver().query(AUTHORS_URI, null, null,
                            null, null);
                    startManagingCursor(cursor);
                    for (int i = 0; i < authors.length(); i++) {
                        JSONObject c = authors.getJSONObject(i);
                        String id = c.getString(TAG_AUTHORS_ID);
                        String name = c.getString(TAG_AUTHORS_NAME);
                        String time = c.getString(TAG_AUTHORS_TIME);
                        values3.put("_id", id);
                        values3.put("name", name);
                        values3.put("timestamp", time);
                        Uri newUri = getContentResolver().insert(AUTHORS_URI, values3);
                        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
                    }
                    stopManagingCursor(cursor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            NEED_URI = THEMES_URI;
            getLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }

    public void onClickSync(MenuItem item) {
        getLoaderManager().destroyLoader(0);
        new GetContacts().execute();
    }

    public void onClickDelete(MenuItem item) {
        getContentResolver().delete(THEMES_URI, null, null);
        getContentResolver().delete(QUOTES_URI, null, null);
        getContentResolver().delete(AUTHORS_URI, null, null);
    }

    public void onClickThemes(MenuItem item) {
        NEED_URI = THEMES_URI;
        NEED_SELECTION = null;
        NEED_PROJECTION = null;
        Action = Show;
        getLoaderManager().restartLoader(0, null, this);
        closeDrawer();
    }

    public void onClickAuthors(MenuItem item) {
        NEED_URI = AUTHORS_URI;
        NEED_SELECTION = null;
        NEED_PROJECTION = null;
        Action = Show;
        getLoaderManager().restartLoader(0, null, this);
        closeDrawer();
    }

    public void onClickQuotes(MenuItem item) {
        NEED_URI = QUOTES_URI;
        NEED_SELECTION = null;
        NEED_PROJECTION = null;
        Action = Show;
        getLoaderManager().restartLoader(0, null, this);
        closeDrawer();
    }

    public void onClickFavourites(MenuItem item) {
        NEED_URI = FAVOURITES_URI;
        NEED_SELECTION = null;
        NEED_PROJECTION = null;
        Action = Show;
        getLoaderManager().restartLoader(0, null, this);
        closeDrawer();
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public class MyCursorAdapter extends SimpleCursorAdapter implements View.OnClickListener {

        protected int[] mFrom = {1};
        protected int[] mTo = {R.id.text12};
        private ViewBinder mViewBinder;
        private ImageSwitcher sw;
        private Cursor myCursor;

        public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flag) {
            super(context, layout, c, from, to, flag);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            String quotesID = cursor.getString(0);
            ContentValues valuesForFavourites = new ContentValues();
            valuesForFavourites.put("_id", quotesID);
            valuesForFavourites.put("quote", cursor.getString(1));

            sw = (ImageSwitcher) view.findViewById(R.id.imageSwitcher);
            sw.removeAllViews();
            sw.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    ImageView myView = new ImageView(getApplicationContext());
                    myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    myView.setLayoutParams(new ImageSwitcher.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                    return myView;
                }
            });
            sw.setOnClickListener(this);

            sw.setImageResource(R.drawable.star_unchecked);
            sw.setTag(false);
            cursor = getContentResolver().query(FAVOURITES_URI, null, null, null, null);
            startManagingCursor(cursor);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    if (cursor.getString(0)
                            .equals(quotesID)) {
                        sw.setImageResource(R.drawable.star_checked);
                        sw.setTag(true);
                    }
                } while (cursor.moveToNext());
            }

            sw.setTag(R.string.sw_tag_key, valuesForFavourites);
            stopManagingCursor(cursor);
        }

        @Override
        public void onClick(View view) {
            ImageSwitcher switcher = (ImageSwitcher) view;
            Cursor cursor = getContentResolver().query(FAVOURITES_URI, null, null, null, null);
            startManagingCursor(cursor);
            if (!(boolean) view.getTag()) {
                getContentResolver().insert(FAVOURITES_URI,
                        (ContentValues) view.getTag(R.string.sw_tag_key));

                switcher.setImageResource(R.drawable.star_checked);
                switcher.setTag(true);
            } else {
                long _id = -1;
                if (cursor.moveToFirst()) {
                    do {
                        String quotesID = (String) ((ContentValues) view.getTag(R.string.sw_tag_key))
                                .get("_id");
                        if (cursor.getString(0)
                                .equals(quotesID)) {
                            _id = cursor.getLong(0);
                        }
                    } while (cursor.moveToNext());
                }

                Uri uri = Uri.parse(FAVOURITES_URI + "/" + _id);
                getContentResolver().delete(uri, null, null);

                switcher.setImageResource(R.drawable.star_unchecked);
                switcher.setTag(false);
            }
        }
    }

}
