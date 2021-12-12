package com.hiskysat.udpchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.hiskysat.udpchat.account.CreateAccountFragmentArgs;
import com.hiskysat.udpchat.account.CreateAccountFragmentDirections;
import com.hiskysat.udpchat.account.CreateAccountViewModel;
import com.hiskysat.udpchat.chats.ChatsFragment;
import com.hiskysat.udpchat.chats.ChatsFragmentArgs;
import com.hiskysat.udpchat.chats.ChatsFragmentDirections;
import com.hiskysat.udpchat.di.ViewModelFactoryEntryPoint;
import com.hiskysat.udpchat.message.MessageViewModel;

import dagger.hilt.android.EntryPointAccessors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelFactoryEntryPoint entryPoint = EntryPointAccessors.fromApplication(getApplicationContext(),
                ViewModelFactoryEntryPoint.class);
        MainActivityViewModel activityViewModel = new ViewModelProvider(this,
                entryPoint.viewModelFactory()).get(MainActivityViewModel.class);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_main);
        splashScreen.setKeepVisibleCondition(() -> {
            if (savedInstanceState == null) {
                activityViewModel.getAccountFoundEvent().observe(MainActivity.this, (event) -> {
                   Long accountId = event.getContentIfNotHandled();
                   if (accountId != null) {
                       if (accountId == MainActivityViewModel.ID_NO_ACCOUNT_FOUND) {
                           setStartDestination(R.id.createAccountFragment);
                       } else {
                           Bundle args = ChatsFragment.createArgs(accountId);
                           setStartDestination(R.id.chatsFragment, args);
                       }
                       setupNavBar();
                   }
                });
                activityViewModel.start();
            }
            return false;
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navContainer);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setupNavBar() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navContainer);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }

    }

    private void setStartDestination(int startDestination) {
        setStartDestination(startDestination, null);
    }

    private void setStartDestination(int startDestinationId, Bundle args) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navContainer);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavInflater inflater = navController.getNavInflater();
            NavGraph graph = inflater.inflate(R.navigation.nav_graph);
            graph.setStartDestination(startDestinationId);
            navController.setGraph(graph, args);
        }
    }


}