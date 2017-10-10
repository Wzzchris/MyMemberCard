package comp5216.sydney.edu.au.mymembercard;

/**
 * Created by WZZ on 09/10/2017.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 1.add user
 * 2.delete user
 * 3.add card to user
 * 4.delete card to user
 * 5.get All card
 * 6.update user(更改邮箱密码)
 * 7.check user either email exists
 * 8.check user match the password or not
 */

public class DBHelper {
    // JDBC driver name and database URL
//    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String DB_URL = "jdbc:jtds:sqlserver://comp5216data.database.windows.net:1433;"
            + "DatabaseName=Comp5216;encrypt=false;";
    private String user = "wzz@comp5216data";
    private String pwd = "woaiwode4S";

    //  Database credentials
//    private static final String USER = "wzz";
//    private static final String PASS = "woaiwode4S";


    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet resultSet;


    // User table name
    private static final String TABLE_Name_Members = "members";
    private static final String TABLE_Name_Accounts = "accounts";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "member_id";
    private static final String COLUMN_USER_NAME = "member_name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_CARD_COMPANY = "card_company";
    private static final String COLUMN_CARD_MEMBER = "card_member";


    public DBHelper() {
        //STEP 2: Register JDBC driver
        try {
//            Class.forName(driver);
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        try {
            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, user, pwd);
            //System.out.println("Creating statement...");
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void addUser(User user) {

        String sql = "insert into members values(?,?,?,?)";

        String id = user.getId();
        String name = user.getName();
        String password = user.getPassword();
        String email = user.getEmail();

        try {
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User makeUser(String email) {

        User user = new User();
        String id = null;
        String name = null;
        String password = null;
        String userEmail = null;


        ArrayList<MemberCard> memberCardList=new ArrayList<MemberCard>();
        String sql = "SELECT *\n" +
                "from accounts LEFT JOIN members on accounts.m_id = members.member_id\n" +
                "where email= ?";


        try {
            stmt = (PreparedStatement) conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                id = rs.getString(1);
                name = rs.getString(5);
                password = rs.getString(6);
                userEmail = rs.getString(7);


                MemberCard memberCard = new MemberCard();
                String company = rs.getString(2);
                String cardNumber = rs.getString(3);

                memberCard.setCompany(company);
                memberCard.setCardNumber(cardNumber);

                memberCardList.add(memberCard);
            }


            user.setId(id);
            user.setName(name);
            user.setPassword(password);
            user.setEmail(userEmail);
            user.setMemberCardArrayList(memberCardList);


            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return user;
    }


    /**
     * Everytime user changed their card folder, the database need to be synchronized.
     */

    public boolean checkUser(String email){

        int countInt = 0;

        String sql = "SELECT count(email)\n" +
                "from members\n" +
                "where email=?";

        try {
            stmt=(PreparedStatement) conn.prepareStatement(sql);
            stmt.setString(1,email);
            ResultSet rs = stmt.executeQuery();

            countInt = rs.getInt(1);

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (countInt>0){
            return true;
        }

        return false;
    }

    public boolean checkUser(String email, String password){
        int countInt = 0;
        long countLong=0;
        String name = new String();


        try {
            String sql;
//            sql="select * from members";
            sql= "SELECT count(email) as number\n" +
                    "from members\n" +
                    "where email='"+email +"' and password = '"+password+"'";
            stmt= conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                countInt = rs.getInt("number");
//                name=rs.getString(2);
            }

//            System.out.print(name);
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (countInt>0){
            return true;
        }

        return false;
    }

    public void test() {
        try {
            String sql;
            sql = "SELECT * FROM " + TABLE_Name_Members;
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(); // DML
            // stmt.executeUpdate(sql); // DDL

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Display values
                System.out.print(rs.getString(1) + " ");
                System.out.print(rs.getString(2) + " ");
                System.out.print(rs.getString(3) + " ");
                System.out.print(rs.getString(4) + " ");
            }
            //STEP 6: Clean-up environment
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
