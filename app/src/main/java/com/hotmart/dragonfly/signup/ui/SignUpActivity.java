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
package com.hotmart.dragonfly.signup.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.hotmart.dragonfly.authenticator.ui.AuthenticatorActivity;
import com.hotmart.dragonfly.rest.model.request.UserSignupRequestVO;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.hotmart.dragonfly.validation.UniqueEmailRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.net.HttpURLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements Validator.ValidationListener {

    private UserService mUserService;

    private Validator mValidator;

    @NotEmpty(messageResId = R.string.required_field)
    @BindView(R.id.sign_up_name)
    protected EditText mName;

    @Email(messageResId = R.string.invalid_email)
    @BindView(R.id.sign_up_email)
    protected EditText mEmail;

    @Password(min = 6, messageResId = R.string.invalid_password)
    @BindView(R.id.sign_up_password)
    protected EditText mPassword;

    @ConfirmPassword(messageResId = R.string.password_dont_match)
    @BindView(R.id.sign_up_password_confirm)
    protected EditText mPasswordConfirm;

    public static Intent createIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserService = OAuth2ServiceFactory.createAnonymousService(UserService.class);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        UniqueEmailRule uniqueEmailRule = new UniqueEmailRule(mName, mUserService);
        mValidator.put(mEmail, uniqueEmailRule);
    }

    @OnClick(R.id.sign_up_next)
    public void onClickNext() {
        mValidator.validate(true);
    }

    @Override
    public void onValidationSucceeded() {
        signUp();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected boolean isLoginRequired() {
        return false;
    }

    private void signUp() {
        UserSignupRequestVO user = new UserSignupRequestVO();
        user.setName(mName.getText().toString());
        user.setEmail(mEmail.getText().toString());
        user.setPassword(mPassword.getText().toString());

        mUserService.signUp(user).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_CREATED:
                        Bundle bundle = new Bundle();
                        bundle.putString(AuthenticatorActivity.PARAM_USER_NAME, mEmail.getText().toString());
                        bundle.putString(AuthenticatorActivity.PARAM_USER_PASS, mPassword.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Snackbar.make(mName, R.string.status_400, Snackbar.LENGTH_LONG).show();
                    default:
                        Snackbar.make(mName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(mName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                Log.e("dragonfly", t.getMessage(), t);
            }
        });
    }
}
