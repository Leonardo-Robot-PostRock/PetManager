package ar.com.petmanager.gui.constants;

import java.awt.*;

/**
 * Constantes de diseño UI para PetManager.
 * Centraliza colores, dimensiones, fuentes y rutas de recursos
 * para mantener consistencia visual en todas las vistas.
 */
public final class UIConstants {

    private UIConstants() {
        throw new UnsupportedOperationException("Clase de constantes, no instanciable");
    }

    // -- Colores de la paleta principal --
    public static final Color COLOR_PRIMARY = new Color(230, 140, 150);
    public static final Color COLOR_PRIMARY_DARK = new Color(210, 110, 120);
    public static final Color COLOR_SECONDARY = new Color(180, 120, 200);
    public static final Color COLOR_ACCENT = new Color(100, 180, 220);
    public static final Color COLOR_BACKGROUND = new Color(255, 250, 250);
    public static final Color COLOR_TEXT_PRIMARY = new Color(60, 60, 60);
    public static final Color COLOR_TEXT_SECONDARY = new Color(140, 140, 140);
    public static final Color COLOR_BORDER = new Color(220, 220, 220);
    public static final Color COLOR_SUCCESS = new Color(34, 139, 34);   // verde oscuro
    public static final Color COLOR_WARNING = new Color(240, 200, 80);
    public static final Color COLOR_ERROR = new Color(220, 100, 100);
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);

    // -- Colores de las cards del dashboard --
    public static final Color COLOR_CARD_OWNER = new Color(160, 120, 220);
    public static final Color COLOR_CARD_PETS = new Color(100, 180, 220);
    public static final Color COLOR_CARD_VETS = new Color(220, 160, 120);
    public static final Color COLOR_CARD_DONORS = new Color(80, 170, 130);

    // -- Dimensiones de la ventana principal --
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int SIDEBAR_WIDTH = 220;
    public static final int HEADER_HEIGHT = 60;

    // -- Dimensiones de botones y cards --
    public static final Dimension BUTTON_SIZE_SMALL = new Dimension(120, 35);
    public static final Dimension BUTTON_SIZE_MEDIUM = new Dimension(160, 40);
    public static final Dimension BUTTON_SIZE_LARGE = new Dimension(200, 50);
    public static final Dimension CARD_SIZE = new Dimension(200, 120);
    public static final Dimension STAT_CARD_SIZE = new Dimension(180, 100);

    // -- Padding y márgenes --
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;
    public static final int PADDING_XLARGE = 30;
    public static final int MARGIN_SECTION = 25;

    // -- Bordes redondeados --
    public static final int BORDER_RADIUS_SMALL = 6;
    public static final int BORDER_RADIUS_MEDIUM = 10;
    public static final int BORDER_RADIUS_LARGE = 15;

    // -- Fuentes --
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 32);

    // -- Rutas de recursos --
    public static final String PATH_BACKGROUND = "/ar/com/petmanager/assets/images/petBackground.jpg";
    public static final String PATH_ICON_PET = "/ar/com/petmanager/assets/images/petFoot.svg";
    public static final String PATH_ICONS_DIR = "/ar/com/petmanager/assets/images/";

    // -- Labels del header --
    public static final String APP_TITLE = "Pet Manager";
    public static final String HEADER_DUEÑOS = "Dueños";
    public static final String HEADER_MASCOTAS = "Mascotas";
    public static final String HEADER_VETERINARIAS = "Veterinarias";
    public static final String HEADER_DONANTES = "Contacto Donantes";
    public static final String HEADER_ADOPCIONES = "Adopciones";

    // -- Labels de botones --
    public static final String BTN_ADD = "Agregar";
    public static final String BTN_EDIT = "Editar";
    public static final String BTN_DELETE = "Eliminar";
    public static final String BTN_SAVE = "Guardar";
    public static final String BTN_CANCEL = "Cancelar";
    public static final String BTN_BACK = "Volver";
    public static final String BTN_SEARCH = "Buscar";
    public static final String BTN_ADOPT = "Dar en Adopción";
    public static final String BTN_VIEW_DETAIL = "Ver Detalle";

    // -- Labels de formularios --
    public static final String LBL_NAME = "Nombre:";
    public static final String LBL_SURNAME = "Apellido:";
    public static final String LBL_DNI = "DNI:";
    public static final String LBL_PHONE = "Teléfono:";
    public static final String LBL_STREET = "Calle:";
    public static final String LBL_CITY = "Ciudad:";
    public static final String LBL_AGE = "Edad:";
    public static final String LBL_WEIGHT = "Peso:";
    public static final String LBL_RACE = "Raza:";
    public static final String LBL_TYPE = "Tipo:";
    public static final String LBL_DESCRIPTION = "Descripción:";
    public static final String LBL_PREFERRED_VET = "Veterinaria preferida:";
    public static final String LBL_IS_SICK = "¿Está enfermo?";
    public static final String LBL_FILTER = "Filtrar:";
    public static final String LBL_PHOTO = "Foto:";

    // -- Mensajes --
    public static final String MSG_CONFIRM_DELETE = "¿Estás seguro de que querés eliminar este registro?";
    public static final String MSG_SAVE_SUCCESS = "Guardado exitosamente.";
    public static final String MSG_DELETE_SUCCESS = "Eliminado exitosamente.";
    public static final String MSG_DNI_EXISTS = "El DNI ya está registrado.";
    public static final String MSG_FIELD_REQUIRED = "Este campo es obligatorio.";
    public static final String MSG_NO_SELECTION = "Seleccioná un registro primero.";
}