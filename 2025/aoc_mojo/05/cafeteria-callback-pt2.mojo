fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var segments = content.strip().split("\n\n")
        var fresh_ranges = segments[0].split("\n")
        var parsed_fresh_ranges = List[Tuple[Int64, Int64]]()
        for fr in fresh_ranges:
            var seg = fr.split("-")
            parsed_fresh_ranges.append(
                (Int64(atol(seg[0])), (Int64(atol(seg[1]))))
            )

        @parameter
        fn fuck(a: Tuple[Int64, Int64], b: Tuple[Int64, Int64]) -> Bool:
            if a[0] == b[0]:
                return a[1] < b[1]
            return a[0] < b[0]

        sort[fuck](parsed_fresh_ranges)
        var count: Int64 = 0
        for x in collapse(parsed_fresh_ranges):
            count += x[1] - x[0] + 1

        print(count)

fn collapse(read ranges: List[Tuple[Int64, Int64]]) -> List[Tuple[Int64, Int64]]:
    var comps = List[Tuple[Int64, Int64]]()
    var comp = ranges[0]
    for i in range(1, len(ranges)):
        var x = ranges[i]
        if comp[1] >= x[0]:
            comp = (comp[0], max(comp[1], x[1]))
        else:
            comps.append(comp)
            comp = x
    comps.append(comp)
    return comps^
