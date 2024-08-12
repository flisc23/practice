import com.github.skjolber.packing.api.*;
import com.github.skjolber.packing.packer.bruteforce.BruteForcePackager;
import com.github.skjolber.packing.packer.laff.FastLargestAreaFitFirstPackager;
import com.github.skjolber.packing.packer.laff.LargestAreaFitFirstPackager;
import com.github.skjolber.packing.packer.plain.PlainPackager;

import java.util.*;
import java.util.stream.Collectors;

public class Packer   {
    public static void main(String[] args) throws Exception {
//        plainPackerTest();
//        test_PROD_case();
//        test_PROD_case_LAF();
        test_10602220_DELIVERY_NOTE();
//        System.out.println("stop");
    }

    public static void test_10602220_DELIVERY_NOTE() throws Exception {
        List<ContainerItem> containers = new ArrayList<>();
//        containers.addAll(buildListContainer(400 , 400 , 400));
        containers.add(buildContainer(100 , 100 , 50));
        containers.add(buildContainer(50 , 50 , 50)); //test
        containers.addAll(buildListContainer(600 , 410 , 800));
        containers.addAll(buildListContainer(400 , 200 , 400));
        containers.addAll(buildListContainer(220 , 200 , 230));
        containers.addAll(buildListContainer(380 , 400 , 600));
        containers.addAll(buildListContainer(200 , 200 , 400));
        containers.addAll(buildListContainer(480 , 410 , 800));
        containers.addAll(buildListContainer(220 , 200 , 230));
        containers.addAll(buildListContainer(200 , 200 , 400));
        containers.addAll(buildListContainer(400 , 200 , 400));
        containers.addAll(buildListContainer(480 , 410 , 800));
        containers.addAll(buildListContainer(400 , 400 , 400));
        containers.addAll(buildListContainer(380 , 400 , 600));
        containers.addAll(buildListContainer(600 , 410 , 800));

        containers.sort(Comparator.comparing(c -> c.getContainer().getVolume()));
        List<StackableItem> products = new ArrayList<>();
        products.add(new StackableItem(Box.newBuilder().withDescription("desc")
                .withSize(200, 200, 100).withRotate3D().withWeight(1).build(), 3));
        products.add(new StackableItem(Box.newBuilder().withDescription("desc")
                .withSize(100, 150, 140).withRotate3D().withWeight(1).build(), 2));

//        double totalProductVolume = products.stream()
//                .mapToLong(product -> product.getStackable().getVolume() * product.getCount())
//                .sum();

        List<PackagerResult> results = new ArrayList<>();

//        PlainPackager packager = PlainPackager.newBuilder().build();
//        FastLargestAreaFitFirstPackager packager = FastLargestAreaFitFirstPackager.newBuilder().build();
        LargestAreaFitFirstPackager packager = LargestAreaFitFirstPackager.newBuilder().build();
//        BruteForcePackager packager = BruteForcePackager.newBuilder().build();


        PackagerResult build = packager.newResultBuilder()
                .withContainers(containers)
                .withMaxContainerCount(10)
                .withStackables(products)
                .build();
        System.out.println("\n PLAIN \n");
        if (build.isSuccess()) {
            build.getContainers().forEach(container -> {
                System.out.println("\t Container: " + container.getDescription() + String.format(" (%s x %s x %s), Volume: %d",
                        container.getStack().getContainerStackValue().getDx(),
                        container.getStack().getContainerStackValue().getDy(),
                        container.getStack().getContainerStackValue().getDz(),
                        container.getVolume()));
                container.getStack().getPlacements().forEach(c -> System.out.println("Pos:" + c.toString() +
                        "Sizes: " + c.getStackValue().getDx() +
                        " x " + c.getStackValue().getDy() +
                        " x " + c.getStackValue().getDz()));
            });
        } else {
            System.out.println("ERROR PACKING");
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

        Optional<PackagerResult> bestResult = results.stream()
                .min(Comparator.comparingInt((PackagerResult res) -> res.getContainers().size())
                        .thenComparingDouble(res -> Packer.averageContainerVolumeUsage(res)));


        // Output the best result
//        if (bestResult.isPresent()) {
//            System.out.println("Best packing result found:");
//            PackagerResult result = bestResult.get();
//            for (Container containerResult : result.getContainers()) {
//                double filledVolume = calculateFilledVolume(containerResult);
//                double totalVolume = containerResult.getStack().getVolume();
//                double percentageFilled = (filledVolume / totalVolume) * 100;
//                System.out.printf("Container: %s, Volume filled: %.2f%%\n",
//                        containerResult.getDescription(), percentageFilled);
//            }
//        } else {
//            System.out.println("No valid packing options found.");
//        }


    }

    private static double averageContainerVolumeUsage(PackagerResult result) {
        return result.getContainers().stream()
                .mapToDouble(Packer::calculateFilledVolume)
                .average()
                .orElse(0);
    }

    private static double calculateFilledVolume(Container containerResult) {
        return containerResult.getStack().getPlacements().stream()
                .mapToDouble(p -> p.getStackable().getVolume())
                .sum();
    }


    private static ContainerItem getRandomContainer() {
        Random random = new Random();
        return new ContainerItem(Container.newBuilder()
                .withDescription(UUID.randomUUID().toString())
                .withEmptyWeight(1)
                .withSize(random.nextInt(100), random.nextInt(100), random.nextInt(100))
                .withMaxLoadWeight(100)
                .build(), 1);
    }

    private static List<ContainerItem> buildListContainer(Integer x, Integer y, Integer z) {
        Random random = new Random();
        Container container = Container.newBuilder()
                .withDescription(UUID.randomUUID().toString())
                .withEmptyWeight(1)
                .withSize(x, y, z)
                .withMaxLoadWeight(100)
                .build();

// with unlimited number of containers available
        List<ContainerItem> containerItems = ContainerItem
                .newListBuilder()
                .withContainer(container)
                .build();
        return  containerItems;
    }

    private static ContainerItem buildContainer(Integer x, Integer y, Integer z) {
        Random random = new Random();
        return new ContainerItem(Container.newBuilder()
                .withDescription(UUID.randomUUID().toString())
                .withEmptyWeight(1)
                .withSize(x, y, z)
                .withMaxLoadWeight(1)
                .build(), 2);
    }

    private static void plainPackerTest() {
        List<ContainerItem> containers = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            containers.add(getRandomContainer());
//        }

//        External Box 600x380x400 BC-640
//        External Box 230x220x200 3C-420
//        External Box 400x400x200 BC-640


//        containers.add(buildContainer(230,220, 200));
//        containers.addAll(buildContainer(400, 400, 200));
        List<ContainerItem> sortedContainers = containers.stream()
                .sorted((c1, c2) -> c1.getContainer().getVolume() < c2.getContainer().getVolume() ? -1 : 1)
                .collect(Collectors.toList());

//        TODO: assuming containers are boxes from packing scheme,
//        TODO: need filtering on those that have volume < products(articles from a line) total volume
//        TODO: and maybe filter ASC by size (could be usefull later on improvements) DONE

//        FastLargestAreaFitFirstPackager packager = FastLargestAreaFitFirstPackager.newBuilder().build();
        PlainPackager packager = PlainPackager.newBuilder().build();

        List<StackableItem> line1_products = getProducts("L1");

        PackagerResult build = packager.newResultBuilder()
                .withContainers(sortedContainers).withMaxContainerCount(2)
                .withStackables(line1_products)
                .build();
        long productsTotalVolume = line1_products.stream()
                .map(stackableItem -> stackableItem.getStackable().getVolume())
                .mapToLong(Long::longValue)
                .sum();
        List<ContainerItem> availableContainers = sortedContainers.stream()
                .filter(containerItem -> containerItem.getContainer().getVolume() >= productsTotalVolume)
                .toList();
        if (availableContainers.isEmpty()) {
            // TODO: check how to handle need of multiple containers (and coupled line condition later), maybe just
            // increase number of container - algorithm will pick how many are needed.
        }
        if (build.isSuccess()) {
            build.getContainers().forEach(container -> {
                System.out.println("\t Container: " + container.getDescription());
                container.getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
            });

        } else {
            System.out.println("COULD NOT BUILD RESULT");
//            TODO: try to pick a bigger box if possible or increase the number of boxes with an appropiate volume to the unfiited items
        }
        // build.get(0).getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
        System.out.println("");
    }
    private static void test_PROD_case() {
        List<ContainerItem> containers = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            containers.add(getRandomContainer());
//        }

//        External Box 600x380x400 BC-640
//        External Box 230x220x200 3C-420
//        External Box 400x400x200 BC-640

//        INNER PACKIG Packing Box PO-xCD-L-200-200-100
//        External box 230x220x200
//        External box 600x380x400
//        External box 800x600x410
//        External box 400x400x200
//        External box 800x480x410
//        External box 400x200x200

//        containers = buildContainer(230, 220, 200);
        List<StackableItem> products = new ArrayList<>();
        products.add(new StackableItem(Box.newBuilder().withDescription("desc")
                .withSize(200, 200, 100).withRotate3D().withWeight(1).build(), 2));
        products.add(new StackableItem(Box.newBuilder().withDescription("desc")
                .withSize(200, 200, 100).withRotate3D().withWeight(1).build(), 2));

//        FastLargestAreaFitFirstPackager packager = FastLargestAreaFitFirstPackager.newBuilder().build();
        PlainPackager packager = PlainPackager.newBuilder().build();

        PackagerResult build = packager.newResultBuilder()
                .withContainers(containers).withMaxContainerCount(2)
                .withStackables(products)
                .build();
        System.out.println("\n PLAIN \n");
        if (build.isSuccess()) {
            build.getContainers().forEach(container -> {
                System.out.println("\t Container: " + container.getDescription());
                container.getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
            });

        } else {
            System.out.println("COULD NOT BUILD RESULT");
//            TODO: try to pick a bigger box if possible or increase the number of boxes with an appropiate volume to the unfiited items
        }
        // build.get(0).getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
        System.out.println("");

    }
    private static void test_PROD_case_LAF() {
        List<ContainerItem> containers = new ArrayList<>();

//        External Box 600x380x400 BC-640
//        External Box 230x220x200 3C-420
//        External Box 400x400x200 BC-640

//        INNER PACKIG Packing Box PO-xCD-L-200-200-100
//        External box 230x220x200
//        External box 600x380x400
//        External box 800x600x410
//        External box 400x400x200
//        External box 800x480x410
//        External box 400x200x200

//        containers.add(buildContainer(230, 220, 200));
        List<StackableItem> products = new ArrayList<>();
        products.add(new StackableItem(Box.newBuilder().withDescription("desc")
                .withSize(200, 200, 100).withRotate3D().withWeight(1).build(), 2));

        LargestAreaFitFirstPackager packager = LargestAreaFitFirstPackager.newBuilder().build();

        PackagerResult build = packager.newResultBuilder()
                .withContainers(containers).withMaxContainerCount(2)
                .withStackables(products)
                .build();
        System.out.println("\nLARGEST AREA FIR FIRST\n");
        if (build.isSuccess()) {
            build.getContainers().forEach(container -> {
                System.out.println("\t Container: " + container.getDescription());
                container.getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
            });

        } else {
            System.out.println("COULD NOT BUILD RESULT");
//            TODO: try to pick a bigger box if possible or increase the number of boxes with an appropiate volume to the unfiited items
        }
        System.out.println("");

    }

    private static List<StackableItem> getProducts(String desc) {
        List<StackableItem> products = new ArrayList<>();
        // line of products
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(2, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 2, 1).withRotate3D().withWeight(1).build(), 1));
//      products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 2, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(3, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(2, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 1, 1).withRotate3D().withWeight(1).build(), 1));
        return products;
    }

}