package com.example.course2.ui.composables.search

import com.example.course2.ui.composables.filter.SearchOptionParameter

sealed class SearchParameters(val name: String)

object SortParameter : SearchOptionParameter<SearchParameters> {

    data object Asc : SearchParameters("По возрастанию")
    data object Desc : SearchParameters("По убыванию")

    override val parameterName: String
        get() = "Сортировать по"
    override val options: List<SearchParameters>
        get() = listOf(Asc, Desc)
    override val defaultValue: SearchParameters
        get() = Asc

    override fun getDisplayText(option: SearchParameters?): String =
        option?.name ?: option.toString()
}

object FilterParameter : SearchOptionParameter<SearchParameters> {


    data object WithComment : SearchParameters("С комментарием")
    data object WithoutComment : SearchParameters("Без комментариев")
    data object NoFilter : SearchParameters("Без фильтра")

    override val parameterName: String
        get() = "Сортировать по"
    override val options: List<SearchParameters>
        get() = listOf(WithComment, WithoutComment, NoFilter)
    override val defaultValue: SearchParameters
        get() = NoFilter

    override fun getDisplayText(option: SearchParameters?): String =
        option?.name ?: option.toString()
}

data class SearchState(
    val query: String = "",
    val parameters: List<SearchParameters?> = listOf()

)