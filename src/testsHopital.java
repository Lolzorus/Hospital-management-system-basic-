import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class testsHopital {
    private Hopital hopital;
    private Medecin drSmith;
    private Medecin drJones;
    private Patient patient1;
    private Patient patient2;
    private Patient patient3;
    private Chambre chambre1;
    private Chambre chambre2;
    private Chambre chambre3;
    private Chambre chambre4;
    private Ordonnance ordonnance1;
    private Medicament aspirine;
    private Medicament ibuprofene;
    private Medicament paracetamol;
    private Medicament vitamineC;
    private Procedure procedure1;
    private Procedure procedure2;

    @BeforeEach
    void setUp() throws Exception {
        hopital = new Hopital();
        drSmith = new Medecin("Smith", "John");
        drJones = new Medecin("Jones", "Alice");
        patient1 = new Patient("Doe", "Jane", 19900101, "F");
        patient2 = new Patient("Roe", "Richard", 19850505, "M");
        patient3 = new Patient("Test", "Patient", 19900505, "M");

        chambre1 = new Chambre(101, 1);
        chambre2 = new Chambre(102, 2);
        chambre3 = new Chambre(103, 3);
        chambre4 = new Chambre(104, 4);

        ordonnance1 = new Ordonnance("21/05/2024", drSmith, patient1);

        aspirine = new Medicament("Aspirine", 2.0, 4.0, 20240101, 20240105);
        ibuprofene = new Medicament("Ibuprofène", 1.0, 3.0, 20240101, 20240103);
        paracetamol = new Medicament("Paracétamol", 1.5, 4.0, 20240101, 20240107);
        vitamineC = new Medicament("VitamineC", 100, null, 20240101, 20240107);

        procedure1 = new Procedure("Physiothérapie", "Thérapie physique", 20240101, 5);
        procedure2 = new Procedure("Chirurgie", "Intervention chirurgicale", 20240101, 10);

        hopital.ajouterMedecin(drSmith);
        hopital.ajouterMedecin(drJones);
        hopital.ajouterPatient(patient1);
        hopital.ajouterPatient(patient2);
        hopital.ajouterChambre(chambre1);
        hopital.ajouterChambre(chambre2);
        hopital.ajouterChambre(chambre3);
        hopital.ajouterChambre(chambre4);
    }

    @Test
    @Order(1) //test aussi le changement de medecin referent d'une hospitalisation sur l'autre
    void test2HospitalisationsEnMemeTempsMemePatient() {
        Hospitalisation hosp1 = new Hospitalisation(chambre1, 0, 10, drSmith);
        Hospitalisation hosp2 = new Hospitalisation(chambre2, 5, 15, drSmith);
        Hospitalisation hosp3 = new Hospitalisation(chambre3, 11, 25, drSmith);
        Hospitalisation hosp4 = new Hospitalisation(chambre4, 26, 30, drJones); // changement de medecin referent

        assertDoesNotThrow(() -> patient1.hospitaliser(hosp1));
        assertThrows(Exception.class, () -> patient1.hospitaliser(hosp2)); // 2ᵉ hospitalisation a lieu pendant la premiere = erreur
        assertDoesNotThrow(() -> patient1.hospitaliser(hosp3)); // 3ᵉ hospitalisation a lieu apres la premiere
        assertDoesNotThrow(() -> patient1.hospitaliser(hosp4));
    }


    ///// test consultation, hospitalisation, puis l'un apres l'autre /////
    @Test
    @Order(2) // test de consultation simple
    void testConsultationSimple() throws Exception {
        Consultation consultation = new Consultation(drSmith, patient1);

        consultation.ajouterOrdonnance(ordonnance1);
        assertEquals(1, consultation.getOrdonnances().size()); // La consultation est bien dans l'ordonnance
        assertEquals(patient1, consultation.getOrdonnances().get(0).getPatient()); // le patient est bien le patient1
        assertEquals(drSmith, consultation.getOrdonnances().get(0).getMedecin()); // le medecin est bie dr Smith

    }

    @Test
    @Order(3)
    void testHospitalisation() throws Exception {
        // Création de deux hospitalisations pour le même patient avec le même médecin référent
        Hospitalisation hospitalisation1 = new Hospitalisation(chambre1, 20240601, 20240610, drSmith);
        Hospitalisation hospitalisation2 = new Hospitalisation(chambre2, 20240605, 20240615, drSmith);
        Hospitalisation hospitalisation3 = new Hospitalisation(chambre3, 20240611, 20240620, drJones);

        // Création d'une consultation pour un autre patient avec un autre médecin (safe test)
        Consultation consultation = new Consultation(drJones, patient2);

        // Hospitalisation du premier patient 2 fois a la suite avec des medecins differents
        patient1.hospitaliser(hospitalisation1);
        patient1.hospitaliser(hospitalisation3);

        // Vérification si le patient 1 est hospitalisé le 5 juin 2024 et le 19 juin 2024
        assertTrue(Patient.estHospitalise(patient1,20240605));
        assertTrue(Patient.estHospitalise(patient1,20240619));

        // Tentative d'hospitaliser à nouveau le même patient pendant une période de chevauchement
        assertThrows(Exception.class, () -> patient1.hospitaliser(hospitalisation2));

        // Ajout d'une consultation pour le patient 2
        patient2.ajouterConsultation(consultation);

        // Ajout d'une ordonnance à la consultation du patient 2
        Ordonnance ordonnance = new Ordonnance("28/06/2024", drJones, patient2);
        consultation.ajouterOrdonnance(ordonnance);

        // Vérification si une seule ordonnance a été ajoutée à la consultation
        assertEquals(1, consultation.getOrdonnances().size());

        // Vérification des détails de l'ordonnance ajoutée
        assertEquals(patient2, ordonnance.getPatient());
        assertEquals(drJones, ordonnance.getMedecin());
        assertEquals("28/06/2024", ordonnance.getDatePrescription());
    }




    @Test
    @Order(4) //test d'hospitalisation puis consultation avec mauvais docteur, puis sortie hopital, puis consultation avec docteur qui avait provoqué l'erreur
    void testConsultationApresHospitalisation() throws Exception {
        Hospitalisation hospitalisation = new Hospitalisation(chambre1, 20240601, 20240610, drSmith);
        Consultation consultation = new Consultation(drJones, patient1);


        patient1.hospitaliser(hospitalisation);
        assertTrue(Patient.estHospitalise(patient1,20240605)); // Vérifie si le patient est hospitalisé à cette date
        assertThrows(Exception.class, () -> consultation.ajouterOrdonnance(new Ordonnance("20240605", drJones, patient1))); // le patient est déjà hospitalisé
        assertEquals(0, consultation.getOrdonnances().size()); // Aucune ordonnance ne devrait être ajoutée, car referent different

        // Terminer l'hospitalisation
        hospitalisation.setDateSortie(20240610);

        // Ordonnance de l'autre docteur apres sortie de l'hopital
        consultation.ajouterOrdonnance(new Ordonnance("15/06/2024", drJones, patient1));
        assertEquals(1, consultation.getOrdonnances().size()); // 1 ordonnance
        assertEquals(patient1, consultation.getOrdonnances().get(0).getPatient()); // le patient correspond
        assertEquals(drJones, consultation.getOrdonnances().get(0).getMedecin()); // le docteur correspond

    }


    ///// test hospitalisation et consultation medecin referent /////////
    @Test
    @Order(5)
    void testPatientHospitaliseConsultationAvecMedecinReferent() throws Exception {
        Hospitalisation hospitalisation = new Hospitalisation(chambre1, 20240601, 20240610, drSmith);
        patient1.hospitaliser(hospitalisation);
        Consultation consultation = new Consultation(drSmith, patient1);
        Ordonnance ordonnance = new Ordonnance("03/06/2024", drSmith, patient1);


        patient1.ajouterConsultation(consultation);
        consultation.ajouterOrdonnance(ordonnance);
        assertEquals(1, consultation.getOrdonnances().size()); // le bon nombre d'ordonnance
        assertEquals(patient1, consultation.getOrdonnances().get(0).getPatient()); // le bon patient
        assertEquals(drSmith, consultation.getOrdonnances().get(0).getMedecin()); // le bon medecin referent
        assertEquals("03/06/2024", consultation.getOrdonnances().get(0).getDatePrescription());
    }

    @Test
    @Order(6) // test aussi pour une ordonnance d'un non referent
    void testPatientHospitaliseConsultationAvecMedecinNonReferent() throws Exception {
        // Hospitalisation avec date de sortie "dynamique" pour les tests.
        Hospitalisation hospitalisation = new Hospitalisation(chambre1, 20240601, Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))), drSmith);
        System.out.println(LocalDate.now());
        patient1.hospitaliser(hospitalisation);
        System.out.println(patient1.getMedecinReferent(20240613));

        // Tentative de consultation avec un medecin pas referent
        Exception exception1 = assertThrows(Exception.class, () -> new Consultation(drJones, patient1));
        assertEquals("Le patient est hospitalisé et ne peut consulter que son médecin référent.", exception1.getMessage());

        // Tentative d'ordonnance d'un medecin pas referent
        Exception exception = assertThrows(Exception.class, () -> new Ordonnance("03/06/2024", drJones, patient1));
        assertEquals("Le patient est hospitalisé et ne peut consulter que son médecin référent.", exception.getMessage());


        // test avec medecin referent pour la forme
        Consultation consultation1 = new Consultation(drSmith, patient1);
        Ordonnance ordonnance = new Ordonnance("03/06/2024", drSmith, patient1);

        patient1.ajouterConsultation(consultation1);
        consultation1.ajouterOrdonnance(ordonnance);
        assertEquals(1, consultation1.getOrdonnances().size()); // le bon nombre d'ordonnance
        assertEquals(patient1, consultation1.getOrdonnances().get(0).getPatient()); // le bon patient
        assertEquals(drSmith, consultation1.getOrdonnances().get(0).getMedecin()); // le bon medecin referent
        assertEquals("03/06/2024", consultation1.getOrdonnances().get(0).getDatePrescription());

    }

    @Test
    @Order(7) // Comme le nom l'indique
    void testPatientNonHospitaliseConsultationAvecNimporteQuelMedecin() throws Exception {
        Consultation consultation = new Consultation(drJones, patient2);
        Ordonnance ordonnance = new Ordonnance("04/06/2024", drJones, patient2);
        Ordonnance ordonnance1 = new Ordonnance("04/06/2024", drSmith, patient2);

        patient2.ajouterConsultation(consultation);
        consultation.ajouterOrdonnance(ordonnance);
        consultation.ajouterOrdonnance(ordonnance1);
        assertEquals(2, consultation.getOrdonnances().size()); // 2 ordonnances
        assertEquals(patient2, consultation.getOrdonnances().get(0).getPatient()); // le bon patient
        assertEquals(drJones, consultation.getOrdonnances().get(0).getMedecin()); // le bon medecin pour la premiere ordonnance
        assertEquals(drSmith, consultation.getOrdonnances().get(1).getMedecin()); // le bon medecin pour la deuxieme ordonnance
        assertEquals("04/06/2024", consultation.getOrdonnances().get(0).getDatePrescription()); //la bonne date
    }



    @Test
    @Order(8) // affiche la liste des traitements actifs pour un patient avec gestion de plusieurs ordonnances
    void testAfficherTraitementsActifs() throws Exception {

        // Create consultations and prescriptions
        Consultation consult1 = new Consultation(drJones, patient2);

        // Create prescriptions and add treatments
        Ordonnance ordonnance1 = new Ordonnance("21/05/2024", drJones, patient2);
        Ordonnance ordonnance2 = new Ordonnance("21/05/2024", drJones, patient2);
        Ordonnance ordonnance3 = new Ordonnance("21/05/2024", drJones, patient2);

        ordonnance1.ajouterTraitement(vitamineC);
        ordonnance2.ajouterTraitement(aspirine);
        ordonnance2.ajouterTraitement(procedure1);

        consult1.ajouterOrdonnance(ordonnance1);
        consult1.ajouterOrdonnance(ordonnance2);
        consult1.ajouterOrdonnance(ordonnance3);

        // Add consultations to the patient
        patient2.ajouterConsultation(consult1);

        // Display active treatments
        consult1.afficherTraitementActifs(21); // Pour le jour 21
    }

    @Test
    @Order(9) // test du comptage des traitements dans une ordonnance + le fait qu'une ordonnance peut avoir plusieurs traitements distincts
    void testOrdonnance() throws Exception {

        Consultation consult1 = new Consultation(drJones, patient2);

        Ordonnance ordonnance = new Ordonnance("01/06/2024", drJones, patient1);

        ordonnance.ajouterTraitement(vitamineC);
        ordonnance.ajouterTraitement(procedure1);
        ordonnance.ajouterTraitement(aspirine);
        ordonnance.ajouterTraitement(procedure2);

        consult1.ajouterOrdonnance(ordonnance);
        patient1.ajouterConsultation(consult1);

        consult1.afficherTraitementActifs(2);
        assertEquals(1, consult1.getOrdonnances().size());
        assert(ordonnance.getTotalMedicamentsProcedures() == 4);
    }

    //////////// Tests des chambres ////////////
    @Test
    @Order(10) // ajout de Patient a une chambre avec scenario d'erreur en cas de surcapacite
    void testChambre() {
        assertDoesNotThrow(() -> chambre1.ajouterPatient(patient1)); // la chambre 1 a un lit
        assertThrows(Exception.class, () -> chambre1.ajouterPatient(patient2)); // 2 personnes pour un lit c'est trop

        assertDoesNotThrow(() -> chambre2.ajouterPatient(patient1)); // la chambre 2 a 2 lit
        assertDoesNotThrow(() -> chambre2.ajouterPatient(patient2));
        assertThrows(Exception.class, () -> chambre1.ajouterPatient(patient3)); // 3 personnes pour 2 lit c'est trop
    }

    @Test
    @Order(11)
    public void dechargeFinHospitalisationPatients() throws Exception {
        // Hospitaliser des patients
        Hospitalisation hospitalisation1 = new Hospitalisation(chambre1, 20240610, 20240612, drJones);
        patient1.hospitaliser(hospitalisation1);

        Hospitalisation hospitalisation2 = new Hospitalisation(chambre2, 20240610, 20240615, drJones);
        patient2.hospitaliser(hospitalisation2);

        Hospitalisation hospitalisation3 = new Hospitalisation(chambre3, 20240610, 20240620, drJones);
        patient3.hospitaliser(hospitalisation3);

        // Vérifier que les patients sont hospitalisés
        assertEquals(1, chambre1.getPatients().size());
        assertEquals(1, chambre2.getPatients().size());
        assertEquals(1, chambre3.getPatients().size());

        // Appeler la méthode pour décharger les patients dont la date de sortie est passée
        int currentDate = 20240616; // Date actuelle
        hopital.dechargerFinHospitalisationPatients(currentDate);

        // Vérifier que patient1 et patient2 sont déchargés mais pas patient3
        assertEquals(0, chambre1.getPatients().size());
        assertEquals(0, chambre2.getPatients().size());
        assertEquals(1, chambre3.getPatients().size());
    }

    @Test
    @Order(12) // test de surcharge de capacite d'une chambre
    public void testHospitalisationOverCapacity() {
        Exception exception = assertThrows(Exception.class, () -> {
            Hospitalisation hospitalisation1 = new Hospitalisation(chambre2, 20240614, 20240620, drJones);
            patient1.hospitaliser(hospitalisation1);
            Hospitalisation hospitalisation2 = new Hospitalisation(chambre2, 20240614, 20240620, drJones);
            patient2.hospitaliser(hospitalisation2);
            Hospitalisation hospitalisation3 = new Hospitalisation(chambre2, 20240614, 20240620, drJones);
            patient3.hospitaliser(hospitalisation3);
        });

        String expectedMessage = "Capacité de la chambre dépassée";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(13) // test de la fonciton decharge qui vide une chambre de son patient
    public void testDechargePatient() throws Exception {
        Hospitalisation hospitalisation = new Hospitalisation(chambre3, 20240614, 20240620, drJones);
        patient3.hospitaliser(hospitalisation);
        assertEquals(1, chambre3.getPatients().size());
        patient3.decharger(20240620);
        assertEquals(0, chambre3.getPatients().size());
    }

    @Test
    @Order(14) // test du scenario d'erreur où on essaie de decharger un patient pas hospitaliser
    public void testDechargerPatientNotInHospital() {
        Exception exception = assertThrows(Exception.class, () -> {
            patient3.decharger(20240620);
        });

        String expectedMessage = "Aucune hospitalisation trouvée pour la date de sortie spécifiée";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(15) // test pour determiner que des chambres sont disponibles (trouverChambreDisponible)
    public void testTrouverChambreDispo() throws Exception {
        Chambre chambreDisponible = hopital.trouverChambreDisponible();
        assertNotNull(chambreDisponible);
        assertTrue(chambreDisponible.getPatients().size() < chambreDisponible.getCapacite());
    }

    @Test
    @Order(16) //Test d'erreur lorsque l'hopital est plein == Pas de chambre disponible ////
    public void testNoAvailableRoom() throws Exception {

        Patient patient4 = new Patient("White", "Mister", 1990, "M");
        Patient patient5 = new Patient("Black", "Mamba", 1990, "M");
        Patient patient6 = new Patient("Yellow", "Peril", 1990, "M");
        Patient patient7 = new Patient("Green", "Sun", 1990, "M");
        Patient patient8 = new Patient("Blue", "Harvest", 1990, "M");
        Patient patient9 = new Patient("Purple", "Rain", 1990, "M");
        Patient patient10 = new Patient("Grey", "Fox", 1990, "M");

        Hospitalisation hospitalisation1 = new Hospitalisation(chambre1, 20240614, 20240620, drJones);
        patient1.hospitaliser(hospitalisation1);
        Hospitalisation hospitalisation2 = new Hospitalisation(chambre2, 20240614, 20240620, drJones);
        patient2.hospitaliser(hospitalisation2);
        Hospitalisation hospitalisation3 = new Hospitalisation(chambre2, 20240614, 20240620, drJones);
        patient3.hospitaliser(hospitalisation3);
        Hospitalisation hospitalisation4 = new Hospitalisation(chambre3, 20240614, 20240620, drJones);
        patient4.hospitaliser(hospitalisation4);
        Hospitalisation hospitalisation5 = new Hospitalisation(chambre3, 20240614, 20240620, drJones);
        patient5.hospitaliser(hospitalisation5);
        Hospitalisation hospitalisation6 = new Hospitalisation(chambre3, 20240614, 20240620, drJones);
        patient6.hospitaliser(hospitalisation6);
        Hospitalisation hospitalisation7 = new Hospitalisation(chambre4, 20240614, 20240620, drJones);
        patient7.hospitaliser(hospitalisation7);
        Hospitalisation hospitalisation8 = new Hospitalisation(chambre4, 20240614, 20240620, drJones);
        patient8.hospitaliser(hospitalisation8);
        Hospitalisation hospitalisation9 = new Hospitalisation(chambre4, 20240614, 20240620, drJones);
        patient9.hospitaliser(hospitalisation9);
        Hospitalisation hospitalisation10 = new Hospitalisation(chambre4, 20240614, 20240620, drJones);
        patient10.hospitaliser(hospitalisation10);

        Exception exception = assertThrows(Exception.class, () -> {
            hopital.trouverChambreDisponible();
        });

        String expectedMessage = "Aucune chambre disponible";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(17) // test chevauchement de 2 hospitalisations ////
    void testChevauchement() {
        Hospitalisation hosp1 = new Hospitalisation(chambre1, 0, 10, drSmith);
        Hospitalisation hosp2 = new Hospitalisation(chambre2, 5, 15, drSmith);
        Hospitalisation hosp3 = new Hospitalisation(chambre3, 11, 20, drSmith);

        assertThrows(ChevauchementException.class, () -> hosp1.chevauche(hosp2));
        System.out.println("Chevauchement détecté entre les hospitalisations.");
        assertDoesNotThrow(() -> hosp1.chevauche(hosp3));
        assertThrows(ChevauchementException.class, () -> hosp2.chevauche(hosp3));
        System.out.println("Chevauchement détecté entre les hospitalisations.");
    }


    ////////////// test dosage medicament sur une ou plusieurs prescriptions et medicament avec limite ou sans limite//////////
    ///////////// Test aussi si le dosage journalier de plusieurs ordonnances depasse le max /////////
    @Test
    @Order(18)
    void testAspirineMaxDose() {
        // Prescription de dose d'aspirine
        Medicament prescription1 = new Medicament("Aspirine", 2.0, 4.0, 20240101, 20240105); // 2g par jour
        Medicament prescription2 = new Medicament("Aspirine", 2.0, 4.0, 20240101, 20240105); // 4g par jour
        Medicament prescription3 = new Medicament("Aspirine", 2.0, 4.0, 20240101, 20240105); // 6g par jour

        // Vérifier que la prescription 1 est acceptée (2g/jour)
        assertDoesNotThrow(() -> aspirine.ajouterTraitement(prescription1));

        // Vérifier que la prescription 2 est toujours acceptée (2+2 = 4g/jour)
        assertDoesNotThrow(() -> aspirine.ajouterTraitement(prescription2));

        // Vérifier que la prescription 3 n'est pas acceptée (2+2+6 = 6g/jour)
        assertThrows(DoseMaximaleDepasseeException.class, () -> aspirine.ajouterTraitement(prescription3));
    }

    @Test
    @Order(19)
    void testIbuprofeneMaxDose() {
        // Prescription de dose d'ibuprofène
        Medicament prescription1 = new Medicament("Ibuprofène", 1.0, 3.0, 20240101, 20240103); // 3g par jour
        Medicament prescription2 = new Medicament("Ibuprofène", 2.5, 3.0, 20240101, 20240103); // 6g par jour

        // Vérifier que la prescription 1 est acceptée (3g/jour)
        assertDoesNotThrow(() -> ibuprofene.ajouterTraitement(prescription1));

        // Vérifier que la prescription 2 est rejetée (1+2.5 = 3.5g/jour)
        assertThrows(DoseMaximaleDepasseeException.class, () -> ibuprofene.ajouterTraitement(prescription2));
    }

    @Test
    @Order(20)
    void testParacetamolMaxDose() {
        // Prescription de dose de paracétamol
        Medicament prescription1 = new Medicament("Paracétamol", 6, 4.0, 20240101, 20240107); // 6g par jour

        // Vérifier que la prescription est rejetée (6g/jour)
        assertThrows(DoseMaximaleDepasseeException.class, () -> paracetamol.ajouterTraitement(prescription1));
    }

    @Test
    @Order(21)
    void testVitamineCMaxDose() {
        // Prescription de dose de paracétamol
        Medicament prescription1 = new Medicament("VitamineC", 100, null, 20240101, 20240107); // 6g par jour

        // Vérifier que la prescription est rejetée (6g/jour)
        assertDoesNotThrow(() -> vitamineC.ajouterTraitement(prescription1));
    }


    ///////// Test divers pour verifier que la plupart des choses où j'ai eu des petits problemes fonctionnent ///////

    @Test
    @Order(22) // test de la detection de l'hospitalisation //
    void testHospitalisationPeriod() {
        Hospitalisation hosp1 = new Hospitalisation(chambre1, 0, 10, drSmith);

        assertTrue(hosp1.estDansPeriode(5)); // le 5 il est toujours dans l'hopital
        assertFalse(hosp1.estDansPeriode(15)); // le 15 il est sorti le 10
    }


    @Test
    @Order(23) // test du medecin referent //
    void testMedecinReferent() {
        Hospitalisation hosp1 = new Hospitalisation(chambre1, 0, 10, drSmith);

        assertEquals(drSmith, hosp1.getMedecinReferent());
    }

    @Test
    @Order(24) // test de l'ajout des traitements ///
    void testAjouterTraitement() {
        ordonnance1.ajouterTraitement(aspirine);
        ordonnance1.ajouterTraitement(ibuprofene);
        ordonnance1.ajouterTraitement(procedure1);

        assertEquals(3, ordonnance1.getTraitements().size());
    }

    @Test
    @Order(25)
    void testGetTraitements() {
        ordonnance1.ajouterTraitement(aspirine);
        ordonnance1.ajouterTraitement(ibuprofene);

        Traitement[] traitements = ordonnance1.getTraitements().toArray(new Traitement[0]);
        assertEquals(2, traitements.length);
        assertInstanceOf(Medicament.class, traitements[0]);
        assertInstanceOf(Medicament.class, traitements[1]);
    }


    @Test
    @Order(26) // pour rappel la procedure 1 dure 5 jours, la procedure 2 dure 10 jours
    void testProcedureEstActive() {
        assertTrue(procedure1.estActif(20240102, 20240101));
        assertFalse(procedure1.estActif(20240106, 20240101)); // + de 5 jours entre date ordonnance et actuel
        assertTrue(procedure2.estActif(20240105, 20240101));
        assertFalse(procedure2.estActif(20240115, 20240101)); // + de 10 jours entre date ordonnance et actuel
    }

    @Test
    @Order(27) //test du status avant date ordonnance
    void testProcedureEstActifBeforeStartDate() {
        assertFalse(procedure1.estActif(20231231, 20240101));
    }


    @Test
    @Order(28) // test de string //
    void testProcedureToString() {
        assertEquals("Procedure{nom='Physiothérapie', description='Thérapie physique', dateDebut=20240101, duree=5}", procedure1.toString());
    }



}