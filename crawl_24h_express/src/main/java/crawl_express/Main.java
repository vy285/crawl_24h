package crawl_express;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import crawl_24h.bai_bao;

public class Main {

	static Document doc = null, doc_bai = null;
	static HashSet<String> list_id = new HashSet<String>(); // kiem soat id cac bai
	static HashSet<String> list_nguon = new HashSet<String>(); // kiem soat nguon ko duoc trung nhau
	static Queue<String> ds_link = new LinkedList<String>();
//	static FileWriter fw;
	static BufferedWriter bw;
	static String ngay_lay;

	public static int phan_loai(String url) {
		int do_dai = url.length();
		if (do_dai != 0) // truong hop link rong
		{
			if (url.substring(do_dai - 5).equals(".html")) {
				return 0; // bai viet
			} else if (url.contains("#box_comment_vne") || url.charAt(do_dai - 1) == '/') {
				return 2; // duong link loai bo
			} else if (url.contains("https://vnexpress.net/")) {
				return 1;
			} 
		}
		return 2;

	}

	public static String get_noi_dung() {
		String noi_dung = "";
		Elements l_doan = doc_bai.select(
				"#dark_theme > section.section.page-detail.top-detail > div > div.sidebar-1 > article > p.Normal");
		
		for (int i = 0; i < l_doan.size() - 1; i++) {
			noi_dung = noi_dung.concat(l_doan.get(i).text());
		}
		return noi_dung;
	}
	
	public static String get_ngay_viet() {
		Elements l_e = doc_bai.select("#dark_theme > section.section.page-detail.top-detail > div > div.sidebar-1 > div.header-content.width_common > span");
		if(l_e.size()==0) {
			return "";
		}
		else {
			return l_e.first().text();
		}
	}
	public static String get_tac_gia()  {
		Elements l_ten = doc_bai.select("#dark_theme > section.section.page-detail.top-detail > div > div.sidebar-1 > article > p.author_mail > strong");
		if (l_ten.size() == 0) {
			l_ten = doc_bai.select("#dark_theme > section.section.page-detail.top-detail > div > div.sidebar-1 > article > p.Normal[style=\"text-align:right;\"]");
			if(l_ten.size()==0) return "";
			else {
				return l_ten.first().text();
			}
		} else {
			return l_ten.first().text();
		}
		
	}

	public static String get_ten_bai(){
		String ten_bai = doc_bai.title().replace("\"", "");
		return ten_bai;
	}

	public static String get_id(String url) {
		String id = "";
		String[] l = url.split("-");
		String l_end = l[l.length - 1];
		id = l_end.substring(0, l_end.length() - 5);
		return id;

	}

	public static void xu_ly_bai_viet(String url)  {
		String id = get_id(url);
		
		// lay thoi gian theo dang yyyy-MM-dd
		String time="";
		if (list_id.add(id)) {
			try {
				doc_bai = Jsoup.connect(url).get();
				if(get_ngay_viet().length()>5) {
					String[] l_doan = get_ngay_viet().split(",");
					String[] times = l_doan[1].split("/");
					String nam = times[2];
					String thang = times[1];
					if(thang.length()<2) thang = "0"+thang;
					String ngay = times[0].substring(1);
					if(ngay.length()<2) ngay = "0"+ngay;
					
					time = nam+"-"+thang+"-"+ngay;
					
					if(time.equals(ngay_lay)) {
						bai_bao bb = new bai_bao(id,get_tac_gia(),get_ngay_viet(),get_ten_bai(),get_noi_dung());
						bw.write(new Gson().toJson(bb));
						bw.write("\n \n ");
					}
				}
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void xu_ly_nguon(String url)  {
		try {
			doc = Jsoup.connect(url).get();
			Elements l_e = doc.select("a");
			int a = l_e.size(); // do trang goc nhin co duong link vo han nen ta gioi han lai
			if (a > 250)
				a = 100;
			for (int i = 0; i < a; i++) {
				Element e = l_e.get(i);
				String link_con = e.attr("abs:href");
				if (phan_loai(link_con) == 0) {
					xu_ly_bai_viet(link_con);
				} else if (phan_loai(link_con) == 1) {
					if (list_nguon.add(link_con)) {
						ds_link.add(link_con);
					}
				} else {
					// khong lam gi
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		catch (IllegalArgumentException e2) {
			e2.printStackTrace();
		}
		
	}

	public static void thuc_hien(String url)  {
		try {
			get_nguon(url);
			LocalDate date = LocalDate.now();
			String date1 = date.toString().replace("-", "_");
			String ten_file = "C:\\Users\\vykro\\Desktop\\crawl\\crawl_24h_express\\crawl_express"+date1+".json";
			
//			String ten_file = "Desktop\\crawl\\crawl_24h_express\\crawl_express"+date1+".json";
			FileWriter fw = new FileWriter(ten_file);
			bw = new BufferedWriter(fw);
			while (!ds_link.isEmpty() && list_id.size() < 500) {
				String url_nguon_ra = ds_link.poll();
//				System.out.println(url_nguon_ra);
				xu_ly_nguon(url_nguon_ra);
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("xong");
	}

	public static void get_nguon(String url) {
		try {
			doc = Jsoup.connect(url).get();
			Elements l_e = doc.select("#wrap-main-nav > nav > ul > li >a");
			for (int i = 1; i < l_e.size(); i++) {
				Element e = l_e.get(i);
				String ten_nguon = e.attr("abs:href");
//				System.out.println(ten_nguon);
				list_nguon.add(ten_nguon);
				ds_link.add(ten_nguon);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

	}

	public static void main(String[] args) {
		LocalDate date = LocalDate.now();
		ngay_lay = date.toString();
		String url = "https://vnexpress.net/";
		list_nguon.add(url);
		thuc_hien(url);
	}
}
