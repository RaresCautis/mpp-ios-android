package com.jetbrains.handson.mpp.mobile

class SearchBarHelper {

    companion object {
        fun filterData(initialData: List<StationDetails>, searchText: String) = initialData.filter {
            checkStringContains(
                it.crs!!,
                searchText
            ) || checkStringContains(it.name, searchText)
        }

        private fun checkStringContains(input: String, substring: String) =
            input.contains(substring, ignoreCase = true)

    }

}