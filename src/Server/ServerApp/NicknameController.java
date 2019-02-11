package Server.ServerApp;

public class NicknameController {
    public static boolean checkNick(String nick){
        if(nick.equals(""))
            return false;
        return true;
    }
}
