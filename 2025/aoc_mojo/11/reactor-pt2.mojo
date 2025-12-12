from collections import Set


alias DAC_BIT: UInt8 = 1
alias FFT_BIT: UInt8 = 2
alias TARGET_MASK: UInt8 = 3


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
        var cache = Dict[UInt64, Int]()
        print(dfs(forward_pass_map, cache, 0, "svr"))


fn dfs(
    read map: Dict[String, List[String]],
    mut cache: Dict[UInt64, Int],
    read seen: UInt8,
    read search: String,
) raises -> Int:
    var key = (hash(search) << 2) | UInt64(seen)
    if key in cache:
        return cache[key]

    ref values = map[search]
    if values[0] == "out":
        return 1 if seen == TARGET_MASK else 0

    var paths = 0
    for v in values:
        var new_seen: UInt8 = seen
        if v == "fft":
            new_seen |= FFT_BIT
        elif v == "dac":
            new_seen |= DAC_BIT

        paths += dfs(map, cache, new_seen, v)

    cache[key] = paths
    return paths
