import com.github.skjolber.packing.api.*;
import com.github.skjolber.packing.packer.plain.PlainPackager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class Packer   {
    public static void main(String[] args) {
        plainPackerTest();
        System.out.println("stop");
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

    private static void plainPackerTest() {
        List<ContainerItem> containers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            containers.add(getRandomContainer());
        }
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

    private static List<StackableItem> getProducts(String desc) {
        List<StackableItem> products = new ArrayList<>();
        // line of products
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(2, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 2, 1).withRotate3D().withWeight(1).build(), 1));
//        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 2, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(3, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(2, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription(desc).withSize(1, 1, 1).withRotate3D().withWeight(1).build(), 1));
        return products;
    }


}