#!/bin/bash -
#===============================================================================
#       ARCHIVO:  panel.sh
#           USO:  ./panel.sh
#
#   DESCRIPCION:  esconde/oculta/muestra el último panel Gnome
#
#      OPCIONES:  ---
#    REQUISITOS:  ---
#         NOTAS:  ---
#         AUTOR: Nebur (Más Vale Manya ...), masvalemanya@gmail.com
#                Santiago Cabrera M.
#           WEB: http://masvale-manya.blogspot.com
#                aisla1000@hotmail.com
#        CREADO: 19/11/10 19:50:56 CET
#    MODIFICADO: 10/11/11 14:30:58 GTM
#===============================================================================

set -o nounset                              # Treat unset variables as an error

titulo="Modificando Gnome Panel"
texto="Que opción prefieres escoger?"
opc1="Ocultar"
opc2="Restaurar"
opc3="Salir"
menu=0
until [ "$(echo "$menu" | grep -w "$opc3")" != "" ]; do
    menu=$(zenity --title "$titulo" --text "$texto" --height 195 --width 263 --list --radiolist  --column "" --column "" TRUE "$opc1" FALSE "$opc2" FALSE "$opc3")
    if [ "$(echo "$menu" | grep -w "$opc1")" != "" ]; then
        gconftool-2 --type bool --set /apps/panel/toplevels/top_panel/auto_hide true
        gconftool-2 --type int --set /apps/panel/toplevels/top_panel/unhide_delay 100000
        gconftool-2 --type bool --set /apps/panel/toplevels/bottom_panel/auto_hide true
        gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/unhide_delay 100000
        gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/x 100000
        gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/y 100000
        gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/auto_hide_size 0
        gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/size 0
        gconftool-2 --type bool --set /apps/nautilus/desktop/computer_icon_visible false
        gconftool-2 --type bool --set /apps/nautilus/desktop/home_icon_visible false
        gconftool-2 --type bool --set /apps/nautilus/desktop/trash_icon_visible false
        gconftool-2 --type bool --set /apps/nautilus/desktop/volumenes_visible false
    fi
    if [ "$(echo "$menu" | grep -w "$opc2")" != "" ]; then
	gconftool-2 --recursive-unset /apps/panel
	gconftool-2 --recursive-unset /apps/nautilus
        #gconftool-2 --type bool --set /apps/panel/toplevels/top_panel/auto_hide true
        #gconftool-2 --type int --set /apps/panel/toplevels/top_panel/unhide_delay 0
        #gconftool-2 --type bool --set /apps/panel/toplevels/bottom_panel/auto_hide true
        #gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/unhide_delay 0
        #gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/x 700
        #gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/y 700
        #gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/auto_hide_size 50
        #gconftool-2 --type int --set /apps/panel/toplevels/bottom_panel/size 25
        #gconftool-2 --type bool --set /apps/nautilus/desktop/computer_icon_visible true
        #gconftool-2 --type bool --set /apps/nautilus/desktop/home_icon_visible true
        #gconftool-2 --type bool --set /apps/nautilus/desktop/trash_icon_visible true
        #gconftool-2 --type bool --set /apps/nautilus/desktop/volumenes_visible true	
    fi
done
