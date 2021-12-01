package com.hiskysat.udpchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.hiskysat.udpchat.account.CreateAccountFragmentDirections;
import com.hiskysat.udpchat.account.CreateAccountViewModel;
import com.hiskysat.udpchat.message.MessageViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityViewModel activityViewModel = new ViewModelProvider(this,
                ViewModelFactory.getInstance(getApplication())).get(MainActivityViewModel.class);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        splashScreen.setKeepVisibleCondition(() -> {
            if (savedInstanceState == null) {
                activityViewModel.getBootNavigationEvent().observe(MainActivity.this, (event) -> {
                   Integer navigation = event.getContentIfNotHandled();
                   if (navigation != null) {
                       switch (navigation) {
                           case MainActivityViewModel.BOOT_CREATE_ACCOUNT:
                               setStartDestination(R.id.createAccountFragment);
                               break;
                           case MainActivityViewModel.BOOT_MAIN_PAGE:
                               setStartDestination(R.id.chatsFragment);
                               break;
                       }
                   }
                });
                activityViewModel.start();
            }
            return false;
        });


    }

    private void setStartDestination(int startDestinationId) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navContainer);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavInflater inflater = navController.getNavInflater();
            NavGraph graph = inflater.inflate(R.navigation.nav_graph);
            graph.setStartDestination(startDestinationId);
            navController.setGraph(graph);
        }
    }

}