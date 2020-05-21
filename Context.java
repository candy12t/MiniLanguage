import java.util.*;
import java.util.regex.*;


public class Context{
    private StringTokenizer st;
    private String token;

    // 行を単語に分割
    public Context(String text, String delim){
        st = new StringTokenizer(text, delim);
        toNext();
    }

    //次のトークンを探し、あればtokenを格納
    public void toNext(){
        token = st.hasMoreTokens() ? st.nextToken() : null;
    }

    //現時点をトークンを返す
    public String currentToken(){
        return token;
    }

    public boolean match(String s){
        return (token != null) ? token.matches(s) : false;
    }
}
