import java.time.LocalDate;
import java.util.*;

public class Hopital {
    private List<Chambre> chambres;
    private List<Medecin> medecins;
    private List<Patient> patients;

    public Hopital() {
        this.chambres = new ArrayList<>();
        this.medecins = new ArrayList<>();
        this.patients = new ArrayList<>();
    }

    public void ajouterChambre(Chambre chambre) {
        chambres.add(chambre);
        this.chambres = chambres;
        //System.out.println("Une chambre a été ajoutée à l'hôpital.");
    }

    public void ajouterMedecin(Medecin medecin) {
        medecins.add(medecin);
        //System.out.println("Un médecin a été ajouté à l'hôpital : " + medecin.getPrenom() + " " + medecin.getNom());
    }

    public void ajouterPatient(Patient patient) {
        patients.add(patient);
        //System.out.println("Un patient a été ajouté à l'hôpital : " + patient.getPrenom() + " " + patient.getNom());
    }



    public Chambre trouverChambreDisponible() throws Exception {
        for (Chambre chambre : chambres) {
            if (chambre.getCapacite() > chambre.getPatients().size()) {
                return chambre;
            }
        }
        throw new Exception("Aucune chambre disponible");
    }

    public void dechargerFinHospitalisationPatients(int currentDate) {
        for (Chambre chambre : chambres) {
            List<Patient> patients = new ArrayList<>(chambre.getPatients());
            for (Patient patient : patients) {
                try {
                    for (Hospitalisation hospitalisation : patient.getHospitalisations()) {
                        if (hospitalisation.getDateSortie() < currentDate) {
                            patient.decharger(hospitalisation.getDateSortie());
                            System.out.println(patient.getNom() + " " + patient.getPrenom() + " a quitter la chambre " + chambre.getNumero());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}