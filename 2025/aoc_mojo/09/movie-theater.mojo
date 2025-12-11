fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var large_x = 0
        var large_y = 0
        var large_area = 0
        for base_idx in range(len(lines)):
          var base_xy = lines[base_idx].split(",") 
          var base_x = atol(base_xy[0])
          var base_y = atol(base_xy[1])
          for other_idx in range(base_idx + 1, len(lines)):
            var other_xy = lines[other_idx].split(",") 
            var other_x = atol(other_xy[0])
            var other_y = atol(other_xy[1])
            var abs_diff_x = abs(base_x - other_x) + 1
            var abs_diff_y = abs(base_y - other_y) + 1
            var area = abs_diff_x * abs_diff_y
            if area > large_area:
              large_x = abs_diff_x
              large_y = abs_diff_y
              large_area = area
        print("x:", large_x)
        print("y:", large_y)
        print("size:", large_area)

fn printty(read list: List[List[String]]):
    for l in list:
        for c in l:
            print(c, end="")
        print()


fn printt[origin: Origin](read list: List[StringSlice[origin]]):
    for l in list:
        print(l)
