//import java.util.*
//
//data class Product(val name: String, val width: Int, val length: Int, val height: Int, var quantity: Double)
//data class Box(
//    val name: String,
//    val width: Int,
//    val length: Int,
//    val height: Int,
//    val maxWeight: Int,
//    val id: String = UUID.randomUUID().toString(),
//    var availableQTY: Int?
//)
//data class PackingScheme( val qty: Double, val box: Box )
//data class ShipmentBatch(
//    val product: Product,
//    val packingSchemes: List<PackingScheme>
//)
//data class Pack(
//    val box: Box,
//    val productsAndQty: MutableMap<Product, Int>,
//)
//
//fun main() {
//    val shipments = listOf(
////        ShipmentBatch(
////            Product("Product 1", 50, 50, 50, 10.00),
////            listOf(
////                PackingScheme(3.00, Box("Small Box-1", 100, 100, 100, 1000, availableQTY = 3)),
////                PackingScheme(5.00, Box("Medium Box-1", 200, 200, 200, 2000, availableQTY = 5)),
////                PackingScheme(10.00, Box("Large Box-1", 500, 500, 500, 3000, availableQTY = 10)),
////            )
////        ),
////        ShipmentBatch(
////            Product("Product 2", 70, 300, 70, 8.00),
////            listOf(
////                PackingScheme(3.00, Box("Small Box-2", 100, 100, 100, 1000, availableQTY = 3)),
////                PackingScheme(5.00, Box("Medium Box-2", 200, 200, 200, 2000, availableQTY = 5)),
////                PackingScheme(10.00, Box("Large Box-2", 500, 500, 500, 3000, availableQTY = 10)),
////            )
////        ),
//        ShipmentBatch(
//            Product("Product 3", 70, 300, 70, 12.00),
//            listOf(
//                PackingScheme(3.00, Box("Small Box-2", 100, 100, 100, 1000, availableQTY = 3)),
//                PackingScheme(5.00, Box("Medium Box-1", 200, 200, 200, 2000, availableQTY = 5)),
//                PackingScheme(10.00, Box("Large Box-1", 500, 500, 500, 3000, availableQTY = 10)),
//                PackingScheme(15.00, Box("X-Large Box-3", 500, 500, 500, 3000, availableQTY = 15)),
//            )
//        )
//    )
//
//
//    // Finding the best packing method
//    val bestPacking = generateOptions(shipments)
//
//}
//
//fun generateOptions(shipments: List<ShipmentBatch>) {
//
//    val packs: MutableList<Pack> = mutableListOf()
//
//    shipments.forEach { batch ->
//        val product = batch.product
//        val packingSchemes = batch.packingSchemes
//        val prodQty = product.quantity
//
//        do {
//            findMatchInSchemes(packingSchemes, product, packs)
//        } while (product.quantity != 0.00)
//
////       "stop".prettyPrint()
//
//        // AFL 6/6/24 TODO : consider reiterating on final version
//    }
//    packs.forEach { it.prettyPrint() }
//
//    println("stop")
//}
//
//private fun findMatchInSchemes(
//    packingSchemes: List<PackingScheme>,
//    product: Product,
//    packs: MutableList<Pack>
//) {
//    var restByDiv = mutableMapOf<Double, Int>()
//    packingSchemes.forEach {
//        restByDiv.set((product.quantity / it.qty), (product.quantity % it.qty).toInt())
//    }
//    restByDiv = restByDiv.toSortedMap()
//
////    val maxQtyFillable = packingSchemes.filter { it.qty < product.quantity }.maxByOrNull { it.qty }?.deepCopy()
////
////    if (maxQtyFillable != null) {
////        maxQtyFillable.box.availableQTY = 0
////        product.quantity -= maxQtyFillable.qty
////        packs.add(Pack(maxQtyFillable.box, mutableMapOf(product to maxQtyFillable.qty.toInt())))
////        return
////    }
//
//    // rest 0 and min nr. of boxes
//    val bestOption = restByDiv.entries.filter { it.value == 0 && it.key != 0.00 }
//
//    if (bestOption.isNotEmpty()) {
//        //qty of products fitting perfectly in boxes
//        val bestScheme =
//            packingSchemes.filter { (product.quantity / it.qty) == bestOption.first().key }
//                          .first()
//                          .deepCopy()
//        val bestPacking = bestOption.first()
//        for (i in 0..bestPacking.key.toInt() - 1) {
//            bestScheme.box.availableQTY = 0
//            product.quantity -= bestScheme.qty
//            packs.add(Pack(bestScheme.box, mutableMapOf(product to bestScheme.qty.toInt())))
//        }
//    } else if (restByDiv.containsFillableBox()) {
//        val optionPicked = restByDiv.entries.filter { it.key > 1.00 }.minBy { it.key }
//        val pickedScheme = packingSchemes.filter { (product.quantity / it.qty) == optionPicked.key }
//                                         .first()
//                                         .deepCopy()
//        pickedScheme.box.availableQTY = 0
//        product.quantity -= pickedScheme.qty
//        packs.add(Pack(pickedScheme.box, mutableMapOf(product to pickedScheme.qty.toInt())))
//    } else {
//        val optionPicked = restByDiv.entries.maxBy { it.key }
//        val pickedScheme = packingSchemes.filter { (product.quantity / it.qty) == optionPicked.key }
//                                         .first()
//                                         .deepCopy()
//        pickedScheme.box.availableQTY = pickedScheme.box.availableQTY!! - optionPicked.value
//        product.quantity -= optionPicked.value
//        packs.add(Pack(pickedScheme.box, mutableMapOf(product to optionPicked.value.toInt())))
//    }
//}
//
//fun MutableMap<Double, Int>.containsFillableBox() = this.keys.any { it > 1.00 }
//fun PackingScheme.deepCopy() = this.copy(box = this.box.copy())
//
//fun Any.prettyPrint(t: String = "") {
//    println("$t ---------------------------------------------------------------------------")
//    println(this.toString())
//    println()
//}
//
//fun Pack.prettyPrint() {
//    println("****_PACK_*****")
//    println("${this.box.name}, Disponible Qty: ${this.box.availableQTY} \n  Content: ")
//    this.productsAndQty.entries.forEach {
//        println("\t\t ${it.key.name}, Qty: ${it.value}")
//    }
//    println()
//
//}
//
