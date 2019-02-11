package Concrete;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe representant une chaine de television
 * Garde en memoire les programmes diffuses, classes par jour de diffusion dans une TreeMap, afin de trier et d'ordonner.
 * Contient aussi toutes les emissions diffusees ou pas qui la concerne, ordonnees dans une HashMap afin de trier les emissions par categorie
 */
public class Chaine {
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* --------------------------------------- ATTRIBUTS ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    private String id;
    private String displayName;
    private TreeMap<LocalDate, ArrayList<Diffusion>> programmes;
    private HashMap<String, ArrayList<Emission>> emissions;

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------- CONSTRUCTEURS ------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    /**
     * Cree un objet de la classe Chaine, remplit durant le parsing
     */
    public Chaine(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.programmes = new TreeMap<>();
        this.emissions = new HashMap<>();
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* ---------------------------------------- METHODES ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    // Getters and Setters ---------------------------------------------

    /**
     * Fourni l'id de la chaine
     * @return String contenant l'id de la chaine
     */
    public String getId() {
        return id;
    }

    /**
     * Fourni le nom de la chaine
     * @return String contenant le nom de la chaine
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Fourni toutes les diffusions de la chaine
     * @return TreeMap contenant pour chaque jour une liste des diffusions de cette journee sur la chaine
     */
    public TreeMap<LocalDate, ArrayList<Diffusion>> getProgrammes() {
        return programmes;
    }

    /**
     * Fourni les emissions de la chaine
     * @return HashMap contenant les emissions appartenant a la chaine, trier selon leurs categories
     */
    public HashMap<String, ArrayList<Emission>> getEmissions() {
        return emissions;
    }

    /**
     * Recherche et fourni une emission de la chaine par rapport a son nom
     * @param nom_emission  Nom de l'emission recherchee
     * @return  Emission correspondante, sinon null
     */
    public Emission getEmissionParNom(String nom_emission) {
        Iterator it;
        Map.Entry pair;
        boolean trouve = false;
        int j;
        Emission e = null;

        // Parcours de la HashMap emissions
        it = emissions.entrySet().iterator();
        while(it.hasNext() && !trouve) {
            pair = (Map.Entry) it.next();
            j = 0;
            while(j < ((ArrayList<Emission>) pair.getValue()).size() && !trouve) {
                e = ((ArrayList<Emission>) pair.getValue()).get(j);
                trouve = e.getTitre().toLowerCase().equals(nom_emission.toLowerCase());
                j++;
            }
        }

        if(trouve)
            return e;
        return null;
    }

    /**
     * Recherche et fourni une emission de la chaine par rapport a une date et une heure precise
     * @param date  Date et horaire de la diffusion
     * @return Diffusion correspondante, sinon null
     */
    public Diffusion getDiffusionParDateHeure(LocalDateTime date) {
        boolean trouve = false;
        int i = 0;
        Diffusion d = null;
        LocalDate jour = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        ArrayList<Diffusion> prog_jour;

        // On voit si la date a bien une programmation pour cette journee
        if(!programmes.containsKey(jour))
            return null;

        // Recherche de la diffusion correspondante
        prog_jour = programmes.get(jour);
        while(!trouve && i < prog_jour.size()) {
            d = prog_jour.get(i);
            trouve = (d.getDebut().isBefore(date) && d.getFin().isAfter(date));
            i++;
        }

        // Envoi de l'emission correspondante au resultat
        if(trouve)
            return d;
        return null;
    }

    /**
     * Recupere les emissions de la categorie recherchee dans les diffusions
     * @param categorie Categorie que l'on recherche
     * @return Fournit une arraylist des emissions de la categorie recherchee
     */
    public ArrayList<Emission> getEmissionsDifParCategorie(String categorie) {
        Emission em;
        ArrayList<Emission> listeEm = new ArrayList<>();

        // Recherche des emissions avec cette personne a ce role
        for(LocalDate date : programmes.keySet()) {
            for(Diffusion dif : programmes.get(date)) {
                em = dif.getEmission();
                if(em.getCategorie().toLowerCase().equals(categorie.toLowerCase()))
                    listeEm.add(em);
            }
        }

        // Envoie du resultat
        if(listeEm.isEmpty())
            return null;
        return listeEm;
    }

    /**
     * Founit la liste des emissions de la chaine ayant un avis du CSA
     * @return  L'arrayList des emissions avec un avis du CSA
     */
    public ArrayList<Emission> getEmissionAvecCSA() {
        ArrayList<Emission> listeEm = new ArrayList<>();

        // Recherche des emissions avec cette personne a ce role
        for(String categ : emissions.keySet()) {
            for(Emission em : emissions.get(categ)) {
                if(em.getRating_system().toUpperCase().equals("CSA"))
                    listeEm.add(em);
            }
        }

        // Envoie du resultat
        if(listeEm.isEmpty())
            return null;
        return listeEm;
    }

    // Override --------------------------------------------------------

    /**
     * Donne retourne le contenu simple de la chaine sous forme de String
     * @return  String contenant les informations simples de la chaine
     */
    @Override
    public String toString() {
        return this.id + " -- " + this.displayName + "\n";
    }

    // Affichages ------------------------------------------------------

    /**
     * Affiche toute la programmation de la chaine
     */
    public void afficherProg() {
        String str = "";

        for(LocalDate date : programmes.keySet()) {
            str += "\n----------------------------- " + date + "-----------------------------\n";
            for(Diffusion dif : programmes.get(date))
                str += dif;
        }

        System.out.println(str);
    }

    /**
     * Affiche la programmation d'un jour de la chaine
     * @param jour  Jour de la programmation que l'on souhaite afficher
     */
    public void afficherProgJour(LocalDate jour) {
        if(!programmes.containsKey(jour)) {
            System.err.println("Erreur : " + displayName + " n'a pas de programmation pour le " + jour);
            return;
        }

        // Affichage des diffusions
        System.out.println("\n----------------------------- " + jour + " -----------------------------\n");
        for(Diffusion dif : programmes.get(jour))
            System.out.println(dif);
    }

    // Autres -----------------------------------------------------------

    /**
     * Ajoute une emission a la chaine, dans la categorie correspondante
     * @param categorie     categorie de l'emission a ajouter (ex: Film)
     * @param emission      emission que l'on souhaite ajouter a la chaine
     */
    public void ajouterEmission(String categorie, Emission emission) {
        // Creation du champ dans la hashmao si inexistante
        if(!this.emissions.containsKey(categorie))
            this.emissions.put(categorie, new ArrayList<Emission>());

        // Ajout de la diffusion si inexistante
        if(!this.emissions.get(categorie).contains(emission))
            this.emissions.get(categorie).add(emission);
    }

    /**
     * Ajoute une diffusion d'une emission dans le programme de la chaine
     * @param jour          Jour de diffusion
     * @param diffusion     Diffusion a ajouter
     */
    public void ajouterDiffusion(LocalDate jour, Diffusion diffusion) {
        // Creation du champ du jour concerne si inexistant
        if(!this.programmes.containsKey(jour))
            this.programmes.put(jour, new ArrayList<Diffusion>());

        // Ajour de la diffusion au jour concerne
        if(!this.programmes.get(jour).contains(diffusion))
            this.programmes.get(jour).add(diffusion);
    }
}
