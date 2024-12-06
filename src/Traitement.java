class Traitement {
    private int dateDebut;
    private int duree;

    public Traitement(int dateDebut, int duree) {
        this.dateDebut = dateDebut;
        this.duree = duree;
    }

    public int getDateDebut() {
        return dateDebut;
    }

    public int getDuree() {
        return duree;
    }

    public boolean estActif(int jour, int dateOrdonnance) {
        return jour >= dateOrdonnance && jour <= dateOrdonnance + getDuree() - 1;
    }

}
