# SpaceTreason
est un petit jeu en 2 dimensions afin de se familiariser avec la librairie LibGDX.
On incarne un vaisseau spatial dans un univers qui tourne en boucle et qui doit accomplir le plus haut score possible,
c'est un shooter (jeu de tir) en 2 dimensions qui se compose d'un niveau de difficulté progressif.

Lorsque le vaisseau est attaqué il perd un demi point de vie dans la barre de vie en bas à gauche et devient intouchable pendant un certain temps
de quelques secondes afin de laisser du répit au joueur.

En fonction du score qui augmente automatiquement en fonction du temps le niveau de difficulté change (nombre de vaisseaux ennemis pour le premier niveau de difficulté, vaisseaux ennemis dans le sens horizontal pour le second niveau de difficulté, comètes pour le troisième niveau de difficulté...).
La difficulté croissante donne également au joueur un tir plus adapté avec les ennemis multidirectionnel et à plus grande zone de touche.
En plus de cela le joueur augmente son score en détruisant des vaisseaux ennemis et comètes.

Le jeu a été réalisé avec la librairie LibGDX en Java basé sur une partie du framework OpenGL développé en C++.

Démonstration du jeu :
(cliquez sur le gif pour revoir au début)

![alt text](https://github.com/zentsugo/SpaceTreason/blob/main/spacetreason_game.gif)


Fenêtre d'introduction au jeu :

![alt text](https://github.com/zentsugo/SpaceTreason/blob/main/spacetreason_play.PNG)

Niveaux de difficultés divers :

![alt text](https://github.com/zentsugo/SpaceTreason/blob/main/spacetreason_beginninglevel.png)

![alt text](https://github.com/zentsugo/SpaceTreason/blob/main/spacetreason_endlevel.png)
