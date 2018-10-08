package moymer.com.db

import com.google.gson.annotations.Expose

data class Category (
        @Expose var categoryId: String,
        @Expose var title: MutableMap<String, String>,
        @Expose var description:  MutableMap<String, String>,
        @Expose var url: String,
        @Expose var labels: List<String>
    )