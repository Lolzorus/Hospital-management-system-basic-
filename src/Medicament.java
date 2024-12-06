import java.util.ArrayList;
import java.util.List;

public class Medicament extends Traitement {
    private String nom;
    private double doseParJour;
    private Double doseMax;
    private List<Traitement> traitements;

    public Medicament(String nom, double doseParJour, Double doseMax, int dateDebut, int dateFin) {
        super(dateDebut, dateFin - dateDebut);
        this.nom = nom;
        this.doseParJour = doseParJour;
        this.doseMax = doseMax;
        this.traitements = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public double getDoseParJour() {
        return doseParJour;
    }

    public Double getDoseMax() {
        return doseMax;
    }

    public void ajouterTraitement(Traitement traitement) throws DoseMaximaleDepasseeException {
        // Vérifier si le traitement est un médicament
        if (traitement instanceof Medicament) {
            double doseTotaleJour = 0;
            // Calculer la dose totale par jour déjà prescrite
            for (Traitement t : traitements) {
                if (t instanceof Medicament) {
                    doseTotaleJour += ((Medicament) t).getDoseParJour();
                }
            }
            // Ajouter la nouvelle dose
            doseTotaleJour += ((Medicament) traitement).getDoseParJour();

            // Vérifier si la dose maximale est définie (gestion des medicaments sans limites) et si la dose totale dépasse la dose maximale
            if (doseMax != null && doseTotaleJour > doseMax) {
                throw new DoseMaximaleDepasseeException("Dose maximale quotidienne dépassée pour " + nom);
            }
        }

        // Ajouter le traitement à la liste des traitements
        traitements.add(traitement);
    }

    @Override
    public boolean estActif(int jour, int dateOrdonnance) {
        int dateFin = dateOrdonnance + getDuree() - 1; // Modifier la date de fin pour être exclusive
        return jour >= dateOrdonnance && jour <= dateFin;
    }
}
