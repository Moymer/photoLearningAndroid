package moymer.com.db

data class Category (
        var categoryId: String,
        var title: MutableMap<String, String>,
        var description:  MutableMap<String, String>,
        var url: String,
        var labels: List<String>,
        var updateDate: Long,
        var opened: Boolean,
        var language: String
    ) {

//    constructor(categoryId: String,
//                title: MutableMap<String, String>,
//                description:  MutableMap<String, String>,
//                url: String,
//                labels: List<String>,
//                updateDate: Long,
//                opened: Boolean,
//                language: String):
//            this(categoryId, title, description, url, labels, updateDate, opened, language)



}