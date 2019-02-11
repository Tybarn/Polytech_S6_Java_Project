package Concrete;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Classe representant la diffusion d'une emission sur une chaine
 */
public class Diffusion {
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* --------------------------------------- ATTRIBUTS ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    private LocalDateTime debut;
    private LocalDateTime fin;
    private Emission emission;
    private Chaine chaine;

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------- CONSTRUCTEURS ------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    /**
     * Cree un objet Diffusion
     * @param debut     Heure de debut de la diffusion
     * @param fin       Heure de fin de la diffusion
     * @param emission  Emission diffusee
     */
    public Diffusion(LocalDateTime debut, LocalDateTime fin, Emission emission, Chaine chaine) {
         this.debut = debut;
         this.fin = fin;
         this.emission = emission;
         this.chaine = chaine;
         this.chaine.ajouterDiffusion(LocalDate.of(debut.getYear(), debut.getMonthValue(), debut.getDayOfMonth()), this);
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* ---------------------------------------- METHODES ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    // Getters - Setters --------------------------------

    /**
     * Fourni la date et heure de debut de la diffusion
     * @return Date et heure de debut
     */
    public LocalDateTime getDebut() {
        return debut;
    }

    /**
     * Fourni la date et heure de fin de la diffusion
     * @return Date et heure de fin
     */
    public LocalDateTime getFin() {
        return fin;
    }

    /**
     * Fourni l'emission concernee par la diffusion
     * @return Emission diffusee
     */
    public Emission getEmission() {
        return emission;
    }

    /**
     * Fourni la chaine concernee par la diffusion
     * @return Chaine sur laquelle est diffusee l'emission
     */
    public Chaine getChaine() {
        return chaine;
    }

    // Override -----------------------------------------

    /**
     * Compare cette diffusion avec une autre diffusion
     * @return Vrai si les deux diffusions sont egales, faux sinon
     */
    @Override
    public boolean equals(Object o) {
        Diffusion tmp;

        if(o instanceof Diffusion) {
            tmp = (Diffusion) o;
            if(this.debut.equals(tmp.getDebut()) && this.fin.equals(tmp.getFin())
                    && this.emission.equals(tmp.getEmission()) && this.chaine.equals(tmp.getChaine()))
                return true;
        }

        return false;
    }

    // Affichage ----------------------------------------

    /**
     * Cree un string contenant les donnees de cette diffusion, dans le but d'etre affiche
     * @return String contenant l'affiche de cette diffusion
     */
    public String toString() {
        return (debut.getHour() < 10 ? "0" :"") + debut.getHour() + ":"
                + (debut.getMinute() < 10 ? "0" :"") + debut.getMinute() + " -> "
                + (fin.getHour() < 10 ? "0" :"") + fin.getHour() + ":"
                + (fin.getMinute() < 10 ? "0" :"") + fin.getMinute() + "\t----\t"
                + emission.affichage_court() + "\n";
    }
}
