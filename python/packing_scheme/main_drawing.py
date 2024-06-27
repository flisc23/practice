from py3dbp import Packer, Bin, Item
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d.art3d import Poly3DCollection, Line3DCollection

packer = Packer()

packer.add_bin(Bin('small-box', 8, 5, 2, 70.0))
# packer.add_bin(Bin('small-box', 1, 5, 1, 70.0))
# packer.add_bin(Bin('medium-box', 11.0, 8.5, 5.5, 70.0))
# packer.add_bin(Bin('large-box', 12.0, 12.0, 5.5, 70.0))

packer.add_item(Item('[item 1]', 2, 2, 1, 1))
packer.add_item(Item('[item 1]', 2, 2, 1, 1))
packer.add_item(Item('[item 1]', 2, 2, 1, 1))
packer.add_item(Item('[item 1]', 1, 1, 1, 1))
packer.add_item(Item('[item 1]', 1, 1, 1, 1))
packer.add_item(Item('50g [item 1]', 5, 3, 2, 1))
# packer.add_item(Item('50g [item 1]', 5, 3, 2, 1))
# packer.add_item(Item('50g [item 1]', 5, 3, 2, 1))

packer.pack()

# Function to draw a 3D box
def draw_box(ax, origin, size, color, label=None):
    x, y, z = origin
    dx, dy, dz = size
    vertices = [
        [(x, y, z), (x + dx, y, z), (x + dx, y + dy, z), (x, y + dy, z)],
        [(x, y, z), (x, y + dy, z), (x, y + dy, z + dz), (x, y, z + dz)],
        [(x, y, z), (x + dx, y, z), (x + dx, y, z + dz), (x, y, z + dz)],
        [(x + dx, y + dy, z + dz), (x, y + dy, z + dz), (x, y + dy, z), (x + dx, y + dy, z)],
        [(x + dx, y + dy, z + dz), (x + dx, y, z + dz), (x + dx, y, z), (x + dx, y + dy, z)],
        [(x + dx, y + dy, z + dz), (x, y + dy, z + dz), (x, y, z + dz), (x + dx, y, z + dz)]
    ]
    box = Poly3DCollection(vertices, alpha=0.5, facecolors=color)
    ax.add_collection3d(box)

    # Draw edges
    edges = [
        [(x, y, z), (x + dx, y, z)], [(x + dx, y, z), (x + dx, y + dy, z)], [(x + dx, y + dy, z), (x, y + dy, z)],
        [(x, y + dy, z), (x, y, z)],
        [(x, y, z), (x, y, z + dz)], [(x + dx, y, z), (x + dx, y, z + dz)],
        [(x + dx, y + dy, z), (x + dx, y + dy, z + dz)], [(x, y + dy, z), (x, y + dy, z + dz)],
        [(x, y, z + dz), (x + dx, y, z + dz)], [(x + dx, y, z + dz), (x + dx, y + dy, z + dz)],
        [(x + dx, y + dy, z + dz), (x, y + dy, z + dz)], [(x, y + dy, z + dz), (x, y, z + dz)]
    ]
    ax.add_collection3d(Line3DCollection(edges, colors='k', linewidths=1, linestyles='-'))

    if label:
        ax.text(x + dx / 2, y + dy / 2, z + dz / 2, label, color='black', fontsize=10, ha='center', va='center')


# Plot the bins and items
fig = plt.figure()
ax = fig.add_subplot(111, projection='3d')

colors = ['r', 'g', 'b', 'y']

max_width, max_height, max_depth = 20, 20, 10
# for b in packer.bins:
#     print(":::::::::::", b.string())
#
#     print("FITTED ITEMS:")
#     for item in b.items:
#         print("====> ", item.string())
#
#     print("UNFITTED ITEMS:")
#     for item in b.unfitted_items:
#         print("====> ", item.string())
#
#     print("***************************************************")
#     print("***************************************************")

ax.set_xlabel('Width')
ax.set_ylabel('Height')
ax.set_zlabel('Depth')
ax.set_xlim(0, max_width)
ax.set_ylim(0, max_height)
ax.set_zlim(0, max_depth)


for bin_idx, b in enumerate(packer.bins):
    print("::::::::::: ", b.string())

    # Draw the bin
    # draw_box(ax, (0, 0, 0), (int(b.width), int(b.height), int(b.depth)), 'cyan', label=b.name)

    max_width = max(max_width, b.width)
    max_height = max(max_height, b.height)
    max_depth = max(max_depth, b.depth)

    print("FITTED ITEMS:")
    for item_idx, item in enumerate(b.items):
        print("====> ", item.string())
        # draw_box(ax, (int(item.position[0]), int(item.position[1]), int(item.position[2])),
        #          (int(b.width), int(b.height), int(b.depth)),
        #          colors[item_idx % len(colors)], label=item.name)

    print("UNFITTED ITEMS:")
    for item in b.unfitted_items:
        print("====> ", item.string())

    print("***************************************************")
    print("***************************************************")

# binn = Bin('small-box', 8, 5, 2, 70.0)
# draw_box(ax, (0, 0, 0), (int(binn.width), int(binn.height), int(binn.depth)), 'cyan', label=b.name)
# draw_box(ax, (0, 0, int(binn.depth)), (int(binn.width), int(binn.height), int(binn.depth)), 'red', label='box-2')
# binn2 = Bin('small-box', 2, 5, 2, 70.0)
# draw_box(ax, (8, 0, 0), (int(binn2.width), int(binn2.height), int(binn2.depth)), 'green', label='box-2')
# draw_box(ax, (10, 0, 0), (10, 20, 8), 'green', label='box-3')

plt.show()






# packer.add_bin(Bin('small-envelope', 11.5, 6.125, 0.25, 10))
# packer.add_bin(Bin('large-envelope', 15.0, 12.0, 0.75, 15))
# packer.add_bin(Bin('medium-2-box', 13.625, 11.875, 3.375, 70.0))
# packer.add_bin(Bin('large-2-box', 23.6875, 11.75, 3.0, 70.0))

# packer.add_item(Item('50g [item 1]', 4, 3, 1.6, 1))
# packer.add_item(Item('50g [item 1]', 5, 3, 1.6, 1))

# packer.add_item(Item('50g [powder 1]', 3.9370, 1.9685, 1.9685, 1))
# packer.add_item(Item('50g [powder 2]', 3.9370, 1.9685, 1.9685, 2))
# packer.add_item(Item('50g [powder 3]', 3.9370, 1.9685, 1.9685, 3))
# packer.add_item(Item('250g [powder 4]', 7.8740, 3.9370, 1.9685, 4))
# packer.add_item(Item('250g [powder 5]', 7.8740, 3.9370, 1.9685, 5))
# packer.add_item(Item('250g [powder 6]', 7.8740, 3.9370, 1.9685, 6))
# packer.add_item(Item('250g [powder 7]', 7.8740, 3.9370, 1.9685, 7))
# packer.add_item(Item('250g [powder 8]', 7.8740, 3.9370, 1.9685, 8))
# packer.add_item(Item('250g [powder 9]', 7.8740, 3.9370, 1.9685, 9))

# packer.pack()

