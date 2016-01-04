import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.opencsv.CSVWriter;


public class ParseJiraPage {

	public static void main(String[] args) throws IOException {
		String filename = args[0];
		String output = filename.replace(".html", ".csv");
		File input = new File("C:\\Users\\Mingrui\\Desktop\\" + filename);
		Document doc = Jsoup.parse(input, "UTF-8", "");
		ArrayList<String> downServers = new ArrayList<>();
		Element table = doc.select("table").get(0); //select the first table.
		Elements rows = table.select("tr");

		 CSVWriter writer = new CSVWriter(new FileWriter(output), ',');
	     // feed in your array (or convert your data to an array)
	     
		for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
			 
		    Element row = rows.get(i);
		    Elements cols = row.select("td");
		    if(cols.size() < 9) continue;
		    String[] entries = new String[8];
		    entries[0] = cols.get(1).text();
		    entries[1] = cols.get(2).text();   
		    entries[2] = cols.get(3).text();
		    entries[3] = cols.get(4).text();
		    entries[4] = cols.get(5).text();
		    entries[5] = cols.get(6).text();
		    entries[6] = cols.get(7).text();
		    entries[7] = cols.get(8).text();
		    System.out.println("ID: " + cols.get(1).text());
		    writer.writeNext(entries);
		    //if (cols.get(7).text().equals("down")) {
		      //  downServers.add(cols.get(5).text());
		    //}
		}
		writer.close();
	}

}
