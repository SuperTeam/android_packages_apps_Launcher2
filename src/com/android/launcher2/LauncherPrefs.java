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

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.content.Intent;
import android.util.Log;

/**
 * @author Dustin
 */
public class LauncherPrefs extends PreferenceActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    this.addPreferencesFromResource(R.xml.launcher_prefs);
	}

    @Override
    public void onPause(){
        super.onPause();   
        this.finish();
    }
	
	@Override
	public boolean onPreferenceTreeClick( PreferenceScreen ps, Preference p){
		if( p.getKey() != null ){
				
		}
		return super.onPreferenceTreeClick(ps,p);		
	}
	
	@Override
	protected void onActivityResult( int rCode, int resCode, Intent data ){
		super.onActivityResult( rCode, resCode, data );
	}

}

