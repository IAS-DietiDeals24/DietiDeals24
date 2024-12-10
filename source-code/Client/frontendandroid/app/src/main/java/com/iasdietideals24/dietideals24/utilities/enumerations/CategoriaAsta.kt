package com.iasdietideals24.dietideals24.utilities.enumerations

import android.content.res.Resources
import com.iasdietideals24.dietideals24.R
import org.koin.java.KoinJavaComponent.inject

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
        private val resources: Resources by inject(Resources::class.java)

        fun fromStringToEnum(stringa: String): CategoriaAsta {
            when (stringa) {
                resources.getString(R.string.category_books) -> return BOOKS
                resources.getString(R.string.category_comics_and_mangas) -> return COMICS_AND_MANGAS
                resources.getString(R.string.category_music) -> return MUSIC
                resources.getString(R.string.category_movies_and_tv_shows) -> return MOVIES_AND_TV_SHOWS
                resources.getString(R.string.category_videogames_and_consoles) -> return VIDEOGAMES_AND_CONSOLES
                resources.getString(R.string.category_electronics) -> return ELECTRONICS
                resources.getString(R.string.category_foods_and_drinks) -> return FOODS_AND_DRINKS
                resources.getString(R.string.category_pets_supplies) -> return PETS_SUPPLIES
                resources.getString(R.string.category_bodycare_and_beauty) -> return BODYCARE_AND_BEAUTY
                resources.getString(R.string.category_sports_and_hobbies) -> return SPORTS_AND_HOBBIES
                resources.getString(R.string.category_clothings_and_wearables) -> return CLOTHINGS_AND_WEARABLES
                resources.getString(R.string.category_home_and_furnitures) -> return HOME_AND_FURNITURES
                resources.getString(R.string.category_vehicles) -> return VEHICLES
                resources.getString(R.string.category_other) -> return OTHER
                else -> return ND
            }
        }

        fun fromEnumToString(enum: CategoriaAsta): String {
            return when (enum) {
                BOOKS -> resources.getString(R.string.category_books)
                COMICS_AND_MANGAS -> resources.getString(R.string.category_comics_and_mangas)
                MUSIC -> resources.getString(R.string.category_music)
                MOVIES_AND_TV_SHOWS -> resources.getString(R.string.category_movies_and_tv_shows)
                VIDEOGAMES_AND_CONSOLES -> resources.getString(R.string.category_videogames_and_consoles)
                ELECTRONICS -> resources.getString(R.string.category_electronics)
                FOODS_AND_DRINKS -> resources.getString(R.string.category_foods_and_drinks)
                PETS_SUPPLIES -> resources.getString(R.string.category_pets_supplies)
                BODYCARE_AND_BEAUTY -> resources.getString(R.string.category_bodycare_and_beauty)
                SPORTS_AND_HOBBIES -> resources.getString(R.string.category_sports_and_hobbies)
                CLOTHINGS_AND_WEARABLES -> resources.getString(R.string.category_clothings_and_wearables)
                HOME_AND_FURNITURES -> resources.getString(R.string.category_home_and_furnitures)
                VEHICLES -> resources.getString(R.string.category_vehicles)
                OTHER -> resources.getString(R.string.category_other)
                else -> ""
            }
        }
    }
}