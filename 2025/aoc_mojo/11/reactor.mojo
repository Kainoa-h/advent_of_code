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
        print(dfs(forward_pass_map, cache, "you"))


fn dfs(
    read map: Dict[String, List[String]],
    mut cache: Dict[String, Int],
    read search: String,
) raises -> Int:
    ref values = map[search]
    if values[0] == "out":
        return 1
    var paths = 0
    for v in values:
        if v in cache:
            paths += cache[v]
            continue
        var ps = dfs(map, cache, v)
        paths += ps
        cache[v] = ps
    return paths
