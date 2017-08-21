package pacrawl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.*;

public class Gui extends JFrame implements ActionListener {

    JButton jb = new JButton("Start Crawl");
    JButton jb2 = new JButton("Show Result");
    JButton jb3 = new JButton("Stop");
    JLabel jl1 = new JLabel("Starting Url");
    JTextField jf1 = new JTextField(40);
    JLabel jl2 = new JLabel("Limit on Url");
    JTextField jf2 = new JTextField(14);
    JTextArea ja = new JTextArea(10, 50);
    int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
    JScrollPane jsp = new JScrollPane(ja, v, h);

    public Gui() {
        setTitle("Parallel Web Crawler");
        setSize(800, 600);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });
        jb.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);

        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JPanel jp4 = new JPanel();
        JPanel jp5 = new JPanel();

        jp1.add(jl1);
        jp1.add(jf1);
        jp2.add(jl2);
        jp2.add(jf2);
        jp3.add(jb);
        jp3.add(jb2);
        jp3.add(jb3);
        jp4.add(jsp);
        jp5.add(jp1);
        jp5.add(jp2);
        jp5.add(jp3);
        jp5.add(jp4);
        setContentPane(jp5);
        setVisible(true);
    }

    private void actionExit() {
        System.exit(0);
    }

    public void actionPerformed(ActionEvent e) {
        Connection conn = null;
        String uri = "jdbc:mysql://localhost:3306/";
        String dbName = "pm";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "root";
        String su = "";
        int cl = 0;
        Object src = e.getSource();
        try {
            Class.forName(driver).newInstance();
            conn = (Connection) DriverManager.getConnection(uri + dbName, userName, password);
            System.out.println("Connected to the database");
        } catch (Exception fe) {
            fe.printStackTrace();
        }
        if (src == jb) {
            try {
                Thread.sleep(100);
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/pm?user=root&password=root");
                Statement st=conn.createStatement();
                st.executeUpdate("delete from bm");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            su = jf1.getText();
            cl = Integer.parseInt(jf2.getText());

            Crawler t = new Crawler(su, cl);
            try {
                Thread.sleep(100);
                System.out.println(".....Downloading thread is working....... ");
                t.urlDownload();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        if (src == jb2) {

            try {
                ja.setText("");
                Statement st = (Statement) conn.createStatement();
                String sql = "select urls from bm";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    ja.append(rs.getString(1) + "\n");

                }
            } catch (Exception ge) {
                ge.printStackTrace();
            }
        }

        if (src == jb3) {
            actionExit();
        }
    }
}
