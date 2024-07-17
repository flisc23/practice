import com.github.skjolber.packing.api.*;
import com.github.skjolber.packing.packer.laff.FastLargestAreaFitFirstPackager;

import java.util.ArrayList;
import java.util.List;

public class Packer   {
    public static void main(String[] args) {

        plainPackerTest();
        System.out.println("stop");
    }

    private static void plainPackerTest() {
        List<ContainerItem> containers = ContainerItem
                .newListBuilder()
                .withContainer(Container.newBuilder().withDescription("ID1")
                        .withEmptyWeight(1)
                        .withSize(2, 3, 2)
                        .withMaxLoadWeight(100)
                        .build(), 2)
                .build();

        FastLargestAreaFitFirstPackager packager = FastLargestAreaFitFirstPackager.newBuilder().build();

        List<StackableItem> products = new ArrayList<>();

        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(2, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(1, 2, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(1, 2, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(3, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(2, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(1, 1, 1).withRotate3D().withWeight(1).build(), 1));
        products.add(new StackableItem(Box.newBuilder().withDescription("X").withSize(1, 1, 1).withRotate3D().withWeight(1).build(), 1));

        PackagerResult build = packager.newResultBuilder().withContainers(containers).withMaxContainerCount(2).withStackables(products).build();
        if(build.isSuccess()) {
            build.getContainers().forEach(container -> {
                System.out.println("\t Container: " + container.getDescription());
                container.getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
            });
        } else {
            System.out.println("COULD NOT BUILD RESULT");
        }
        // build.get(0).getStack().getPlacements().forEach(c -> System.out.println(c.toString()));
        System.out.println("");
    }


}