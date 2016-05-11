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
package com.hotmart.dragonfly.validation;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.mobsandgeeks.saripaar.QuickRule;

import java.io.IOException;

import retrofit2.Response;

public class UniqueEmailRule extends QuickRule<EditText> {

    private View mView;

    private UserService mUserService;

    public UniqueEmailRule(View view, UserService userService) {
        this.mView = view;
        this.mUserService = userService;
    }

    @Override
    public boolean isValid(EditText editText) {
        if ("".equals(editText.getText().toString().trim())) {
            return false;
        }

        try {
            Response<Void> response = mUserService.checkEmailAvailability(editText.getText().toString()).execute();
            switch (response.code()) {
                case 200:
                    return false;
                case 400:
                    Snackbar.make(mView, R.string.status_400, Snackbar.LENGTH_LONG).show();
                case 404:
                    return true;
                default:
                    Snackbar.make(mView, R.string.status_500, Snackbar.LENGTH_LONG).show();
            }
        } catch(IOException e) {
            Snackbar.make(mView, R.string.status_500, Snackbar.LENGTH_LONG).show();
            Log.e("dragonfly", e.getMessage(), e);
        }

        return false;
    }

    @Override
    public String getMessage(Context context) {
        return mView.getContext().getString(R.string.email_unavailable);
    }
}
