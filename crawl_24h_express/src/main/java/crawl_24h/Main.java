package crawl_24h;

import  org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static String url = "https://www.24h.com.vn/sitemap-article-daily.xml";
    static Document doc_bai= null;
        public  static  String get_noi_dung(String url) {
//            Document doc = Jsoup.connect(url).get();
            String noi_dung = "";
            Elements l_doan = doc_bai.select("#article_body > article.cate-24h-foot-arti-deta-info > p");
            if(l_doan.size()<5){
                return "0";
            }
            else {
                for(int i=0;i<l_doan.size()-2;i++){
                    noi_dung = noi_dung.concat(l_doan.get(i).text());
                }
                return noi_dung;
            }


        }
// nhung con so cuoi duong link chinh la id cua bai viet
    public  static String get_id(String url){
        String id="";
        String [] l = url.split("-");
        String l_end = l[l.length-1];
        id = l_end.substring(0,l_end.length()-5);
        return  id;
    }

    public static  String get_ten_bai(String url) {
        String ten_bai = doc_bai.title().replace("\"","");
        return ten_bai;
    }

    public static String get_tac_gia(String url) {
//        Document doc = Jsoup.connect(url).get();
        Elements l_ten = doc_bai.getElementsByClass("nguontin nguontinD bld mrT10 mrB10 fr");
        if(l_ten.size()==0){
            return "";
        }
        else {
            return l_ten.first().text();
        }
    }

    public  static void get_link(String url) {
        Document doc;
		try {
			doc = Jsoup.connect(url).get();
	        LocalDate date = LocalDate.now();
	        String date1 = date.toString().replace("-","_");
	        String ten_file = "C:\\Users\\vykro\\Desktop\\crawl\\crawl_24h_express\\crawl_24h"+date1+".json";
	        FileWriter fw = new FileWriter(ten_file);

	        Elements l_link = doc.getElementsByTag("loc");
	        Elements l_date = doc.getElementsByTag("lastmod");
	        for (int i = 0; i < l_link.size(); i++) {
	            String link = l_link.get(i).text();
	            try {
		            doc_bai = Jsoup.connect(link).get();

				} catch (Exception e) {
					continue;
				}
	            
	            
	            bai_bao bb = new bai_bao();
	            bb.setId1(get_id(link));
	            bb.setNgay_viet(l_date.get(i).text());
	            bb.setTac_gia(get_tac_gia(link));
	            bb.setTen_bai(get_ten_bai(link));
	            bb.setNoi_dung(get_noi_dung(link));
	            fw.write(new Gson().toJson(bb));
	            fw.write("\n \n");
	            fw.flush();
	        }
	        fw.close();
	        System.out.println("xong");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  }
    public static void main(String[] args) {
        get_link(url);
    }
}

