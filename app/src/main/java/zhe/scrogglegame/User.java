package zhe.scrogglegame;

/**
 * Created by songz on 12/23/2017.
 */

public class User {
    private String userName;
    private String userToken;
    public User(){

    }
    public User(String userName){
        this.userName = userName;
    }

    public void setUserToken(String userToken){
        this.userToken = userToken;
    }

    public String getUserToken(){
        return this.userToken;
    }

    public String getUserName(){
        return this.userName;
    }
}
