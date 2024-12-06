class Procedure extends Traitement {
    private String nom;
    private String description;

    public Procedure(String nom, String description, int dateDebut, int duree) {
        super(dateDebut, duree);
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean estActif(int jour, int dateOrdonnance) {
        int dateFin = dateOrdonnance + getDuree() - 1; // Modifier la date de fin pour être exclusive
        return jour >= dateOrdonnance && jour <= dateFin;
    }

    @Override
    public String toString() {
        return "Procedure{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + getDateDebut() +
                ", duree=" + getDuree() +
                '}';
    }
}
