import java.util.*;

class Patient {
    private String nom;
    private String prenom;
    private int dateNaissance;
    private String sexe;
    private List<Hospitalisation> hospitalisations;
    private List<Consultation> consultations;

    public Patient(String nom, String prenom, int dateNaissance, String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.hospitalisations = new ArrayList<>();
        this.consultations = new ArrayList<>();
    }
    // sert à hospitaliser un patient et a verifier que les dates ne se chevauchent pas entre elles
    public void hospitaliser(Hospitalisation hospitalisation) throws Exception {
        for (Hospitalisation h : hospitalisations) {
            if (hospitalisation.chevauche(h)) {
                throw new Exception("Périodes d'hospitalisation se chevauchent");
            }
        }
        hospitalisations.add(hospitalisation);
        hospitalisation.getChambre().ajouterPatient(this);
    }
    // sert à decharger un patient et verifier que c'est bien un patient hospitalisé
    public void decharger(int dateSortie) throws Exception {
        boolean found = false;
        for (Hospitalisation hospitalisation : hospitalisations) {
            if (hospitalisation.estDansPeriode(dateSortie)) {
                hospitalisation.setDateSortie(dateSortie);
                hospitalisation.getChambre().retirerPatient(this);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new Exception("Aucune hospitalisation trouvée pour la date de sortie spécifiée");
        }
    }
    // sert a creer une consultation
    public void ajouterConsultation(Consultation consultation) {
        consultations.add(consultation);
        System.out.println(consultation);
    }

    // sert à recuperer les ordonnances actives d'un patient à partir de la liste des ordonnances
    public List<Ordonnance> getOrdonnancesActives(int jour) {
        List<Ordonnance> ordonnancesActives = new ArrayList<>();
        for (Consultation consultation : consultations) {
            for (Ordonnance ordonnance : consultation.getOrdonnances()) {
                if (ordonnance.estActive(jour)) {
                    ordonnancesActives.add(ordonnance);
                }
            }
        }
        return ordonnancesActives;
    }

    // verifie si un patient est hospitalisé
    public static boolean estHospitalise(Patient patient, int jour) {
        for (Hospitalisation hospitalisation : patient.getHospitalisations()) {
            if (hospitalisation.estDansPeriode(jour)) {
                return true;
            }
        }
        return false;
    }

    // retourne le medecin referent d'un patient
    public Medecin getMedecinReferent(int jour) {
        for (Hospitalisation hospitalisation : hospitalisations) {
            if (hospitalisation.estDansPeriode(jour)) {
                return hospitalisation.getMedecinReferent();
            }
        }
        return null;
    }

    public List<Hospitalisation> getHospitalisations() {
        return hospitalisations;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }
}
