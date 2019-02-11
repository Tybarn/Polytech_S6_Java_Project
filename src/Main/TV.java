package Main;

import Concrete.Chaine;
import Concrete.Diffusion;
import Concrete.Emission;
import Concrete.Episode;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Classe principale de l'application, contient les fonctionnalites et les chaines de l'application
 */
public class TV {
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* --------------------------------------- ATTRIBUTS ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    private ArrayList<Chaine> chaines;

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------- CONSTRUCTEURS ------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    /**
     * Constructeur vide, ne cree aucune chaine et necessite plus tard d'appeler parserXML
     */
    public TV() {chaines = null;}

    /**
     * Constructeur creant les chaines de television
     * @param name_file: nom du fichier xml contenant toutes les chaines et programmes
     */
    public TV(String name_file) {
        chaines = null;
        try {
            parserXML(name_file);
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* ---------------------------------------- METHODES ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    // Getters et Setters ----------------------------------------------

    /**
     * Permet de recuperer une chaine par son ID
     * @param id    ID de la chaine recherchee
     * @return      Retourne la chaine trouvee, ou null si non trouvee
     */
    public Chaine getChaine(String id) {
        int i = 0;
        while(i < chaines.size() && !chaines.get(i).getId().equals(id))
            i++;
        if(i == chaines.size())
            return null;
        return chaines.get(i);
    }

    /**
     * Permet de recuperer la liste des chaines
     * @return  ArrayList des chaines tv
     */
    public ArrayList<Chaine> getChaines() {
        return chaines;
    }

    // Parser ---------------------------------------------------------

    /**
     * Cree tous les objets necessaires a application a partir du fichier xml passe en parametre
     * @exception: FileNotFoundException, IOException
     * @param name_file: Nom du fichier XML, avec son chemin
     */
    public void parserXML(String name_file) throws Exception {
        // Variables XML
        int eventType;
        String eventName;
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlsr = xmlif.createXMLStreamReader(new FileReader(name_file));

        // Variables pour chaine
        Chaine tmp_ch = null;
        String id_ch= null, display_name_ch = null;

        // Variables pour emission
        String title_em = null, sub_title_em = null, desc_em = null, category_em = null, length_em = null, length_type_em = null,
                country_em = null, rating_em = null, rating_system_em = null, stereo_em = null, ep_num_em = null, date_em = null;
        String video_aspect = null, video_quality = null;
        boolean video_colour = false, episode = false;
        int num_ep = 0, num_saison_ep = 0;
        String[] parse_num_ep = null;
        HashMap<String, ArrayList<String>> credits_em = null;

        // Variable pour diffusion
        LocalDateTime start_dif = null, stop_dif = null;
        DateTimeFormatter date_format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss xxxx");

        chaines = new ArrayList<Chaine>();

        // Parser
        while(xmlsr.hasNext()) {
            eventType = xmlsr.next();
            // Recuperation des donnees
            if(eventType == XMLEvent.START_ELEMENT) {
                eventName = xmlsr.getName().toString();
                switch(eventName) {
                    case "channel":
                        id_ch = xmlsr.getAttributeValue(null, "id");
                        break;
                    case "programme":
                        tmp_ch = this.getChaine(xmlsr.getAttributeValue(null, "channel"));
                        start_dif = LocalDateTime.parse(xmlsr.getAttributeValue(null, "start"), date_format);
                        stop_dif = LocalDateTime.parse(xmlsr.getAttributeValue(null, "stop"), date_format);
                        break;
                    case "display-name":
                        display_name_ch = xmlsr.getElementText();
                        break;
                    case "title":
                        title_em = xmlsr.getElementText();
                        break;
                    case "sub-title":
                        sub_title_em = xmlsr.getElementText();
                        break;
                    case "desc":
                        desc_em = xmlsr.getElementText();
                        break;
                    case "credits":
                        credits_em = new HashMap<>();
                        break;
                    case "director":
                    case "actor":
                    case "writer":
                    case "adapter":
                    case "producer":
                    case "composer":
                    case "editor":
                    case "presenter":
                    case "commentator":
                    case "guest":
                        if(!credits_em.containsKey(eventName))
                            credits_em.put(eventName.toLowerCase(), new ArrayList<String>());
                        credits_em.get(eventName.toLowerCase()).add(xmlsr.getElementText());
                        break;
                    case "date":
                        date_em = xmlsr.getElementText();
                        break;
                    case "category":
                        category_em = xmlsr.getElementText().toLowerCase();
                        break;
                    case "length":
                        length_type_em = xmlsr.getAttributeValue(null, "units");
                        length_em = xmlsr.getElementText();
                        break;
                    case "country":
                        country_em = xmlsr.getElementText();
                        break;
                    case "aspect":
                        video_aspect = xmlsr.getElementText();
                        break;
                    case "quality":
                        video_quality = xmlsr.getElementText();
                        break;
                    case "colour":
                        video_colour = true;
                        break;
                    case "rating":
                        rating_system_em = xmlsr.getAttributeValue(null, "system");
                        while(xmlsr.next() != XMLEvent.START_ELEMENT);
                        if(xmlsr.getName().toString().equals("value"))
                            rating_em = xmlsr.getElementText();
                        break;
                    case "stereo":
                        stereo_em = xmlsr.getElementText();
                        break;
                    case "episode-num":
                        episode = true;
                        ep_num_em = xmlsr.getElementText();
                        break;
                }
            }
            // Creation des objets
            else if(eventType == XMLEvent.END_ELEMENT) {
                eventName = xmlsr.getName().toString();
                switch(eventName) {

                    // Creation de la chaine
                    case "channel":
                        if(id_ch != null && display_name_ch != null) {
                            tmp_ch = new Chaine(id_ch, display_name_ch);
                            chaines.add(tmp_ch);
                        }
                        else {
                            System.err.println("ParserXML : Erreur lors de la creation d'une chaine");
                            return;
                        }
                        id_ch = null;
                        display_name_ch = null;
                        tmp_ch = null;
                        break;

                    // Creation de l'emission et de ses diffusions
                    case "programme":
                        if(tmp_ch == null) {
                            System.err.println("Parser : Erreur : Chaine non trouvee");
                            return;
                        }
                        else if(title_em == null) {
                            System.err.println("Parser : Erreur : L'emission ne possede pas de titre");
                            return;
                        }

                        // Parsing de la categorie
                        if(category_em != null)
                            category_em = (category_em.split(" ")[0]).toUpperCase();

                        // Creation des objets
                        // objet Emission
                        if(!episode) {
                            new Emission(title_em, sub_title_em, desc_em, Double.parseDouble(length_em), length_type_em,
                                    country_em, category_em, date_em, stereo_em, rating_em, rating_system_em, credits_em, tmp_ch, video_aspect,
                                    video_quality, video_colour, start_dif, stop_dif);
                        }
                        // objet Episode
                        else {
                            // Parsing du numero d'episode et de sa saison
                            parse_num_ep = ep_num_em.split("\\.");
                            if(parse_num_ep.length == 1) {
                                num_saison_ep = 1;
                                if(!parse_num_ep[0].equals(""))
                                    num_ep = Integer.parseInt(parse_num_ep[0]) + 1;
                                else
                                    num_ep = 0;
                            }
                            else {
                                if(!parse_num_ep[0].equals("") && !parse_num_ep[1].equals("")) {
                                    num_saison_ep = Integer.parseInt(parse_num_ep[0]) + 1;
                                    num_ep = Integer.parseInt(parse_num_ep[1]) + 1;
                                }
                                else {
                                    num_saison_ep = 1;
                                    num_ep = 1;
                                }
                            }
                            // Creation de l'episode
                            new Episode(title_em, sub_title_em, desc_em, Double.parseDouble(length_em), length_type_em,
                                    country_em, category_em, date_em, stereo_em, rating_em, rating_system_em, credits_em, tmp_ch, video_aspect,
                                    video_quality, video_colour, start_dif, stop_dif, num_saison_ep, num_ep);
                            episode = false;
                        }
                        // Reset variables
                        title_em = null;
                        sub_title_em = null;
                        desc_em = null;
                        length_em = null;
                        length_type_em = null;
                        country_em = null;
                        category_em = null;
                        date_em = null;
                        stereo_em = null;
                        rating_em = null;
                        credits_em = null;
                        tmp_ch = null;
                        video_aspect = null;
                        video_quality = null;
                        video_colour = false;
                        start_dif = null;
                        stop_dif = null;
                        break;
                }
            }
        }
    }

    // Fonctionnalités de l'application --------------------------------------

    /**
     * Affiche la liste des chaines tv dans la consoles
     */
    public void consulterListeChaines() {
        if(chaines.isEmpty()) {
            System.err.println("Erreur : Aucune chaine presente dans le programme");
            return;
        }
        for(Chaine c : chaines)
            System.out.println(c);
    }

    /**
     * Affiche les jours ayant une programmation
     */
    public void consulterJoursProgrammes() {
        ArrayList<LocalDate> res = new ArrayList<>();

        if(chaines.isEmpty()) {
            System.err.println("Erreur : Aucune chaine presente dans le programme");
            return;
        }

        // Recherche des jours dans la programmation de chaque chaines
        for(Chaine c : chaines) {
            for(LocalDate date : c.getProgrammes().keySet()) {
                if(!res.contains(date))
                    res.add(date);
            }
        }

        // Trier les dates
        Collections.sort(res);

        // Affichage du resultat
        for(LocalDate ld : res)
            System.out.println(ld + "\n");
    }

    /**
     * Affiche la programmation d'une journee pour une chaine
     * @param nom_chaine    Chaine dont l'on souhaite voir la programmation
     * @param jour          Jour dont l'on souhaite voir la programme
     */
    public void consulterProgJourChaine(String nom_chaine, LocalDate jour) {
        int i = 0;

        // Recherche de la chaine
        while(i < chaines.size() && !nom_chaine.toLowerCase().equals(chaines.get(i).getDisplayName().toLowerCase()))
            i++;

        if(i == chaines.size()) {
            System.err.println("Erreur : Chaine non trouvee");
            return;
        }

        // Affichage du resultat
        chaines.get(i).afficherProgJour(jour);
    }

    /**
     * Affiche la fiche d'une emission
     * @param nom_emission  Nom de l'emission
     */
    public void consulterFicheEmission(String nom_emission) {
        int i = 0;
        Emission e = null;

        // Recherche de l'emission
        while(i < chaines.size() && e == null)
            e = chaines.get(i++).getEmissionParNom(nom_emission);

        // Affichage du resultat
        if(e != null)
            System.out.println(e.affichage_long());
        else
            System.err.println("Emission non trouvee");
    }

    /**
     * Affiche les emissions diffusees a une date et heure precise
     * @param date  Moment dont l'on souhaite connaitre les diffusions
     */
    public void consulterProgDateHeurePrecise(LocalDateTime date) {
        Diffusion d;

        System.out.println("----------------------" + date.toString().replace('T', ' ') + "----------------------");

        // Recherche dans chaque emission de l'emission diffusee a la date demandee
        for(Chaine c : chaines) {
            System.out.println(c);
            d = c.getDiffusionParDateHeure(date);
            if(d == null)
                System.out.println("Aucune emission programmee pour cet horaire\n");
            else
                System.out.println(d);
        }
    }

    /**
     * Affiche la liste des films concernant un acteur ou un realisateur
     * @param categorie Categorie d'emission recherchee
     * @param nom       Nom de la personne recherchee
     * @param role      Role qu'elle occupe (acteur ou realisateur)
     */
    public void consulterEmissionDifParPersonne(String categorie, String nom, String role) {
        ArrayList<Emission> listeEm, res = new ArrayList<>();

        // Recherche des films diffusees de chaque chaine
        for(Chaine c: chaines) {
            listeEm = c.getEmissionsDifParCategorie(categorie.toLowerCase());
            if(listeEm != null) {
                // On ajoute les emissions ou la personne est presente dans ce role
                for(Emission em : listeEm) {
                    if(em.recherchePersonne(nom, role))
                        res.add(em);
                }
            }
        }

        // Affichage du resultat
        System.out.println("-------------------- " + nom + " : " + role + " --------------------");
        if(!res.isEmpty()) {
            for (Emission e : res) {
                System.out.println(e.affichage_court() + "\n");
            }
        }
        else
            System.out.println("Aucun " + categorie + " correspondant");
    }


    /**
     * Affiche la liste des acteurs par ordre decroissant de leurs appartitions dans les films diffuses
     */
    public void analyseActeursNbAppaEmDif(String categorie, String role) {
        ArrayList<Emission> listeEm;
        HashMap<String, Integer> res = new HashMap<>();
        HashMap<String, ArrayList<String>> credits;
        ArrayList<String> personnes;

        // Comptage des apparations par acteurs dans les films diffuses
        for(Chaine c : chaines) {
            // Recuperation des films diffuses
            listeEm = c.getEmissionsDifParCategorie(categorie.toLowerCase());
            // Recuperation des credits et ajout des acteurs au resultat pour chaque emission
            if(listeEm != null) {
                for (Emission em : listeEm) {
                    credits = em.getCredits();
                    // Ajout des acteurs au comptage
                    if (credits != null && credits.containsKey(role.toLowerCase())) {
                        personnes = credits.get(role.toLowerCase());
                        for (String pers : personnes) {
                            if (res.containsKey(pers.toLowerCase()))
                                res.put(pers.toLowerCase(), res.get(pers.toLowerCase()) + 1);
                            else
                                res.put(pers.toLowerCase(), 1);
                        }
                    }
                }
            }
        }

        if(!res.isEmpty()) {
            // On trie le resultat de maniere decroissante
            Object[] obj = res.entrySet().toArray();
            Arrays.sort(obj, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue().compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });

            // Affichage du resultat
            for (Object o : obj) {
                System.out.println(((Map.Entry<String, Integer>) o).getValue() + " ---- " + ((Map.Entry<String, Integer>) o).getKey());
            }
        }
    }

    /**
     * Affiche le nombre d'emission par categorie et par jour
     */
    public void analyseNbEmissionParCategParJour() {
        TreeMap<LocalDate, HashMap<String,Integer>> res = new TreeMap<>();
        TreeMap<LocalDate, ArrayList<Diffusion>> tmp;
        String categ_tmp;

        // Recherche dans les diffusions de chaque chaine des differentes categories pour ensuite compter les categories
        for(Chaine c : chaines) {
            tmp = c.getProgrammes();
            for(LocalDate date_tmp : tmp.keySet()) {
                for(Diffusion dif : tmp.get(date_tmp)) {
                    categ_tmp = dif.getEmission().getCategorie();
                    // Creation d'une entree pour cette date
                    if(!res.containsKey(date_tmp)) {
                        res.put(date_tmp, new HashMap<String,Integer>());
                        res.get(date_tmp).put(categ_tmp, 1);
                    }
                    // On a deja cette date dans le resultat
                    else {
                        // Creation de la nouvelle categorie
                        if(!res.get(date_tmp).containsKey(categ_tmp))
                            res.get(date_tmp).put(categ_tmp, 1);
                        // Ajout dans la categorie
                        else
                            res.get(date_tmp).put(categ_tmp, res.get(date_tmp).get(categ_tmp) + 1);
                    }
                }
            }
        }

        // Affichage resultat
        if(!res.isEmpty()) {
            // Affichage du resultat par date
            for(LocalDate date : res.keySet()) {
                // Affichage pour chaque jour
                System.out.println("---------- " + date + " ----------");
                for(String cat : res.get(date).keySet()) {
                    // Affichage du resultat pour chaque categorie
                    System.out.println(cat + " : " + res.get(date).get(cat));
                }
                System.out.print("\n");
            }
        }
    }

    /**
     * Affiche par chaine le nombre d'emission par avis du CSA
     */
    public void analyseNbEmissionParCSAParChaine() {
        HashMap<String, HashMap<String,Integer>> res = new HashMap<>();
        ArrayList<Emission> tmp;
        String name, rating;
        Iterator it_tmp, it_res;
        Map.Entry pair_tmp, pair_res;

        // Traitement pour chacune des chaines
        for(Chaine c: chaines) {
            // Recuperation des emissions avec un avis CSA pour cette chaine
            tmp = c.getEmissionAvecCSA();
            if(tmp != null) {
                name = c.getDisplayName();
                res.put(name, new HashMap<>());
                for(Emission em : tmp) {
                    rating = em.getRating().toLowerCase();
                    // Creation de l'avis car inexistant
                    if(!res.get(name).containsKey(rating))
                        res.get(name).put(rating,1);
                    // Ajout de l'avis au compteur
                    else
                        res.get(name).put(rating, res.get(name).get(rating) + 1);
                }
            }
        }

        // Affichage resultat
        if(!res.isEmpty()) {
            // Affichage du resultat par date
            for(String chaine : res.keySet()) {
                // Affichage pour chaque jour
                System.out.println("---------- " + chaine + " ----------");
                for(String cat : res.get(chaine).keySet()) {
                    // Affichage du resultat pour chaque categorie
                    System.out.println(cat + " : " + res.get(chaine).get(cat));
                }
                System.out.print("\n");
            }
        }
    }

    /**
     * Affiche les chaines par ordre decroissant de l'age moyen de ses films diffuses
     */
    public void analyseChaineAgeMoyenFilmDecr() {
        HashMap<String, Integer> res = new HashMap<>();
        ArrayList<Emission> tmp;
        String date;
        int ageSomme, nb_em;

        // Calcule pour chaque chaine de l'age moyen
        for(Chaine c : chaines) {
            tmp = c.getEmissionsDifParCategorie("film");
            if(tmp != null) {
                ageSomme = 0;
                nb_em = 0;
                for(Emission em : tmp) {
                    date = em.getDate_creation();
                    if(date != null && !date.equals("")) {
                        ageSomme += LocalDateTime.now().getYear() - Integer.parseInt(date);
                        nb_em++;
                    }
                }
                res.put(c.getDisplayName(), (ageSomme / nb_em));
            }
        }

        // Affichage du resultat
        if(!res.isEmpty()) {
            // On trie le resultat de maniere decroissante
            Object[] obj = res.entrySet().toArray();
            Arrays.sort(obj, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Map.Entry<String, Integer>) o2).getValue().compareTo(((Map.Entry<String, Integer>) o1).getValue());
                }
            });

            // Affichage du resultat
            for (Object o : obj) {
                System.out.println(((Map.Entry<String, Integer>) o).getValue() + " ---- " + ((Map.Entry<String, Integer>) o).getKey());
            }
            System.out.print("\n");
        }
    }

    /**
     * Recherche stricte dans la description de chacune des emissions, les mots cles
     * @param mots  Ensemble des mots a trouver dans une description
     */
    public void rechercheMotsCles(String...mots) {
        HashMap<String, ArrayList<Emission>> tmp;
        ArrayList<Emission> res = new ArrayList<Emission>();
        int i;

        // recherche dans les emissions de chaque chaine les mots cles
        for(Chaine c : chaines) {
            tmp = c.getEmissions();
            if(tmp != null) {
                // Parcours de la hashmap, de chaque categorie
                for(String categ : tmp.keySet()) {
                    // Parcours de toutes les emissions de cette categorie
                    for(Emission em : tmp.get(categ)) {
                        // On regarde si la descritpion contient tous les mots recherches
                        i = 0;
                        for(String str : mots) {
                            if(em.getDesc().toLowerCase().contains(str.toLowerCase()))
                                i++;
                        }
                        if(i == mots.length && !res.contains(em))
                            res.add(em);
                    }
                }
            }
        }

        // Affichage du resultat
        System.out.println("--------------------- Resultats de la recherche ---------------------");
        System.out.println("Nombre de resultats : " + res.size() + "\n");
        if(res.isEmpty())
            System.err.println("Aucune description ne comporte tous ces mots");
        else
            for(Emission em : res)
                System.out.println(em.affichage_court());
    }

    /* //////////////////////////////////////////////////////////////////////////////////////////////// */
    /* -------------------------------------------- MAIN ---------------------------------------------- */
    /* //////////////////////////////////////////////////////////////////////////////////////////////// */

    public static void main(String args[]) {
        TV tv;
        Scanner sc = new Scanner(System.in);
        String path_xml, nom_chaine, nom_em, date_in, nom_pers, role, mots, choix_str;
        String[] date, heure;
        File file;
        int choix_menu = -1;
        Pattern pat_choix = Pattern.compile("\\d+"),
                pat_date = Pattern.compile("\\d{4}-\\d{2}-\\d{2}"),
                pat_heure = Pattern.compile("\\d{4}-\\d{2}-\\d{2}/([0-9]|(0[0-9])|(1[0-9])|(2[0-3])):[0-5][0-9]"),
                pat_mots_cles = Pattern.compile("(\\w,)*\\w");
        Matcher m_choix, m_tmp;


        // Lancement du programme--------------------------------------------------------------
        // Recuperation du xml
        System.out.println("Veuillez fournir le chemin vers le .xml telecharge sur xmltv.fr (chemin absolu ou en partant de l'emplacement du .jar) : ");
        path_xml = sc.nextLine();

        // Test de l'existance du fichier
        file = new File(path_xml);
        if(!file.exists()) {
            System.err.println("Erreur : Fichier introuvable");
            return;
        }
        if(file.isDirectory()) {
            System.err.println("Erreur : Le fichier est un repertoire");
            return;
        }

        // Creation et chargement des donnees de l'application depuis le xml
        tv = new TV(path_xml);

        // Coeur de l'application ----------------------------------------------------------

        System.out.println("Bienvenue dans l'application de gestion du programme tv");
        while(true) {
            // Affichage du menu
            System.out.println("0 :\tQuitter l'application");
            System.out.println("1 :\tConsulter la liste des chaines");
            System.out.println("2 :\tConsulter la liste des jours disposant de programmes tv");
            System.out.println("3 :\tConsulter la programmation d'une chaine pour un jour donne");
            System.out.println("4 :\tConsulter la fiche d'une emission");
            System.out.println("5 :\tConsulter la liste des emissions qui seront en cours de diffusion a un moment donne");
            System.out.println("6 :\tConsulter la liste des films concernant un realisateur ou un acteur");
            System.out.println("7 :\tVoir la liste des acteurs ordonnee par leur nombre d'apparitions dans des films diffuses");
            System.out.println("8 :\tVoir le nombre d'emissions de chaque type par jour sur la periode");
            System.out.println("9 :\tLe nombre d'emissions par categorie CSA par chaine");
            System.out.println("10 :\tVoir la liste des chaines par anciennete moyenne decroissante des films diffuses");
            System.out.println("11 :\tEffectuer une recherche par mots cles stricte");
            System.out.println("Veuillez entrer une valeur :");

            // Choix de l'utilisateur
            choix_str = sc.nextLine();
            // Test pour savoir si c'est bien un nombre
            m_choix = pat_choix.matcher(choix_str);
            if(m_choix.find())
                choix_menu = Integer.parseInt(choix_str);
            else
                choix_menu = -1;

            // Execution de la fonctionnalite souhaitee, si chiffre correct
            switch(choix_menu) {
                case 0:
                    System.out.println("Fin de l'application");
                    return;
                case 1:
                    System.out.println("\n----------------------------- Chaines ---------------------------------\n");
                    tv.consulterListeChaines();
                    System.out.println("\n-----------------------------------------------------------------------\n");
                    break;
                case 2:
                    System.out.println("\n----------------------------- Jours Programmes ---------------------------------\n");
                    tv.consulterJoursProgrammes();
                    System.out.println("\n--------------------------------------------------------------------------------\n");
                    break;
                case 3:
                    // Recuperation de la chaine
                    System.out.println("Entrez le nom de la chaine : ");
                    nom_chaine = sc.nextLine();
                    if(nom_chaine.equals("")) {
                        System.err.println("Erreur : Rien n'a ete entre pour le nom de la chaine");
                        break;
                    }
                    // Recuperation de la date
                    System.out.println("Entrez la date souhaitee au format Annee-Mois-Jour chiffre (ex: 2018-05-30) : ");
                    date_in = sc.nextLine();
                    if(date_in.equals("")) {
                        System.err.println("Erreur : Rien n'a ete entre pour la date");
                        break;
                    }
                    // test pour savoir si la date est conforme
                    m_tmp = pat_date.matcher(date_in);
                    if(!m_tmp.find()) {
                        System.err.println("Erreur : Format de la date incorrect");
                        break;
                    }
                    // Execution de la fonctionnalite
                    date = date_in.split("-");
                    System.out.println("\n----------------------------- Programme  ---------------------------------\n");
                    tv.consulterProgJourChaine(nom_chaine, LocalDate.of(Integer.parseInt(date[0]),
                            Integer.parseInt(date[1]), Integer.parseInt(date[2])));
                    System.out.println("\n--------------------------------------------------------------------------\n");
                    break;
                case 4:
                    System.out.println("Entrez le nom de l'emission : ");
                    nom_em = sc.nextLine();
                    if(nom_em.equals("")) {
                        System.err.println("Erreur : Rien n'a ete entre pour le nom de l'emission");
                        break;
                    }
                    System.out.println("\n----------------------------- Emission ---------------------------------\n");
                    tv.consulterFicheEmission(nom_em);
                    System.out.println("\n------------------------------------------------------------------------\n");
                    break;
                case 5:
                    // Recuperation de la date
                    System.out.println("Entrez la date souhaitee au format Annee-Mois-Jour/Heure:Minutes chiffre (ex: 2018-05-30/20:30) : ");
                    date_in = sc.nextLine();
                    if(date_in.equals("")) {
                        System.err.println("Erreur : Rien n'a ete entre pour la date");
                        break;
                    }
                    // test pour savoir si la date est conforme
                    m_tmp = pat_heure.matcher(date_in);
                    if(!m_tmp.find()) {
                        System.err.println("Erreur : Format de la date incorrect");
                        break;
                    }
                    // Execution de la fonctionnalite
                    date = date_in.split("/")[0].split("-");
                    heure = date_in.split("/")[1].split(":");
                    System.out.println("\n----------------------------- Programme Heure Precise ---------------------------------\n");
                    tv.consulterProgDateHeurePrecise(LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                            Integer.parseInt(date[2]), Integer.parseInt(heure[0]), Integer.parseInt(heure[1])));
                    System.out.println("\n---------------------------------------------------------------------------------------\n");
                    break;
                case 6:
                    // Recuperation du nom et du role
                    System.out.println("Entrez le nom de la personne recherchee : ");
                    nom_pers = sc.nextLine();
                    if(nom_pers.equals("")) {
                        System.err.println("Erreur : Rien n'a ete entre pour le nom de la personne");
                        break;
                    }
                    System.out.println("Entrez le role de cette personne (guest ou director EXCLUSIVEMENT) : ");
                    role = sc.nextLine();
                    if(role.equals("")) {
                        System.err.println("Erreur : Rien n'a ete entre pour le role");
                        break;
                    }
                    // Test sur le role en lui-meme si correspondant
                    role = role.toLowerCase();
                    if(!role.equals("guest") && !role.equals("director")) {
                        System.err.println("Erreur : Role incorrect, ne peut etre que guest ou director");
                        break;
                    }
                    // Execution de la fonctionnalite
                    System.out.println("\n----------------------------- Emissions Personne precise ---------------------------------\n");
                    tv.consulterEmissionDifParPersonne("film",nom_pers,role);
                    System.out.println("\n------------------------------------------------------------------------------------------\n");
                    break;
                case 7:
                    System.out.println("\n----------------------------- Nombre Apparitions ---------------------------------\n");
                    tv.analyseActeursNbAppaEmDif("film","guest");
                    System.out.println("\n----------------------------------------------------------------------------------\n");
                    break;
                case 8:
                    System.out.println("\n----------------------------- Nombre Emissions par Categ et Jour ---------------------------------\n");
                    tv.analyseNbEmissionParCategParJour();
                    System.out.println("\n--------------------------------------------------------------------------------------------------\n");
                    break;
                case 9:
                    System.out.println("\n----------------------------- Nombre Emissions par CSA et Chaine ---------------------------------\n");
                    tv.analyseNbEmissionParCSAParChaine();
                    System.out.println("\n--------------------------------------------------------------------------------------------------\n");
                    break;
                case 10:
                    System.out.println("\n------------- Liste des chaines par age moyen decroissant des films -------------\n");
                    tv.analyseChaineAgeMoyenFilmDecr();
                    System.out.println("\n-----------------------------------------------------------------------------------\n");
                    break;
                case 11:
                    // Recuperation des mots cles
                    System.out.println("Entrez les mots cles a rechercher, separes par des virgules (ex : nature,oiseau) ");
                    mots = sc.nextLine();
                    if(mots.equals("")) {
                        System.err.println("Erreur : Aucun mots cles saisis");
                        break;
                    }
                    // Test du format de la saisie
                    m_tmp = pat_mots_cles.matcher(mots);
                    if(!m_tmp.find()) {
                        System.err.println("Erreur : Format des mots cles entres incorrect");
                        break;
                    }
                    // Execution de la fonctionnalite
                    System.out.println("\n----------------------------- Recherche Mots Cles ---------------------------------\n");
                    tv.rechercheMotsCles(mots.split(","));
                    System.out.println("\n-----------------------------------------------------------------------------------\n");
                    break;
            }
        }
    }
}