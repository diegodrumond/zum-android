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
package com.hotmart.dragonfly.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class SharePreferencesUtils {

    public static final String TAG = "SharedPreferencesHelper";

    public static Map<String, ?> readAll(Context context, String file) {

        Map<String, ?> prefs = null;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            prefs = sharedPrefs.getAll();

        } catch (Exception e) {
            prefs = null;
            Log.e(TAG, e.getMessage(), e);
        }

        return prefs;
    }

    public static boolean read(Context context, String file, String key,
            boolean defaultValue) {

        boolean value = false;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            value = sharedPrefs.getBoolean(key, defaultValue);

        } catch (Exception e) {
            value = defaultValue;
            Log.e(TAG, e.getMessage(), e);
        }

        return value;
    }

    public static String read(Context context, String file, String key,
            String defaultValue) {

        String value = null;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            value = sharedPrefs.getString(key, defaultValue);

        } catch (Exception e) {
            value = defaultValue;
            Log.e(TAG, e.getMessage(), e);
        }

        return value;
    }

    public static long read(Context context, String file, String key, long defaultValue) {

        long value = Long.MIN_VALUE;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            value = sharedPrefs.getLong(key, defaultValue);

        } catch (Exception e) {
            value = defaultValue;
            Log.e(TAG, e.getMessage(), e);
        }

        return value;
    }

    public static boolean write(Context context, String file, String key, boolean value) {

        boolean sucess = false;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putBoolean(key, value);

            editor.commit();

            sucess = true;

        } catch (Exception e) {
            sucess = false;
            Log.e(TAG, e.getMessage(), e);
        }

        return sucess;
    }

    public static boolean write(Context context, String file, String key, String value) {

        boolean sucess = false;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putString(key, value);

            editor.commit();

            sucess = true;

        } catch (Exception e) {
            sucess = false;
            Log.e(TAG, e.getMessage(), e);
        }

        return sucess;
    }

    public static boolean write(Context context, String file, String key, long value) {

        boolean sucess = false;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putLong(key, value);

            editor.commit();

            sucess = true;

        } catch (Exception e) {
            sucess = false;
            Log.e(TAG, e.getMessage(), e);
        }

        return sucess;
    }

    public static boolean remove(Context context, String file, String key) {

        boolean sucess = false;

        try {

            SharedPreferences sharedPrefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.remove(key);

            editor.commit();

            sucess = true;

        } catch (Exception e) {
            sucess = false;
            Log.e(TAG, e.getMessage(), e);
        }

        return sucess;
    }

}
