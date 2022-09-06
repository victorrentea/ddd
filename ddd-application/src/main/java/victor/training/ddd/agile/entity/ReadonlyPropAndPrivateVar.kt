package victor.training.ddd.agile.entity

import victor.training.ddd.agile.entity.Sprint.Status

class ReadonlyPropAndPrivateVar(var some:Some) {
    var status: Status = Status.CREATED
         private set

    fun start() {
        some.f()
        status = Status.STARTED
    }

    class Some {
        var status: Status = Status.CREATED
            private set
        internal fun f() {
            status = Status.STARTED
        }
    }
}



fun main() {
//    val r = ReadonlyPropAndPrivateVar(Some());

//    println(r.status)
//    r.status = Status.FINISHED
//    r.start()
}