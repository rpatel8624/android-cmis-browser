/*
 * Copyright (C) 2010 Florian Maul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fmaul.android.cmis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.fmaul.android.cmis.asynctask.ItemPropertiesDisplayTask;
import de.fmaul.android.cmis.repo.CmisItemLazy;
import de.fmaul.android.cmis.repo.CmisPropertyFilter;
import de.fmaul.android.cmis.repo.CmisRepository;
import de.fmaul.android.cmis.utils.ActionUtils;
import de.fmaul.android.cmis.utils.IntentIntegrator;

public class DocumentDetailsActivity extends ListActivity {

	private CmisItemLazy item;
	private Button view, download, share, edit, delete, qrcode, pref;
	private Activity activity;
	private List<Map<String, ?>> itemProperties;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.document_details_main);
		
		
		//Restart & Screen Rotation
		itemProperties = (List<Map<String, ?>>) getLastNonConfigurationInstance();
		
		activity = this;
		
		item = (CmisItemLazy) getIntent().getExtras().getSerializable("item");
		
		setTitleFromIntent();
		displayActionIcons();
		displayPropertiesFromIntent();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    final List<Map<String, ?>> data = getItemProperties();
	    return data;
	}
	
	private void displayActionIcons(){
		
		download = (Button) findViewById(R.id.download);
		view = (Button) findViewById(R.id.view);
		share = (Button) findViewById(R.id.share);
		edit = (Button) findViewById(R.id.editmetadata);
		delete = (Button) findViewById(R.id.delete);
		qrcode = (Button) findViewById(R.id.qrcode);
		pref = (Button) findViewById(R.id.preference);
		
		//File
		if (item != null && item.getSize() != null){
			
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActionUtils.openDocument(activity, item);
				}
			});
			
			download.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActionUtils.saveAs(activity, activity.getIntent().getStringExtra("workspace"), item);
				}
			});
			
			edit.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
			//qrcode.setVisibility(View.GONE);
			
		} else {
			//FOLDER
			view.setVisibility(View.GONE);
			download.setVisibility(View.GONE);
			edit.setVisibility(View.GONE);
			//share.setVisibility(View.GONE);
			//qrcode.setVisibility(View.GONE);
			delete.setVisibility(View.GONE);
		}
		
		
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActionUtils.shareDocument(activity, activity.getIntent().getStringExtra("workspace"), item);
			}
		});
		
		qrcode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentIntegrator.shareText(activity, item.getSelfUrl());
			}
		});
		
		pref.setOnClickListener(new OnClickListener() {
			private CharSequence[] cs;

			@Override
			public void onClick(View v) {
				
				cs = CmisPropertyFilter.getFiltersLabel(DocumentDetailsActivity.this, item); 
				
				AlertDialog.Builder builder = new AlertDialog.Builder(DocumentDetailsActivity.this);
				builder.setTitle(R.string.cmis_repo_choose_workspace);
				
				builder.setSingleChoiceItems(cs, -1, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						 dialog.dismiss();
			        	 new ItemPropertiesDisplayTask(DocumentDetailsActivity.this, null, CmisPropertyFilter.getFilters(item).get(which)).execute();  
					}
				});
				
				
				builder.setNegativeButton(DocumentDetailsActivity.this.getText(R.string.cancel), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   dialog.cancel();
			           }
			       });
				
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	private void setTitleFromIntent() {
		setTitle(getString(R.string.title_details) + " '" + item.getTitle() + "'");
	}

	private void displayPropertiesFromIntent() {
		new ItemPropertiesDisplayTask(this).execute();
		//new ItemPropertiesDisplayTask(this, null, CmisPropertyFilter.getFilter(item)).execute();
	}

	CmisRepository getRepository() {
		return ((CmisApp) getApplication()).getRepository();
	}
	
	List<Map<String, ?>> getItemProperties() {
		return ((CmisApp) getApplication()).getItemProperties();
	}
	
}
