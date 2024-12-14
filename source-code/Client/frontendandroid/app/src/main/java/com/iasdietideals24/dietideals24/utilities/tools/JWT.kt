package com.iasdietideals24.dietideals24.utilities.tools

import java.util.Base64

class JWT {
    companion object {
        private fun decodePayload(jwt: String): String {
            val tempList = jwt.split(".")

            return if (tempList.size > 1)
                String(tempList[1].let {
                    Base64.getDecoder().decode(it)
                })
            else ""
        }

        fun getUserEmail(jwt: String): String {
            val jsonData = decodePayload(jwt)
            val emailRegex = Regex("\"email\":\"(?<email>.*?)\"")
            val matchResult = emailRegex.find(jsonData)
            return matchResult?.groups?.get("email")?.value ?: ""
        }

        fun getExpirationDate(jwt: String): Long {
            val jsonData = decodePayload(jwt)
            val expirationDateRegex = Regex("\"exp\":(?<exp>\\d+)")
            val matchResult = expirationDateRegex.find(jsonData)
            return matchResult?.groups?.get("exp")?.value?.toLong() ?: 0
        }
    }
}