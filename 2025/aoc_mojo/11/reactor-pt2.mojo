from collections import Set


fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var forward_pass_map = Dict[String, List[String]]()
        for line in lines:
            var segments = line.split(" ")
            var key = String(segments[0][:-1])
            var value = List[String]()
            for v in segments[1:]:
                value.append(String(v))
            forward_pass_map[key] = value^
        var cache = Dict[String, Int]()
        print(dfs(forward_pass_map, cache, 0, "svr"))


fn dfs(
    read map: Dict[String, List[String]],
    mut cache: Dict[String, Int],
    read seen: UInt8,
    read search: String,
) raises -> Int:
    ref values = map[search]
    if values[0] == "out":
        return 1 if seen == 3 else 0
    var paths = 0

    var key = search + "_" + String(seen)
    if key in cache:
      return cache[key]

    for v in values:
        var new_seen: UInt8 = seen
        if v == "fft":
            new_seen |= 1 << 1
        elif v == "dac":
            new_seen |= 1

        var ps = dfs(map, cache, new_seen, v)
        paths += ps

    cache[key] = paths
    return paths
