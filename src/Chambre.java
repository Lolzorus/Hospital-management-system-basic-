import java.util.ArrayList;
import java.util.List;

class Chambre {
    private int numero;
    private int capacite;
    private List<Patient> patients;

    public Chambre(int numero, int capacite) {
        this.numero = numero;
        this.capacite = capacite;
        this.patients = new ArrayList<>();
    }

    //ajouter un patient à la chambre et verifie que le nombre de patients ne depasse pas la capacite
    public void ajouterPatient(Patient patient) throws Exception {
        if (patients.size() >= capacite) {
            throw new Exception("Capacité de la chambre dépassée");
        }
        patients.add(patient);
    }

    public void retirerPatient(Patient patient) {
        patients.remove(patient);
    }

    public int getCapacite() {
        return capacite;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public int getNumero() {
        return numero;
    }
}
