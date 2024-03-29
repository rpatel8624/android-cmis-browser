package de.fmaul.android.cmis.asynctask;

import org.dom4j.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import de.fmaul.android.cmis.R;
import de.fmaul.android.cmis.model.Server;
import de.fmaul.android.cmis.repo.CmisItem;
import de.fmaul.android.cmis.utils.ActionUtils;
import de.fmaul.android.cmis.utils.FeedLoadException;
import de.fmaul.android.cmis.utils.FeedUtils;

public class FeedItemDisplayTask extends AsyncTask<String, Void, CmisItem> {

	private static final String TAG = "FeedItemDisplayTask";
	
	private final Activity activity;
	private ProgressDialog pg;
	private Server server;
	private String url;
	private int action;
	
	public static final int DISPLAY_DETAILS = 0;
	public static final int DISPLAY_FOLDER = 1;

	public FeedItemDisplayTask(Activity activity, final Server server, String url) {
		this(activity, server, url, -1);
	}

	public FeedItemDisplayTask(Activity activity, final Server server, String url, int action) {
		super();
		this.activity = activity;
		this.url = url;
		this.server = server;
		this.action = action;
	}

	@Override
	protected void onPreExecute() {
		pg = ProgressDialog.show(activity, "", activity.getText(R.string.loading), true);
	}

	@Override
	protected CmisItem doInBackground(String... params) {
		try {
			Document doc = FeedUtils.readAtomFeed(url, server.getUsername(), server.getPassword());
			return CmisItem.createFromFeed(doc.getRootElement());
		} catch (FeedLoadException fle) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(CmisItem item) {
		if (item != null){
			
			if (action == -1){
				if (item.getBaseType() == null){
					action = DISPLAY_FOLDER;
				} else {
					action = DISPLAY_DETAILS;
				}
			}
			
			switch (action) {
			case DISPLAY_DETAILS:
				ActionUtils.displayDocumentDetails(activity, server, item);
			break;
			case DISPLAY_FOLDER:
				ActionUtils.openNewListViewActivity(activity, item);
			break;
			default:
				break;
			}
		} else {
			ActionUtils.displayMessage(activity, R.string.favorite_error_loading);
		}
		pg.dismiss();
	}

	@Override
	protected void onCancelled() {
		pg.dismiss();
	}
}