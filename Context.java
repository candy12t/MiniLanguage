import java.util.*;
import java.util.regex.*;
public class Context{
  private StringTokenizer st;
  private String token;
  public Context(String text, String delim){
    st = new StringTokenizer(text, delim); //行を単語に分割
    toNext();
  }
  public void toNext(){//次のトークンを探し、あればtokenを格納
    token = st.hasMoreTokens() ? st.nextToken() : null;
  }
  public String currentToken(){//現時点をトークンを返す
    return token;
  }
  public boolean match(String s){
    return (token != null) ? token.matches(s) : false;
  }
}
