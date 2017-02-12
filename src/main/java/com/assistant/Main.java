package com.assistant;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static String file = "C:\\java_work_github\\2017work\\assistant-app\\src\\main\\resources\\text.txt";

    public static void main(String[] args) throws Exception {
        List<String> words = getWords(file);
        addToBase(words);
    }

    public static List<String> getWords(String file) {
        BufferedReader br = null;
        List<String> words = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            words = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                logger.debug("was received word: {}", line);
                words.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return words;
    }

    public static void addToBase(List<String> words) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Assistant", "root", "root");
            ps = con.prepareStatement("INSERT INTO words (name) values(?)");
            String word;
            for (int i = 0; i < words.size(); i++) {
                word = words.get(i);
                logger.debug("was added word: {}",word);
                ps.setString(1, word);
                ps.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                con.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }


}
