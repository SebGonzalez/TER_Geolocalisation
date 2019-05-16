# TER_Geolocalisation
Projet réalisé dans le cadre du TER de M1 Informatique consistant à proposer une interface graphique qui place les éléments d’une base de données de personnes et lieux sur une carte géographique


## Fichiers de cartes

https://drive.google.com/drive/folders/1db0J0XCMSX9zGkZyX3dkkKjwLpnyQHc0?usp=sharing

- Mettre le bon chemin dans MainWindow -> "private String graphHopperPath = {chemin};"

## Créer un nouveau pack de cartes:

- Télécharger les cartes sur https://download.geofabrik.de/
- Merge les cartes avec Osmium si besoin
  osmium merge fichier1.osm.pbf fichier2.osm.pbf [...] -o fichierRésultat.osm.pbf
- java ImportOSMFile [Chemin du fichier osm.pbf/osm.bz2/osm] [chemin du repertoire GraphHopper] [nom du sous repertoire du pack de maps créé]
