import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MiniLanguage{
  public static void main(String[] args){
    //�t�@�C���̓��e��ǂݍ��݁A�s���ƂɃ��X�gallLines�Ɋi�[���A�g��for���ŉ��s�������đS���̍s��A��
    Path path = Paths.get("sample_text/sample1.txt"); //������ς��ēǂݍ��ރt�@�C���ύX
    List<String> allLines = null;
    String delim = " \t,\n";
    String allText = "";
    try{
      allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
    }catch(IOException e){}
    for(String s: allLines)
      allText += (s + "\n");
    Context ct = new Context(allText, delim);
    Prog prog = new Prog();
    prog.parse(ct);
    prog.exe();

    JFrame jf = new JFrame("���s����");
    MyCanvas mc = new MyCanvas();
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mc.addMouseListener(mc);
    mc.addMouseMotionListener(mc);
    mc.setPreferredSize(new Dimension(700, 700));
    jf.getContentPane().add(mc);
    jf.pack();
    jf.setVisible(true);
  }
}

class MyCanvas extends JPanel implements MouseListener, MouseMotionListener{
  double alpha = 30;
  double beta = 40;
  int doX, doY;

  public void paintComponent(Graphics g){
    int _dx = 0, _dy = 0, dx = 0, dy = 0;
    super.paintComponent(g);
    setBackground(Color.black);
    g.setColor(Color.white);
    Figure3D.display(alpha, beta, getWidth()/2, getHeight()/2, g);
  }
  public void mousePressed(MouseEvent e){
    doX = e.getX();
    doY = e.getY();
  }
  public void mouseDragged(MouseEvent e){
    alpha = alpha + e.getY() - doY;
    beta = beta + e.getX() -doX;
    doX = e.getX();
    doY = e.getY();
    repaint();
  }
  public void mouseReleased(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseMoved(MouseEvent e){}
}
//Prog -> Com_list [EOF]
class Prog{
  private Com_list clist;
  public void parse(Context ct){
    clist = new Com_list();
    clist.parse(ct);
    //[EOF]�͂��̌�͉����Ȃ��Ƃ����Ӗ��Ȃ̂�
    if(ct.currentToken() != null){
      System.out.println("Prog:���͂̍Ō�ɗ]���ȃg�[�N��������܂�");
      System.exit(0);
    }
  }
  void exe(){
    clist.exe();
  }
}

//Com_list -> [Rep_com Prim_com] Com_list?
class Com_list{
  private Rep_com rcom;
  private Prim_com pcom;
  private Com_list clist;
  public void parse(Context ct){
    //if~else��Rep_com��Prim_com������
    if(ct.match("\\{")){
      rcom = new Rep_com();
      rcom.parse(ct);
    }
    else{
      pcom = new Prim_com();
      pcom.parse(ct);
    }
    //Com_list������Ƃ��́ARep_com��Prim_com�Ŏn�܂�̂ňȉ���if���Ŕ���
    if(ct.match("\\{") || ct.match("walk") || ct.match("move") || ct.match("go_far") || ct.match("move_far")){
      clist = new Com_list();
      clist.parse(ct);
    }
  }
  void exe(){
    if(rcom != null){
      rcom.exe();
    }
    else{
      pcom.exe();
    }
    if(clist != null){
      clist.exe();
    }
  }
}

//Rep_com -> { Com_list? [0-9]+ }
class Rep_com{
  int inum = 0;
  private Com_list clist;
  public void parse(Context ct){
    if(ct.match("\\{")){
      ct.toNext();
    }
    else{
      System.out.println("Rep_com: '{'������܂���: " +ct.currentToken());
      System.exit(0);
    }
    if(ct.match("\\{") || ct.match("walk") || ct.match("move") || ct.match("go_far") || ct.match("move_far")){
      clist = new Com_list();
      clist.parse(ct);
    }
    if(ct.match("[0-9]+")){
      //0���܂ގ��R����\��������𐮐��l�ɕϊ�
      inum = Integer.parseInt(ct.currentToken());
      ct.toNext();
      if(ct.match("\\}")){
        ct.toNext();
      }
      else{
        System.out.println("Rep_com: '}'������܂���: " +ct.currentToken());
        System.exit(0);
      }
    }
    else{
      System.out.println("Rep_com: ����������܂���: " +ct.currentToken());
      System.exit(0);
    }
  }
  void exe(){
      for(int i = 0; i < inum; i++){
        clist.exe();
    }
  }
}

/*Prim_com -> walk [+-]?[0-9]+(.[0-9]+)? [+-]?[0-9]+(.[0-9]+)?
              | move [+-]?[0-9]+(.[0-9]+)? [+-]?[0-9]+(.[0-9]+)?
              | go_far [+-]?[0-9]+(.[0-9]+)?
              | move_far [+-]?[0-9]+(.[0-9]+)?*/
class Prim_com{
  static double Pos_r = 0, Pos_th = 0, Pos_phi = 0;
  String op;
  double fnum1, fnum2;
  static boolean draw_flag;
  public void parse(Context ct){
    if(ct.match("walk") || ct.match("move")){
      op = ct.currentToken();
      ct.toNext();
      if(ct.match("[+-]?[0-9]+(.[0-9]+)?")){
        //������\��������������l�ɕϊ�
        fnum1 = Double.parseDouble(ct.currentToken());
        ct.toNext();
      }
      if(ct.currentToken().matches("[+-]?[0-9]+(.[0-9]+)?") && ct.currentToken() != null){
        fnum2 = Double.parseDouble(ct.currentToken());
        ct.toNext();
      }
      else{
        System.out.println("�����̐����K�؂ł͂���܂���");
        System.exit(0);
      }
    }
    else if(ct.match("go_far") || ct.match("move_far")){
      op = ct.currentToken();
      ct.toNext();
      if(ct.currentToken().matches("[+-]?[0-9]+(.[0-9]+)?") && ct.currentToken() != null){
        fnum1 = Double.parseDouble(ct.currentToken());
        fnum2 = 0;
        ct.toNext();
      }
    }
    else{
      System.out.println("Prim_com :walk, move, go_far, move_far�̂����ꂩ������܂���:" +ct.currentToken());
      System.exit(0);
    }
  }
  void exe(){
    //���������Ȃ���ړ����邩���Ȃ��̋�� ���������Ȃ�true
    if(op.equals("walk") || op.equals("go_far")){
      draw_flag = true;
    }
    else{
      draw_flag = false;
    }
    if(op.equals("walk") || op.equals("move")){
      Pos_th += fnum1;
      Pos_phi += fnum2;
    }
    else{
      Pos_r += fnum1;
    }
    Figure3D.prim(Pos_r, Pos_th, Pos_phi, draw_flag);
  }
}

class Figure3D{
  static ArrayList<Point3D> list = new ArrayList<Point3D>();
  static void prim(double r, double th, double phi, boolean flag){
    double D2R = Math.PI/180.0;
    double x, y, z;
    //�ɍ��W���璼�����W�֕ϊ�
    x = r*Math.sin(th*D2R)*Math.sin(phi*D2R);
    y = r*Math.cos(th*D2R);
    z = r*Math.sin(th*D2R)*Math.cos(phi*D2R);
    list.add(new Point3D(x, y, z, flag));
  }
  static void display(double alpha, double beta, int px, int py, Graphics g){
    int _dx = 0, _dy = 0, dx = 0, dy = 0;
    Point p2d;
    int N = list.size();
    int i = 0;
    for(Point3D p: list){
      p2d = p.get2D(alpha, beta, px, py);//list����v�f�����o���Aget2D�����s���APoint�I�u�W�F�N�g���擾
      dx = (int)p2d.getX();//Point�I�u�W�F�N�g����x���W���擾
      dy = (int)p2d.getY();//y���W���擾
      if(p.flag == true && i++ > 0){
        g.setColor(Color.getHSBColor((float)i/N, 1.0f, 1.0f));
        g.drawLine(_dx, _dy, dx, dy);
      }
      _dx = dx;//�����_�̓_����O�̓_�ɂ���
      _dy = dy;
    }
  }
}

class Point3D{
  private double x, y, z;
  boolean flag;
  Point3D(double _x, double _y, double _z, boolean _flag){
    x = _x;
    y = _y;
    z = _z;
    flag = _flag;
  }
  //�O�������W����񎟌����W�֕ϊ�
  Point get2D(double alpha, double beta, int px, int py){//���_(px, py)
    double D2R = Math.PI/180.0;
    double a = alpha*D2R, b = beta*D2R;
    double x1, y1, zt;
    int x2d, y2d;
    double sinA = Math.sin(a), sinB = Math.sin(b);
    double cosA = Math.cos(a), cosB = Math.cos(b);
    //���ˉe�ϊ�
    x1 = x*cosB + z*sinB;
    zt = -x*sinB + z*cosB;
    y1 = y*cosA - zt*sinA;
    x2d = px+(int)Math.rint(x1);
    y2d = py-(int)Math.rint(y1);
    return new Point(x2d, y2d);
  }
}
