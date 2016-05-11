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
package com.hotmart.dragonfly.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hotmart.dragonfly.R;

public class ButtonLoading extends LinearLayout {
    private TextView mLabel;
    private ProgressBar mLoading;
    private View mValue;
    private boolean isLoading = false;

    public ButtonLoading(Context context, AttributeSet attrs) {
        super(context, attrs);

        mValue = LayoutInflater.from(getContext()).inflate(R.layout.card, this, true);

        mLabel = (TextView) mValue.findViewById(R.id.label);
        mLoading = (ProgressBar) mValue.findViewById(R.id.loading);
    }

    public void setLoading(boolean loading) {
        isLoading = loading;

        if (loading) {
            mValue.setEnabled(false);
            mLoading.setVisibility(VISIBLE);
        } else {
            mLoading.setVisibility(GONE);
            mValue.setEnabled(true);
        }
    }

    public void setText(String text){
        mLabel.setText(text);
    }

    public boolean isLoading() {
        return isLoading;
    }
}
