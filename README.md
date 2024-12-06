## French 
# Explication classes principales :
      Patient : Représente un patient avec des attributs comme le nom, prénom, date de naissance, sexe, et des listes pour les hospitalisations, consultations, et ordonnances. Il a des méthodes pour gérer ses hospitalisations, ajouter des consultations, et obtenir des informations spécifiques comme son médecin référent.
      Medecin : Représente un médecin avec des attributs tels que le nom et le prénom. Il peut être associé à des consultations et ordonnances.
      Chambre : Représente une chambre d'hôpital avec un numéro, une capacité et une liste de patients actuellement dans la chambre. Il a des méthodes pour ajouter ou retirer des patients et pour récupérer des informations sur sa capacité et ses patients.
      Hospitalisation : Représente une hospitalisation d'un patient dans une chambre avec une période d'entrée et de sortie, ainsi qu'un médecin référent. Il a des méthodes pour vérifier si une hospitalisation chevauche une autre et pour vérifier si elle est dans une période spécifique.
      Consultation : Représente une consultation d'un patient par un médecin, avec une liste d'ordonnances. Il a des méthodes pour ajouter des ordonnances et afficher les traitements actifs.
      Ordonnance : Représente une ordonnance donnée par un médecin à un patient à une certaine date, avec une liste de traitements associés.
      Traitements : Peuvent être de 2 types : Médicament ou procédure. Chaque type a ses spécificités.

# Consignes simples :
      Un patient peut avoir plusieurs hospitalisations, consultations et ordonnances.
      Un médecin peut être associé à plusieurs consultations et ordonnances.
      Une chambre peut contenir plusieurs patients.
      Une hospitalisation est liée à un patient, une chambre et un médecin référent.
      Une consultation implique un patient, un médecin et des ordonnances.
      Une ordonnance est liée à un patient, un médecin et des traitements.

# Fonctionnalités principales attendues:
      1) Gestion des patients, médecins, chambres, hospitalisations, consultations et ordonnances.
      2) Suivi des hospitalisations et des consultations.
      3) Attribution de médecins référents.
      4) Prescription et suivi des traitements par ordonnance.

# Classes et Méthodes Principales
      1.Patient
      Attributs : nom, prenom, dateDeNaissance, sexe
      Méthodes : hospitaliser(), decharger(), estHospitalise(), ajouterConsultation(), getOrdonnancesActives(), getMedecinReferent(), getHospitalisations()
      
      2. Medecin
      Attributs : nom, prenom
      Méthodes : getNom(), getPrenom()
      
      4.Chambre
      Attributs : numero, capacite, patients
      Méthodes : ajouterPatient(), retirerPatient(), getCapacite(), getPatients(), getNumero()
      
      5.Hospitalisation
      Attributs : chambre, dateEntree, dateSortie, medecinReferent
      Méthodes : chevauche(), estDansPeriode()
      
      6.Consultation
      Attributs : medecin, patient, ordonnances
      Méthodes : ajouterOrdonnance(), afficherTraitementActifs()
      
      7.Ordonnance
      Attributs : date, medecin, patient, traitements
      Méthodes : estCompatible(), afficherDetailsActifs(),ajouterTraitement(), getters divers et varies
      
      8.Traitement
      Attributs : dateDebut, duree
      Méthodes : estActif()
      Medicament (hérite de Traitement)
      Attributs : nom, doseParJour, doseMax, traitements
      Méthodes : ajouterTraitement()et getters
      
      9. Procedure (hérite de Traitement)
      Attributs : nom, description
      Méthodes : getDescription et toString
      
      Nous avons aussi une classe hôpital qui comporte la liste des chambre, medecin et patient ainsi que des methode pour les ajouter aux listes pertinentes. 
      Hôpital comporte aussi 2 grandes méthodes qui n’étaient pas explicitement demander dans l’énoncé : trouverChambreDisponible () et dechargerFinHospitalisationPatients()

# Relations entre Classes
      Patient à Hospitalisation : Un patient peut avoir plusieurs hospitalisations (1-to-many).
      Patient à Consultation : Un patient peut avoir plusieurs consultations (1-to-many).
      Patient à Ordonnance : Un patient peut avoir plusieurs ordonnances (1-to-many).
      Medecin à Hospitalisation : Un médecin peut être référent pour plusieurs hospitalisations (1-to-many).
      Medecin à Consultation : Un médecin peut avoir plusieurs consultations (1-to-many).
      Medecin à Ordonnance : Un médecin peut émettre plusieurs ordonnances (1-to-many).
      Chambre à Hospitalisation : Une chambre peut avoir plusieurs hospitalisations (1-to-many).
      Hospitalisation à Chambre : Une hospitalisation est liée à une chambre (many-to-1).
      Consultation à Ordonnance : Une consultation peut avoir plusieurs ordonnances (1-to-many).
      Ordonnance à Traitement : Une ordonnance peut contenir plusieurs traitements (1-to-many).
      Traitement à Medicament et Procedure : Les traitements sont soit des médicaments, soit des procédures (inheritance).

