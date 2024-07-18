package bismk.uas.aplikasi;

public class Barang {
    int id;
    String NamaBarang;
    String TipeBarang;
    double berat;
    double harga;
    String kdsupplier;

    public Barang(int id, String namaBarang, String tipeBarang, double berat, double harga, String kdsupplier) {
        this.id = id;
        NamaBarang = namaBarang;
        TipeBarang = tipeBarang;
        this.berat = berat;
        this.harga = harga;
        this.kdsupplier = kdsupplier;
    }


}
