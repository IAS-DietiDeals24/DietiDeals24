package com.iasdietideals24.dietideals24.utilities.enumerations

enum class CategoriaAsta {
    BOOKS,
    COMICS_AND_MANGAS,
    MUSIC,
    MOVIES_AND_TV_SHOWS,
    VIDEOGAMES_AND_CONSOLES,
    ELECTRONICS,
    FOODS_AND_DRINKS,
    PETS_SUPPLIES,
    BODYCARE_AND_BEAUTY,
    SPORTS_AND_HOBBIES,
    CLOTHINGS_AND_WEARABLES,
    HOME_AND_FURNITURES,
    VEHICLES,
    OTHER,
    ND;

    companion object {
        fun fromStringToEnum(stringa: String): CategoriaAsta {
            when (stringa) {
                "Libri" -> return BOOKS
                "Fumetti e manga" -> return COMICS_AND_MANGAS
                "Musica" -> return MUSIC
                "Film e serie TV" -> return MOVIES_AND_TV_SHOWS
                "Videogiochi e console" -> return VIDEOGAMES_AND_CONSOLES
                "Elettronica" -> return ELECTRONICS
                "Cibi e bevande" -> return FOODS_AND_DRINKS
                "Scorte animali domestici" -> return PETS_SUPPLIES
                "Cura del corpo e bellezza" -> return BODYCARE_AND_BEAUTY
                "Sport e tempo libero" -> return SPORTS_AND_HOBBIES
                "Abiti e indossabili" -> return CLOTHINGS_AND_WEARABLES
                "Casa e arredamenti" -> return HOME_AND_FURNITURES
                "Veicoli" -> return VEHICLES
                "Altro" -> return OTHER
                else -> return ND
            }
        }

        fun fromEnumToString(enum: CategoriaAsta): String {
            when (enum) {
                BOOKS -> return "Libri"
                COMICS_AND_MANGAS -> return "Fumetti e manga"
                MUSIC -> return "Musica"
                MOVIES_AND_TV_SHOWS -> return "Film e serie TV"
                VIDEOGAMES_AND_CONSOLES -> return "Videogiochi e console"
                ELECTRONICS -> return "Elettronica"
                FOODS_AND_DRINKS -> return "Cibi e bevande"
                PETS_SUPPLIES -> return "Scorte animali domestici"
                BODYCARE_AND_BEAUTY -> return "Cura del corpo e bellezza"
                SPORTS_AND_HOBBIES -> return "Sport e tempo libero"
                CLOTHINGS_AND_WEARABLES -> return "Abiti e propose"
                HOME_AND_FURNITURES -> return "Casa e arredamenti"
                VEHICLES -> return "Veicoli"
                OTHER -> return "Altro"
                ND -> return "Non specificato"
            }
        }
    }
}