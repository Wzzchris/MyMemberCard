package comp5216.sydney.edu.au.mymembercard;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by WZZ on 05/10/2017.
 */

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private MemberCard memberCard;
    private ArrayList<MemberCard> memberCardArrayList;

    public User(){

        this.id=new Date().toString();
    }

    public User(String name, String email, String password) {
        this.id = new Date().toString();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<MemberCard> getMemberCardList() {
        return memberCardArrayList;
    }

    public void setMemberCardArrayList(ArrayList<MemberCard> memberCardArrayList) {
        this.memberCardArrayList = memberCardArrayList;
    }

    public void addMemberCard(MemberCard memberCard) {
        this.memberCardArrayList.add(memberCard);
    }

    public void deleteMemberCard(String cardnumber) {
        for (int i = 0; i < memberCardArrayList.size(); i++) {
            if (memberCardArrayList.get(i).getCardNumber() == cardnumber) {
                memberCardArrayList.remove(i);
            } else continue;
        }
    }
}
