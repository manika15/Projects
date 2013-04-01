package edu.gatech.arktos;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class Session {

	private SpreadsheetService service = new SpreadsheetService(
			"MySpreadsheetIntegration-v1");
	private ArrayList<GradesDB> existingDB = new ArrayList<GradesDB>();

	public Object login(String username, String password)
			throws AuthenticationException {

		try {
			service.setUserCredentials(username, password);
			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	public Object logout() {
		if (service != null) {
			try {
				service = null;
				for(GradesDB db : existingDB){
					db.close();
				}
				this.finalize();
				return 0;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		} else
			return null;

	}

	public GradesDB getDBByName(String gradesDb) throws IOException,
			ServiceException, URISyntaxException {
		GradesDB gd = new GradesDB();
		gd.setService(service);

		URL metafeedUrl = new URL(
				"https://spreadsheets.google.com/feeds/spreadsheets/private/full");
		SpreadsheetFeed resultFeed = service.getFeed(metafeedUrl,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> entries = resultFeed.getEntries();
		for (SpreadsheetEntry spreadsheet : entries) {
			if (spreadsheet.getTitle().getPlainText().equals(gradesDb)) {
				gd.setSpreadsheet(spreadsheet);
			}

		}
		
		existingDB.add(gd);
		return gd;

	}

}
