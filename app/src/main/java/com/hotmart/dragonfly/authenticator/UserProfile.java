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
import android.content.Context;

public class UserProfile {

    private final Long mId;
    private final String mName;
    private final String mEmail;
    private String mUrlPhoto;

    private UserProfile(Long id, String name, String email, String urlPhoto) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
        this.mUrlPhoto = urlPhoto;
    }

    private UserProfile(Long id, String name, String email) {
        this.mId = id;
        this.mName = name;
        this.mEmail = email;
    }

    public static final UserProfile getInstance(Context context) {
        AccountManager mAccountManager = AccountManager.get(context);
        Account[] accounts = mAccountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account mAccount = accounts[0];

            return new UserProfile(
                    Long.parseLong(mAccountManager.getUserData(mAccount, "id")),
                    mAccountManager.getUserData(mAccount, "name"),
                    mAccount.name
            );
        }

        return null;
    }

    public Long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUrlPhoto() {
        return mUrlPhoto;
    }

}
