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
package com.hotmart.dragonfly.invite.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteActivity extends BaseActivity {

    @BindView(R.id.bottom_sheet)
    BottomSheetLayout mBottomSheet;

    private IntentPickerSheetView mIntentPickerSheet;

    public static Intent createIntent(Context context) {
        return new Intent(context, InviteActivity.class);
    }

    @Override
    protected int getSelfNavigationMenuItem() {
        return R.id.nav_invite;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_activity);
        ButterKnife.bind(this);
        configureGui();
        mBottomSheet.showWithSheetView(mIntentPickerSheet);
    }

    protected void configureGui() {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        mIntentPickerSheet = new IntentPickerSheetView(this, shareIntent, R.string.share_with,
        new IntentPickerSheetView.OnIntentPickedListener() {
           @Override
           public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
               mBottomSheet.dismissSheet();
               String message;
               if (activityInfo.label.equals("Facebook")) {
                   message = getString(R.string.hackathon_url);
               } else {
                   message = String.format(getString(R.string.share_message), getString(R.string.app_name));
               }
               shareIntent.putExtra(Intent.EXTRA_TEXT, message);
               startActivity(activityInfo.getConcreteIntent(shareIntent));
           }
        });

        mIntentPickerSheet.setFilter(new IntentPickerSheetView.Filter() {
            @Override
            public boolean include(IntentPickerSheetView.ActivityInfo info) {
                return !info.componentName.getPackageName().startsWith("com.android");
            }
        });
    }
}
