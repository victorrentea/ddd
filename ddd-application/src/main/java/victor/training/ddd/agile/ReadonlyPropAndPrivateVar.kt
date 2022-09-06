package victor.training.ddd.agile.entity

import victor.training.ddd.agile.entity.Sprint.SprintStatus

class ReadonlyPropAndPrivateVar(val someBLO:SomeBLO) {
//    val couponId: CouponId = 2L
    val couponId: CouponId = CouponId(1L)

    var status: SprintStatus = SprintStatus.CREATED
         private set

    fun start() {
        someBLO.f()
        status = SprintStatus.STARTED
    }

    public class SomeBLO {
        var status: SprintStatus = SprintStatus.CREATED
            private set
        internal fun f() {
            status = SprintStatus.STARTED
        }
    }
}

//typealias CouponId = Long
@JvmInline
value class CouponId(val id:Long)


fun main() {
    val r = ReadonlyPropAndPrivateVar(someBLO = ReadonlyPropAndPrivateVar.SomeBLO())

//    println(r.status)
//    r.status = Status.FINISHED
//    r.start()
}