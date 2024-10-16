package packing_scheme

import com.github.skjolber.packing.api.*
import com.github.skjolber.packing.packer.laff.LargestAreaFitFirstPackager
import java.util.*
import java.util.function.Consumer

class KotlinPacker {

}

fun main() {
//    println("asd")
    test_10602220_DELIVERY_NOTE()
}

private fun buildListContainer(x: Int, y: Int, z: Int): List<ContainerItem> {
    val random = Random()
    val container: Container = Container.newBuilder()
        .withDescription(UUID.randomUUID().toString())
        .withEmptyWeight(1)
        .withSize(x, y, z)
        .withMaxLoadWeight(100)
        .build()

    // with unlimited number of containers available
    val containerItems = ContainerItem
        .newListBuilder()
        .withContainer(container)
        .build()
    return containerItems
}

//class ContainerItemComparator : Comparator<ContainerItem> {
//    override fun compare(c1: ContainerItem, c2: ContainerItem): Int {
//        return compareValuesBy(c1, c2, { it.container.stackValues[0].dx },
//            { it.container.stackValues[0].dy },
//            { it.container.stackValues[0].dz})
//    }
//}

val containerItemComparator = Comparator<ContainerItem> { c1, c2 ->
    compareValuesBy(c1, c2, { it.container.stackValues[0].dx },
        { it.container.stackValues[0].dy },
        { it.container.stackValues[0].dz})
}

@Throws(Exception::class)
fun test_10602220_DELIVERY_NOTE() {
    val containers: MutableList<ContainerItem> = ArrayList()
    //        containers.add(buildContainer(100 , 100 , 50)); //test
//        containers.add(buildContainer(50 , 50 , 50)); //test
    containers.addAll(buildListContainer(400, 400, 400))
    containers.addAll(buildListContainer(600, 410, 800))
    containers.addAll(buildListContainer(400, 400, 200))
    containers.addAll(buildListContainer(220, 200, 230))
    containers.addAll(buildListContainer(380, 400, 600))
    containers.addAll(buildListContainer(200, 200, 400))
    containers.addAll(buildListContainer(480, 410, 800))
    containers.addAll(buildListContainer(220, 200, 230))
    containers.addAll(buildListContainer(200, 200, 400))
    containers.addAll(buildListContainer(400, 200, 400))
    containers.addAll(buildListContainer(480, 410, 800))
    containers.addAll(buildListContainer(400, 400, 400))
    containers.addAll(buildListContainer(380, 400, 600))
    containers.addAll(buildListContainer(600, 410, 800))

    containers.sortWith(Comparator.comparing { c: ContainerItem ->
        c.container.volume
    })
    val uniqueContainers = containers.toSortedSet(containerItemComparator).toList()

    uniqueContainers.forEach { c -> println("${c.container.stackValues[0].dx} x " +
            "                          ${c.container.stackValues[0].dy} x " +
            "                          ${c.container.stackValues[0].dz}") }
    val products: MutableList<StackableItem> = ArrayList()
    products.add(
        StackableItem(
            Box.newBuilder().withDescription("desc")
                .withSize(200, 200, 100).withRotate3D().withWeight(1).build(), 3
        )
    )
    products.add(
        StackableItem(
            Box.newBuilder().withDescription("desc")
                .withSize(100, 150, 140).withRotate3D().withWeight(1).build(), 2
        )
    )

    //        double totalProductVolume = products.stream()
//                .mapToLong(product -> product.getStackable().getVolume() * product.getCount())
//                .sum();
    val results: List<PackagerResult> = ArrayList()

    //        PlainPackager packager = PlainPackager.newBuilder().build();
//        FastLargestAreaFitFirstPackager packager = FastLargestAreaFitFirstPackager.newBuilder().build();
    val packager = LargestAreaFitFirstPackager.newBuilder().build()


    //        BruteForcePackager packager = BruteForcePackager.newBuilder().build();
    val build = packager.newResultBuilder()
        .withContainers(containers)
        .withMaxContainerCount(10)
        .withStackables(products)
        .build()
    println("\n PLAIN \n")
    if (build.isSuccess) {
        build.containers.forEach(Consumer { container: Container ->
            println(
                "\t Container: " + container.description + String.format(
                    " (%s x %s x %s), Volume: %d",
                    container.stack.containerStackValue.dx,
                    container.stack.containerStackValue.dy,
                    container.stack.containerStackValue.dz,
                    container.volume
                )
            )
            container.stack.placements
                .forEach(Consumer { c: StackPlacement ->
                    println(
                        "Pos:" + c.toString() +
                                "Sizes: " + c.stackValue.dx +
                                " x " + c.stackValue.dy +
                                " x " + c.stackValue.dz
                    )
                })
        })
    } else {
        println("ERROR PACKING")
    }

    // Different strategies for different maximum container counts
//        for (int maxContainerCount = 1; maxContainerCount <= 5; maxContainerCount++) {
//            FastLargestAreaFitFirstPackager packager = FastLargestAreaFitFirstPackager.newBuilder().build();
//
//            PackagerResult result = packager.newResultBuilder()
//                    .withContainers(containers)
//                    .withMaxContainerCount(maxContainerCount)
//                    .withStackables(products)
//                    .build();
//
//            if (result.isSuccess()) {
//                results.add(result);
//            }
//        }
}