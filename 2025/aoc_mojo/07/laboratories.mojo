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
        print(path_find(grid))
        # printty(grid)


fn path_find(mut grid: List[List[String]]) raises -> Int:
    var start_idx = grid[0].index("S")
    grid[1][start_idx] = "|"
    var split_count = 0
    for row_idx in range(1, len(grid) - 1):
        ref row = grid[row_idx]
        for col_idx in range(0, len(row)):
            ref char = row[col_idx]
            if char != "|":
                continue
            if grid[row_idx + 1][col_idx] == "^":
                grid[row_idx + 1][col_idx - 1] = "|"
                grid[row_idx + 1][col_idx + 1] = "|"
                split_count += 1
            else:
                grid[row_idx + 1][col_idx] = "|"
    return split_count


fn printty(read list: List[List[String]]):
    for l in list:
        for c in l:
            print(c, end="")
        print()


fn printt[origin: Origin](read list: List[StringSlice[origin]]):
    for l in list:
        print(l)
