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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.util.Log;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class HttpAuthenticator implements Authenticator {

    private AccountManager mAccountManager;

    public HttpAuthenticator(AccountManager mAccountManager) {
        this.mAccountManager = mAccountManager;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        Account[] accounts = mAccountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        if (accounts.length != 0) {
            String oldToken = mAccountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);
            if (oldToken != null) {
                mAccountManager.invalidateAuthToken(AuthConstants.ACCOUNT_TYPE, oldToken);
            }
            try {
                String token = mAccountManager.blockingGetAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE, false);
                if (token == null) {
                    // TODO verify deprecation
                    mAccountManager.removeAccount(accounts[0], null, null);
                }
            } catch (OperationCanceledException e) {
                Log.e("dragonfly", e.getMessage(), e);
            } catch (AuthenticatorException e) {
                Log.e("dragonfly", e.getMessage(), e);
            }
        }

        return response.request().newBuilder().build();
    }
}
