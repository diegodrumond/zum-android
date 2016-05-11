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
package com.hotmart.dragonfly.authenticator.ui;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.AuthConstants;
import com.hotmart.dragonfly.authenticator.model.AccessToken;
import com.hotmart.dragonfly.authenticator.service.FacebookService;
import com.hotmart.dragonfly.authenticator.service.OAuth2Service;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.hotmart.dragonfly.home.ui.HomeActivity;
import com.hotmart.dragonfly.places.ui.PlacesActivity;
import com.hotmart.dragonfly.rest.model.response.UserResponseVO;
import com.hotmart.dragonfly.signup.ui.SignUpActivity;
import com.hotmart.dragonfly.profile.ui.ChangePasswordActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.sromku.simple.fb.SimpleFacebook;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class AuthenticatorActivity extends AccountAuthenticatorActivity implements Validator.ValidationListener {

    public static final int SIGN_UP_REQUEST = 1;

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public static final String PARAM_USER_NAME = "USER_NAME";
    public static final String PARAM_USER_PASS = "USER_PASS";
    private static final String PREF_FIRST_RUN = "FIRST_RUN";

    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private OAuth2Service mOAuth2Service;
    private ProgressDialog mLoadDialog;
    private FacebookService facebookService;

    private Validator mValidator;

    private SimpleFacebook mSimpleFacebook;

    @BindView(R.id.login_form)
    protected LinearLayout mLoginForm;

    @Email(messageResId = R.string.invalid_email)
    @BindView(R.id.accountName)
    protected TextInputEditText mAccountName;

    @Password(min = 6, messageResId = R.string.invalid_password)
    @BindView(R.id.accountPassword)
    protected TextInputEditText mAccountPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);


        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mOAuth2Service = OAuth2ServiceFactory.createAnonymousService(OAuth2Service.class);
        mAccountManager = AccountManager.get(getBaseContext());

        mAuthTokenType = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        if (mAuthTokenType == null) {
            mAuthTokenType = AuthConstants.AUTHTOKEN_TYPE;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_UP_REQUEST && resultCode == RESULT_OK) {
            mAccountName.setText(data.getStringExtra(PARAM_USER_NAME));
            mAccountPassword.setText(data.getStringExtra(PARAM_USER_PASS));

            mLoginForm.setVisibility(View.GONE);
            mLoadDialog = ProgressDialog.show(AuthenticatorActivity.this, null, getString(R.string.loading), true);

            signIn(true);
        } else {
            mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick(R.id.signIn)
    public void onClickSignIn() {
        mValidator.validate();
    }

    @OnClick(R.id.signUp)
    public void onClickSignUp() {
        startActivityForResult(SignUpActivity.createIntent(this), SIGN_UP_REQUEST);
    }

    @OnClick(R.id.facebook)
    public void onClickFacebook(View view) {
        facebookService.login();
    }

    @OnClick(R.id.signInHelp)
    public void onClickSignInHelp(View view) {
        Intent redefinirSenhaIntent = new Intent(this,ChangePasswordActivity.class);
        startActivity(redefinirSenhaIntent);
    }

    @Override
    public void onValidationSucceeded() {
        signIn(false);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                showError(view, message);
            }
        }
    }

    // TODO verifiy template to change snackbar text color
    private void showError(View view, int res) {
        Snackbar snack = Snackbar.make(view, res, Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    // TODO verifiy template to change snackbar text color
    private void showError(View view, String message) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackView = snack.getView();
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }


    private void signIn(final boolean firstLogin) {
        final String username = mAccountName.getText().toString();
        final String password = mAccountPassword.getText().toString();
        final String accountType = mAuthTokenType;

        // TODO refactor
        new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {

                Bundle data = new Bundle();
                try {
                    AccessToken accessToken;
                    Response<AccessToken> response = mOAuth2Service.getAccessToken(username, password, AuthConstants.CLIENT_AUTHORIZATION).execute();
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        accessToken = response.body();
                    } else {
                        throw new RuntimeException(getString(R.string.authentication_failed));
                    }

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, accessToken.getAccessToken());
                    data.putString(PARAM_USER_PASS, accessToken.getRefreshToken());

                    Account account = addOrFindAccount(username, accessToken.getAccessToken(), accessToken.getRefreshToken());

                    mAccountManager.setAuthToken(account, AuthConstants.AUTHTOKEN_TYPE, accessToken.getAccessToken());

                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (mLoadDialog != null) {
                    mLoadDialog.dismiss();
                }
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    showError(mLoginForm, intent.getStringExtra(KEY_ERROR_MESSAGE));
                    mLoginForm.setVisibility(View.VISIBLE);
                } else {
                    finishLogin(firstLogin, intent);
                }
            }
        }.execute();
    }

    private Account addOrFindAccount(String email, String accessToken, String refreshToken) {
        Account[] accounts = mAccountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        Account account = accounts.length != 0 ? accounts[0] :
                new Account(email, AuthConstants.ACCOUNT_TYPE);

        if (accounts.length == 0) {
            mAccountManager.addAccountExplicitly(account, refreshToken, getUserProfile("Bearer " + accessToken));
        } else {
            mAccountManager.setPassword(accounts[0], refreshToken);
        }

        return account;
    }

    private Bundle getUserProfile(String token) {
        Bundle bundle = new Bundle();
        UserService userService = OAuth2ServiceFactory.createAnonymousService(UserService.class);
        try {
            Response<UserResponseVO> response = userService.getUserInfo(token).execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                UserResponseVO profile = response.body();
                bundle.putString("id", String.valueOf(profile.getId()));
                bundle.putString("name", profile.getName());
            } else {
                // TODO create specific exception
                throw new IOException(response.message());
            }
        } catch (IOException e) {
            Log.e("dragonfly", e.getMessage(), e);
            showError(mLoginForm, R.string.status_500);
        }

        return bundle;
    }

    private void finishLogin(boolean firstLogin, Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        String password = intent.getStringExtra(PARAM_USER_PASS);

        final Intent authIntent = new Intent();
        authIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
        authIntent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AuthConstants.ACCOUNT_TYPE);
        if (authToken != null) {
            authIntent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
        }
        authIntent.putExtra(AccountManager.KEY_PASSWORD, password);
        setAccountAuthenticatorResult(authIntent.getExtras());
        setResult(Activity.RESULT_OK, authIntent);

        if (firstLogin) {
            startActivity(new Intent(getBaseContext(), PlacesActivity.class));
        } else {
            startActivity(new Intent(getBaseContext(), HomeActivity.class));
        }

        finish();
    }

    public void defineSimpleFacebook(SimpleFacebook mSimpleFacebook){
       this.mSimpleFacebook =  mSimpleFacebook;
    }

    @Override
    protected void onResume() {
        super.onResume();
        facebookService = new FacebookService(this);
    }
}