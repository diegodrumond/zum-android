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

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.UserProfile;
import com.hotmart.dragonfly.authenticator.ui.EditUserActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationMenuViewModel {

    private final View mView;
    private final UserProfile mUserProfile;

    @BindView(R.id.nav_profile_name)
    protected TextView mName;

    @BindView(R.id.nav_profile_email)
    protected TextView mEmail;

    public NavigationMenuViewModel(View viewRoot) {
        ButterKnife.bind(this, viewRoot);
        this.mView = viewRoot;
        this.mUserProfile = UserProfile.getInstance(viewRoot.getContext());
    }

    public void update() {
        if (mUserProfile != null) {
            mName.setText(mUserProfile.getName());
            mEmail.setText(mUserProfile.getEmail());
        }
    }

    @OnClick(R.id.nav_profile_config)
    public void onClickProfileConfig() {
        Intent intent = new Intent(mView.getContext(), EditUserActivity.class);
        mView.getContext().startActivity(intent);
    }
}
