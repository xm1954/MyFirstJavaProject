package stock;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

interface Parse_Method{
     Object[] search_stocks(String search) throws IOException;
     List show_stock_rate(String stock_code) throws IOException;
     List show_stock_info(String stock_code) throws IOException;
}

abstract public class Stock implements Parse_Method{
    static List<String> added_stock = new ArrayList<>();
}

class Parse extends Stock{
    @Override
    public Object[] search_stocks(String search) throws IOException{
        List<String> stock = new ArrayList<>();
        String search_encode = URLEncoder.encode(search, "cp949");
        Connection.Response response = Jsoup.connect("http://finance.naver.com/search/searchList.nhn?query="+search_encode)
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
        Connection.Response response = Jsoup.connect("http://finance.daum.net/item/main.daum?code="+stock_code)
                .method(Connection.Method.GET)
                .execute();
        Document document = response.parse();
        Elements infos = document.select("body > div#wrap > div#topWrap > div > ul").select(".list_stockrate").select("li:not(li.txt_trade)");
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
        System.out.println(stock_info);
        Elements more_infos = document.select("#stockContent > ul > li > dl > dd > :not(span)");
        System.out.println(more_infos);
        System.out.println(more_infos);
        return stock_info;
    }
    @Override
    public List show_stock_info(String stock_code) throws IOException {
        List<String> stock_info = new ArrayList<>();
        Connection.Response response = Jsoup.connect("http://m.stock.naver.com/api/html/item/getOverallInfo.nhn?code="+stock_code)
                .method(Connection.Method.GET)
                .execute();
        Document document = response.parse();
        Elements content_parse = document.select("body > div.ct_box.total_info._total_quote_summary > ul > li").not(".item-hide").select("span");
        for (Element info: content_parse){
            stock_info.add(info.text());
        }
        stock_info.remove(stock_info.size()-1);
        stock_info.remove(stock_info.size()-1);
        System.out.println(stock_info);
        return stock_info;
    }

}