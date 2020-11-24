package read;
import java.net.URL;

import java.io.IOException;
import org.jsoup.Jsoup;    // Need to download Jsoup
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class urlReadToTxtversion2 {

    public static void main(String[] args) throws Exception {

        
		URL url = new URL("https://en.wikipedia.org/wiki/Java");
		Document doc = Jsoup.parse(url,3*1000);
		
		String text = doc.body().text();
		
		System.out.println(text);
		
    }
}