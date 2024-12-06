import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Consultation {
    private Medecin medecin;
    private Patient patient;
    private List<Ordonnance> ordonnances;

    public Consultation(Medecin medecin, Patient patient) throws Exception {
        this.medecin = medecin;
        this.patient = patient;
        this.ordonnances = new ArrayList<>();

        // Test si le patient est hospitalisé
        int jour = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        if (Patient.estHospitalise(patient, jour)) {
            Medecin medecinReferent = patient.getMedecinReferent(jour);
            if (!medecin.equals(medecinReferent)) {
                throw new Exception("Le patient est hospitalisé et ne peut consulter que son médecin référent.");
            }
        }
    }

    public void ajouterOrdonnance(Ordonnance ordonnance) throws Exception {
        // Chercher les ordonnances actives pour le jour
        int dayOfMonth = ordonnance.getDayOfMonth();
        List<Ordonnance> ordonnancesActives = patient.getOrdonnancesActives(dayOfMonth);

        // Verifie leur compatibilite
        for (Ordonnance o : ordonnancesActives) {
            if (!o.estCompatible(ordonnance)) {
                throw new Exception("Dose maximale journalière dépassée");
            }
        }

        // Si tout va bien ajoute l'ordonnance
        ordonnances.add(ordonnance);
    }


    public List<Ordonnance> getOrdonnances() {
        return ordonnances;
    }

    public void afficherTraitementActifs(int jourActuel) {
        StringBuilder sb = new StringBuilder();

        // affiche toute les ordonnances
        for (Ordonnance ordonnance : getOrdonnances()) {
            ordonnance.afficherDetailsActifs(sb, jourActuel);
            sb.append("\n"); // Newline
        }

        // affiche tout
        System.out.println(sb);
    }

    @Override
    public String toString() {
        return "Consultation with " + patient.getPrenom() + " " + patient.getNom() +
                ", Medecin: " + medecin.getNom() + " " + medecin.getPrenom() +
                ", Ordonnances: " + ordonnances.size();
    }


}
