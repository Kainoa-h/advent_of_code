fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var grid = List[List[String]]()
        for line in lines:
            var row = List[String]()
            for c in line.codepoint_slices():
                row.append(String(c))
            grid.append(row^)
        var start_idx = grid[0].index("S")
        grid[1][start_idx] = "|"
        var cache = Dict[String, Int]()
        print(dfs(grid, cache, 1, grid[1].index("|")))


fn dfs(mut grid: List[List[String]], mut cache: Dict[String, Int], row_idx: Int, col_idx: Int) raises -> Int:
    if row_idx == len(grid):
        return 1
    var key = "{}-{}".format(row_idx, col_idx)
    if key in cache:
      return cache[key]

    var node = grid[row_idx][col_idx]
    if node != "^":
        var val = dfs(grid, cache, row_idx + 1, col_idx)
        cache[key] = val
        return val
    else:
        var val = dfs(grid, cache, row_idx , col_idx - 1) + dfs(
            grid, cache, row_idx , col_idx + 1
        )
        cache[key] = val
        return val
