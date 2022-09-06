package victor.training.ddd.agile.dto

class BacklogItemDto(val productId: Long, var title: String, var description: String) {
    var id: Long? = null
    var version: Long? = null

}