class Hospitalisation {
    private Chambre chambre;
    private int dateEntree;
    private int dateSortie;
    private Medecin medecinReferent;

    public Hospitalisation(Chambre chambre, int dateEntree, int dateSortie, Medecin medecinReferent) {
        this.chambre = chambre;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.medecinReferent = medecinReferent;
    }


    //detecte le chevauchement entre deux hospitalisations
    public boolean chevauche(Hospitalisation autre) throws ChevauchementException {
        if (this.dateEntree < autre.dateSortie && this.dateSortie > autre.dateEntree) {
            throw new ChevauchementException("Chevauchement détecté entre les hospitalisations.");
        }
        return false;
    }
    //periode d'hospitalisation
    public boolean estDansPeriode(int jour) {
        return jour >= dateEntree && jour <= dateSortie;
    }

    public Medecin getMedecinReferent() {
        return medecinReferent;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public int getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(int i) {
        dateSortie = i;
    }


}
