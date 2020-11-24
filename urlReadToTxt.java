/*  PoeAn Lu
    Hackathon project
    URL to txt file
   */
// Or use JSoup but keep for now  
   
import java.util.Scanner;
//import java.util.Scanner.skip(Pattern pattern);
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class urlReadToTxt {
	static Scanner scnr = new Scanner(System.in);
	//public Scanner skip(Pattern pattern);
    /*
	public Scanner skip(Pattern pattern) {
		
		
	}
	*/
	
	public static void main(String[] args) throws MalformedURLException, IOException {

        BufferedReader br = null;
		
		// gets site name
		System.out.println("Enter link to document here: ");
		String urlName = scnr.nextLine();    //site name

        try {

            URL url = new URL(urlName);
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
				
                if (line.contains("<p>")) {
				//    while (!(line.equals("</p>"))) {
				        line = line.replaceAll("\\<[[^>]*>","");
						sb.append(line);
				        sb.append(System.lineSeparator());
				//    }
				}
            }
			//sb = sb.replaceAll("\\<[[^>]*>","");
            System.out.println(sb);
        } finally {

            if (br != null) {
                br.close();
            }
        }
    }
}