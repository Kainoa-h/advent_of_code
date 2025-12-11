@fieldwise_init
struct Vector(Copyable, ImplicitlyCopyable, Movable, Stringable):
    var x: Int
    var y: Int

    fn __init__(out self, read str: StringSlice) raises:
        var xyz = str.split(",")
        self.x = atol(xyz[0])
        self.y = atol(xyz[1])

    fn __str__(self) -> String:
        return String(self.x) + ", " + String(self.y)


struct Edge(Copyable, ImplicitlyCopyable, Movable):
    var v1: Vector
    var v2: Vector
    var min_x: Int
    var max_x: Int
    var min_y: Int
    var max_y: Int
    var is_vertical: Bool


    fn __init__(out self, v1: Vector, v2: Vector) raises:
        if v1.x != v2.x and v1.y != v2.y:
            raise Error("not a line")
        self.v1 = v1
        self.v2 = v2
        self.min_x= min(v1.x,v2.x)
        self.max_x= max(v1.x,v2.x)
        self.min_y= min(v1.y,v2.y)
        self.max_y= max(v1.y,v2.y)
        self.is_vertical = v1.x == v2.x

    fn check_intersect(self, a: Vector, b: Vector) -> Bool:
      var box_min_x = min(a.x, b.x)
      var box_max_x = max(a.x, b.x)
      var box_min_y = min(a.y, b.y)
      var box_max_y = max(a.y, b.y)
    
      if self.v1.x == self.v2.x:
        var x_intersects = box_min_x < self.v1.x and self.v1.x < box_max_x
        var y_within_bounds = max(box_min_y, self.min_y) < min(box_max_y, self.max_y)
        return x_intersects and y_within_bounds

      var y_intersects = box_min_y < self.v1.y and self.v1.y < box_max_y
      var x_within_bounds = max(box_min_x, self.min_x) < min(box_max_x, self.max_x)
      return y_intersects and x_within_bounds


fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var corners = parse_str_to_vecs(lines)
        var edges = parse_vecs_to_edges(corners)

        var largest_area = 0
        for base_idx in range(len(corners)):
          var base_vec = corners[base_idx]
          for other_idx in range(base_idx + 1, len(corners)):
            var other_vec = corners[other_idx]
            if has_intersecting_boundaries_check(edges, base_vec, other_vec):
              continue
            if is_outside_check(edges, base_vec, other_vec):
              continue
            var abs_diff_x = abs(base_vec.x - other_vec.x) + 1
            var abs_diff_y = abs(base_vec.y - other_vec.y) + 1
            var area = abs_diff_x * abs_diff_y
            if area > largest_area:
              largest_area = area
        print(largest_area)


fn is_outside_check(read edges: List[Edge], v1: Vector, v2: Vector ) -> Bool:
  var count = 0
  var x:Float64 = (v1.x + v2.x) // 2 + 0.1
  var y:Int = (v1.y + v2.y)//2
  for edge in edges:
    if edge.is_vertical:
      continue
    if Float64(min(edge.v1.x, edge.v2.x)) > x or x > Float64(max(edge.v1.x, edge.v2.x)):
      continue
    if edge.v1.y > y:
      count += 1
  return count % 2 == 0


fn has_intersecting_boundaries_check(read edges: List[Edge], v1: Vector, v2: Vector) -> Bool:
  for edge in edges:
    if edge.check_intersect(v1, v2):
      return True
  return False

fn parse_vecs_to_edges(read corners: List[Vector]) raises -> List[Edge]:
    var list = List[Edge]()
    for i in range(len(corners)):
        var this_vec = corners[i]
        var next_vec = corners[(i + 1) % len(corners)]
        list.append(Edge(this_vec, next_vec))
    return list^


fn parse_str_to_vecs[
    origin: Origin
](read lines: List[StringSlice[origin]]) raises -> List[Vector]:
    var corners = List[Vector]()
    for line in lines:
        var xy = line.split(",")
        var x = atol(xy[0])
        var y = atol(xy[1])
        corners.append(Vector(x, y))
    return corners^
