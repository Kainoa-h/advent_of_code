fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var segments = content.strip().split("\n\n")
        var fresh_ranges = segments[0].split("\n")
        var parsed_fresh_ranges = List[Tuple[Int64, Int64]]()
        for fr in fresh_ranges:
          var seg = fr.split("-")
          parsed_fresh_ranges.append((Int64(atol(seg[0])), Int64(atol(seg[1]))))
        var available_ingredients = segments[1].split("\n")
        print(find_fresh_and_available(parsed_fresh_ranges, available_ingredients))


fn find_fresh_and_available[
    origin: Origin
](
    read fresh_ranges: List[Tuple[Int64, Int64]],
    read available_ingredients: List[StringSlice[origin]],
) raises -> Int32 :
    var count = 0
    for x in available_ingredients:
      var ing = Int64(atol(x))
      for fresh_range in fresh_ranges:
        if ing >= fresh_range[0] and ing <= fresh_range[1]:
          count += 1
          break
    return count


fn printty(read list: List[List[String]]):
    for l in list:
        for c in l:
            print(c, end="")
        print()


fn printt[origin: Origin](read list: List[StringSlice[origin]]):
    for l in list:
        print(l)
