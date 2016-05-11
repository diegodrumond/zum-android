/*
 * This file is part of Zum.
 * 
 * Zum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Zum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.hotmart.dragonfly.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.about.ui.AboutActivity;
import com.hotmart.dragonfly.authenticator.AuthConstants;
import com.hotmart.dragonfly.authenticator.ui.AuthenticatorActivity;
import com.hotmart.dragonfly.check.ui.CheckLogActivity;
import com.hotmart.dragonfly.home.ui.HomeActivity;
import com.hotmart.dragonfly.invite.ui.InviteActivity;
import com.hotmart.dragonfly.places.ui.PlacesActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnAccountsUpdateListener {

    // delay to launch nav drawer item, to allow close animation to play
    private static final int NAVIGATION_MENU_LAUNCH_DELAY = 250;

    private AccountManager mAccountManager;
    private NavigationMenuViewModel mNavigationMenuViewModel;

    protected Handler mHandler;

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Nullable
    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawer;

    @Nullable
    @BindView(R.id.nav_view)
    protected NavigationView mNavigationView;

    protected Unbinder mButterKnifeUnbinder;

    protected int getSelfNavigationMenuItem() {
        return R.id.invalid_menu;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mAccountManager = AccountManager.get(getBaseContext());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mButterKnifeUnbinder = ButterKnife.bind(this);
        setupViews();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mButterKnifeUnbinder = ButterKnife.bind(this, view);
        setupViews();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mButterKnifeUnbinder = ButterKnife.bind(this, view);
        setupViews();
    }

    private void setupViews() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        if (mDrawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
            MenuItem menuItem = mNavigationView.getMenu().findItem(getSelfNavigationMenuItem());
            if (menuItem != null) {
                menuItem.setChecked(true);
            }

            mNavigationMenuViewModel = new NavigationMenuViewModel(mNavigationView.getHeaderView(0));
            mNavigationMenuViewModel.update();
        }
    }

    @Override
    public void setTitle(int resId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resId);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        if (mDrawer == null) {
            return false;
        }

        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == getSelfNavigationMenuItem()) {
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }

        // Aplica um atraso antes de abrir uma Activity do Drawer Menu
        // para mostrar a animação.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.nav_check) {
                    createBackStack(HomeActivity.createIntent(BaseActivity.this));
                } else if (id == R.id.nav_places) {
                    createBackStack(PlacesActivity.createIntent(BaseActivity.this));
                } else if (id == R.id.nav_check_log) {
                    createBackStack(CheckLogActivity.createIntent(BaseActivity.this));
                } else if (id == R.id.nav_invite) {
                    createBackStack(InviteActivity.createIntent(BaseActivity.this));
                } else if (id == R.id.nav_about) {
                    createBackStack(AboutActivity.createIntent(BaseActivity.this));
                }
            }
        }, NAVIGATION_MENU_LAUNCH_DELAY);

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Enables back navigation for activities that are launched from the NavBar. See
     * {@code AndroidManifest.xml} to find out the parent activity names for each activity.
     * @param intent
     */
    private void createBackStack(Intent intent) {
        final Bundle bundle = ActivityOptionsCompat
                .makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
                .toBundle();

        TaskStackBuilder builder = TaskStackBuilder.create(this);
        builder.addNextIntentWithParentStack(intent);
        builder.startActivities(bundle);
    }

    protected boolean isDrawerOpen() {
        return mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeDrawer() {
        if (mDrawer != null) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    protected boolean isLoginRequired() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isLoginRequired()) {
            mAccountManager.addOnAccountsUpdatedListener(this, null, true);
        }
    }

    @Override
    protected void onPause() {
        if (isLoginRequired()) {
            mAccountManager.removeOnAccountsUpdatedListener(this);
        }
        super.onPause();
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        for (Account account : accounts) {
            if (AuthConstants.ACCOUNT_TYPE.equals(account.type)) {
                return;
            }
        }

        // No accounts so start the authenticator activity
        Intent intent = new Intent(this, AuthenticatorActivity.class);
        startActivity(intent);
        finish();
    }
}
