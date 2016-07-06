package local_server;

import gui.ControlPanel;

import java.io.*;
import java.net.Socket;

/**
 * Created by hrant on 11/19/15.
 */
public class FileReader implements Runnable {

    private String SITE_NAME = null;
    private String FILE_PATH = null;
    private String SITES_DIR_PATH;
    private int PORT;

    private InputStream inputStream;
    private OutputStream outputStream;


    public FileReader(Socket socket, String SITES_DIR_PATH) throws IOException {
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.SITES_DIR_PATH = SITES_DIR_PATH;
        this.PORT = socket.getLocalPort();
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            SITE_NAME = reader.readLine();
            SITE_NAME = SITE_NAME.substring(4).replace("HTTP/1.1", "").trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FILE_PATH = SITES_DIR_PATH + SITE_NAME;

        BufferedReader fileReader = null;
        try {
            String html = "";
            if (SITE_NAME.equals("/")) {
                File dir = new File(SITES_DIR_PATH);
                String sitesListFileHtml = "<html>\n\r" +
                        "<head>\n\r" +
                        "<title>Octavia local server</title>\n\r" +
                        "</head>\n\r" +
                        "<body>\n\r" +
                        "<h1>Octavia local server home page</h1>\n\r" +
                        "<br>\n\r";
                String sitesList[] = dir.list();
                for (int i = 0; i < sitesList.length; i++) {
                    sitesListFileHtml += "<a href='http://localhost:" + PORT + "/" + sitesList[i] + "'>" + sitesList[i] + "</a>" + "\n\r" + "<br>\n\r";
                }
                sitesListFileHtml += "</body>\n\r" + "</html>";
                html = sitesListFileHtml;
            } else {
                try {
                    fileReader = new BufferedReader(new java.io.FileReader(FILE_PATH));
                } catch (java.io.FileNotFoundException e){
                    FILE_PATH += "/index.html";
                }
                try {
                    fileReader = new BufferedReader(new java.io.FileReader(FILE_PATH));
                    String h;
                    while ((h = fileReader.readLine()) != null) {
                        html += h + "\r\n";
                    }
                } catch (java.io.FileNotFoundException fe) {
                    html = "<h2>Index page not found</h2>";
                }
            }

            String header = "HTTP/1.1 200 OK\r\n" +
                    "Server: OctaviaLocalServer/2015-2-12\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + html.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = header + html;
            outputStream.write(result.getBytes());
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
