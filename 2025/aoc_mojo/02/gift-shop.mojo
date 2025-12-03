fn main() raises:
    with open("real.txt", "r") as f:
        var content = f.read()
        var lines =content.strip().split(",")
        var count = 0
        for line in lines:
            count += eval_range(line)
        print(count)


fn eval_range(read range_str: StringSlice) raises -> Int:
    var ranges = range_str.split("-")
    var start: Int = atol(ranges[0])
    var end: Int = atol(ranges[1])
    var count: Int = 0
    # print("eval range: ", start, "-", end);
    for i in range(start, end + 1):
        # print("checking for patters in: ", i)
        if check_for_patterns(String(i)):
          # print("                                  pattern found in :" + String(i))
          count += i
    return count


fn check_for_patterns(read numb_str: String) -> Bool:
    if numb_str.byte_length() % 2 != 0:
      return False

    var divisiors = find_divisiors(numb_str.byte_length())

    for div in divisiors:
      if validate_pattern(numb_str, div):
        return True
    return False


fn validate_pattern(read number_str: String, read mask_len: Int) -> Bool:
    # print("Validating:", number_str, " for mask size: ",mask_len)
    if (number_str.byte_length() / mask_len) % 2 != 0:
      return False
    var mask = number_str[:mask_len]
    for x in range(mask_len, number_str.byte_length(), mask_len):
        if number_str[x : x + mask_len] != mask:
            return False
    return True


fn find_divisiors(read x: Int) -> List[Int]:
    var divisiors = List[Int]()
    for i in range(1, Int(x**0.5 ) + 1):
        if x % i == 0:
            divisiors.append(i)
            if i != x // i and i != 1:
                divisiors.append(Int(x // i))
    return divisiors^
