package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

/**
 * MainActivity - handles toolbar, drawer menu, and fragment navigation.
 * Author: Nair Parvathi
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * onCreate method to initialize the activity
     *
     * @param savedInstanceState : is a reference to a Bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderToolbar(); // Render toolbar
        setDefaultFragment(); // Set default fragment
    }

    /**
     * Sets the default fragment (Home) if it is not present
     */
    private void setDefaultFragment() {
        Home homeFragment = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, homeFragment, "HOME")
                .commit();
    }

    /**
     * Renders the toolbar and menu items
     *
     * @param menu Menu object to inflate
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Creates and renders the activity toolbar
     */
    private void renderToolbar() {
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar); // Causes Android to call onCreateOptionsMenu(Menu menu)
        toolbar.bringToFront(); // Brings toolbar layout to the top

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.background_dark));
        toolbar.getOverflowIcon().setTint(getResources().getColor(android.R.color.background_dark));

        // Add navigation drawer
        renderDrawerMenu(toolbar);
    }

    /**
     * Creates and renders the DrawerMenu
     *
     * @param toolbar Toolbar instance
     */
    private void renderDrawerMenu(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Handles the event when an option menu item is selected
     *
     * @param item Toolbar menu item
     * @return true if the item is handled
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); // Begin FragmentTransaction
        ft.setReorderingAllowed(true);

        final Fragment homeFragment = new Home();
        final Fragment savedFragment = new SavedEvents();
        final Fragment detailFragment = new Details();

        if (item.getItemId() == R.id.help) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            String tagName = fragments.get(0).getTag();
            switch (tagName) {
                case "HOME":
                    showHelpDialogue("The User enters the city name and the distance, " +
                            "then when clicking the 'FIND' button, " +
                            "a list of nearby events will appear. By clicking each event, " +
                            "the user will see event details!");
                    break;
                case "SAVED":
                    showHelpDialogue("This section shows saved events. " +
                            "You can delete saved events by clicking the DEL button.");
                    break;
                case "DETAIL":
                    showHelpDialogue("Click on an item to view the event details. " +
                            "There is a link to redirect to the event's webpage, and you can save it.");
                    break;
            }
        } else if (item.getItemId() == R.id.tool_home) {
            ft.replace(R.id.frameLayout, homeFragment, "HOME");
            ft.commit();
        } else if (item.getItemId() == R.id.tool_saved) {
            ft.replace(R.id.frameLayout, savedFragment, "SAVED");
            ft.commit();
        } else if (item.getItemId() == R.id.tool_lastSearched) {
            ft.replace(R.id.frameLayout, detailFragment, "DETAIL");
            ft.commit();
        }
        return true;
    }

    /**
     * Displays a help dialogue
     *
     * @param helpText The help text to display
     */
    public void showHelpDialogue(String helpText) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Help")
                .setMessage(helpText)
                .setCancelable(true)
                .setNegativeButton("Close", (dialog, id) -> dialog.cancel())
                .create()
                .show();
    }

    /**
     * Handles the event when a navigation item is selected
     *
     * @param item Navigation menu item
     * @return true if the item is handled
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setReorderingAllowed(true);

        final Fragment homeFragment = new Home();
        final Fragment savedFragment = new SavedEvents();
        final Fragment detailFragment = new Details();

        if (item.getItemId() == R.id.home) {
            ft.replace(R.id.frameLayout, homeFragment, "HOME");
            ft.commit();
        } else if (item.getItemId() == R.id.savedEvents) {
            ft.replace(R.id.frameLayout, savedFragment, "SAVED");
            ft.commit();
        } else if (item.getItemId() == R.id.lastSearchedEvent) {
            ft.replace(R.id.frameLayout, detailFragment, "DETAIL");
            ft.commit();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Handles back button press to navigate through fragments
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
