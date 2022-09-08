package victor.training.ddd.agile.entity

import javax.persistence.Embeddable

@Embeddable
data class ProductOwner(
    val poname:String,
    val pophone:String,
    val poemail:String,
) {

}