package pacrawl;

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;

class Crawler {

    int seedno = 1, endurl, urlinlist = 0, count = 0, rowid = 0;
    public static List<String> result;
    public static List<String> tempresult = null;
    public static String url = "";
    int limit = 0;
    public String textdata;

    Crawler(String url, int limit) {
        this.url = url;
        this.limit = limit;

        System.out.println("Link extractor thread ");
        Thread t = new Thread() {
            public void run() {
                try {
                    downloader(Crawler.url);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        };
        t.start();
        result = new ArrayList<String>();
    }

    public void downloader(String url) {
        try {

            URL my_url = new URL(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(my_url.openStream()));
            String strTemp = "";
            StringBuffer file = new StringBuffer("");
            while (null != (strTemp = br.readLine())) {
                file.append(strTemp);
            }
            strTemp = file.toString();
            result = extractUrls(strTemp);

            for (int i = 0; i < endurl; i++) {
                System.out.println();
                System.out.println();

                System.out.println(urlinlist + "....NEXT PAGE.............");

                System.out.println(result.get(urlinlist));

                urlinlist++;

                downloader(result.get(urlinlist));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    List<String> extractUrls(String value) {

        Connection conn = null;
        String uri = "Class.";
        String dbName = "pm";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "root";

        try {
            Thread.sleep(100);

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/pm?user=root&password=root");

        } catch (Exception e) {
            e.printStackTrace();
        }

        tempresult = new ArrayList<String>();

        String urlPattern = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);

        while (m.find()) {
            if (endurl <= limit) {
                String link = m.group(1);

                tempresult.add(value.substring(m.start(0), m.end(0)));
                String g = "'" + link + "'";

                try {
                    Statement st = conn.createStatement();

                    String sql = "insert into bm values(" + rowid++ + "," + g + ")";
                    textdata = g + "\n";
                    st.executeUpdate(sql);

                } catch (Exception e) {
                    System.out.println(e);
                }

                System.out.println(endurl + "......." + value.substring(m.start(0), m.end(0)));
                endurl++;

            } else {
                urlDownload();
            }

        }

        result.addAll(tempresult);

        return result;
    }

    void fileDownload(String fAddress, String destinationDir) {

        int slashIndex = fAddress.lastIndexOf('/');
        int periodIndex = fAddress.lastIndexOf('.');

        String fileName = fAddress.substring(slashIndex + 1);

        if (periodIndex >= 1 && slashIndex >= 0 && slashIndex < fAddress.length() - 1) {
            fileUrl(fAddress, fileName, destinationDir);
        } else {
            System.err.println("path or file name.");
        }
    }

    void fileUrl(String fAddress, String localFileName, String destinationDir) {

        int size = 1024;

        OutputStream outStream = null;
        URLConnection uCon = null;

        InputStream ik = null;
        try {
            URL Url;
            byte[] buf;
            int ByteRead, ByteWritten = 0;
            Url = new URL(fAddress);
            outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + "\\" + localFileName));

            uCon = Url.openConnection();
            ik = uCon.getInputStream();
            buf = new byte[size];
            while ((ByteRead = ik.read(buf)) != -1) {
                outStream.write(buf, 0, ByteRead);
                ByteWritten += ByteRead;
            }
            System.out.println("Downloaded Successfully.");
            System.out.println("File name:\"" + localFileName + "\"\nNo ofbytes :" + ByteWritten);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ik.close();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void urlDownload() {

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/pm?user=root&password=root");

            System.out.println("Connected to the database");

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Statement st = conn.createStatement();
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();

            if (count == 0) {
                String s2 = "drop table bm";
                String s1 = "create table bm (rowid int(4),urls varchar(400))";
                int rs2 = st2.executeUpdate(s2);
                int rs1 = st1.executeUpdate(s1);

                count++;

            }
            String sql = "select urls from bm where rowid>=" + count + "";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1));

                fileDownload(rs.getString(1), "E:/PCrawl");
                count++;
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    void close() {

        close();
    }
}

public class UrlFinder {

    public static void main(String[] args) {
        Gui bh1 = new Gui();

    }
}
