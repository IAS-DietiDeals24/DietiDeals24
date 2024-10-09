package com.iasdietideals24.dietideals24.utilities.tools

data class Page<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val last: Boolean,
    val first: Boolean,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val empty: Boolean
)