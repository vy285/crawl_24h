package crawl_24h;


public class bai_bao {
    public String id1;

    public String tac_gia;
    public String ngay_viet;
    public String ten_bai;

    public String noi_dung;

    public bai_bao() {
    }

    

    public bai_bao(String id1, String tac_gia, String ngay_viet, String ten_bai, String noi_dung) {
		this.id1 = id1;
		this.tac_gia = tac_gia;
		this.ngay_viet = ngay_viet;
		this.ten_bai = ten_bai;
		this.noi_dung = noi_dung;
	}



	public void setId1(String id1) {
        this.id1 = id1;
    }
    public String getId1() {
        return id1;
    }
    public String getTac_gia() {
        return tac_gia;
    }

    public void setTac_gia(String tac_gia) {
        this.tac_gia = tac_gia;
    }

    public String getNgay_viet() {
        return ngay_viet;
    }

    public void setNgay_viet(String ngay_viet) {
        this.ngay_viet = ngay_viet;
    }

    public String getNoi_dung() {
        return noi_dung;
    }

    public void setNoi_dung(String noi_dung) {
        this.noi_dung = noi_dung;
    }

    public String getTen_bai() {
        return ten_bai;
    }

    public void setTen_bai(String ten_bai) {
        this.ten_bai = ten_bai;
    }
    @Override
    public String toString(){
        return "\"bai_bao[id"+id1+"\n"+",ten_bai="+ten_bai+"\n"
                +",tac_gia="+tac_gia+"\n"+",ngay_viet="+ngay_viet
                +"\n"+",noi_dung="+noi_dung+"]";
    }

}
