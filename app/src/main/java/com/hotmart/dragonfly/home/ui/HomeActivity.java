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
package com.hotmart.dragonfly.home.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.places.ui.MyAddressActivity;
import com.hotmart.dragonfly.rest.model.response.CheckLastVO;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.hotmart.dragonfly.ui.LastDateLoader;

import org.joda.time.DateTime;
import org.joda.time.Days;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<CheckLastVO>{

    @BindView(R.id.last_verification_time)
    TextView lastVerificationTime;

    @BindView(R.id.last_verification)
    TextView verification;

    public static Intent createIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected int getSelfNavigationMenuItem() {
        return R.id.nav_check;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        setTitle(R.string.empty);
        ButterKnife.bind(this);

        checkLastDate();
    }

    private void checkLastDate() {
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @OnClick(R.id.btn_check)
    public void onClickBtnCheck(View view) {
        startActivity(MyAddressActivity.createIntent(this));
    }

    @Override
    public Loader<CheckLastVO> onCreateLoader(int id, Bundle args) {

        return new LastDateLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<CheckLastVO> loader, CheckLastVO data) {
        if (data != null){

            DateTime date = new DateTime(data.getVerificationDate());
            DateTime today = new DateTime();

            int days = Days.daysBetween(date, today).getDays();

            lastVerificationTime.setVisibility(View.VISIBLE);
            lastVerificationTime.setText(getString(R.string.x_period, days));
            verification.setText(getString(R.string.last_checking));
        } else {
            verification.setText(getString(R.string.first_check));
        }
    }

    @Override
    public void onLoaderReset(Loader<CheckLastVO> loader) {

    }
}
