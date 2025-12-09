# 4798221306190 is too low
# 6691658578316522283 is too high
fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.rstrip().split("\n")
        print(parse(lines))

fn find_len_of_longest_line[
    origin: Origin
](read lines: List[StringSlice[origin]]) -> Int64:
    var m = -1
    for l in lines:
        m = max(m, len(l))
    return m
fn find_op(slice: StringSlice) -> String:
  for c in slice.codepoint_slices():
    if c != " ":
      return String(c)
  return ""

fn safe_index[
    origin: Origin
    ](read lines: List[StringSlice[origin]], row:Int64, col:Int64) raises -> String:
  if row >= len(lines):
    return " "
  if col >= len(lines[row]):
    return " "
  return lines[row][col]

fn parse[
    origin: Origin
](read lines: List[StringSlice[origin]]) raises -> Int64:
  var operations = [x for x in [String(y) for y in lines[-1].split(" ")] if x != " " and x != ""]
  var len_of_longest_line = find_len_of_longest_line(lines)
  var ranges = List[Tuple[Int64, Int64]]()
  var start:Int64 = 0
  var end:Int64
  for col_idx in range(len_of_longest_line):
    var found_empty_col = True
    for row_idx in range(len(lines)):
      if safe_index(lines, Int(row_idx), Int(col_idx)) != " ":
        found_empty_col = False
        break
    if found_empty_col:
      end = col_idx
      ranges.append((start, end))
      start = end + 1
  ranges.append((start, len_of_longest_line))

  var total_val:Int64= 0
  for r in ranges:
    var start = r[0]
    var end = r[1]
    var op = find_op(lines[-1][Int(start):Int(end)])
    if op == "":
      continue
    var sub_val:Int64= 1 if op == "*" else 0
    # print("start: ", start, ", end: ", end)
    for col_idx in range(start, end):
      var str_val = String("")
      for line_idx in range(len(lines)-1):
        var char = safe_index(lines, Int64(line_idx), Int64(col_idx))
        if char != " ":
          str_val += char
      if len(str_val.strip()) == 0:
        continue
      var val = atol(str_val.strip())
      sub_val = sub_val * val if op == "*" else sub_val + val

    total_val += sub_val
    # print(sub_val)

  return total_val
