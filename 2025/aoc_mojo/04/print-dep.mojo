fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var grid = List[List[String]]()
        for line in lines:
            var char_list = List[String]()
            for i in range(len(line)):
              char_list.append(line[i])
            grid.append(char_list^)
        print(mark_tp(grid))
        # printty(grid)
        


fn mark_tp(mut grid: List[List[String]]) -> Int:
    var count = 0
    for row_idx in range(len(grid)):
        ref row_str = grid[row_idx]
        for col_idx in range(len(row_str)):
          if grid[row_idx][col_idx] != ".":
            count += 1 if check_surrounding(grid, row_idx, col_idx) < 4 else 0
    return count


fn check_surrounding(
    mut grid: List[List[String]], read row_idx: Int, read col_idx: Int
) -> Int:
    var count = 0
    count += bounds_checked_read(grid, row_idx - 1, col_idx - 1)
    count += bounds_checked_read(grid, row_idx - 1, col_idx)
    count += bounds_checked_read(grid, row_idx - 1, col_idx + 1)
    count += bounds_checked_read(grid, row_idx, col_idx - 1)
    count += bounds_checked_read(grid, row_idx, col_idx + 1)
    count += bounds_checked_read(grid, row_idx + 1, col_idx - 1)
    count += bounds_checked_read(grid, row_idx + 1, col_idx)
    count += bounds_checked_read(grid, row_idx + 1, col_idx + 1)
    if count < 4:
      ref row = grid[row_idx]
      row[col_idx] = "x"
    return count


fn bounds_checked_read(
    read grid: List[List[String]], read row_idx: Int, read col_idx: Int
) -> Int:
    if row_idx < 0 or col_idx < 0:
        return 0
    if row_idx >= len(grid):
        return 0
    ref row = grid[row_idx]
    if col_idx >= len(row):
        return 0
    return 1 if row[col_idx] != "." else 0


fn printty(read list: List[List[String]]):
    for l in list:
      for c in l:
        print(c,end="")
      print()


fn printt[origin: Origin](read list: List[StringSlice[origin]]):
    for l in list:
        print(l)
