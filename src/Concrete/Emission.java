package Concrete;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe representant une emission en elle-meme.
 * Il n'y a pas de classe pour chaque categorie car les donnees ne sont pas tres differentes selon les categories,
 * nous menant a une seule sous-classe Emission, pour gerer correctement les numero de saison et episode.
 */
public class Emission {
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* --------------------------------------- ATTRIBUTS ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    protected String titre;
    protected String sousTitre;
    protected String desc;
    protected double duree;
    protected String type_duree;
    protected String pays;
    protected String categorie;
    protected String date_creation;
    protected String stereo;
    protected String rating;
    protected String rating_system;
    protected ArrayList<Diffusion> diffusions;
    protected Chaine chaine;
    protected HashMap<String, ArrayList<String>> credits;
    protected Video video;

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------- CONSTRUCTEURS ------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    /**
     * Cree un objet Emission
     * @param chaine    Chaine a laquelle appartient l'emission
     */
    public Emission(String titre, String sousTitre, String desc, double duree, String type_duree, String pays, String categorie,
                    String date_creation, String stereo, String rating, String rating_system, HashMap credits, Chaine chaine,
                    String video_aspect, String video_quality, boolean video_colour, LocalDateTime dif_debut, LocalDateTime dif_fin) {
        this.titre = titre;
        this.sousTitre = (sousTitre == null) ? "" : sousTitre;
        this.desc = (desc == null) ? "" : desc;
        this.duree = duree;
        this.type_duree = (type_duree == null) ? "" : type_duree;
        this.pays = (pays == null) ? "" : pays;
        this.categorie = (categorie == null) ? "" : categorie;
        this.date_creation = (date_creation == null) ? "" : date_creation;
        this.stereo = (stereo == null) ? "" : stereo;
        this.rating = (rating == null) ? "" : rating;
        this.rating_system = (rating_system == null) ? "" : rating_system;
        this.credits = credits;
        this.chaine = chaine;
        this.chaine.ajouterEmission(categorie, this);
        this.video = (video_aspect == null && video_quality == null ? null : new Video(video_colour, video_aspect, video_quality, this));
        this.diffusions = new ArrayList<>();
        this.diffusions.add(new Diffusion(dif_debut, dif_fin, this, chaine));
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* ---------------------------------------- METHODES ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    // Getters - Setters ----------------------------------------------

    /**
     * Titre de l'emission
     * @return  String contenant le titre de l'emission
     */
    public String getTitre() { return titre; }

    /**
     * Sous-titre de l'emission
     * @return  String contenant le sous-titre de l'emission
     */
    public String getSousTitre() {
        return sousTitre;
    }

    /**
     * Description de l'emission
     * @return  String contenant la description de l'emission
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Duree de l'emission
     * @return  double contenant la duree de l'emission
     */
    public double getDuree() {
        return duree;
    }

    /**
     * Le type de la duree de l'emission (miniutes, etc.)
     * @return  String contenant le type de duree de l'emission
     */
    public String getType_duree() {
        return type_duree;
    }

    /**
     * Pays d'origine de l'emission
     * @return  String contenant le pays d'origine de l'emission
     */
    public String getPays() { return pays; }

    /**
     * Categorie a laquelle appartient l'emission
     * @return  String contenant la categorie de l'emission
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Date / Annee de creation de l'emission
     * @return  String contenant la date de creation de l'emission
     */
    public String getDate_creation() {
        return date_creation;
    }

    /**
     * Specifite sonore stereo de l'emission
     * @return  String contenant le type de son de l'emission
     */
    public String getStereo() {
        return stereo;
    }

    /**
     * Ais / Note de l'emission
     * @return  String contenant la note de l'emission
     */
    public String getRating() {
        return rating;
    }

    /**
     * L'agence / le type de notation de l'emission
     * @return  String contenant l'agence de notation de l'emission
     */
    public String getRating_system() {
        return rating_system;
    }

    /**
     * Toutes les diffusions de l'emission
     * @return  ArrayList contenant les diffusions de cette emission
     */
    public ArrayList<Diffusion> getDiffusions() {
        return diffusions;
    }

    /**
     * Chaine a laquelle appartient l'emission
     * @return  Chaine a laquelle appartient l'emission
     */
    public Chaine getChaine() {
        return chaine;
    }

    /**
     * Ensemble des participants a l'emission
     * @return  HashMap contenant toutes les personnes participant a l'emission, trie selon leurs roles
     */
    public HashMap<String, ArrayList<String>> getCredits() {
        return credits;
    }

    /**
     * Specificites video de l'emission
     * @return  une objet Video contenant les specificites video de l'emission
     */
    public Video getVideo() {
        return video;
    }

    // Override --------------------------------------------------------

    /**
     * Compare une autre instance d'emission avec celle-ci
     * @return  Vrai si les deux instances comportent exactement les memes donnees, faux sinon
     */
    @Override
    public boolean equals(Object o) {
        Emission tmp;

        if(o instanceof Emission) {
            tmp = (Emission) o;
            if(this.titre.equals(tmp.getTitre()) && this.sousTitre.equals(tmp.getSousTitre())
                    && this.desc.equals(tmp.getDesc()) && this.duree == tmp.getDuree()
                    && this.type_duree.equals(tmp.getType_duree()) && this.pays.equals(tmp.getPays())
                    && this.categorie.equals(tmp.getCategorie()) && this.date_creation.equals(tmp.getDate_creation())
                    && this.stereo.equals(tmp.getStereo()) && this.rating.equals(tmp.getRating())
                    && this.rating_system.equals(tmp.getRating_system()))
                return true;
        }

        return false;
    }

    // Affichages ------------------------------------------------------

    /**
     * Affichage de l'emission dans un format court, peu detaille
     * @return  String contenant le format court
     */
    public String affichage_court() {
        return titre + " ("+ categorie + ")";
    }

    /**
     * Affichage de l'emission dans un format long, tres detaille
     * @return  String contenant le format long
     */
    public String affichage_long() {
        return titre + (!sousTitre.equals("") ? " ---- " + sousTitre : "") + " ---- " + categorie
                + (desc.equals("") ? "" : "\n\n" + desc)
                + (duree == 0 ? "" : "\n\n" + duree + " " + type_duree)
                + (pays.equals("") ? "" : "\n\n" + pays) + (date_creation.equals("") ? "" :  " ------ ") + date_creation
                + (stereo.equals("") ? "" : "\n\nSon :" + stereo)
                + (video == null ? "" : "\n\n"+ video)
                + (rating_system.equals("") && rating.equals("") ? "" : "\n\nRating : ") + rating_system
                + (rating_system.equals("") ? "" : " :\t" + rating) + "\n" + affichage_credits();
    }

    /**
     * Affichage des credits
     * @return  String contanant l'affichage des credits
     */
    protected String affichage_credits() {
        String str = "\n";

        // Pas de credits
        if(credits == null)
            return "";

        // Ajout de chaque participant a l'affichage
        for(String role : credits.keySet())
            str += role + ": " + credits.get(role) + "\n";

        return str;
    }

    // Autres -------------------------------------------------------

    /**
     * Regarde si il y a une personne avec ce nom et ce role dans cette emission
     * @param nom   Nom de la personne
     * @param role  Role de la personne
     * @return Vrai si trouvee, faux sinon
     */
    public boolean recherchePersonne(String nom, String role) {
        // On regarde s'il y a bien ce role
        if(credits == null || !credits.containsKey(role.toLowerCase()))
            return false;

        // Recherche dans l'arraylist du role la personne
        for(String pers : credits.get(role.toLowerCase())) {
            if(pers.toLowerCase().contains(nom.toLowerCase()))
                return true;
        }

        return false;
    }
}
