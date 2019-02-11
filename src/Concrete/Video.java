package Concrete;

/**
 * Classe conteant les specificites video d'une emission
 */
public class Video {
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* --------------------------------------- ATTRIBUTS ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    private boolean couleur;
    private String aspect;
    private String qualite;
    private Emission emission;

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------- CONSTRUCTEURS ------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    /**
     * Cree un objet Video, detaillant la qualite video d'une emission
     * @param couleur: Couleur ou Noir et blanc (Oui / Non)
     * @param aspect: Aspect horizontal:vertical (16:9, ...)
     * @param qualite: Detail sur la qualite (HDTV, ...)
     * @param emission: Emission associee a cette configuration video*/
    public Video(boolean couleur, String aspect, String qualite, Emission emission) {
        this.couleur = couleur;
        this.aspect = aspect;
        this.qualite = qualite;
        this.emission = emission;
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* ---------------------------------------- METHODES ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    // Getters et Setters ---------------------------------------

    public boolean isCouleur() {
        return couleur;
    }

    public String getAspect() {
        return aspect;
    }

    public String getQualite() {
        return qualite;
    }

    public Emission getEmission() {
        return emission;
    }

    // Override --------------------------------------------------

    @Override
    public String toString() {
        return (couleur  ? "En couleur\t" : "") + (aspect == null ? "" : aspect +  "\t") + (qualite == null ? "" : qualite);
    }
}
