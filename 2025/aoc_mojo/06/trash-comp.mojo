fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        print(parse(lines))


fn find_len_of_longest_line[
    origin: Origin
](read lines: List[StringSlice[origin]]) -> Int:
    var m = -1
    for l in lines:
        m = max(m, len(l))
    return m


fn parse[
    origin: Origin
](read lines: List[StringSlice[origin]]) raises -> Int:
  var grid = List[List[Int]]()
  var operations = [x for x in [String(y) for y in lines[-1].split(" ")] if x != " " and x != ""]
  for i in range(0, len(lines)-1):
    var row = [atol(x) for x in [String(y) for y in lines[i].split(" ")] if x != " " and x != ""]
    grid.append(row^)

  var total_sum = 0
  for col_idx in range(len(operations)):
    var op = operations[col_idx]
    var sub_sum = 1 if op == "*" else 0
    for row_idx in range(len(grid)):
      var val = grid[row_idx][col_idx]
      sub_sum = sub_sum * val if op == "*" else sub_sum + val
    total_sum += sub_sum
  return total_sum


fn printty(read list: List[List[String]]):
    for l in list:
        for c in l:
            print(c, end="")
        print()


fn printt[origin: Origin](read list: List[StringSlice[origin]]):
    for l in list:
        print(l)
