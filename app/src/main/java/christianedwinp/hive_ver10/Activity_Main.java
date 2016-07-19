package christianedwinp.hive_ver10;

import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.roughike.bottombar.BottomBar;
import android.view.Menu;

import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;


public class Activity_Main extends AppCompatActivity {
    //Declaring Local Variable
    private android.support.design.widget.CoordinatorLayout coordinatorLayout;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

//        CREATING BOTTOM BAR
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.Mainpage); //casting the view into an object
        //bottomBar = BottomBar.attachShy(coordinatorLayout, findViewById(R.id.myScrollingContent), savedInstanceState); -> To implement hide bottom bar when scrolling
        bottomBar = BottomBar.attach(this, savedInstanceState);//create bottom bar object
        bottomBar.setItems(R.menu.mainpage_menu);
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            //when click tab menu first time, view fragment move to the designated tab
            @Override
            public void onMenuTabSelected(@IdRes int itemId) {
                switch (itemId) {
                    case R.id.home_item:
                        Activity_home homeFragment = new Activity_home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.Mainpage, homeFragment).commit();
                        break;
                    case R.id.drone_item:
                        Activity_Mydrone droneFragment = new Activity_Mydrone();
                        getSupportFragmentManager().beginTransaction().replace(R.id.Mainpage, droneFragment).commit();
                        break;
                    case R.id.gallery_item:
                        Activity_gallery galleryFragment = new Activity_gallery();
                        getSupportFragmentManager().beginTransaction().replace(R.id.Mainpage, galleryFragment).commit();
                        break;
                    case R.id.explore_item:
                        Activity_Explore exploreFragment = new Activity_Explore();
                        getSupportFragmentManager().beginTransaction().replace(R.id.Mainpage, exploreFragment).commit();
                        break;
                }
            }

            //snack bar message pop up when user click menu selected twice or more
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch (menuItemId) {
                    case R.id.home_item:
                        Snackbar.make(coordinatorLayout, "You are already at home page", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.drone_item:
                        Snackbar.make(coordinatorLayout, "You are already at drone page", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.gallery_item:
                        Snackbar.make(coordinatorLayout, "You are already at gallery page", Snackbar.LENGTH_LONG).show();
                        break;
                    case R.id.explore_item:
                        Snackbar.make(coordinatorLayout, "You are already at explore page", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });

        // Make a Badge for the first tab, with red background color and a value of "13".
        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(0, "#FF0000", 13);
        unreadMessages.show();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainpage_menu, menu);
        return true;
    }

}
