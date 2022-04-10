# Report 3
Meno: Patrik Grman

Projekt: Unified Webcomic Subscriber

Repo: https://github.com/MrRedstoner/UnifiedWebcomicSub

Verejna url: este nie

Cas: stvrty tyzden prace

Bolo planovane:
 - source atributy
 - polling
 - posielanie emailov
 - ignore source
 - posty od moderatorov

Bolo spravene:
 - source atributy pridavanie interne [7f32cf](7f32cf1f279a42977fab50e428cdff629c4de5b6)
 - polling [7f32cf](7f32cf1f279a42977fab50e428cdff629c4de5b6)
 - posielanie emailov service [050eb1](050eb126581570fc97785fc759c5948593554f9c)
 - posielanie emailov daily resolve&format [f633f8](f633f8f69163e4ac70b25e30bf55dbc8e5c869b8)
 - ignore source front&back [8a97d0](8a97d0cbaafd3ffa7d934912d5e25d5f7aa7b569)

Rozdiely:
 - nie je implementovane ignorovanie pri resolvinu - malo by byt rychle dorobitelne
 - source atributy nemaju rest controller ani frontend
 - modposty neboli spravene

Cisty cas: asi 30 hodin (nemeral som presne)

Plan na dalsi tyzden:
 - ignore source v resolvovani
 - tyzdenne maily
 - modposty
 - upratovanie, prerobenie frontend

Problemy:
 - prokrastinacia
 - neposluchajuci test pri pokuse o setup

Celkovo implementovane:
 - Napojenie na DB
 - Register, login, logout
 - User settings
 - Sourcy okrem atributov
 - Grupy
 - Jednoduchy polling
 - Denne maily
 - Frontend pre to, co je v backende

Chyba implementacia
 - Source atributy
 - Tyzdenne maily
 - Mod post a poll
 - Subscribe na moderatorov
 - Udelovanie mod, admin
