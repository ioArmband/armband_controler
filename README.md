armband_controler
=================

Ce projet représente la partie "serveur" de l'ioArmband 
Il dépend du projet ioCommunication_library

Voir le wiki pour plus d'informations

## Installation


### Clonage

Clonez le repository dans le dossier de votre choix

`git clone https://github.com/ioArmband/armband_controler.git`

Clonez aussi le répertoire de librairie de communication

`git clone https://github.com/ioArmband/ioCommunication_library.git`

### Instalation sous Eclipse
#### Importation du projet

`File > Import > Existing Maven Project`

Choisissez le dossier du projet `armband_controler` et appuyez sur `Finish`

Répétez la même opération: importez le projet `ioCommunication_library` dans le même workspace.

#### Configuration du lancement de l'applicaction

Accedez au menu des configurations de lancement.

`Run > Run configurations...`

Créez une nouvelle configuration 

`Java Application > New`

Definissez le nom de la configuration:

`armband_controler`

Choisissez le projet: 

`Project > Browse... > armband_controler`

Définissez la classe principale:

`Main class > Search... > org.ioarmband.controler.Main`

Dans l'onglet `(x)= Arguments`

Saisissez dans la case `VM arguments` : 

`-Djava.library.path="${workspace_loc}\armband_controler\lib;${env_var:PATH}"`

Enregistrez la configuration `Apply`