## English

# Main Classes Explanation:

      Patient: Represents a patient with attributes like name, surname, date of birth, gender, and lists for hospitalizations, consultations, and prescriptions. It has methods to manage hospitalizations, add consultations, and retrieve specific information like the referring doctor.
      Medecin (Doctor): Represents a doctor with attributes like name and surname. A doctor can be associated with consultations and prescriptions.
      Chambre (Room): Represents a hospital room with a number, capacity, and a list of patients currently in the room. It has methods to add or remove patients and retrieve information about its capacity and current patients.
      Hospitalisation (Hospitalization): Represents a patient’s hospitalization in a room, with entry and exit dates, and a referring doctor. It has methods to check if a hospitalization overlaps with another or if it falls within a specific period.
      Consultation: Represents a consultation between a patient and a doctor, with a list of prescriptions. It has methods to add prescriptions and display active treatments.
      Ordonnance (Prescription): Represents a prescription given by a doctor to a patient on a certain date, with a list of associated treatments.
      Traitements (Treatments): Can be of two types: medication or procedure. Each type has its specifics.

# Simple Rules:
      A patient can have multiple hospitalizations, consultations, and prescriptions.
      A doctor can be associated with multiple consultations and prescriptions.
      A room can accommodate multiple patients.
      A hospitalization is linked to a patient, a room, and a referring doctor.
      A consultation involves a patient, a doctor, and prescriptions.
      A prescription is linked to a patient, a doctor, and treatments.
      
# Main Features Expected:
      Management of patients, doctors, rooms, hospitalizations, consultations, and prescriptions.
      Tracking of hospitalizations and consultations.
      Assigning referring doctors.
      Prescription and tracking of treatments via prescriptions.

# Main Classes and Methods:

      Patient:
      Attributes: name, surname, date of birth, gender
      Methods: hospitaliser(), decharger(), estHospitalise(), ajouterConsultation(), getOrdonnancesActives(), getMedecinReferent(), getHospitalisations()
      
      Medecin:
      Attributes: name, surname
      Methods: getNom(), getPrenom()
      
      Chambre:
      Attributes: number, capacity, patients
      Methods: ajouterPatient(), retirerPatient(), getCapacite(), getPatients(), getNumero()
      
      Hospitalisation:
      Attributes: room, entry date, exit date, referring doctor
      Methods: chevauche(), estDansPeriode()
      
      Consultation:
      Attributes: doctor, patient, prescriptions
      Methods: ajouterOrdonnance(), afficherTraitementActifs()
      
      Ordonnance:
      Attributes: date, doctor, patient, treatments
      Methods: estCompatible(), afficherDetailsActifs(), ajouterTraitement(), various getters
      
      Traitement:
      Attributes: start date, duration
      Methods: estActif()
      Medicament (Medication) (inherits from Traitement):
      
      Attributes: name, daily dose, max dose, treatments
      Methods: ajouterTraitement() and getters
      Procedure (inherits from Traitement):
      
      Attributes: name, description
      Methods: getDescription(), toString()
      Additional Class – Hospital: Hospital class contains lists of rooms, doctors, and patients, with methods to add them to the relevant lists.
      The hospital class also has two main methods not explicitly required in the description: trouverChambreDisponible() and dechargerFinHospitalisationPatients().

# Relationships Between Classes:
      Patient to Hospitalisation: A patient can have multiple hospitalizations (1-to-many).
      Patient to Consultation: A patient can have multiple consultations (1-to-many).
      Patient to Ordonnance: A patient can have multiple prescriptions (1-to-many).
      Medecin to Hospitalisation: A doctor can be the referring doctor for multiple hospitalizations (1-to-many).
      Medecin to Consultation: A doctor can have multiple consultations (1-to-many).
      Medecin to Ordonnance: A doctor can issue multiple prescriptions (1-to-many).
      Chambre to Hospitalisation: A room can accommodate multiple hospitalizations (1-to-many).
      Hospitalisation to Chambre: A hospitalization is linked to a room (many-to-1).
      Consultation to Ordonnance: A consultation can have multiple prescriptions (1-to-many).
      Ordonnance to Traitement: A prescription can contain multiple treatments (1-to-many).
      Traitement to Medicament and Procedure: Treatments are either medications or procedures (inheritance).
