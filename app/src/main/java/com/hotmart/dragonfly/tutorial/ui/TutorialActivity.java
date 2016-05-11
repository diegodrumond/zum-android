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
package com.hotmart.dragonfly.tutorial.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.home.ui.HomeActivity;
import com.hotmart.dragonfly.tools.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends AppCompatActivity {

    @BindView(R.id.tutorial_viewpage)
    ViewPager mTutorialViewpage;
    @BindView(R.id.button_container)
    LinearLayout mButtonContainer;
    @BindView(R.id.tutorial_pular)
    TextView mTutorialPular;
    @BindView(R.id.tutorial_anterior)
    TextView mTutorialAnterior;
    @BindView(R.id.tutorial_proximo)
    TextView mTutorialProximo;
    @BindView(R.id.indicator_1)
    LinearLayout mIndicator1;
    @BindView(R.id.indicator_2)
    LinearLayout mIndicator2;
    private TutorialPagerAdapter mPagerAdapter;
    private Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_activity);
        ButterKnife.bind(this);
        mPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        session = new Session(getApplicationContext());
        mTutorialViewpage.setAdapter(mPagerAdapter);
        mIndicator2.setEnabled(false);

        if (session.isFirstAccess()) {
            session.setFirstAccess(false);
        } else {
            openHome();
        }

        mTutorialViewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mTutorialAnterior.setVisibility(View.GONE);
                    mTutorialProximo.setVisibility(View.VISIBLE);
                    mTutorialProximo.setText(getString(R.string.tutorial_next));
                    mIndicator1.setEnabled(true);
                    mIndicator2.setEnabled(false);
                } else if (position == 1) {
                    mTutorialAnterior.setVisibility(View.VISIBLE);
                    mTutorialProximo.setVisibility(View.VISIBLE);
                    mTutorialProximo.setText(getString(R.string.tutorial_comecar));
                    mIndicator1.setEnabled(false);
                    mIndicator2.setEnabled(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick(R.id.tutorial_anterior)
    public void onClickTutorialAnterior(View view) {
        mTutorialViewpage.setCurrentItem(mTutorialViewpage.getCurrentItem() - 1);
    }

    @OnClick(R.id.tutorial_proximo)
    public void onClickTutorialProximo(View view) {
        if (mTutorialViewpage.getCurrentItem() == (mPagerAdapter.getCount() - 1)) {
            openHome();
        } else {
            mTutorialViewpage.setCurrentItem(mTutorialViewpage.getCurrentItem() + 1);
        }
    }

    @OnClick(R.id.tutorial_pular)
    public void onClickJump(View v) {
        openHome();
    }

    private void openHome() {
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }
}
