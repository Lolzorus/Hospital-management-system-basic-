import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Ordonnance {
    private LocalDate date;
    private Medecin medecin;
    private Patient patient;
    private List<Traitement> traitements;

    public Ordonnance(String dateString, Medecin medecin, Patient patient) throws Exception {
        this.date = parseDate(dateString);
        this.medecin = medecin;
        this.patient = patient;
        this.traitements = new ArrayList<>();

        // Test si le patient est hospitalisé
        int jour = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        if (Patient.estHospitalise(patient, jour)) {
            Medecin medecinReferent = patient.getMedecinReferent(jour);
            if (!medecin.equals(medecinReferent)) {
                throw new Exception("Le patient est hospitalisé et ne peut consulter que son médecin référent.");
            }
        }
    }

    // Methode pour parse la date
    private LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    // Methode opur avoir le jour du mois en integer
    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Traitement> getTraitements() {
        return traitements;
    }

    // Example of adding getter for patient
    public Patient getPatient() {
        return patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    // verification des doses maximales
    public boolean estCompatible(Ordonnance autre) {
        Map<String, Double> dosesParMedicament = new HashMap<>();
        for (Traitement traitement : traitements) {
            if (traitement instanceof Medicament) {
                Medicament medicament = (Medicament) traitement;
                dosesParMedicament.put(medicament.getNom(), dosesParMedicament.getOrDefault(medicament.getNom(), 0.0) + medicament.getDoseParJour());
            }
        }
        for (Traitement traitement : autre.getTraitements()) {
            if (traitement instanceof Medicament) {
                Medicament medicament = (Medicament) traitement;
                double doseTotale = dosesParMedicament.getOrDefault(medicament.getNom(), 0.0) + medicament.getDoseParJour();
                if (doseTotale > medicament.getDoseMax()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean estActive(int jour) {
        return jour <= getDate().plusDays(getDuree()).getDayOfMonth();
    }

    private long getDuree() {
        return traitements.stream().mapToLong(Traitement::getDuree).sum();
    }

    // surcharge pour ajouter traitement
    public void ajouterTraitement(Medicament medicament) {
        traitements.add(medicament);
    }

    public void ajouterTraitement(Procedure procedure) {
        traitements.add(procedure);
    }

    public void afficherDetailsActifs(StringBuilder sb, int jour) {
        // Recoit la liste des ordonnances pour un Jour J
        List<Ordonnance> ordonnancesActives = patient.getOrdonnancesActives(jour);

        // Header pour les ordonnances actives
        sb.append(ordonnancesActives.size()).append(" ordonnances actives (aujourd'hui est le ").append(String.format("%02d/%02d/%d)%n",
                jour, LocalDate.now().getMonthValue(), LocalDate.now().getYear()));

        // Tentative d'enregistrement pas type
        Map<String, List<String>> traitementsParMedicament = new HashMap<>();

        // Boucle pour afficher tous les traitements (medicaments et procedures)
        for (Ordonnance ordonnance : ordonnancesActives) {
            for (Traitement traitement : ordonnance.getTraitements()) {
                if (traitement instanceof Medicament) { //formatage si medicament
                    Medicament medicament = (Medicament) traitement;
                    String nom = medicament.getNom();
                    String dosage = String.format("%.0f", medicament.getDoseParJour());
                    String duree = String.valueOf(traitement.getDuree());
                    LocalDate dateFinTraitement = ordonnance.getDate().plusDays(traitement.getDuree());
                    String traitementInfo = String.format(">> %s cachet(s) par jour jusqu'au %02d/%02d/%d (ordonnance du %s pendant %s jours)%n",
                            dosage, dateFinTraitement.getDayOfMonth(), dateFinTraitement.getMonthValue(), dateFinTraitement.getYear(),
                            ordonnance.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), duree);
                    traitementsParMedicament.computeIfAbsent(nom, k -> new ArrayList<>()).add(traitementInfo);
                } else if (traitement instanceof Procedure) { // formatage si procedure
                    Procedure procedure = (Procedure) traitement;
                    String nom = procedure.getNom();
                    String description = procedure.getDescription();
                    String duree = String.valueOf(traitement.getDuree());
                    String traitementInfo = String.format(">> %s : %s (ordonnance du %s pour %s jour(s))%n",
                            nom, description, ordonnance.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), duree);
                    traitementsParMedicament.computeIfAbsent(nom, k -> new ArrayList<>()).add(traitementInfo);
                }
            }
        }

        // On ajoute a stringbuilder
        for (Map.Entry<String, List<String>> entry : traitementsParMedicament.entrySet()) {
            sb.append("> ").append(entry.getKey()).append("\n");
            for (String traitementInfo : entry.getValue()) {
                sb.append(traitementInfo);
            }
            sb.append("\n"); // newline pour la mise en page
        }

    }
    // 3 methode pour compter les traitements dans une ordonnance
    public int getNombreMedicaments() {
        int count = 0;
        for (Traitement traitement : traitements) {
            if (traitement instanceof Medicament) {
                count++;
            }
        }
        return count;
    }

    public int getNombreProcedures() {
        int count = 0;
        for (Traitement traitement : traitements) {
            if (traitement instanceof Procedure) {
                count++;
            }
        }
        return count;
    }

    public int getTotalMedicamentsProcedures() {
        return getNombreMedicaments() + getNombreProcedures();
    }


    public String getDatePrescription() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}
