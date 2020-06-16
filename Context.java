import java.util.*;
import java.util.regex.*;
public class Context{
  private StringTokenizer st;
  private String token;
  public Context(String text, String delim){
    st = new StringTokenizer(text, delim); //�s��P��ɕ���
    toNext();
  }
  public void toNext(){//���̃g�[�N����T���A�����token���i�[
    token = st.hasMoreTokens() ? st.nextToken() : null;
  }
  public String currentToken(){//�����_���g�[�N����Ԃ�
    return token;
  }
  public boolean match(String s){
    return (token != null) ? token.matches(s) : false;
  }
}
