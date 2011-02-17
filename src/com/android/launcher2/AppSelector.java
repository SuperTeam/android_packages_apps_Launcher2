/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.view.ViewGroup;
import android.content.Context;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import java.util.List;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.os.Handler;
import java.lang.Runnable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import java.util.Collections;
import java.util.Comparator;
import android.app.Activity;

import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.NullPointerException;


import com.android.launcher.R;

/**
* @author Dustin Jorge
*/

public final class AppSelector extends ListActivity{

    public static final String PACKAGENAME = "pname";

    private static final String TAG = "HotseatAppSelector";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate( icicle );
		this.setResult( Activity.RESULT_CANCELED );
        this.setTitle( this.getString( R.string.hotseat_selector_title ) );
        this.loadAppsList();
    }

    private void loadAppsList(){
        final AppSelector _self = AppSelector.this;
        final PackageManager mPackageManager = this.getPackageManager();
        final Handler h = new Handler();
		this.executeThreaded(this, this.getString(R.string.select_app_working),
		    new ThreadProcessHelper() {
		
			    public void perform() {
                    Intent mainIntent = new Intent( Intent.ACTION_MAIN, null );
                    mainIntent.addCategory( Intent.CATEGORY_LAUNCHER );
                    List<ResolveInfo> pkgAppsList = mPackageManager.queryIntentActivities( mainIntent, 0);
                    Collections.sort( pkgAppsList, new MAppComperator( mPackageManager ) );
                    int x = pkgAppsList.size();
                    MApp[] allApps = new MApp[ x ];
                    for( int i = 0; i < x; i++ ){
                       /** TODO
                       *    Figure out how to tap into the Launcher's stored app cache to minimize time loading apps
                       */
                       ResolveInfo mPackage = pkgAppsList.get( i );
                       String pLabel = mPackage.loadLabel( mPackageManager ).toString();
                       String pName = mPackage.activityInfo.packageName;
                       Drawable pIcon = mPackage.loadIcon( mPackageManager );
                       allApps[ i ] = new MApp( pLabel, pName, pIcon );
                    }
                    final MApp[] fAllApps = allApps;
                    h.post( new Runnable(){
                        public void run(){
                            _self.setListAdapter( new App_List_Adapter( _self, fAllApps ) );
                        }
                    });
                }
            }
        );
    }

	@Override
	public void onListItemClick(ListView parent, View v, int position,
			long id) {
            try{
                MApp selectedApp = (MApp) parent.getAdapter().getItem( position );
                Intent result = new Intent();
                result.putExtra( AppSelector.PACKAGENAME, selectedApp.getPackageName() );                
        		this.setResult( Activity.RESULT_OK, result );
                this.finish();
            }catch( NullPointerException e ){
                // invalid position
                return;
            }
    }

	private void executeThreaded(final Context c, String txt,
			final ThreadProcessHelper helper) {
		    final ProgressDialog pD = new ProgressDialog(c);
		    final Handler h = new Handler();
		    pD.setCancelable(true);
		    pD.setTitle(txt);
		    pD.show();
		    new Thread(new Runnable() {
			    public void run() {
				    Looper.prepare();
				    helper.perform();
				    h.post(new Runnable() {
					    public void run() {
						    pD.cancel();
					    }
				    });
			    }
		    }).start();
    }

    private class App_List_Adapter extends BaseAdapter {

        private MApp[] apps;
        private Context c;

        public App_List_Adapter( Context c, MApp[] apps ){
            this.c = c;
            this.apps = apps;
        } 

		public int getCount() {
			return apps.length;
		}

		public Object getItem(int position) {
            Object item = null;
			try{
                item = apps[ position ];
            }catch( ArrayIndexOutOfBoundsException e ){
                e.printStackTrace();
                // return null item            
            };
            return item;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			MApp thisApp = (MApp) this.getItem(position);
            if( convertView == null ){			
                convertView = (LinearLayout) LayoutInflater.from( this.c ).inflate(
					R.layout.app_chooser_list, parent, false );
            }
			TextView label = (TextView) convertView.findViewById( R.id.label );
			label.setText( thisApp.getLabel() );
            ImageView mIcon = (ImageView) convertView.findViewById( R.id.icon );
            mIcon.setImageDrawable( thisApp.getIcon() );
            return convertView;
        }

    }

    private class MApp{
        
        private String label;
        private String packageName;
        private Drawable icon;

        public MApp( String label, String pName, Drawable icon ){
            this.label = label;
            this.packageName = pName;
            this.icon = icon;
        }

        public String getLabel(){
            return this.label;
        }

        public String getPackageName(){
            return this.packageName;
        }

        public Drawable getIcon(){
            return this.icon;
        }
      
    }
    
    public class MAppComperator implements Comparator<ResolveInfo>{

        private PackageManager pM;

        public MAppComperator( PackageManager mPackageManager ){
            this.pM = mPackageManager;
        }
 
        public int compare(ResolveInfo a1, ResolveInfo a2) {
            String a1Name = a1.loadLabel( this.pM ).toString();
            String a2Name = a2.loadLabel( this.pM ).toString();
            return a1Name.compareTo( a2Name );
        }
    
    }

    private interface ThreadProcessHelper {

	    // perform whatever
	    public void perform();

    }

}
