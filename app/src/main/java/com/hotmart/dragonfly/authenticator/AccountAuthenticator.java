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
package com.hotmart.dragonfly.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.model.AccessToken;
import com.hotmart.dragonfly.authenticator.service.OAuth2Service;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.ui.AuthenticatorActivity;

import retrofit2.Response;

public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private Context mContext;

    private OAuth2Service mOAuth2Service;

    public AccountAuthenticator(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mOAuth2Service = OAuth2ServiceFactory.createAnonymousService(OAuth2Service.class);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d("dragonfly", "getAuthToken() account="+account.name+ " type="+account.type);

        final Bundle bundle = new Bundle();

        // If the caller requested an authToken type we don't support, then
        // return an error
        if (!authTokenType.equals(AuthConstants.ACCOUNT_TYPE)) {
            Log.d("dragonfly", "invalid authTokenType" + authTokenType);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return bundle;
        }

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken
        final AccountManager accountManager = AccountManager.get(mContext);
        // Password is storing the refresh token
        final String password = accountManager.getPassword(account);
        if (password != null) {
            Log.i("dragonfly", "Trying to refresh access token");
            try {
                AccessToken accessToken;
                Response<AccessToken> call = mOAuth2Service.refreshAccessToken(password, AuthConstants.CLIENT_AUTHORIZATION).execute();
                switch (call.code()) {
                    case 200:
                        accessToken = call.body();
                        break;
                    default:
                        throw new RuntimeException(mContext.getString(R.string.authentication_failed));
                }

                if (accessToken != null && !TextUtils.isEmpty(accessToken.getAccessToken())) {
                    bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                    bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, AuthConstants.ACCOUNT_TYPE);
                    bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken.getAccessToken());
                    accountManager.setPassword(account, accessToken.getRefreshToken());
                    return bundle;
                }
            } catch (Exception e) {
                Log.e("dragonfly", "Failed refreshing token.", e);
            }
        }

        // Otherwise... start the login intent
        Log.i("dragonfly", "Starting login activity");
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType.equals(AuthConstants.ACCOUNT_TYPE) ? authTokenType : null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
