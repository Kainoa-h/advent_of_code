from math import sqrt
from collections import Set


@fieldwise_init
struct Vector(Copyable, Equatable, ImplicitlyCopyable, Movable, Stringable):
    var x: Int
    var y: Int
    var z: Int

    fn __init__(out self, read str: StringSlice) raises:
        var xyz = str.split(",")
        self.x = atol(xyz[0])
        self.y = atol(xyz[1])
        self.z = atol(xyz[2])

    fn __str__(self) -> String:
        return String(self.x) + ", " + String(self.y) + ", " + String(self.z)

    fn __eq__(self, other: Vector) -> Bool:
        if self.x != other.x:
            return False
        if self.y != other.y:
            return False
        if self.z != other.z:
            return False
        return True

    fn dist_from(mut self, other: Vector) -> Float64:
        return sqrt(
            pow(Float64(self.x - other.x), 2)
            + pow(Float64(self.y - other.y), 2)
            + pow(Float64(self.z - other.z), 2)
        )


struct VectorPair(Comparable, Copyable, Movable):
    var v1: Vector
    var v2: Vector
    var dist: Float64

    fn __init__(out self, v1: Vector, v2: Vector):
        self.v1 = v1
        self.v2 = v2
        self.dist = sqrt(
            pow(Float64(v1.x - v2.x), 2)
            + pow(Float64(v1.y - v2.y), 2)
            + pow(Float64(v1.z - v2.z), 2)
        )

    fn get_other(self, other: Vector) -> Optional[Vector]:
        if self.v1 == other:
            return self.v2
        if self.v2 == other:
            return self.v1
        return None

    fn __lt__(self, other: VectorPair) -> Bool:
        return self.dist < other.dist

    fn __eq__(self, other: VectorPair) -> Bool:
        return self.dist < other.dist


fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var junc_boxes = List[Vector]()
        for line in lines:
            junc_boxes.append(Vector(line))
        var sorted_pairs = get_sorted_distance_pairs(junc_boxes)[:1000]
        var circuits = List[Set[String]]()
        for pair in sorted_pairs:
            var v1_str = String(pair.v1)
            var v2_str = String(pair.v2)

            var matching_set_idxs = List[Int]()
            for i in range(len(circuits)):
                if v1_str in circuits[i] or v2_str in circuits[i]:
                    matching_set_idxs.append(i)

            if len(matching_set_idxs) == 0:
                circuits.append(Set[String](v1_str, v2_str))

            elif len(matching_set_idxs) == 1:
                circuits[matching_set_idxs[0]].add(v1_str)
                circuits[matching_set_idxs[0]].add(v2_str)
            else:
                removed_set = circuits.pop(matching_set_idxs[1])
                circuits[matching_set_idxs[0]] |= removed_set

        sort_sets_by_size(circuits)
        var sum = len(circuits[0]) * len(circuits[1]) * len(circuits[2])
        print(sum)
        # for c in circuits:
        #   print("set size:", len(c))
        #   for v in c:
        #     print(v)
        #   print()


fn sort_sets_by_size[T: KeyElement](mut sets: List[Set[T]]):
    @parameter
    fn sort_by_size(a: Set[T], b: Set[T]) -> Bool:
        return len(a) > len(b)

    sort[sort_by_size](sets)


fn get_sorted_distance_pairs(read vecs: List[Vector]) -> List[VectorPair]:
    var pairs = List[VectorPair]()
    for i in range(len(vecs) - 1):
        var base_vec = vecs[i]
        for j in range(i + 1, len(vecs)):
            var cmp_vec = vecs[j]
            pairs.append(VectorPair(base_vec, cmp_vec))
    sort(pairs)
    return pairs^


fn printty(read list: List[List[String]]):
    for l in list:
        for c in l:
            print(c, end="")
        print()


fn printt[origin: Origin](read list: List[StringSlice[origin]]):
    for l in list:
        print(l)
