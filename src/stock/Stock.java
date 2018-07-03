package stock;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

interface Parse_Method{
     Object[] search_stocks(String search) throws IOException;
     List show_stock_rate(String stock_code) throws IOException;
}

abstract public class Stock implements Parse_Method{
    static List<String> added_stock = new ArrayList<>();
}

class Parse extends Stock{
    @Override
    public Object[] search_stocks(String search) throws IOException{
        List<String> stock = new ArrayList<>();
        String search_encode = URLEncoder.encode(search, "cp949");
        Connection.Response response = Jsoup.connect("https://finance.naver.com/search/searchList.nhn?query="+search_encode)
                .method(Connection.Method.GET)
                .execute();
        Document document = response.parse();
        Elements stock_datas = document.select(".tit").select("a");
        for (Element stock_data : stock_datas){
            String real_code = stock_data.attr("href");
            stock.add(real_code.substring(real_code.lastIndexOf("=")+1) + "  " + stock_data.text());
        }
        return stock.toArray();
    }
    @Override
    public List show_stock_rate(String stock_code) throws IOException{
        List<String> stock_info = new ArrayList<>();
        List<String> more_stock_info = new ArrayList<>();
        Connection.Response response = Jsoup.connect("http://finance.daum.net/item/main.daum?code="+stock_code)
                .method(Connection.Method.GET)
                .execute();
        Document document = response.parse();
        Elements infos = document.select("body > div#wrap > div#topWrap > div > ul").select(".list_stockrate").select("li:not(li.txt_trade)");
        Elements more_infos = document.select("#stockContent > ul > li > dl > dd");
        for (Element info: infos) {
            if (info.select("span").attr("class").equals("sise down")){
                stock_info.add("-"+info.text());
            }
            else {
                stock_info.add(info.text());
            }
        }
        stock_info.remove(stock_info.size()-1);
        stock_info.add(document.select("#topWrap > div > ul > li:nth-child(2) > a").text());
        stock_info.add(document.select("body > div#wrap > div#topWrap > div > ul").select(".list_stockrate").select("li.txt_trade").select("span.num_trade").get(0).text());
        stock_info.add(document.select("body > div#wrap > div#topWrap > div > ul").select(".list_stockrate").select("li.txt_trade").select("span.num_trade").get(1).text()+"백만원");
        int count = 0;
        for (Element info: more_infos) {
            if (count == 5){
                more_stock_info.add(info.ownText());
            }
            else if ((count != 9) && (count != 11) && (count != 12) && (count != 13)) {
                more_stock_info.add(info.text());
            }
            count++;
        }
        stock_info.addAll(more_stock_info);
        System.out.println(stock_info);
        return stock_info;
    }
}