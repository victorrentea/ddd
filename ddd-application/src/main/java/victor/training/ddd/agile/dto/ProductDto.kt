package victor.training.ddd.agile.dto

class ProductDto {
    var id: Long? = null
    var code: String
    var name: String
    var mailingList: String
    var poEmail: String? = null
    var poName: String? = null
    var poPhone: String? = null

    constructor(id: Long?, code: String, name: String, mailingList: String) {
        this.id = id
        this.code = code
        this.name = name
        this.mailingList = mailingList
    }


    constructor(code: String, name: String, mailingList: String) {
        this.code = code
        this.name = name
        this.mailingList = mailingList
    }
}