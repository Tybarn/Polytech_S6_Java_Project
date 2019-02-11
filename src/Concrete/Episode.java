package Concrete;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Une Emission specialisee en tant qu'Episode d'une saison, avec un numero de saison et d'episode dans cette saison
 */
public class Episode extends Emission {
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* --------------------------------------- ATTRIBUTS ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    private int num_saison;
    private int num_ep;

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------- CONSTRUCTEURS ------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    /**
     * Cree un objet serie, derive d'une emission, possedant en plus une saison et un numero d'episode
     * @param num_saison: Numero de la saison correspondant a l'episode de la serie
     * @param num_ep: Numero de l'episode dans la saison
     */
    public Episode(String titre, String sousTitre, String desc, double duree, String type_duree, String pays, String categorie,
                   String date_creation, String stereo, String rating, String rating_system, HashMap credits, Chaine chaine, String video_aspect,
                   String video_quality, boolean video_colour, LocalDateTime dif_debut, LocalDateTime dif_fin, int num_saison, int num_ep) {

        super(titre,sousTitre,desc,duree,type_duree,pays,categorie,date_creation,stereo,rating,rating_system,credits,chaine,
                video_aspect, video_quality, video_colour, dif_debut, dif_fin);
        this.num_saison = num_saison;
        this.num_ep = num_ep;
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* ---------------------------------------- METHODES ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    // Getters et Setters ---------------------------------------------

    /**
     * Numero de la saison a laquelle appartient l'episode
     * @return  Entier representant le numero de la saison
     */
    public int getNum_saison() {
        return num_saison;
    }

    /**
     * Numero que represente cet episode dans la saison
     * @return  Entier reprensentant le numero de cet episode
     */
    public int getNum_ep() {
        return num_ep;
    }

    // Affichages ------------------------------------------------------

    /**
     * Affichage de l'episode dans un format court, peu detaille
     * @return  String contenant le format court
     */
    public String affichage_court() {
        return super.affichage_court() + ": Saison " + num_saison + " Episode " + num_ep;
    }

    /**
     * Affichage de l'episode dans un format long, tres detaille
     * @return  String contenant le format long
     */
    public String affichage_long() {
        return titre + (sousTitre.equals("") ? " ---- " + sousTitre : "")
                + "\nSaison " + num_saison + "\tEpisode "+ num_ep
                + (desc.equals("") ? "" : "\n\n" + desc)
                + (duree == 0 ? "" : "\n\n" + duree + " " + type_duree)
                + (pays.equals("") ? "" : "\n\n" + pays) + (date_creation.equals("") ? "" :  " ------ ") + date_creation
                + (stereo.equals("") ? "" : "\n\nSon :" + stereo)
                + (video == null ? "" : "\n\n"+ video)
                + (rating_system.equals("") && rating.equals("") ? "" : "\n\nRating : ") + rating_system
                + (rating_system.equals("") ? "" : " :\t" + rating) + "\n" + super.affichage_credits();
    }
}
