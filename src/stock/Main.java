package stock;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class GUIMain extends JFrame{
    JButton search = new JButton("종목 검색 및 추가");
    JButton price = new JButton("주가 확인");
    JButton notice = new JButton("주가 알림이 등록");
    JButton info = new JButton("프로그램 정보");
    GridLayout grid = new GridLayout(4,1,5,5);
    Menu menu = new Menu();
    GUIMain(){
        setTitle("증권 시세 알림이");
        setSize(300,300);
        setLayout(grid);
        add(search);
        add(price);
        add(notice);
        add(info);
        search.addActionListener(menu);
        price.addActionListener(menu);
        notice.addActionListener(menu);
        info.addActionListener(menu);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class GUISearch extends JFrame{
    JPanel top = new JPanel();
    JTextField name = new JTextField();
    JButton search_btn = new JButton("검색");
    JButton add_btn = new JButton("추가");
    JList result = new JList();
    JScrollPane scroll = new JScrollPane(result);
    GridLayout gl = new GridLayout(1,2,5,5);
    BorderLayout bl = new BorderLayout();
    Stock p = new Parse();
    GUISearch(){
        setTitle("종목 검색 및 추가");
        setSize(400,300);
        setLayout(bl);
        top.setLayout(gl);
        top.add(name);
        top.add(search_btn);
        add(top, "North");
        add(scroll, "Center");
        add(add_btn, "South");
        search_btn.addActionListener(e -> {
            try {
                result.setListData(p.search_stocks(name.getText()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        add_btn.addActionListener(e -> {
            String selected = (String) result.getSelectedValue();
            if (!Stock.added_stock.contains(selected))
                Stock.added_stock.add(selected);
            System.out.println(selected);
        });
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

class GUIPrice extends JFrame{
    JList stock_list = new JList();
    JPanel stock_etc = new JPanel();
    JPanel stock_info = new JPanel();
    JPanel stock_more_info = new JPanel();
    JPanel stock_top = new JPanel();
    JScrollPane scroll = new JScrollPane(stock_list);
    JLabel now_stock_price = new JLabel();
    JLabel stock_gap = new JLabel();
    JLabel stock_gap_rate = new JLabel();
    JLabel stock_name = new JLabel();
    JLabel stock_market = new JLabel();
    JLabel past = new JLabel();
    JLabel c_prise = new JLabel();
    JLabel high = new JLabel();
    JLabel low = new JLabel();
    JLabel trade = new JLabel();
    JLabel payment = new JLabel();
    JLabel stock_val = new JLabel();
    JLabel forigener = new JLabel();
    JLabel high_52 = new JLabel();
    JLabel low_52 = new JLabel();
    Font font = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 30);
    Font font2 = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 15);
    Font font3 = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 12);
    Font font4 = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 13);
    Stock stock = new Parse();
    GridLayout gl = new GridLayout(1,2,10,0);
    GridLayout gl2 = new GridLayout(3,1,0,0);
    GridLayout gl3 = new GridLayout(5,2,0,10);
    BorderLayout bl = new BorderLayout();
    ListSelectionListener stock_refrash = e -> {
        String selected = (String) stock_list.getSelectedValue();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (selected.equals(stock_list.getSelectedValue())){
                    String code = selected.substring(0,6);
                    String name = selected.substring(8);
                    try {
                        List<String> stock_data = stock.show_stock_rate(code);
                        List<String> stock_info = stock.show_stock_info(code);
                        stock_name.setText(name);
                        String gap = stock_data.get(1);
                        if (Integer.parseInt(stock_data.get(1).replace(",", ""))>0){
                            gap = "▲ "+ stock_data.get(1);
                            now_stock_price.setForeground(Color.red);
                            stock_gap.setForeground(Color.red);
                            stock_gap_rate.setForeground(Color.red);
                        }
                        else if (Integer.parseInt(stock_data.get(1).replace(",", ""))==0){
                            gap = "- "+ stock_data.get(1);
                            now_stock_price.setForeground(Color.GRAY);
                            stock_gap.setForeground(Color.GRAY);
                            stock_gap_rate.setForeground(Color.GRAY);
                        }
                        else if (Integer.parseInt(stock_data.get(1).replace(",", ""))<0){
                            gap = "▼ "+ stock_data.get(1);
                            now_stock_price.setForeground(Color.blue);
                            stock_gap.setForeground(Color.blue);
                            stock_gap_rate.setForeground(Color.blue);
                        }
                        if (stock_data.get(3).equals("코스피")){
                            stock_market.setText("KOSPI");
                            now_stock_price.setText(stock_data.get(0));
                            stock_gap.setText(gap);
                            stock_gap_rate.setText(stock_data.get(2));
                        }
                        else if (stock_data.get(3).equals("코스닥")){
                            stock_market.setText("KOSDAQ");
                            now_stock_price.setText(stock_data.get(0));
                            stock_gap.setText(gap);
                            stock_gap_rate.setText(stock_data.get(2));
                        }
                        past.setText("전일    "+stock_info.get(0));
                        c_prise.setText("시가    "+stock_info.get(1));
                        high.setText("고가    "+stock_info.get(2));
                        low.setText("저가    "+stock_info.get(3));
                        trade.setText("거래량    "+stock_info.get(4));
                        payment.setText("대금    "+stock_info.get(5));
                        stock_val.setText("시총   "+stock_info.get(6));
                        forigener.setText("외인소진율    "+stock_info.get(7));
                        high_52.setText("52주 최고    "+stock_info.get(8));
                        low_52.setText("52주 최저     "+stock_info.get(9));

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    timer.cancel();
                }
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        timer.cancel();
                    }
                });
            }
        };
        timer.schedule(task, 0,1000);
    };
    GUIPrice(){
        setTitle("주가 확인");
        setSize(450,300);
        setLayout(bl);
        stock_etc.setLayout(gl);
        stock_top.setLayout(gl2);
        stock_info.setLayout(new BoxLayout(stock_info, BoxLayout.Y_AXIS));
        stock_info.setAlignmentX(Component.CENTER_ALIGNMENT);
        stock_more_info.setLayout(gl3);
        now_stock_price.setFont(font);
        stock_name.setFont(font);
        stock_market.setFont(font3);
        stock_gap.setFont(font2);
        stock_gap_rate.setFont(font2);
        past.setFont(font4);
        c_prise.setFont(font4);
        high.setFont(font4);
        low.setFont(font4);
        trade.setFont(font4);
        payment.setFont(font4);
        stock_val.setFont(font4);
        forigener.setFont(font4);
        high_52.setFont(font4);
        low_52.setFont(font4);
        now_stock_price.setVerticalAlignment(SwingConstants.TOP);
        now_stock_price.setHorizontalAlignment(SwingConstants.CENTER);
        stock_name.setVerticalAlignment(SwingConstants.CENTER);
        stock_name.setHorizontalAlignment(SwingConstants.CENTER);
        stock_market.setVerticalAlignment(SwingConstants.TOP);
        stock_market.setHorizontalAlignment(SwingConstants.CENTER);
        stock_gap.setVerticalAlignment(SwingConstants.TOP);
        stock_gap.setHorizontalAlignment(SwingConstants.RIGHT);
        stock_gap_rate.setVerticalAlignment(SwingConstants.TOP);
        stock_gap_rate.setHorizontalAlignment(SwingConstants.LEFT);
        past.setVerticalAlignment(SwingConstants.CENTER);
        past.setHorizontalAlignment(SwingConstants.CENTER);
        c_prise.setVerticalAlignment(SwingConstants.CENTER);
        c_prise.setHorizontalAlignment(SwingConstants.CENTER);
        high.setVerticalAlignment(SwingConstants.CENTER);
        high.setHorizontalAlignment(SwingConstants.CENTER);
        low.setVerticalAlignment(SwingConstants.CENTER);
        low.setHorizontalAlignment(SwingConstants.CENTER);
        trade.setVerticalAlignment(SwingConstants.CENTER);
        trade.setHorizontalAlignment(SwingConstants.CENTER);
        payment.setVerticalAlignment(SwingConstants.CENTER);
        payment.setHorizontalAlignment(SwingConstants.CENTER);
        stock_val.setVerticalAlignment(SwingConstants.CENTER);
        stock_val.setHorizontalAlignment(SwingConstants.CENTER);
        forigener.setVerticalAlignment(SwingConstants.CENTER);
        forigener.setHorizontalAlignment(SwingConstants.CENTER);
        high_52.setVerticalAlignment(SwingConstants.CENTER);
        high_52.setHorizontalAlignment(SwingConstants.CENTER);
        low_52.setVerticalAlignment(SwingConstants.CENTER);
        low_52.setHorizontalAlignment(SwingConstants.CENTER);
        stock_list.setListData(stock.added_stock.toArray());
        stock_top.add(stock_name);
        stock_top.add(stock_market);
        stock_top.add(now_stock_price);
        stock_etc.add(stock_gap);
        stock_etc.add(stock_gap_rate);
        stock_more_info.add(past);
        stock_more_info.add(c_prise);
        stock_more_info.add(high);
        stock_more_info.add(low);
        stock_more_info.add(trade);
        stock_more_info.add(payment);
        stock_more_info.add(stock_val);
        stock_more_info.add(forigener);
        stock_more_info.add(high_52);
        stock_more_info.add(low_52);
        stock_info.add(stock_top);
        stock_info.add(stock_etc);
        stock_info.add(stock_more_info);
        add(scroll, "West");
        add(stock_info, "Center");
        stock_list.addListSelectionListener(stock_refrash);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

class GUINotice extends JFrame{
    JPanel Ui = new JPanel();
    JPanel stock_info = new JPanel();
    JList stock_list = new JList();
    JScrollPane scroll = new JScrollPane(stock_list);
    JLabel stock_price = new JLabel();
    JLabel stock_name = new JLabel();
    JLabel stock_market = new JLabel();
    JSlider set_price = new JSlider();
    JButton notice_set = new JButton("알림 설정");
    Font font = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 30);
    Font font2 = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 12);
    GridLayout gl = new GridLayout(3,1);
    GridLayout gl2 = new GridLayout(3,1);
    BorderLayout bl = new BorderLayout();
    Stock stock = new Parse();
    ListSelectionListener set_notice = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            String selected = (String) stock_list.getSelectedValue();
            String code = selected.substring(0,6);
            String name = selected.substring(8);
            try {
                List<String> stock_rate = stock.show_stock_rate(code);
                stock_price.setText(stock_rate.get(0));
                stock_name.setText(name);
                if (stock_rate.get(3).equals("코스피")){
                    stock_market.setText("KOSPI");
                }
                else if (stock_rate.get(3).equals("코스닥")){
                    stock_market.setText("KOSDAQ");
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    };
    GUINotice(){
        setTitle("주가 알림이 등록");
        setSize(400,300);
        setLayout(bl);
        Ui.setLayout(gl);
        stock_info.setLayout(gl2);
        stock_list.setListData(stock.added_stock.toArray());
        stock_name.setFont(font);
        stock_price.setFont(font);
        stock_market.setFont(font2);
        stock_price.setVerticalAlignment(SwingConstants.TOP);
        stock_price.setHorizontalAlignment(SwingConstants.CENTER);
        stock_name.setVerticalAlignment(SwingConstants.CENTER);
        stock_name.setHorizontalAlignment(SwingConstants.CENTER);
        stock_market.setVerticalAlignment(SwingConstants.TOP);
        stock_market.setHorizontalAlignment(SwingConstants.CENTER);
        stock_info.add(stock_name);
        stock_info.add(stock_market);
        stock_info.add(stock_price);
        Ui.add(stock_info);
        Ui.add(set_price);
        Ui.add(notice_set);
        add(scroll, "West");
        add(Ui, "Center");
        stock_list.addListSelectionListener(set_notice);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

class GUIInfo extends JFrame{
    JLabel p_name = new JLabel();
    JLabel content = new JLabel();
    Font font = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 30);
    Font font2 = new Font("Apple SD 산돌고딕 Neo", Font.PLAIN, 12);
    JButton go_git = new JButton("개발자 Github 가기");
    Menu menu = new Menu();
    BorderLayout bl = new BorderLayout();
    GUIInfo(){
        setLayout(bl);
        setSize(300,300);
        p_name.setFont(font);
        p_name.setText("증권 시세 알림이");
        p_name.setVerticalAlignment(SwingConstants.CENTER);
        p_name.setHorizontalAlignment(SwingConstants.CENTER);
        content.setFont(font2);
        content.setText("<html>제작자 : 20221 정민우<br>버전 : V1.0<br>빌드 일자 : 2018.06.23</html>");
        content.setVerticalAlignment(SwingConstants.CENTER);
        content.setHorizontalAlignment(SwingConstants.CENTER);
        add(go_git, "South");
        add(content,"Center");
        add(p_name, "North");
        go_git.addActionListener(menu);
        setTitle("프로그램 정보");
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}

class Menu implements ActionListener{
    public void actionPerformed(ActionEvent e){
        Stock p = new Parse();
        JButton btn = (JButton)e.getSource();
        switch (btn.getText()) {
            case "종목 검색 및 추가":
                new GUISearch();
                break;
            case "주가 확인":
                new GUIPrice();
                break;
            case "주가 알림이 등록":
                new GUINotice();
                break;
            case "프로그램 정보":
                new GUIInfo();
                break;
            case "개발자 Github 가기":
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/Phantom-Cat"));
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (URISyntaxException e2) {
                    e2.printStackTrace();
                }
                break;
        }
    }
}

public class Main{
    public static void main(String args[]) {
        for (int i = 0; i < 10; i++){
            try {
                new Parse().show_stock_rate("005930");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new GUIMain();
    }
}