fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines = content.strip().split("\n")
        var sum = 0
        for line in lines:
          sum += process(line)
        print(sum)

fn process(read line: StringSlice) raises -> Int:
    var bank:List[Int] = [atol(line[i]) for i in range(len(line))]
    var most_sig_idx = find_first_index_of_max(bank[:-1])
    var least_sig_idx = find_first_index_of_max(bank[most_sig_idx+1:]) + most_sig_idx + 1
    var jojo = String(bank[most_sig_idx]) + String(bank[least_sig_idx])
    return atol(jojo)
    
fn find_first_index_of_max(read bank:Span[Int]) -> Int:
  var max = bank[0]
  var max_idx = 0
  for i in range(1, len(bank)):
    var current = bank[i]
    if current > max:
      max = current
      max_idx = i
  return max_idx


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


