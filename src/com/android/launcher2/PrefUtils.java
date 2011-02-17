/*
 * Copyright (C) 2011 Dustin Jorge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher2;

import com.android.launcher.R;
import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;

public class PrefUtils{

    private SharedPreferences sp;
    private Context c;

    public PrefUtils( Context c ){
        this.c = c;
    }

    public SharedPreferences getSharedPrefs(){
        if( this.sp == null ){
            this.sp = PreferenceManager.getDefaultSharedPreferences( this.c );
        }
        return this.sp;
    }

    public String getPrefString( int key, int defKey ){
        return this.getSharedPrefs().getString(
                this.c.getString( key ),
                this.c.getString( defKey ) );
    }

    public boolean getBooleanPrefEquals( int key, int defKey, String eq ){
        return this.getSharedPrefs().getString(
                this.c.getString( key ),
                this.c.getString( defKey ) ).equals( eq );
    }

    public boolean getBooleanPref( int key, int defKey ){
        return Boolean.parseBoolean( this.getSharedPrefs().getString(
                this.c.getString( key ),
                this.c.getString( defKey ) ) );
    }

}
