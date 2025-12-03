# 167420927217838 is to low
# 169347417057382
fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var sum = 0
        for line in lines:
          sum += process(line)
        print(sum)
        print(sum == 169347417057382)

fn process(read line: StringSlice) raises -> Int:
    var bank:List[Int] = [atol(line[i]) for i in range(len(line))]
    var num_to_cull = len(bank) - 12
    var most_sig_idx = find_first_index_of_max(bank[:num_to_cull + 1])
    var indexes_left_to_cull = num_to_cull - most_sig_idx
    # remove culled head
    for _i in range(most_sig_idx):
      _ = bank.pop(0)

    # remove remaining smallest
    for _i in range(indexes_left_to_cull):
      var min_idx = find_first_index_where_next_is_larger(bank)
      _ = bank.pop(min_idx)

    var str = "" 
    for x in bank:
      str += String(x)
    return atol(str)
    
fn find_first_index_of_max(read bank:Span[Int]) -> Int:
  var max = bank[0]
  var max_idx = 0
  for i in range(1, len(bank)):
    var current = bank[i]
    if current > max:
      max = current
      max_idx = i
  return max_idx

fn find_first_index_where_next_is_larger(read bank:List[Int]) -> Int:
  var prev = bank[0]
  for i in range(1, len(bank)):
    var current = bank[i]
    if current > prev:
      return i-1
    prev = current
      
  return len(bank) - 1

fn print_list(list: List[Int]):
  print("[", end="")
  for i in range(len(list)):
      print(list[i], end="")
      if i < len(list) - 1:
          print(", ", end="")
  print("]")


fn print_span(list: Span[Int]):
  print("[", end="")
  for i in range(len(list)):
      print(list[i], end="")
      if i < len(list) - 1:
          print(", ", end="")
  print("]")


